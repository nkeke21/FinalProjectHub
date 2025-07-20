package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TournamentRegistration;
import ge.join2play.join2playback.model.enums.RegistrationStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class TournamentRegistrationInMemoryRepository implements TournamentRegistrationRepository {
    private final Map<UUID, TournamentRegistration> registrations = new ConcurrentHashMap<>();

    public TournamentRegistrationInMemoryRepository() {
    }

    @Override
    public TournamentRegistration save(TournamentRegistration registration) {
        if (registration.getId() == null) {
            registration.setId(UUID.randomUUID());
        }
        if (registration.getRegisteredAt() == null) {
            registration.setRegisteredAt(Instant.now());
        }
        registration.setUpdatedAt(Instant.now());
        
        registrations.put(registration.getId(), registration);
        return registration;
    }

    @Override
    public TournamentRegistration getById(UUID id) {
        return registrations.get(id);
    }

    @Override
    public List<TournamentRegistration> getByTournamentId(UUID tournamentId) {
        return registrations.values().stream()
                .filter(registration -> registration.getTournamentId().equals(tournamentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TournamentRegistration> getByUserId(UUID userId) {
        return registrations.values().stream()
                .filter(registration -> registration.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public TournamentRegistration getByTournamentIdAndUserId(UUID tournamentId, UUID userId) {
        return registrations.values().stream()
                .filter(registration -> registration.getTournamentId().equals(tournamentId) 
                        && registration.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public TournamentRegistration update(TournamentRegistration registration) {
        if (registrations.containsKey(registration.getId())) {
            registration.setUpdatedAt(Instant.now());
            registrations.put(registration.getId(), registration);
            return registration;
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        registrations.remove(id);
    }

    @Override
    public int countByTournamentId(UUID tournamentId) {
        return (int) registrations.values().stream()
                .filter(registration -> registration.getTournamentId().equals(tournamentId)
                        && registration.getStatus() == RegistrationStatus.REGISTERED)
                .count();
    }
} 