package com.frost060.learningsprintboot.service;

import com.frost060.learningsprintboot.dao.FakeDataDao;
import com.frost060.learningsprintboot.model.User;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private FakeDataDao fakeDataDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    void shouldGetAllUsers() {
        UUID newUserId = UUID.randomUUID();
        User newUser = new User(newUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        ImmutableList<User> users = new ImmutableList.Builder<User>()
                .add(newUser)
                .build();

        given(fakeDataDao.selectAllUsers()).willReturn(users);
        List<User> allUsers = userService.getAllUsers(Optional.empty());

        assertThat(allUsers).hasSize(1);

        User user = allUsers.get(0);

        assertUserFields(user);
    }

    @Test
    public void shouldGetAllUsersByGender() {
        UUID newUserId = UUID.randomUUID();
        User newUser = new User(newUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        UUID newUserTestId = UUID.randomUUID();
        User newUserTest = new User(newUserTestId, "newTest", "testOther", User.Gender.OTHER, 23, "newTestOther@gmail.com");

        ImmutableList<User> users = new ImmutableList.Builder<User>()
                .add(newUser)
                .add(newUserTest)
                .build();

        given(fakeDataDao.selectAllUsers()).willReturn(users);
        List<User> filteredUsers = userService.getAllUsers(Optional.of("OTHER"));

        assertThat(filteredUsers).hasSize(1);
        User user = filteredUsers.get(0);
        assertUserTestFields(user);
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsInvalid() {
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("asdasda")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid gender");
    }

    @Test
    void shouldGetUser() {
        UUID newUserId = UUID.randomUUID();
        User newUser = new User(newUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        given(fakeDataDao.selectUserByUserId(newUserId)).willReturn(Optional.of(newUser));

        Optional<User> optionalUser = userService.getUser(newUserId);
        assertThat(optionalUser.isPresent()).isTrue();
        assertUserFields(optionalUser.get());
    }

    @Test
    void shouldUpdateUser() {
        UUID newUserId = UUID.randomUUID();
        User newUser = new User(newUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        given(fakeDataDao.selectUserByUserId(newUserId)).willReturn(Optional.of(newUser));
        given(fakeDataDao.updateUser(newUser)).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        int updateResult = userService.updateUser(newUser);

        verify(fakeDataDao).selectUserByUserId(newUserId);
        verify(fakeDataDao).updateUser(captor.capture());

        User user = captor.getValue();
        assertUserFields(user);

        assertThat(updateResult).isEqualTo(1);
    }

    @Test
    void shouldRemoveUser() {
        UUID newUserId = UUID.randomUUID();
        User newUser = new User(newUserId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        given(fakeDataDao.selectUserByUserId(newUserId)).willReturn(Optional.of(newUser));
        given(fakeDataDao.deleteUserByUserId(newUserId)).willReturn(1);

        int deleteResult = userService.removeUser(newUserId);

        verify(fakeDataDao).selectUserByUserId(newUserId);
        verify(fakeDataDao).deleteUserByUserId(newUserId);

        assertThat(deleteResult).isEqualTo(1);
    }

    @Test
    void shouldInsertUser() {
        UUID userId = UUID.randomUUID();
        User newUser = new User(userId, "test", "girl", User.Gender.FEMALE, 30, "test.girl@gmail.com");

        given(fakeDataDao.insertUser(any(UUID.class), any(User.class))).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        int insertResult = userService.insertUser(newUser);

        verify(fakeDataDao).insertUser(eq(userId), captor.capture());

        User user = captor.getValue();

        assertUserFields(user);
        assertThat(insertResult).isEqualTo(1);
    }

    public void assertUserFields(User user) {
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getFirstName()).isEqualTo("test");
        assertThat(user.getLastName()).isEqualTo("girl");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getEmail()).isEqualTo("test.girl@gmail.com");
        assertThat(user.getUserId()).isNotNull();
    }

    public void assertUserTestFields(User user) {
        assertThat(user.getAge()).isEqualTo(23);
        assertThat(user.getFirstName()).isEqualTo("newTest");
        assertThat(user.getLastName()).isEqualTo("testOther");
        assertThat(user.getGender()).isEqualTo(User.Gender.OTHER);
        assertThat(user.getEmail()).isEqualTo("newTestOther@gmail.com");
        assertThat(user.getUserId()).isNotNull();
    }
}
