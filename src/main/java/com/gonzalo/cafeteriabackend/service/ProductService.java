package com.gonzalo.cafeteriabackend.service;

import com.gonzalo.cafeteriabackend.model.Product;
import com.gonzalo.cafeteriabackend.model.ProductTranslation;
import com.gonzalo.cafeteriabackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product, MultipartFile image) throws IOException {

        // 1. Manejo de la imagen
        if (image != null && !image.isEmpty()) {
            // Pasamos los bytes Y el tipo de contenido (ej: image/png)
            product.setImageData(image.getBytes(), image.getContentType());
        }

        // 2. Limpiar traducciones previas para evitar duplicados
        if (product.getTranslations() == null) {
            product.setTranslations(new ArrayList<>());
        } else {
            product.getTranslations().clear();
        }

        // 3. Agregar Traducción Español
        if (product.getNameEs() != null && !product.getNameEs().isEmpty()) {
            ProductTranslation es = new ProductTranslation();
            es.setLanguageCode("es");
            es.setName(product.getNameEs());
            es.setDescription(product.getDescriptionEs());
            es.setProduct(product); // Vínculo obligatorio
            product.getTranslations().add(es);
        }

        // 4. Agregar Traducción Inglés
        if (product.getNameEn() != null && !product.getNameEn().isEmpty()) {
            ProductTranslation en = new ProductTranslation();
            en.setLanguageCode("en");
            en.setName(product.getNameEn());
            en.setDescription(product.getDescriptionEn());
            en.setProduct(product); // Vínculo obligatorio
            product.getTranslations().add(en);
        }

        // 5. Guardar el producto (esto guardará automáticamente las traducciones por el Cascade)
        return productRepository.save(product);
    }


}