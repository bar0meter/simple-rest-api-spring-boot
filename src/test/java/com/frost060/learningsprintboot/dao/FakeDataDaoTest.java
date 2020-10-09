package com.frost060.learningsprintboot.dao;

import com.frost060.learningsprintboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FakeDataDaoTest {

    private FakeDataDao fakeDataDao;

    @BeforeEach
    void setUp() {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    void shouldSelectAllUsers() {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getEmail()).isEqualTo("john.doe@gmail.com");
        assertThat(user.getUserId()).isNotNull();
    }

    @Test
    void shouldSelectUserByUserId() {
        UUID testUserId = UUID.randomUUID();
        User testUser = new User(testUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");
        fakeDataDao.insertUser(testUserId, testUser);

        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

        Optional<User> optionalTestUser = fakeDataDao.selectUserByUserId(testUserId);
        assertThat(optionalTestUser.isPresent()).isTrue();
        assertThat(optionalTestUser.get()).isEqualToComparingFieldByField(testUser);
    }

    @Test
    void shouldUpdateUser() {
        UUID userId = fakeDataDao.selectAllUsers().get(0).getUserId();
        User newUser = new User(userId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        fakeDataDao.updateUser(newUser);
        Optional<User> optionalNewUser = fakeDataDao.selectUserByUserId(userId);
        assertThat(optionalNewUser.isPresent()).isTrue();

        assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
        assertThat(optionalNewUser.get()).isEqualToComparingFieldByField(newUser);
    }

    @Test
    void shouldDeleteUserByUserId() {
        UUID userId = fakeDataDao.selectAllUsers().get(0).getUserId();
        fakeDataDao.deleteUserByUserId(userId);
        assertThat(fakeDataDao.selectUserByUserId(userId).isPresent()).isFalse();
        assertThat(fakeDataDao.selectAllUsers()).isEmpty();
    }

    @Test
    void shouldInsertUser() {
        UUID newUserId = UUID.randomUUID();
        User newUser = new User(newUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        fakeDataDao.insertUser(newUserId, newUser);
        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

        Optional<User> optionalTestUser = fakeDataDao.selectUserByUserId(newUserId);
        assertThat(optionalTestUser.isPresent()).isTrue();
        assertThat(optionalTestUser.get()).isEqualToComparingFieldByField(newUser);
    }
}
