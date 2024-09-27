package com.example.repository;

import com.example.entity.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HumanRepository extends JpaRepository<Human, Integer> {
    Optional<Human> findByUsername(String username);
    List<Human> findByName(String name);
    List<Human> findByDayOfBirthAfter(LocalDate date);
    List<Human> findByDayOfBirthBefore(LocalDate date);
}
