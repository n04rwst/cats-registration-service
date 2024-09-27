package com.example.service;

import com.example.producer.CatsInfoProducer;
import lombok.RequiredArgsConstructor;
import com.example.dto.CreateHumanRequest;
import com.example.dto.GetHumanResponse;
import com.example.dto.UpdateHumanRequest;
import com.example.entity.Human;
import com.example.mapper.HumanMapper;
import com.example.repository.HumanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class HumanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HumanService.class);

    @Autowired
    private CatsInfoProducer catsInfoProducer;

    @Autowired
    private final HumanRepository humanRepository;

    @Autowired
    private final HumanMapper humanMapper;

    @RabbitListener(queues = "${rabbitmq.create.human.queue.name}")
    public void create(CreateHumanRequest dto) {
        if (Objects.nonNull(dto)) {
            LOGGER.info(String.format("Received create message: %s", dto));

            Human human = humanMapper.toEntityFromCreate(dto);
            humanRepository.save(human);
            if (Objects.nonNull(human.getCats()))
                catsInfoProducer.sendCatsInfoToSave(human.getCats());
        }
    }

    public List<GetHumanResponse> findByName(String name) {
        return humanMapper.toDto(humanRepository.findByName(name));
    }

    public List<GetHumanResponse> findByDayOfBirthAfter(LocalDate date) {
        return humanMapper.toDto(humanRepository.findByDayOfBirthAfter(date));
    }

    public List<GetHumanResponse> findByDayOfBirthBefore(LocalDate date) {
        return humanMapper.toDto(humanRepository.findByDayOfBirthBefore(date));
    }

    public GetHumanResponse findById(Integer id) {
        return humanMapper.toDto(humanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find human with id " + id)));
    }

    public List<GetHumanResponse> findAll() {
        return humanMapper.toDto(humanRepository.findAll());
    }

    @RabbitListener(queues = "${rabbitmq.update.human.queue.name}")
    public void update(UpdateHumanRequest dto) {
        if (Objects.nonNull(dto)) {
            LOGGER.info(String.format("Received update message: %s", dto));

            Human human = humanMapper.toEntityFromUpdate(dto);
            humanRepository.save(human);
            if (Objects.nonNull(human.getCats()))
                catsInfoProducer.sendCatsInfoToSave(human.getCats());
        }
    }

    @RabbitListener(queues = "${rabbitmq.delete.human.queue.name}")
    public void delete(Integer id) {
        LOGGER.info(String.format("Received delete id message: %d", id));

        Human human = humanRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Can't find human with id " + id));

        if (Objects.nonNull(human.getCats()))
            catsInfoProducer.sendCatsInfoToDelete(human.getCats());

        humanRepository.deleteById(id);
    }
}
