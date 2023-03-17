package com.example.sbmongodbreactive.service;

import com.example.sbmongodbreactive.dto.ProductDto;
import com.example.sbmongodbreactive.repositories.ProductRepository;
import com.example.sbmongodbreactive.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll().map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id) {
        return productRepository.findById(id).map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductByPriceInRange(double min, double max) {
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(AppUtils::dtoToEntity) // just convert to dto
                .flatMap(productRepository::insert) // use flatMap to take from Mono and save
                .map(AppUtils::entityToDto); // convert back to dto to return
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return productRepository.findById(id) // find from DB
                .flatMap(product -> productDtoMono.map(AppUtils::dtoToEntity) // convert from Mono to entity
                        .doOnNext(e -> e.setId(id))) // and then set ID to map
                .flatMap(productRepository::save) // save it
                .map(AppUtils::entityToDto); // return as dto
    }
    
    public Mono<Void> deleteProduct(String id) {
        return productRepository.deleteById(id);
    }
}
