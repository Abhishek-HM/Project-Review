package com.projectwork.product_service;

import com.projectwork.product_service.dto.ProductRequest;
import com.projectwork.product_service.dto.ProductResponse;
import com.projectwork.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port+"").concat("/api/product");
    }

    @Test
    public void shouldAddProduct(){
        ProductRequest productRequest = new ProductRequest("iPhone","iPhone-13", BigDecimal.valueOf(100));
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl,productRequest, String.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("Product Successfully Created",response.getBody());
    }

    @Test
    public void shouldGetAllProduct(){
        ProductRequest productRequest = new ProductRequest("iPhone","iPhone-13", BigDecimal.valueOf(100));
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl,productRequest, String.class);

        ResponseEntity<List<ProductResponse>> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>(){}
        );

        List<ProductResponse> responseList = responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(false,responseList.isEmpty());
    }

}
