package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.errors.UserAlreadyExistsError;
import ge.join2play.join2playback.model.errors.UserDoesNotExistError;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    public UserInMemoryRepository() {
        User user1 = new User(
                UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097"),
                "Kakha Salukvadze",
                "kakha@example.com",
                "+995555123456",
                LocalDate.of(1995, 6, 15),
                "Sports enthusiast and backend developer.",
                "securepassword123"
        );

        User user2 = new User(
                UUID.fromString("24fb6e3f-2dac-4eac-8df1-abc456789012"),
                "John Doe",
                "john.doe@example.com",
                "+995555987654",
                LocalDate.of(1990, 3, 22),
                "Loves football and tennis.",
                "john123"
        );

        User user3 = new User(
                UUID.fromString("35ac7d4e-3fac-5fab-9ed2-cba987654321"),
                "Jane Smith",
                "jane.smith@example.com",
                "+995599123789",
                LocalDate.of(1988, 11, 5),
                "Basketball player and outdoor enthusiast.",
                "jane123"
        );

        User user4 = new User(
                UUID.fromString("46bd8e5f-4abc-6a1c-8ef3-dcba87654321"),
                "Mike Johnson",
                "mike.j@example.com",
                "+995591234567",
                LocalDate.of(1992, 8, 10),
                "Runner and sports event organizer.",
                "mike123"
        );

        User user5 = new User(
                UUID.fromString("57ce9f60-5bcd-7b2d-9f04-edcba7654321"),
                "Emily Davis",
                "emily.d@example.com",
                "+995592345678",
                LocalDate.of(1993, 1, 30),
                "Volleyball fan and team leader.",
                "emily123"
        );

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);
        users.put(user4.getId(), user4);
        users.put(user5.getId(), user5);
    }

    @Override
    public User getById(UUID id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public User save(User user) {
        if (getById(user.getId()) != null) {
            throw new UserAlreadyExistsError("Cannot save: user with ID " + user.getId() + "already exists.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = users.replace(user.getId(), user);
        if (oldUser == null) {
            throw new UserDoesNotExistError("Cannot update: user with ID " + user.getId() + " does not exist.");
        }
        return user;
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public List<User> findByNameStartsWithIgnoreCaseOrEmailStartsWithIgnoreCase(String queryString) {
        String query = queryString.toLowerCase();

        return users.values().stream()
                .filter(user -> user.getName().toLowerCase().startsWith(query) ||
                        user.getEmail().toLowerCase().startsWith(query))
                .collect(Collectors.toList());
    }
}
