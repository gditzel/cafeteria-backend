package com.gonzalo.cafeteriabackend.repository;

import com.gonzalo.cafeteriabackend.model.BinaryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryDataRepository extends JpaRepository<BinaryData, Long> {
}