package services;

import models.User;
import repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static User createUser(String name, String email, String phone) {
        String newUserId = UUID.randomUUID().toString().substring(0, 3);
        return new User(newUserId, name, email, phone);
    }

    public void saveUser(User user) {
        userRepository.addUser(user);
    }

    public User getUser(String userId) throws Exception {
        return this.userRepository.getUser(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
