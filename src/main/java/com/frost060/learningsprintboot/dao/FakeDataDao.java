package com.frost060.learningsprintboot.dao;

import com.frost060.learningsprintboot.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FakeDataDao implements UserDao {

    private final Map<UUID, User> database;

    public FakeDataDao() {
        database = new HashMap<>();
        UUID userId = UUID.randomUUID();
        database.put(userId,
                new User(userId, "John", "Doe", User.Gender.MALE, 22, "john.doe@gmail.com"));
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Optional<User> selectUserByUserId(UUID userId) {
        return Optional.ofNullable(database.get(userId));
    }

    @Override
    public int updateUser(User user) {
        database.put(user.getUserId(), user);
        return 1;
    }

    @Override
    public int deleteUserByUserId(UUID userId) {
        database.remove(userId);
        return 1;
    }

    @Override
    public int insertUser(UUID userId, User user) {
        database.put(userId, user);
        return 1;
    }
}
