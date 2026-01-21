package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.TableEntity;
import com.gonzalo.cafeteriabackend.repository.TableRepository;
import org.springframework.stereotype.Service; // Soluciona error 'cannot find symbol class Service'
import java.util.List; // Soluciona error 'cannot find symbol class List'

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<TableEntity> getAllTables() {
        // Usamos el nuevo método para mantener el orden visual del salón
        return tableRepository.findAllByOrderByNumberAsc();
    }
}