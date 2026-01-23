package com.gonzalo.cafeteriabackend.controller;

import com.gonzalo.cafeteriabackend.model.TableEntity;
import com.gonzalo.cafeteriabackend.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "http://localhost:4200")
public class TableController {

    @Autowired
    private TableRepository tableRepository;

    @GetMapping
    public List<TableEntity> getAllTables() {
        // Retorna todas las mesas (ID, número y estado) de la base de datos
        return tableRepository.findAll();
    }
}