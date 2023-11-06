package repositories;

import models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    Map<String, User> userMap;

    public User getUser(String userId) throws Exception {
        if(!userMap.containsKey(userId)) {
            throw new Exception("User Id does not exist");
        }

        return userMap.get(userId);
    }

    public UserRepository() {
        this.userMap = new HashMap<>();
    }

    public String addUser(User user) {
        userMap.put(user.getId(), user);
        return user.getId();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        for(Map.Entry<String, User> user: this.userMap.entrySet()) {
            users.add(user.getValue());
        }
        return users;
    }
}
