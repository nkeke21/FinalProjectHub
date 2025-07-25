package ge.join2play.join2playback.repository.interfaces;

import ge.join2play.join2playback.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> getById(UUID id);

    User save(User user);

    User update(User user);

    void delete(UUID id);

    List<User> getAll();

    void clear();

    List<User> findByNameStartsWithIgnoreCaseOrEmailStartsWithIgnoreCase(String queryString);

    User findByEmail(String email);

    User findByEmailOrName(String emailOrName);
}
