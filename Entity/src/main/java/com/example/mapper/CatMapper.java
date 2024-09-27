package com.example.mapper;

import com.example.dto.CreateCatRequest;
import com.example.entity.Cat;
import com.example.dto.GetCatResponse;
import com.example.dto.UpdateCatRequest;
import com.example.repository.CatRepository;
import com.example.repository.HumanRepository;
import com.example.securityconfig.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CatMapper {
    @Autowired
    private final CatRepository catRepository;

    @Autowired
    private final HumanRepository humanRepository;

    public Cat toEntityFromCreate(CreateCatRequest dto) {
        Cat cat = Cat.builder().
                name(dto.getName()).
                dayOfBirth(dto.getDayOfBirth()).
                breed(dto.getBreed()).
                color(dto.getColor()).
                build();

        var ownerId = ((CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getId();
        cat.setOwner(humanRepository.findById(ownerId).orElse(null));

        if (Objects.nonNull(dto.getFriendsId())) {
            cat.setFriends(new HashSet<>());
            dto.getFriendsId().
                    forEach(i -> cat.addFriend(catRepository.findById(i).orElse(null)));
        }

        return cat;
    }

    public Cat toEntityFromUpdate(UpdateCatRequest dto) {
        Cat cat = Cat.builder().
                id(dto.getId()).
                name(dto.getName()).
                dayOfBirth(dto.getDayOfBirth()).
                breed(dto.getBreed()).
                color(dto.getColor()).
                build();

        if (Objects.nonNull(cat.getOwner()))
            dto.setOwnerId(cat.getOwner().getId());

        if (Objects.nonNull(dto.getFriendsId())) {
            cat.setFriends(new HashSet<>());
            dto.getFriendsId().
                    forEach(i -> cat.addFriend(catRepository.findById(i).orElse(null)));
        }

        return cat;
    }

    public GetCatResponse toDto(Cat cat) {
        GetCatResponse catDto = GetCatResponse.builder().
                id(cat.getId()).
                name(cat.getName()).
                dayOfBirth(cat.getDayOfBirth()).
                breed(cat.getBreed()).
                color(cat.getColor()).
                build();

        if (Objects.nonNull(cat.getOwner()))
            catDto.setOwnerId(cat.getOwner().getId());

        if (Objects.nonNull(cat.getFriends())) {
            catDto.setFriendsId(new HashSet<>());
            cat.getFriends().
                    forEach(friend -> catDto.getFriendsId().add(friend.getId()));
        }

        return catDto;
    }

    public List<GetCatResponse> toDto(List<Cat> cats) {
        return cats.stream().map(this::toDto).toList();
    }
}
