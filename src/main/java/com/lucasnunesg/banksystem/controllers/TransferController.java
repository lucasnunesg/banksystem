package com.lucasnunesg.banksystem.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/transfers")
public class TransferController {

    private final TransferService service;

    @Autowired
    public TransferController(TransferService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> findAll() {
        List<Transfer> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Transfer> findById(@PathVariable Long id) {
        Transfer obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<Transfer> transfer(@RequestBody TransferDto dto) throws JsonProcessingException {
        var transfer = service.transfer(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(transfer.getId()).toUri();
        return ResponseEntity.created(uri).body(transfer);
    }
}
