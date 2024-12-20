package dev.farhan.movieist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User registerUser(User user) {
        user.setPassword(hashPassword(user.getPassword()));
        return repository.save(user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    public boolean authenticateUser(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean isUserAuthenticated(String username) {
        return repository.findByUsername(username).isPresent();
    }

    public User addToWishlist(String username, String movieId) {
        User user = findByUsername(username);
        if (user != null && !user.getWishlist().contains(movieId)) {
            user.getWishlist().add(movieId);
            return repository.save(user);
        }
        return user;
    }

    public User removeFromWishlist(String username, String movieId) {
        User user = findByUsername(username);
        if (user != null) {
            user.getWishlist().remove(movieId);
            return repository.save(user);
        }
        return user;
    }

    public List<String> getWishlist(String username) {
        User user = findByUsername(username);
        return user != null ? user.getWishlist() : null;
    }
}

