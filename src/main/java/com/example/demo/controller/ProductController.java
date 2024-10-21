package com.example.demo.controller;

import com.example.demo.services.ReactiveCrudService;
import com.example.demo.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@Component
@SessionScoped
public class ProductController {
    private Long productId;

    @Autowired
    private ReactiveCrudService<Product, Long> reactiveCrudService;

    private String apiMessage;
    private List<Product> products = new ArrayList<>();
    private Product newProduct = new Product();
    @PostConstruct
    public void init() {

        reactiveCrudService.getAll().subscribe(
                product -> {

                    products.add(product);
                    System.out.println("Received API data: " + product.getName());
                },
                error -> {

                    System.err.println("Error fetching data from API: " + error.getMessage());
                    apiMessage = "Error fetching data!";
                }
        );
    }

    public List<Product> getProducts() {
        return products;
    }

    public void loadProduct() {
        reactiveCrudService.getById(productId).subscribe(
                fetchedProduct -> this.newProduct = fetchedProduct,
                error -> System.err.println("Error fetching product: " + error.getMessage())
        );
    }

    public void addProduct() {
        reactiveCrudService.create(newProduct).subscribe(
                createdProduct -> {
                    products.add(createdProduct);
                    apiMessage = "Product created successfully: " + createdProduct.getName();
                    newProduct = new Product(); // Clear the form for new input
                },
                error -> {
                    apiMessage = "Error creating product: " + error.getMessage();
                }
        );
    }



    public void updateProduct(Long id, Product updatedProduct) {
        reactiveCrudService.update(id, updatedProduct).subscribe(
                updatedProductData -> {

                    int index = products.indexOf(updatedProductData);
                    if (index >= 0) {
                        products.set(index, updatedProductData);
                    }
                    System.out.println("Updated Product: " + updatedProductData.getName());
                },
                error -> {
                    System.err.println("Error updating product: " + error.getMessage());
                }
        );
    }

    public String prepareEdit(Long id) {
        this.productId = id;
        return "editProduct?faces-redirect=true";
    }
    public void deleteProduct(Long id) {
        reactiveCrudService.delete(id).subscribe(
                null,
                error -> {
                    System.err.println("Error deleting product: " + error.getMessage());
                },
                () -> {
                    products.clear();
                    init();
                    System.out.println("Product deleted successfully.");
                }
        );
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }


    public Long getProductId() {
        return productId;
    }


}
