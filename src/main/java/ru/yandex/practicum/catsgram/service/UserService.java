package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {
    private final HashMap<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail().equals(null)) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (users.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextInt());
        user.setRegistrationDate(Instant.now());
        users.put(getNextInt(), user);
        return user;
    }

    public User update(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        User oldUser = users.get(user.getId());
        if (user.getUsername() != null && user.getPassword() != null && user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
            oldUser.setUsername(user.getUsername());
            oldUser.setPassword(user.getPassword());
        }
        return oldUser;
    }

    private long getNextInt() {
        return users.keySet().stream().mapToLong(Long::longValue).max().orElse(0) + 1;
    }

    public Optional<User> findUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }
}
