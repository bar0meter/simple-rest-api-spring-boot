package com.frost060.learningsprintboot.it;

import com.frost060.learningsprintboot.clientproxy.UserResourceV1;
import com.frost060.learningsprintboot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class UserIT {

    @Autowired
    private UserResourceV1 userResourceV1;

    @Test
    void shouldInsertUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John", "Doe",
                User.Gender.MALE, 22, "john.doe@gmail.com");

        // When
        userResourceV1.insertNewUser(user);

        // Then
        User john = userResourceV1.fetchUser(userId);
        assertThat(john).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldDeleteUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John", "Doe",
                User.Gender.MALE, 22, "john.doe@gmail.com");

        // When
        userResourceV1.insertNewUser(user);

        // Then
        User john = userResourceV1.fetchUser(userId);
        assertThat(john).isEqualToComparingFieldByField(user);

        // When
        userResourceV1.deleteUser(userId);

        // Then
        assertThatThrownBy(() -> userResourceV1.fetchUser(userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldUpdateUser() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John", "Doe",
                User.Gender.MALE, 22, "john.doe@gmail.com");

        // When
        userResourceV1.insertNewUser(user);

        User updatedUser = new User(userId, "Alex", "Doe",
                User.Gender.MALE, 32, "alex.doe@gmail.com");

        userResourceV1.updateUser(updatedUser);

        // Then
        User alex = userResourceV1.fetchUser(userId);
        assertThat(alex).isEqualToComparingFieldByField(updatedUser);
    }

    @Test
    void shouldFetchUsersByGender() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "John", "Doe",
                User.Gender.MALE, 22, "john.doe@gmail.com");

        // When
        userResourceV1.insertNewUser(user);

        List<User> femaleUsers = userResourceV1.fetchUsers(User.Gender.FEMALE.name());
        assertThat(femaleUsers).extracting("userId").doesNotContain(user.getUserId());
        assertThat(femaleUsers).extracting("firstName").doesNotContain(user.getFirstName());
        assertThat(femaleUsers).extracting("lastName").doesNotContain(user.getLastName());
        assertThat(femaleUsers).extracting("age").doesNotContain(user.getAge());
        assertThat(femaleUsers).extracting("email").doesNotContain(user.getEmail());

        List<User> maleUsers = userResourceV1.fetchUsers(User.Gender.MALE.name());
        assertThat(maleUsers).extracting("userId").contains(user.getUserId());
        assertThat(maleUsers).extracting("firstName").contains(user.getFirstName());
        assertThat(maleUsers).extracting("lastName").contains(user.getLastName());
        assertThat(maleUsers).extracting("age").contains(user.getAge());
        assertThat(maleUsers).extracting("email").contains(user.getEmail());
    }
}
