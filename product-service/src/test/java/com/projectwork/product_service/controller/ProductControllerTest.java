package com.projectwork.product_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectwork.product_service.dto.ProductRequest;
import com.projectwork.product_service.dto.ProductResponse;
import com.projectwork.product_service.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void shouldCreateProductAndReturnProductSuccessfullyCreated() throws Exception {
        ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/product").contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated())
                .andReturn();
        Assertions.assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("iPhone")
                .description("iphone 13")
                .price(BigDecimal.valueOf(100))
                .build();
    }
    @Test
    void shouldReturnAllListOfProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(getProductResponse());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("iPhone"))
                .andExpect(jsonPath("$[0].description").value("iphone 13"))
                .andExpect(jsonPath("$[0].price").value(100))
                .andExpect(jsonPath("$[1].name").value("iPhone"))
                .andExpect(jsonPath("$[1].description").value("iphone 12"))
                .andExpect(jsonPath("$[1].price").value(95));
    }
    private List<ProductResponse> getProductResponse() {
        ProductResponse product1 = ProductResponse.builder()
                .name("iPhone")
                .description("iphone 13")
                .price(BigDecimal.valueOf(100))
                .build();

        ProductResponse product2 = ProductResponse.builder()
                .name("iPhone")
                .description("iphone 12")
                .price(BigDecimal.valueOf(95))
                .build();

        return List.of(product1, product2);
    }
    @Test
    void shouldReturnNoProductsFoundInProductService() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(productResponseList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    private List<ProductResponse> productResponseList() {
        List<ProductResponse> productResponseList = new ArrayList<>();
        return productResponseList;
    }
}
