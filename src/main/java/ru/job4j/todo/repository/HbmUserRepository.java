package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbmUserRepository implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Optional<User> optionalUser = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            optionalUser = Optional.of(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        Session session = sf.openSession();
        Optional<User> optionalUser = Optional.empty();
        try {
            session.beginTransaction();
            optionalUser = session.createQuery(
                            "from User as u where u.email = :fEmail and "
                                    + "u.password = :fPassword", User.class)
                    .setParameter("fEmail", email)
                    .setParameter("fPassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return optionalUser;
    }
}
