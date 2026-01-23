package com.gonzalo.cafeteriabackend.repository;

import com.gonzalo.cafeteriabackend.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Importante para que no de error 'cannot find symbol'

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    // Esto asegura que PostgreSQL siempre devuelva las mesas del 1 al 10
    List<TableEntity> findAllByOrderByNumberAsc();
}