package com.gonzalo.cafeteriabackend.repository;

import com.gonzalo.cafeteriabackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c ORDER BY c.sortOrder ASC")
    List<Category> findAllOrderBySortOrder();

    List<Category> findByIsActiveTrueOrderBySortOrderAsc();
}