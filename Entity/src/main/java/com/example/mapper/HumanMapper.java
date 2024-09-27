package com.example.mapper;

import com.example.repository.CatRepository;
import lombok.RequiredArgsConstructor;
import com.example.entity.Human;
import com.example.dto.CreateHumanRequest;
import com.example.dto.GetHumanResponse;
import com.example.dto.UpdateHumanRequest;
import com.example.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HumanMapper {
    @Autowired
    private final CatRepository catRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Human toEntityFromCreate(CreateHumanRequest dto) {
        Human human = Human.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.valueOf(dto.getRole()))
                .name(dto.getName())
                .dayOfBirth(dto.getDayOfBirth())
                .build();

        if (Objects.nonNull(dto.getCatsId())) {
            human.setCats(new HashSet<>());
            dto.getCatsId().
                    forEach(catId -> human.addCat(catRepository.findById(catId).orElse(null)));
        }

        return human;
    }

    public Human toEntityFromUpdate(UpdateHumanRequest dto) {
        Human human = Human.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.valueOf(dto.getRole()))
                .name(dto.getName())
                .dayOfBirth(dto.getDayOfBirth())
                .build();

        if (Objects.nonNull(dto.getCatsId())) {
            human.setCats(new HashSet<>());
            dto.getCatsId().
                    forEach(catId -> human.addCat(catRepository.findById(catId).orElse(null)));
        }

        return human;
    }

    public GetHumanResponse toDto(Human human) {
        GetHumanResponse humanDto = GetHumanResponse.builder()
                .id(human.getId())
                .username(human.getUsername())
                .role(human.getRole().toString())
                .name(human.getName())
                .dayOfBirth(human.getDayOfBirth())
                .build();

        if (Objects.nonNull(human.getCats())) {
            humanDto.setCatsId(new HashSet<>());
            human.getCats().forEach(cat -> humanDto.getCatsId().add(cat.getId()));
        }

        return humanDto;
    }

    public List<GetHumanResponse> toDto(List<Human> humans) {
        return humans.stream().map(this::toDto).toList();
    }
}
