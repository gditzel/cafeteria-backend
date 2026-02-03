package com.gonzalo.cafeteriabackend.infrastructure.adapter.in;

import com.gonzalo.cafeteriabackend.application.port.in.product.GetProductsForMenu;
import com.gonzalo.cafeteriabackend.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetProductsForMenu getProductsForMenu;

    @Test
    void getMenuReturnsProducts() throws Exception {
        Product product = new Product();
        product.setId(3L);
        product.setPrice(8.5);

        when(getProductsForMenu.execute(new GetProductsForMenu.GetProductsForMenuRequest()))
                .thenReturn(new GetProductsForMenu.GetProductsForMenuResponse(List.of(product)));

        mockMvc.perform(get("/api/public/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].price").value(8.5));
    }
}
