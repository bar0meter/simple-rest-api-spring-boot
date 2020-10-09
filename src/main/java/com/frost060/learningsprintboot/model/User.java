package com.frost060.learningsprintboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private final UUID userId;

    @NotNull
    private final String firstName;

    @NotNull
    private final String lastName;

    @NotNull
    private final Gender gender;

    @NotNull
    @Min(value = 0)
    @Max(value = 150)
    private final int age;

    @NotNull
    @Email
    private final String email;

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public User(
            @JsonProperty("userId") UUID userId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("gender") Gender gender,
            @JsonProperty("age") int age,
            @JsonProperty("email") String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getDateOfBirth() {
        return LocalDate.now().minusYears(age).getYear();
    }

    public static User newUser(UUID userId, User old) {
        return new User(userId, old.getFirstName(), old.getLastName(),
                old.getGender(), old.getAge(), old.getEmail());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
