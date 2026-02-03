package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.BinaryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryDataRepository extends JpaRepository<BinaryData, Long> {
}
