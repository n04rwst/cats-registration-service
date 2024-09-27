package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UpdateCatRequest implements Serializable {
    private Integer id;
    private String name;
    private LocalDate dayOfBirth;
    private String breed;
    private String color;
    private Integer ownerId;
    private Set<Integer> friendsId;
}
