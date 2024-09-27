package com.example.controller;

import com.example.producer.CreateCatProducer;
import com.example.producer.DeleteCatProducer;
import com.example.producer.UpdateCatProducer;
import com.example.service.CatService;
import lombok.AllArgsConstructor;
import com.example.dto.CreateCatRequest;
import com.example.dto.GetCatResponse;
import com.example.dto.UpdateCatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/cats")
public class CatController {
    @Autowired
    private CreateCatProducer createProducer;

    @Autowired
    private UpdateCatProducer updateProducer;

    @Autowired
    private DeleteCatProducer deleteProducer;

    @Autowired
    private final CatService catService;

    @PostMapping(value = "/new-cat")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateCatRequest createCatRequest) {
        createProducer.sendCreateMessage(createCatRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.body.ownerId == principal.id")
    public ResponseEntity<GetCatResponse> getById(@PathVariable Integer id) {
        final GetCatResponse catDto = catService.findById(id);

        return catDto != null ?
                new ResponseEntity<>(catDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/name/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GetCatResponse>> getCatByName(@PathVariable String name) {
        final List<GetCatResponse> catsDto = catService.findByName(name);

        return catsDto != null && !catsDto.isEmpty() ?
                new ResponseEntity<>(catsDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<GetCatResponse>> getCatByColor(@PathVariable String color) {
        List<GetCatResponse> catsDto = catService.findByColor(color);

        return catsDto != null && !catsDto.isEmpty() ?
                new ResponseEntity<>(catsDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/breed/{breed}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<GetCatResponse>> getCatByBreed(@PathVariable String breed) {
        List<GetCatResponse> catsDto = catService.findByBreed(breed);

        return catsDto != null && !catsDto.isEmpty() ?
                new ResponseEntity<>(catsDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/owner/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GetCatResponse>> getCatByOwnerId(@PathVariable Integer id) {
        final List<GetCatResponse> catsDto = catService.findByOwnerId(id);

        return catsDto != null && !catsDto.isEmpty() ?
                new ResponseEntity<>(catsDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<GetCatResponse>> getAll() {
        List<GetCatResponse> catsDto = catService.findAll();

        return catsDto != null && !catsDto.isEmpty() ?
                new ResponseEntity<>(catsDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody UpdateCatRequest updateCatRequest) {
        updateProducer.sendUpdateMessage(updateCatRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')" +
            "and @catService.findById(#id).getOwnerId() == principal.id")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        deleteProducer.sendCreateMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
