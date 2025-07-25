package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Tournament;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.TournamentRequest;
import ge.join2play.join2playback.model.dto.TournamentResponse;
import ge.join2play.join2playback.repository.interfaces.TournamentRepository;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final UserPermissionService userPermissionService;

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository, UserRepository userRepository, UserPermissionService userPermissionService) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.userPermissionService = userPermissionService;
    }

    public TournamentResponse createTournament(TournamentRequest request, UUID hostId) {
        // Validate host exists
        var hostOptional = userRepository.getById(hostId);
        if (hostOptional.isEmpty()) {
            throw new RuntimeException("Host user not found");
        }
        User host = hostOptional.get();

        // Check if user has permission to host tournaments
        if (!userPermissionService.canHostTournaments(hostId)) {
            throw new RuntimeException("User does not have permission to host tournaments");
        }

        // Create new tournament
        Tournament tournament = new Tournament();
        tournament.setId(UUID.randomUUID());
        tournament.setName(request.getName());
        tournament.setDescription(request.getDescription());
        tournament.setSportType(request.getSportType());
        tournament.setFormat(request.getFormat());
        tournament.setTournamentType(request.getTournamentType());
        tournament.setHostId(hostId);
        tournament.setHostName(host.getName());
        tournament.setLocation(request.getLocation());
        tournament.setLatitude(request.getLatitude());
        tournament.setLongitude(request.getLongitude());
        tournament.setStartDate(Instant.ofEpochMilli(request.getStartDate()));
        tournament.setEndDate(Instant.ofEpochMilli(request.getEndDate()));
        tournament.setRegistrationDeadline(Instant.ofEpochMilli(request.getRegistrationDeadline()));
        tournament.setMaxParticipants(request.getMaxParticipants());
        tournament.setCurrentParticipants(0);
        tournament.setEntryFee(request.getEntryFee());
        tournament.setPrizePool(request.getPrizePool());
        tournament.setMinAge(request.getMinAge());
        tournament.setMaxAge(request.getMaxAge());
        tournament.setRules(request.getRules());
        tournament.setCreatedAt(Instant.now());
        tournament.setUpdatedAt(Instant.now());

        Tournament savedTournament = tournamentRepository.save(tournament);
        return convertToResponse(savedTournament);
    }

    public TournamentResponse getTournamentById(UUID id) {
        Tournament tournament = tournamentRepository.getById(id);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }
        return convertToResponse(tournament);
    }

    public List<TournamentResponse> getAllTournaments() {
        return tournamentRepository.getAllTournaments().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TournamentResponse> getTournamentsByHost(UUID hostId) {
        return tournamentRepository.getTournamentsByHost(hostId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TournamentResponse updateTournament(UUID id, TournamentRequest request, UUID hostId) {
        Tournament existingTournament = tournamentRepository.getById(id);
        if (existingTournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        // Check if user is the host
        if (!existingTournament.getHostId().equals(hostId)) {
            throw new RuntimeException("Only tournament host can update the tournament");
        }

        // Update tournament fields
        existingTournament.setName(request.getName());
        existingTournament.setDescription(request.getDescription());
        existingTournament.setSportType(request.getSportType());
        existingTournament.setFormat(request.getFormat());
        existingTournament.setTournamentType(request.getTournamentType());
        existingTournament.setLocation(request.getLocation());
        existingTournament.setLatitude(request.getLatitude());
        existingTournament.setLongitude(request.getLongitude());
        existingTournament.setStartDate(Instant.ofEpochMilli(request.getStartDate()));
        existingTournament.setEndDate(Instant.ofEpochMilli(request.getEndDate()));
        existingTournament.setRegistrationDeadline(Instant.ofEpochMilli(request.getRegistrationDeadline()));
        existingTournament.setMaxParticipants(request.getMaxParticipants());
        existingTournament.setEntryFee(request.getEntryFee());
        existingTournament.setPrizePool(request.getPrizePool());
        existingTournament.setMinAge(request.getMinAge());
        existingTournament.setMaxAge(request.getMaxAge());
        existingTournament.setRules(request.getRules());
        existingTournament.setUpdatedAt(Instant.now());

        Tournament updatedTournament = tournamentRepository.update(existingTournament);
        return convertToResponse(updatedTournament);
    }

    public void deleteTournament(UUID id, UUID hostId) {
        Tournament tournament = tournamentRepository.getById(id);
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        // Check if user is the host
        if (!tournament.getHostId().equals(hostId)) {
            throw new RuntimeException("Only tournament host can delete the tournament");
        }

        tournamentRepository.delete(id);
    }

    private TournamentResponse convertToResponse(Tournament tournament) {
        return new TournamentResponse(
                tournament.getId(),
                tournament.getName(),
                tournament.getDescription(),
                tournament.getSportType(),
                tournament.getFormat(),
                tournament.getTournamentType(),
                tournament.getHostId(),
                tournament.getHostName(),
                tournament.getLocation(),
                tournament.getLatitude(),
                tournament.getLongitude(),
                tournament.getStartDate().toEpochMilli(),
                tournament.getEndDate().toEpochMilli(),
                tournament.getRegistrationDeadline().toEpochMilli(),
                tournament.getMaxParticipants(),
                tournament.getCurrentParticipants(),
                tournament.getEntryFee(),
                tournament.getPrizePool(),
                tournament.getMinAge(),
                tournament.getMaxAge(),
                tournament.getRules(),
                tournament.getCreatedAt().toEpochMilli(),
                tournament.getUpdatedAt().toEpochMilli()
        );
    }
} 