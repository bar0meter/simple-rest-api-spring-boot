package com.frost060.learningsprintboot.service;

import com.frost060.learningsprintboot.dao.UserDao;
import com.frost060.learningsprintboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers(Optional<String> gender) {
        List<User> users = userDao.selectAllUsers();
        if (gender.isEmpty()) {
            return users;
        }
        try {
            User.Gender theGender = User.Gender.valueOf(gender.get().toUpperCase());
            return users.stream()
                    .filter(user -> user.getGender().equals(theGender))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new IllegalStateException("Invalid gender", ex);
        }
    }

    public Optional<User> getUser(UUID userId) {
        return userDao.selectUserByUserId(userId);
    }

    public int updateUser(User user) {
        Optional<User> optionalUser = getUser(user.getUserId());
        if (optionalUser.isPresent()) {
            return userDao.updateUser(user);
        }
        throw new NotFoundException("user" + user.getUserId() + " not found");
    }

    public int removeUser(UUID userId) {
        UUID id = getUser(userId)
                .map(User::getUserId)
                .orElseThrow(() -> new NotFoundException("user" + userId + " not found"));
        return userDao.deleteUserByUserId(id);
    }

    public int insertUser(User user) {
        UUID userId = user.getUserId() == null ? UUID.randomUUID() : user.getUserId();
        return userDao.insertUser(userId, User.newUser(userId, user));
    }
}
