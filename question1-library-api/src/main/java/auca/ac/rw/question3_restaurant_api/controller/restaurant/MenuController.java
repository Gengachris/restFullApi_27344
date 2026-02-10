package auca.ac.rw.question3_restaurant_api.controller.restaurant;



import auca.ac.rw.question3_restaurant_api.model.restaurant.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private List<MenuItem> menuItems = new ArrayList<>();

    public MenuController() {
        // Initialize with sample menu items
        menuItems.add(new MenuItem(1L, "Caesar Salad", "Fresh romaine lettuce with Caesar dressing", 8.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Garlic Bread", "Toasted bread with garlic butter", 4.99, "Appetizer", true));
        menuItems.add(new MenuItem(3L, "Grilled Salmon", "Fresh salmon with lemon herb seasoning", 18.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Chicken Parmesan", "Breaded chicken with marinara sauce and cheese", 16.99, "Main Course", true));
        menuItems.add(new MenuItem(5L, "Beef Steak", "Premium beef steak cooked to perfection", 24.99, "Main Course", true));
        menuItems.add(new MenuItem(6L, "Chocolate Cake", "Rich chocolate cake with ganache", 6.99, "Dessert", true));
        menuItems.add(new MenuItem(7L, "Apple Pie", "Homemade apple pie with vanilla ice cream", 5.99, "Dessert", false));
        menuItems.add(new MenuItem(8L, "Coffee", "Freshly brewed coffee", 2.99, "Beverage", true));
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        Optional<MenuItem> menuItem = menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();

        if (menuItem.isPresent()) {
            return new ResponseEntity<>(menuItem.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> itemsByCategory = menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        return new ResponseEntity<>(itemsByCategory, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems(@RequestParam boolean available) {
        List<MenuItem> availableItems = menuItems.stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());

        return new ResponseEntity<>(availableItems, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByName(@RequestParam String name) {
        List<MenuItem> matchingItems = menuItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(matchingItems, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        // Generate new ID
        long newId = menuItems.stream()
                .mapToLong(MenuItem::getId)
                .max()
                .orElse(0) + 1;
        menuItem.setId(newId);

        menuItems.add(menuItem);
        return new ResponseEntity<>(menuItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        Optional<MenuItem> menuItem = menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();

        if (menuItem.isPresent()) {
            MenuItem item = menuItem.get();
            item.setAvailable(!item.isAvailable());
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMenuItem(@PathVariable Long id) {
        boolean removed = menuItems.removeIf(item -> item.getId().equals(id));

        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

