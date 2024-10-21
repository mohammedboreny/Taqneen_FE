package com.example.demo.services;

import com.example.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class ReactiveApiService implements ReactiveCrudService<Product,Long> {

    private final WebClient webClient;

    public ReactiveApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/").build();
    }


    @Override
    public Flux<Product> getAll() {
        return webClient.get()
                .uri("product")
                .retrieve()
                .bodyToFlux(Product.class);
    }
    @Override
    public Flux<Product> getById(Long id) {
        return webClient.get()
                .uri("product/{id}", id)
                .retrieve()
                .bodyToFlux(Product.class);
    }
    @Override
    public Flux<Product> create(Product product) {
        return webClient.post()
                .uri("product")
                .bodyValue(product)
                .retrieve()
                .bodyToFlux(Product.class);
    }

    @Override
    public Flux<Product> update(Long id, Product product) {
        return webClient.put()
                .uri("product/{id}", id)
                .bodyValue(product)
                .retrieve()
                .bodyToFlux(Product.class);
    }

    @Override
    public Flux<String> delete(Long id) {
        return webClient.delete()
                .uri("product/{id}", id)
                .retrieve()
                .bodyToFlux(String.class);
    }
}
