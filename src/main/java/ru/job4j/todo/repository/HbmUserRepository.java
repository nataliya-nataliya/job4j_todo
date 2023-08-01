package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<User> optionalUser;
        try {
            crudRepository.run(session -> session.persist(user));
            optionalUser = Optional.of(user);
        } catch (PersistenceException e) {
            optionalUser = Optional.empty();
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return crudRepository.optional(
                "from User as u where u.email = :fEmail and "
                        + "u.password = :fPassword", User.class,
                Map.of("fEmail", email, "fPassword", password)
        );
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                "from User as u where u.id = :fId", User.class,
                Map.of("fId", id)
        );
    }
}
