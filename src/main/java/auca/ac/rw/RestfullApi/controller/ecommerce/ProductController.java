package auca.ac.rw.RestfullApi.controller.ecommerce;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.rw.RestfullApi.model.ecommerce.Product;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();
    private Long nextId = 11L; 

    public ProductController() {
       
        products.add(new Product(1L, "iPhone 14 Pro", "Latest Apple smartphone with A16 chip", 1099.99, "Electronics", 15, "Apple"));
        products.add(new Product(2L, "Samsung Galaxy S23", "Android flagship with amazing camera", 899.99, "Electronics", 20, "Samsung"));
        products.add(new Product(3L, "Sony WH-1000XM4", "Wireless noise-cancelling headphones", 349.99, "Electronics", 8, "Sony"));
        
        
        products.add(new Product(4L, "MacBook Pro 16", "Apple M2 Pro chip, 16-inch display", 2499.99, "Laptops", 5, "Apple"));
        products.add(new Product(5L, "Dell XPS 15", "Premium Windows laptop with 4K display", 1899.99, "Laptops", 7, "Dell"));
        products.add(new Product(6L, "Lenovo ThinkPad X1", "Business laptop with great keyboard", 1599.99, "Laptops", 10, "Lenovo"));
        
        
        products.add(new Product(7L, "Nike Air Max 270", "Comfortable sneakers for daily wear", 149.99, "Clothing", 25, "Nike"));
        products.add(new Product(8L, "Adidas Ultraboost", "Running shoes with responsive cushioning", 179.99, "Clothing", 12, "Adidas"));
        products.add(new Product(9L, "Levi's 501 Jeans", "Classic straight fit jeans", 89.99, "Clothing", 30, "Levi's"));
        
        
        products.add(new Product(10L, "Clean Code", "Robert Martin's guide to writing better code", 45.99, "Books", 50, "Pearson"));
    }

    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        int start = page * limit;
        int end = Math.min(start + limit, products.size());
        
        if (start >= products.size()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        
        List<Product> paginatedProducts = products.subList(start, end);
        return new ResponseEntity<>(paginatedProducts, HttpStatus.OK);
    }

   
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = findProductById(productId);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> productsByCategory = products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        
        if (!productsByCategory.isEmpty()) {
            return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

  
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> productsByBrand = products.stream()
                .filter(product -> product.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
        
        if (!productsByBrand.isEmpty()) {
            return new ResponseEntity<>(productsByBrand, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> matchingProducts = products.stream()
                .filter(product -> 
                    product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        
        if (!matchingProducts.isEmpty()) {
            return new ResponseEntity<>(matchingProducts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        
        List<Product> productsInRange = products.stream()
                .filter(product -> product.getPrice() >= min && product.getPrice() <= max)
                .collect(Collectors.toList());
        
        if (!productsInRange.isEmpty()) {
            return new ResponseEntity<>(productsInRange, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> inStockProducts = products.stream()
                .filter(product -> product.getStockQuantity() > 0)
                .collect(Collectors.toList());
        
        if (!inStockProducts.isEmpty()) {
            return new ResponseEntity<>(inStockProducts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        product.setProductId(nextId++);
        products.add(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Product existingProduct = findProductById(productId);
        
        if (existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setBrand(updatedProduct.getBrand());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            
            return new ResponseEntity<>(existingProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStockQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        
        Product product = findProductById(productId);
        
        if (product != null) {
            product.setStockQuantity(quantity);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        Product product = findProductById(productId);
        
        if (product != null) {
            products.remove(product);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Product findProductById(Long productId) {
        return products.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}