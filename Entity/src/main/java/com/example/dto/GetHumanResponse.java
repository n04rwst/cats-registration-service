package com.example.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class GetHumanResponse implements Serializable {
    private Integer id;
    private String username;
    private String role;
    private String name;
    private LocalDate dayOfBirth;
    private Set<Integer> catsId;
}
