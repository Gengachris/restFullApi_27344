package auca.ac.rw.question4_ecommerce_api.controller.ecommerce;



import auca.ac.rw.question4_ecommerce_api.model.ecommerce.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();

    public ProductController() {
        // Initialize with sample products
        products.add(new Product(1L, "iPhone 15", "Latest Apple smartphone", 999.99, "Electronics", 50, "Apple"));
        products.add(new Product(2L, "MacBook Pro", "Professional laptop", 1999.99, "Electronics", 25, "Apple"));
        products.add(new Product(3L, "Nike Air Max", "Running shoes", 129.99, "Footwear", 100, "Nike"));
        products.add(new Product(4L, "Adidas Ultraboost", "Running shoes", 179.99, "Footwear", 75, "Adidas"));
        products.add(new Product(5L, "Samsung Galaxy S23", "Android smartphone", 899.99, "Electronics", 40, "Samsung"));
        products.add(new Product(6L, "Sony Headphones", "Wireless noise cancelling headphones", 299.99, "Electronics", 30, "Sony"));
        products.add(new Product(7L, "Levi's Jeans", "Classic denim jeans", 79.99, "Clothing", 200, "Levi's"));
        products.add(new Product(8L, "H&M T-Shirt", "Cotton t-shirt", 19.99, "Clothing", 150, "H&M"));
        products.add(new Product(9L, "Kitchen Knife Set", "Professional kitchen knives", 149.99, "Home", 60, "Wusthof"));
        products.add(new Product(10L, "Coffee Maker", "Automatic drip coffee maker", 89.99, "Home", 80, "Keurig"));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        int startIndex = page * limit;
        int endIndex = Math.min(startIndex + limit, products.size());

        if (startIndex >= products.size()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        List<Product> paginatedProducts = products.subList(startIndex, endIndex);
        return new ResponseEntity<>(paginatedProducts, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> product = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();

        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> productsByCategory = products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        return new ResponseEntity<>(productsByCategory, HttpStatus.OK);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> productsByBrand = products.stream()
                .filter(product -> product.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());

        return new ResponseEntity<>(productsByBrand, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> matchingProducts = products.stream()
                .filter(product -> product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        product.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(matchingProducts, HttpStatus.OK);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {

        List<Product> productsInRange = products.stream()
                .filter(product -> product.getPrice() >= min && product.getPrice() <= max)
                .collect(Collectors.toList());

        return new ResponseEntity<>(productsInRange, HttpStatus.OK);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> inStockProducts = products.stream()
                .filter(product -> product.getStockQuantity() > 0)
                .collect(Collectors.toList());

        return new ResponseEntity<>(inStockProducts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        // Generate new ID
        long newId = products.stream()
                .mapToLong(Product::getProductId)
                .max()
                .orElse(0) + 1;
        product.setProductId(newId);

        products.add(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Optional<Product> existingProduct = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setCategory(updatedProduct.getCategory());
            product.setStockQuantity(updatedProduct.getStockQuantity());
            product.setBrand(updatedProduct.getBrand());

            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStockQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        Optional<Product> product = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();

        if (product.isPresent()) {
            Product p = product.get();
            p.setStockQuantity(quantity);
            return new ResponseEntity<>(p, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean removed = products.removeIf(product -> product.getProductId().equals(productId));

        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

