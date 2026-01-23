package com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa;

import com.gonzalo.cafeteriabackend.infrastructure.adapter.out.jpa.entity.ProductTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {
}
