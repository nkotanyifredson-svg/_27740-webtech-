package auca.ac.rw.RestfullApi.controller.restaurant;


import auca.ac.rw.RestfullApi.model.restaurant.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private List<MenuItem> menuItems = new ArrayList<>();
    private Long nextId = 9L; 

    
    public MenuController() {
     
        menuItems.add(new MenuItem(1L, "Bruschetta", "Toasted bread with tomatoes, garlic, and basil", 8.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Calamari", "Fried squid with marinara sauce", 12.99, "Appetizer", true));
        
        
        menuItems.add(new MenuItem(3L, "Grilled Salmon", "Fresh salmon with lemon butter sauce", 24.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Beef Steak", "8oz ribeye steak with mashed potatoes", 29.99, "Main Course", true));
        menuItems.add(new MenuItem(5L, "Vegetable Pasta", "Penne pasta with seasonal vegetables", 16.99, "Main Course", false));
        
      
        menuItems.add(new MenuItem(6L, "Tiramisu", "Classic Italian dessert", 7.99, "Dessert", true));
        menuItems.add(new MenuItem(7L, "Chocolate Lava Cake", "Warm cake with molten center", 8.99, "Dessert", true));
        
        
        menuItems.add(new MenuItem(8L, "House Wine", "Glass of red or white wine", 9.99, "Beverage", true));
    }

   
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return new ResponseEntity<>(menuItems, HttpStatus.OK);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        MenuItem item = findMenuItemById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getItemsByCategory(@PathVariable String category) {
        List<MenuItem> itemsByCategory = menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        
        if (!itemsByCategory.isEmpty()) {
            return new ResponseEntity<>(itemsByCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableItems(@RequestParam boolean available) {
        List<MenuItem> availableItems = menuItems.stream()
                .filter(item -> item.isAvailable() == available)
                .collect(Collectors.toList());
        
        if (!availableItems.isEmpty()) {
            return new ResponseEntity<>(availableItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchItemsByName(@RequestParam String name) {
        List<MenuItem> matchingItems = menuItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        
        if (!matchingItems.isEmpty()) {
            return new ResponseEntity<>(matchingItems, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        menuItem.setId(nextId++);
        menuItems.add(menuItem);
        return new ResponseEntity<>(menuItem, HttpStatus.CREATED);
    }

    
    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        MenuItem item = findMenuItemById(id);
        
        if (item != null) {
            item.setAvailable(!item.isAvailable());
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        MenuItem item = findMenuItemById(id);
        
        if (item != null) {
            menuItems.remove(item);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    private MenuItem findMenuItemById(Long id) {
        return menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}