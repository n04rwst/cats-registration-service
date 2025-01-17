package com.example.repository;

import com.example.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat, Integer> {
    List<Cat> findByName(String name);
    List<Cat> findByColor(String color);
    List<Cat> findByBreed(String breed);
    List<Cat> findByOwnerId(Integer id);
}
