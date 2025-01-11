package com.example.boong_boong.service;

import com.example.boong_boong.entity.User;
import com.example.boong_boong.repository.UserRepository;
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
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  // Update a user
  public User updateUser(Long id, User userDetails) {
    return userRepository.findById(id).map(user -> {
      user.setName(userDetails.getName());
      user.setEmail(userDetails.getEmail());
      return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
  }

  // Delete a user
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
