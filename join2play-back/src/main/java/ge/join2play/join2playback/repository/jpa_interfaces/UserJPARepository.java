package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJPARepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT(:queryString, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT(:queryString, '%'))")
    List<User> findByNameStartsWithIgnoreCaseOrEmailStartsWithIgnoreCase(@Param("queryString") String queryString);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.email) = LOWER(:emailOrName) OR " +
            "LOWER(u.name) = LOWER(:emailOrName)")
    Optional<User> findByEmailOrNameIgnoreCase(@Param("emailOrName") String emailOrName);

    @Query("SELECT u FROM User u WHERE u.birthDate BETWEEN :startDate AND :endDate")
    List<User> findByBirthDateBetween(@Param("startDate") Long startDate, @Param("endDate") Long endDate);
}