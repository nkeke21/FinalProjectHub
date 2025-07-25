package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.exceptions.UserAlreadyExistsException;
import ge.join2play.join2playback.model.exceptions.UserDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("userRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class UserDBRepository implements UserRepository {

    private final UserJPARepository userJPARepository;

    @Autowired
    public UserDBRepository(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    @Override
    public Optional<User> getById(UUID id) {
        return userJPARepository.findById(id);
    }

    @Override
    public User save(User user) {
        if (user.getId() != null && userJPARepository.existsById(user.getId())) {
            throw new UserAlreadyExistsException("Cannot save: user with ID " + user.getId() + " already exists.");
        }

        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }

        return userJPARepository.save(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || !userJPARepository.existsById(user.getId())) {
            throw new UserDoesNotExistException("Cannot update: user with ID " + user.getId() + " does not exist.");
        }

        return userJPARepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        userJPARepository.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userJPARepository.findAll();
    }

    @Override
    public void clear() {
        userJPARepository.deleteAll();
    }

    @Override
    public List<User> findByNameStartsWithIgnoreCaseOrEmailStartsWithIgnoreCase(String queryString) {
        return userJPARepository.findByNameStartsWithIgnoreCaseOrEmailStartsWithIgnoreCase(queryString);
    }

    @Override
    public User findByEmail(String email) {
        return userJPARepository.findByEmailIgnoreCase(email).orElse(null);
    }

    @Override
    public User findByEmailOrName(String emailOrName) {
        return userJPARepository.findByEmailOrNameIgnoreCase(emailOrName).orElse(null);
    }
}
