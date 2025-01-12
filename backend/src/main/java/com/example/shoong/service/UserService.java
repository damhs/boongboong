package com.example.shoong.service;

import com.example.shoong.entity.User;
import com.example.shoong.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Create a new user
  public User createUser(User user) {
    return userRepository.save(user);
  }

  // Get all users
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Get a user by ID
  public Optional<User> getUserById(String id) {
    return userRepository.findById(id);
  }

  // Update a user
  public User updateUser(String id, User userDetails) {
    return userRepository.findById(id).map(user -> {
      user.setName(userDetails.getName());
      user.setPassword(userDetails.getPassword());
      return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
  }

  // Delete a user
  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }
}
