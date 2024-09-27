package com.example.service;

import lombok.RequiredArgsConstructor;
import com.example.securityconfig.CustomUserDetails;
import com.example.dto.CreateCatRequest;
import com.example.dto.GetCatResponse;
import com.example.dto.UpdateCatRequest;
import com.example.entity.Cat;
import com.example.mapper.CatMapper;
import com.example.repository.CatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatService.class);

    @Autowired
    private final CatRepository catRepository;
    @Autowired
    private final CatMapper catMapper;

    @RabbitListener(queues = "${rabbitmq.create.cat.queue.name}")
    public void create(CreateCatRequest dto) {
        if (Objects.nonNull(dto)) {
            LOGGER.info(String.format("Received create message: %s", dto));
            Cat cat = catMapper.toEntityFromCreate(dto);
            catRepository.save(cat);

            Set<Cat> friends = cat.getFriends();
            if (Objects.nonNull(friends)) {
                friends.forEach(friend -> friend.addFriend(cat));
                catRepository.saveAll(cat.getFriends());
            }
        }
    }

    public GetCatResponse findById(Integer id) {
        return catMapper.toDto(catRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can't find cat with id: " + id)));
    }

    public List<GetCatResponse> findByName(String name) {
        List<GetCatResponse> catsDto = catMapper.toDto(catRepository.findByName(name));
        return filterByAuthUser(catsDto);
    }

    public List<GetCatResponse> findByColor(String color) {
        List<GetCatResponse> catsDto = catMapper.toDto(catRepository.findByColor(color));
        return filterByAuthUser(catsDto);
    }

    public List<GetCatResponse> findByBreed(String breed) {
        List <GetCatResponse> catsDto = catMapper.toDto(catRepository.findByBreed(breed));
        return filterByAuthUser(catsDto);
    }

    public List<GetCatResponse> findByOwnerId(Integer id) {
        return catMapper.toDto(catRepository.findByOwnerId(id));
    }

    public List<GetCatResponse> findAll() {
        List<GetCatResponse> catsDto = catMapper.toDto(catRepository.findAll());
        return filterByAuthUser(catsDto);
    }

    @RabbitListener(queues = "${rabbitmq.update.cat.queue.name}")
    public void update(UpdateCatRequest dto) {
        if (Objects.nonNull(dto)) {
            LOGGER.info(String.format("Received create message: %s", dto));

            var currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            if (currentUser.getAuthorities().toString().equals("[ROLE_ADMIN]")
                    || (currentUser.getAuthorities().toString().equals("[ROLE_USER]")
                    && currentUser.getId().equals(catRepository.findById(dto.getId())
                    .orElseThrow().getOwner().getId()))) {

                var outdatedCat = catRepository.findById(dto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Can't update cat with id: " + dto.getId()));

                Cat cat = catMapper.toEntityFromUpdate(dto);

                List<Cat> differences;

                Set<Cat> friends = cat.getFriends();
                if (Objects.nonNull(friends)) {
                    if (outdatedCat.getFriends().toArray().length <= cat.getFriends().toArray().length) {
                        differences = cat.getFriends().stream()
                                .filter(i -> !outdatedCat.getFriends().contains(i)).toList();
                        catRepository.save(cat);
                        differences.forEach(friend -> friend.addFriend(cat));
                    }
                    else {
                        differences = outdatedCat.getFriends().stream()
                                .filter(i -> !cat.getFriends().contains(i)).toList();
                        catRepository.save(cat);
                        differences.forEach(friend -> friend.removeFriend(cat));
                    }
                    catRepository.saveAll(differences);
                }
            }

            else
                throw new IllegalArgumentException("Can't update cat with id: " + dto.getId());
        }

        throw new IllegalArgumentException("Can't update a cat: null object received");
    }

    @RabbitListener(queues = "${rabbitmq.delete.cat.queue.name}")
    public void delete(Integer id) {
        LOGGER.info(String.format("Received delete id message: %d", id));
        Cat cat = catRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Can't find cat with id: " + id));
        Set<Cat> friends = cat.getFriends();

        if (Objects.nonNull(friends)) {
            friends.forEach(friend -> friend.removeFriend(cat));
            friends.clear();
        }

        catRepository.deleteById(id);
    }

    public List<GetCatResponse> filterByAuthUser(List<GetCatResponse> catDtos) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().stream().anyMatch(i -> i.getAuthority().equals("ROLE_USER"))) {
            var ownerId = ((CustomUserDetails) auth.getPrincipal()).getId();
            return catDtos.stream().filter(catDto -> catDto.getOwnerId().equals(ownerId)).toList();
        }

        return catDtos;
    }

    @RabbitListener(queues = "${rabbitmq.save.cat.info.queue.name}")
    public void saveCatsByOwner(Set<Cat> cats) {
        catRepository.saveAll(cats);
    }

    @RabbitListener(queues = "${rabbitmq.delete.cat.info.queue.name}")
    public void deleteCatsByOwner(Set<Cat> cats) {
        cats.forEach((cat) -> catRepository.deleteById(cat.getId()));
    }

}
