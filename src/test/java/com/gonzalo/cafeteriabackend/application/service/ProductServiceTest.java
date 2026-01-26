package com.gonzalo.cafeteriabackend.application.usecase.product;

import com.gonzalo.cafeteriabackend.application.port.in.product.DeleteProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetActiveProducts;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetAllProducts;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductById;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductImageData;
import com.gonzalo.cafeteriabackend.application.port.in.product.ReorderProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.SaveProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.ToggleProductStatus;
import com.gonzalo.cafeteriabackend.application.port.out.ProductRepositoryPort;
import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUseCasesTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private ProductUseCases productUseCases;

    @Test
    void getAllProductsUsesRepositoryOrder() {
        Product product = new Product();
        when(productRepository.findAllOrderBySortOrderAsc()).thenReturn(List.of(product));

        List<Product> result = productUseCases
                .execute(new GetAllProducts.GetAllProductsRequest())
                .products();

        assertThat(result).containsExactly(product);
    }

    @Test
    void getActiveProductsUsesRepository() {
        Product product = new Product();
        when(productRepository.findActive()).thenReturn(List.of(product));

        List<Product> result = productUseCases
                .execute(new GetActiveProducts.GetActiveProductsRequest())
                .products();

        assertThat(result).containsExactly(product);
    }

    @Test
    void getProductByIdReturnsNullWhenMissing() {
        when(productRepository.findById(9L)).thenReturn(Optional.empty());

        Product result = productUseCases
                .execute(new GetProductById.GetProductByIdRequest(9L))
                .product();

        assertThat(result).isNull();
    }

    @Test
    void getBinaryDataByProductIdReturnsProductData() {
        BinaryData data = new BinaryData();
        Product product = new Product();
        product.setBinaryData(data);
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));

        BinaryData result = productUseCases
                .execute(new GetProductImageData.GetProductImageDataRequest(5L))
                .imageData();

        assertThat(result).isEqualTo(data);
    }

    @Test
    void saveNewProductAssignsSortOrderAndImage() {
        Product existing = new Product();
        existing.setSortOrder(4);
        when(productRepository.findAll()).thenReturn(List.of(existing));

        Product toSave = new Product();
        BinaryData image = new BinaryData();
        image.setData(new byte[]{1, 2, 3});

        productUseCases.execute(new SaveProduct.SaveProductRequest(toSave, image));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();
        assertThat(saved.getSortOrder()).isEqualTo(5);
        assertThat(saved.getBinaryData()).isEqualTo(image);
    }

    @Test
    void saveExistingProductKeepsImageWhenNoNewImage() {
        Product existing = new Product();
        existing.setId(2L);
        BinaryData stored = new BinaryData();
        stored.setData(new byte[]{9});
        existing.setBinaryData(stored);
        existing.setSortOrder(7);

        when(productRepository.findById(2L)).thenReturn(Optional.of(existing));

        Product toSave = new Product();
        toSave.setId(2L);

        productUseCases.execute(new SaveProduct.SaveProductRequest(toSave, null));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();
        assertThat(saved.getBinaryData()).isEqualTo(stored);
        assertThat(saved.getSortOrder()).isEqualTo(7);
    }

    @Test
    void toggleProductStatusFlipsFlag() {
        Product product = new Product();
        product.setId(1L);
        product.setIsActive(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productUseCases.execute(new ToggleProductStatus.ToggleProductStatusRequest(1L));

        assertThat(product.getIsActive()).isFalse();
        verify(productRepository).save(product);
    }

    @Test
    void deleteProductDelegatesToRepository() {
        productUseCases.execute(new DeleteProduct.DeleteProductRequest(4L));

        verify(productRepository).deleteById(4L);
    }

    @Test
    void reorderSwapsNeighborAndFlushes() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setSortOrder(1);
        Product p2 = new Product();
        p2.setId(2L);
        p2.setSortOrder(2);
        Product p3 = new Product();
        p3.setId(3L);
        p3.setSortOrder(3);
        when(productRepository.findAllOrderBySortOrderAsc()).thenReturn(List.of(p1, p2, p3));

        productUseCases.execute(new ReorderProduct.ReorderProductRequest(2L, "up"));

        assertThat(p1.getSortOrder()).isEqualTo(2);
        assertThat(p2.getSortOrder()).isEqualTo(1);
        verify(productRepository).save(p1);
        verify(productRepository).save(p2);
        verify(productRepository).flush();
    }

    @Test
    void reorderNoopWhenMissing() {
        when(productRepository.findAllOrderBySortOrderAsc()).thenReturn(List.of());

        productUseCases.execute(new ReorderProduct.ReorderProductRequest(9L, "down"));

        verify(productRepository, never()).save(org.mockito.ArgumentMatchers.any(Product.class));
        verify(productRepository, never()).flush();
    }
}
