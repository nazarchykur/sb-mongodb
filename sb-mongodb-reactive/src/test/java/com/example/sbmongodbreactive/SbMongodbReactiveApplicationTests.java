package com.example.sbmongodbreactive;

import com.example.sbmongodbreactive.controllers.ProductController;
import com.example.sbmongodbreactive.dto.ProductDto;
import com.example.sbmongodbreactive.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProductController.class)
class SbMongodbReactiveApplicationTests {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private ProductService productService;

    @Test
    void saveProduct() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("1", "phone", 1, 749.99));
        when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post().uri("/api/v1/products")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk(); //200
    }

    @Test
    void getAllProducts() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("1", "phone", 1, 749.99),
                new ProductDto("2", "laptop", 1, 2000));
        when(productService.getAllProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("1", "phone", 1, 749.99))
                .expectNext(new ProductDto("2", "laptop", 1, 2000))
                .verifyComplete();
    }

    @Test
    void getProductById() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("1", "phone", 1, 749.99));
        when(productService.getProductById(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/api/v1/products/1")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getName().equals("phone"))
                .verifyComplete();
    }

    @Test
    void updateProduct() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("1", "phone", 1, 749.99));
        when(productService.updateProduct(productDtoMono, "1")).thenReturn(productDtoMono);

        webTestClient.put().uri("/api/v1/products/1")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk(); //200
    }

    @Test
    public void deleteProduct() {
        when(productService.deleteProduct(any())).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/products/1")
                .exchange()
                .expectStatus().isOk(); //200
    }
}
