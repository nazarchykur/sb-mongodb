package com.example.sbmongodbreactive.repositories;

import com.example.sbmongodbreactive.dto.ProductDto;
import com.example.sbmongodbreactive.entities.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
    
}
