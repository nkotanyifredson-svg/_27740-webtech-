package auca.ac.rw.RestfullApi.controller.user;


import auca.ac.rw.RestfullApi.model.user.UserProfile;
import auca.ac.rw.RestfullApi.model.user.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private List<UserProfile> users = new ArrayList<>();
    private Long nextId = 7L;

    
    public UserProfileController() {
       
        users.add(new UserProfile(1L, "john_doe", "john.doe@email.com", "John Doe", 
                                  28, "USA", "Software developer and tech enthusiast", true));
        users.add(new UserProfile(2L, "jane_smith", "jane.smith@email.com", "Jane Smith", 
                                  32, "Canada", "Digital marketing specialist", true));
        users.add(new UserProfile(3L, "mike_wilson", "mike.wilson@email.com", "Mike Wilson", 
                                  45, "UK", "Project manager with 15 years experience", false));
        users.add(new UserProfile(4L, "sarah_johnson", "sarah.j@email.com", "Sarah Johnson", 
                                  23, "Australia", "Recent graduate in Computer Science", true));
        users.add(new UserProfile(5L, "ahmed_hassan", "ahmed.h@email.com", "Ahmed Hassan", 
                                  35, "Egypt", "Full-stack developer and instructor", true));
        users.add(new UserProfile(6L, "maria_garcia", "maria.g@email.com", "Maria Garcia", 
                                  29, "Spain", "UX/UI designer and artist", false));
    }

    
    private <T> ApiResponse<T> createSuccessResponse(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

   
    private <T> ApiResponse<T> createErrorResponse(String message) {
        return new ApiResponse<>(false, message, null);
    }

    
    private UserProfile findUserById(Long userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

   
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllUsers() {
        ApiResponse<List<UserProfile>> response = createSuccessResponse(
            "Users retrieved successfully", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserById(@PathVariable Long userId) {
        UserProfile user = findUserById(userId);
        
        if (user != null) {
            ApiResponse<UserProfile> response = createSuccessResponse(
                "User found successfully", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<UserProfile> response = createErrorResponse(
                "User not found with ID: " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createUser(@RequestBody UserProfile user) {
       
        boolean usernameExists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(user.getUsername()));
        
        if (usernameExists) {
            ApiResponse<UserProfile> response = createErrorResponse(
                "Username already exists: " + user.getUsername());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        
      
        boolean emailExists = users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
        
        if (emailExists) {
            ApiResponse<UserProfile> response = createErrorResponse(
                "Email already exists: " + user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        
        user.setUserId(nextId++);
        user.setActive(true); 
        users.add(user);
        
        ApiResponse<UserProfile> response = createSuccessResponse(
            "User profile created successfully", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

   
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateUser(
            @PathVariable Long userId, @RequestBody UserProfile updatedUser) {
        
        UserProfile existingUser = findUserById(userId);
        
        if (existingUser != null) {
            
            if (!existingUser.getUsername().equals(updatedUser.getUsername())) {
                boolean usernameExists = users.stream()
                        .anyMatch(u -> u.getUsername().equalsIgnoreCase(updatedUser.getUsername()));
                if (usernameExists) {
                    ApiResponse<UserProfile> response = createErrorResponse(
                        "Username already exists: " + updatedUser.getUsername());
                    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
                }
            }
            
            
            if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
                boolean emailExists = users.stream()
                        .anyMatch(u -> u.getEmail().equalsIgnoreCase(updatedUser.getEmail()));
                if (emailExists) {
                    ApiResponse<UserProfile> response = createErrorResponse(
                        "Email already exists: " + updatedUser.getEmail());
                    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
                }
            }
            
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setCountry(updatedUser.getCountry());
            existingUser.setBio(updatedUser.getBio());
            
            ApiResponse<UserProfile> response = createSuccessResponse(
                "User profile updated successfully", existingUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<UserProfile> response = createErrorResponse(
                "User not found with ID: " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        UserProfile user = findUserById(userId);
        
        if (user != null) {
            users.remove(user);
            ApiResponse<Void> response = createSuccessResponse(
                "User profile deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<Void> response = createErrorResponse(
                "User not found with ID: " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/search/username")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByUsername(
            @RequestParam String q) {
        
        List<UserProfile> matchingUsers = users.stream()
                .filter(user -> user.getUsername().toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
        
        if (!matchingUsers.isEmpty()) {
            ApiResponse<List<UserProfile>> response = createSuccessResponse(
                "Found " + matchingUsers.size() + " user(s) matching username: " + q, 
                matchingUsers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<List<UserProfile>> response = createErrorResponse(
                "No users found with username containing: " + q);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/country")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByCountry(
            @RequestParam String q) {
        
        List<UserProfile> matchingUsers = users.stream()
                .filter(user -> user.getCountry().toLowerCase().contains(q.toLowerCase()))
                .collect(Collectors.toList());
        
        if (!matchingUsers.isEmpty()) {
            ApiResponse<List<UserProfile>> response = createSuccessResponse(
                "Found " + matchingUsers.size() + " user(s) from " + q, 
                matchingUsers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<List<UserProfile>> response = createErrorResponse(
                "No users found from: " + q);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/age-range")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByAgeRange(
            @RequestParam int min,
            @RequestParam int max) {
        
        if (min > max) {
            ApiResponse<List<UserProfile>> response = createErrorResponse(
                "Invalid age range: min age cannot be greater than max age");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        List<UserProfile> matchingUsers = users.stream()
                .filter(user -> user.getAge() >= min && user.getAge() <= max)
                .collect(Collectors.toList());
        
        if (!matchingUsers.isEmpty()) {
            ApiResponse<List<UserProfile>> response = createSuccessResponse(
                "Found " + matchingUsers.size() + " user(s) between ages " + min + " and " + max, 
                matchingUsers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<List<UserProfile>> response = createErrorResponse(
                "No users found between ages " + min + " and " + max);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/search/active")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getActiveUsers() {
        List<UserProfile> activeUsers = users.stream()
                .filter(UserProfile::isActive)
                .collect(Collectors.toList());
        
        ApiResponse<List<UserProfile>> response = createSuccessResponse(
            "Found " + activeUsers.size() + " active user(s)", activeUsers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/search/inactive")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getInactiveUsers() {
        List<UserProfile> inactiveUsers = users.stream()
                .filter(user -> !user.isActive())
                .collect(Collectors.toList());
        
        ApiResponse<List<UserProfile>> response = createSuccessResponse(
            "Found " + inactiveUsers.size() + " inactive user(s)", inactiveUsers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateUser(@PathVariable Long userId) {
        UserProfile user = findUserById(userId);
        
        if (user != null) {
            if (user.isActive()) {
                ApiResponse<UserProfile> response = createErrorResponse(
                    "User is already active");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            user.setActive(true);
            ApiResponse<UserProfile> response = createSuccessResponse(
                "User account activated successfully", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<UserProfile> response = createErrorResponse(
                "User not found with ID: " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateUser(@PathVariable Long userId) {
        UserProfile user = findUserById(userId);
        
        if (user != null) {
            if (!user.isActive()) {
                ApiResponse<UserProfile> response = createErrorResponse(
                    "User is already inactive");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            user.setActive(false);
            ApiResponse<UserProfile> response = createSuccessResponse(
                "User account deactivated successfully", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<UserProfile> response = createErrorResponse(
                "User not found with ID: " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{userId}/toggle-status")
    public ResponseEntity<ApiResponse<UserProfile>> toggleUserStatus(@PathVariable Long userId) {
        UserProfile user = findUserById(userId);
        
        if (user != null) {
            user.setActive(!user.isActive());
            String status = user.isActive() ? "activated" : "deactivated";
            ApiResponse<UserProfile> response = createSuccessResponse(
                "User account " + status + " successfully", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse<UserProfile> response = createErrorResponse(
                "User not found with ID: " + userId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}