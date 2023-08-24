package ru.job4j.todo.service;


import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.TimeZone;

public interface UserService {
    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findById(int id);

    Collection<TimeZone> getAllTimeZone();

}
