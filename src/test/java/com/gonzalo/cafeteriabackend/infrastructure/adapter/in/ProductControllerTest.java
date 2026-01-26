package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.product.DeleteProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetActiveProducts;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetAllProducts;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductById;
import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductImageData;
import com.gonzalo.cafeteriabackend.application.port.in.product.ReorderProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.SaveProduct;
import com.gonzalo.cafeteriabackend.application.port.in.product.ToggleProductStatus;
import com.gonzalo.cafeteriabackend.domain.model.BinaryData;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetAllProducts getAllProducts;

    @MockBean
    private GetActiveProducts getActiveProducts;

    @MockBean
    private GetProductById getProductById;

    @MockBean
    private GetProductImageData getProductImageData;

    @MockBean
    private SaveProduct saveProduct;

    @MockBean
    private ReorderProduct reorderProduct;

    @MockBean
    private DeleteProduct deleteProduct;

    @MockBean
    private ToggleProductStatus toggleProductStatus;

    @Test
    void getAllProductsReturnsList() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setIsActive(true);
        product.setPrice(10.0);

        when(getAllProducts.execute(any(GetAllProducts.GetAllProductsRequest.class)))
                .thenReturn(new GetAllProducts.GetAllProductsResponse(List.of(product)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].isActive").value(true))
                .andExpect(jsonPath("$[0].price").value(10.0));
    }

    @Test
    void getActiveProductsReturnsList() throws Exception {
        Product product = new Product();
        product.setId(2L);
        product.setIsActive(true);

        when(getActiveProducts.execute(any(GetActiveProducts.GetActiveProductsRequest.class)))
                .thenReturn(new GetActiveProducts.GetActiveProductsResponse(List.of(product)));

        mockMvc.perform(get("/api/products/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }

    @Test
    void getProductByIdReturnsNotFoundWhenMissing() throws Exception {
        when(getProductById.execute(new GetProductById.GetProductByIdRequest(5L)))
                .thenReturn(new GetProductById.GetProductByIdResponse(null));

        mockMvc.perform(get("/api/products/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductByIdReturnsProduct() throws Exception {
        Product product = new Product();
        product.setId(5L);
        product.setPrice(3.5);

        when(getProductById.execute(new GetProductById.GetProductByIdRequest(5L)))
                .thenReturn(new GetProductById.GetProductByIdResponse(product));

        mockMvc.perform(get("/api/products/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.price").value(3.5));
    }

    @Test
    void getProductImageReturnsData() throws Exception {
        BinaryData data = new BinaryData();
        data.setContentType("image/jpeg");
        data.setData(new byte[]{1, 2, 3});

        when(getProductImageData.execute(new GetProductImageData.GetProductImageDataRequest(9L)))
                .thenReturn(new GetProductImageData.GetProductImageDataResponse(data));

        mockMvc.perform(get("/api/products/9/image"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));
    }

    @Test
    void getProductImageReturnsNotFoundWhenMissing() throws Exception {
        when(getProductImageData.execute(new GetProductImageData.GetProductImageDataRequest(9L)))
                .thenReturn(new GetProductImageData.GetProductImageDataResponse(null));

        mockMvc.perform(get("/api/products/9/image"))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveProductParsesMultipartAndReturnsDto() throws Exception {
        Product saved = new Product();
        saved.setId(10L);
        saved.setIsActive(true);
        saved.setPrice(12.5);

        when(saveProduct.execute(any(SaveProduct.SaveProductRequest.class)))
                .thenReturn(new SaveProduct.SaveProductResponse(saved));

        String productJson = "{\"isActive\":true,\"price\":12.5,\"stock\":3}";

        MockMultipartFile productPart = new MockMultipartFile(
                "product",
                "product.json",
                MediaType.APPLICATION_JSON_VALUE,
                productJson.getBytes()
        );
        MockMultipartFile imagePart = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{9, 8}
        );

        mockMvc.perform(multipart("/api/products")
                        .file(productPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.price").value(12.5));

        ArgumentCaptor<SaveProduct.SaveProductRequest> captor =
                ArgumentCaptor.forClass(SaveProduct.SaveProductRequest.class);
        verify(saveProduct).execute(captor.capture());
        assertThat(captor.getValue().imageData().getData()).containsExactly(9, 8);
        assertThat(captor.getValue().imageData().getContentType())
                .isEqualTo(MediaType.IMAGE_PNG_VALUE);
    }

    @Test
    void reorderCallsUseCase() throws Exception {
        mockMvc.perform(post("/api/products/3/reorder")
                        .param("direction", "down"))
                .andExpect(status().isOk());

        verify(reorderProduct).execute(new ReorderProduct.ReorderProductRequest(3L, "down"));
    }

    @Test
    void deleteProductCallsUseCase() throws Exception {
        mockMvc.perform(delete("/api/products/6"))
                .andExpect(status().isNoContent());

        verify(deleteProduct).execute(new DeleteProduct.DeleteProductRequest(6L));
    }

    @Test
    void toggleStatusCallsUseCase() throws Exception {
        mockMvc.perform(patch("/api/products/7/toggle"))
                .andExpect(status().isOk());

        verify(toggleProductStatus).execute(new ToggleProductStatus.ToggleProductStatusRequest(7L));
    }
}
