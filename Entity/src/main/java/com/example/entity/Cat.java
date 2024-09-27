package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;

    private LocalDate dayOfBirth;

    private String breed;

    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerid")
    private Human owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friendship",
            joinColumns = { @JoinColumn(name = "firstcatid") },
            inverseJoinColumns = { @JoinColumn(name = "secondcatid") })
    private Set<Cat> friends;

    public void addFriend(Cat cat) {
        this.friends.add(cat);
    }

    public void removeFriend(Cat cat) {
        this.friends.remove(cat);
    }
}
