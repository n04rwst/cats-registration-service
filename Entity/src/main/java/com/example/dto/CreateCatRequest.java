package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class CreateCatRequest implements Serializable {
    private String name;
    private LocalDate dayOfBirth;
    private String breed;
    private String color;
    private Set<Integer> friendsId;
}
