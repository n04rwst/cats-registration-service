package com.example.controller;

import com.example.producer.*;
import com.example.service.HumanService;
import lombok.RequiredArgsConstructor;
import com.example.dto.CreateHumanRequest;
import com.example.dto.GetHumanResponse;
import com.example.dto.UpdateHumanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class HumanController {
    @Autowired
    private CreateHumanProducer createProducer;

    @Autowired
    private UpdateHumanProducer updateProducer;

    @Autowired
    private DeleteHumanProducer deleteProducer;

    @Autowired
    private final HumanService humanService;

    @PostMapping(value = "/new-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Integer> create(@RequestBody CreateHumanRequest humanDto) {
        createProducer.sendCreateMessage(humanDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') and principal.id == #id or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GetHumanResponse> getById(@PathVariable Integer id) {
        final GetHumanResponse humanDto = humanService.findById(id);

        return humanDto != null ? new ResponseEntity<>(humanDto, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/name/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GetHumanResponse>> getByName(@PathVariable String name) {
        final List<GetHumanResponse> humansDto = humanService.findByName(name);

        return humansDto != null && !humansDto.isEmpty() ?
                new ResponseEntity<>(humansDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/date-after/{date}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GetHumanResponse>> getHumanByDayOfBirthAfter(@PathVariable LocalDate date) {
        final List<GetHumanResponse> humansDto = humanService.findByDayOfBirthAfter(date);

        return humansDto != null && !humansDto.isEmpty() ?
                new ResponseEntity<>(humansDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/date-before/{date}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GetHumanResponse>> getHumanByDayOfBirthBefore(@PathVariable LocalDate date) {
        final List<GetHumanResponse> humansDto = humanService.findByDayOfBirthBefore(date);

        return humansDto != null && !humansDto.isEmpty() ?
                new ResponseEntity<>(humansDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<GetHumanResponse>> getAll() {
        final List<GetHumanResponse> humansDto = humanService.findAll();

        return humansDto != null && !humansDto.isEmpty() ?
                new ResponseEntity<>(humansDto, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_USER') and principal.id == #humanDto.getId or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody UpdateHumanRequest humanDto) {
        updateProducer.sendUpdateMessage(humanDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') and principal.id == #id or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        deleteProducer.sendDeleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
