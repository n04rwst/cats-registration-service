package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Friendship {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firstcatid")
    private Cat firstCat;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondcatid")
    private Cat secondCat;
}