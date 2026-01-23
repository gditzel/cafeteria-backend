package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.ProductUseCase;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.dto.ProductDto;
import com.gonzalo.cafeteriabackend.infrastructure.adapter.in.mapper.ProductMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/public/menu")
@CrossOrigin(origins = "http://localhost:4200")
public class MenuController {
    private final ProductUseCase productUseCase;

    public MenuController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @GetMapping
    public List<ProductDto> getMenu() {
        return productUseCase.getProductsForMenu().stream()
                .map(ProductMapper::toDto)
                .toList();
    }
}
