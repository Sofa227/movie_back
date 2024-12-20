package dev.farhan.movieist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        boolean isAuthenticated = userService.authenticateUser(user.getUsername(), user.getPassword());
        if (isAuthenticated) {
            return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/wishlist/add")
    public ResponseEntity<?> addToWishlist(@RequestBody Map<String, String> payload) {
        User user = userService.addToWishlist(payload.get("username"), payload.get("movieId"));
        if (user != null) {
            return new ResponseEntity<>(user.getWishlist(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found or movie already in wishlist", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/wishlist/remove")
    public ResponseEntity<?> removeFromWishlist(@RequestBody Map<String, String> payload) {
        User user = userService.removeFromWishlist(payload.get("username"), payload.get("movieId"));
        if (user != null) {
            return new ResponseEntity<>(user.getWishlist(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/wishlist/{username}")
    public ResponseEntity<?> getWishlist(@PathVariable String username) {
        List<String> wishlist = userService.getWishlist(username);
        if (wishlist != null) {
            return new ResponseEntity<>(wishlist, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}

