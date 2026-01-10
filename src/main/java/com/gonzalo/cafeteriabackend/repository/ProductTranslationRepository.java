package com.gonzalo.cafeteriabackend.repository;

import com.gonzalo.cafeteriabackend.model.ProductTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {
}