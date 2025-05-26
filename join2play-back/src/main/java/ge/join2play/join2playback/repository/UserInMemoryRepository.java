package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.errors.UserAlreadyExistsError;
import ge.join2play.join2playback.model.errors.UserDoesNotExistError;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    public UserInMemoryRepository() {
        UUID userId = UUID.fromString("13fa5e4e-1d9e-4a2a-9a20-7385f24e9097");

        User user = new User(
                userId,
                "Kakha Salukvadze",
                "kakha@example.com",
                "+995555123456",
                LocalDate.of(1995, 6, 15),
                "Sports enthusiast and backend developer.",
                "securepassword123"
        );

        users.put(userId, user);
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
}
