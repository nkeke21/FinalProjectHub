package ge.join2play.join2playback.service;

import ge.join2play.join2playback.model.Tournament;
import ge.join2play.join2playback.model.TournamentRegistration;
import ge.join2play.join2playback.model.TournamentRegistrationNotification;
import ge.join2play.join2playback.model.User;
import ge.join2play.join2playback.model.dto.TournamentRegistrationRequest;
import ge.join2play.join2playback.model.dto.TournamentRegistrationResponse;
import ge.join2play.join2playback.model.enums.RegistrationStatus;
import ge.join2play.join2playback.repository.interfaces.TournamentRegistrationNotificationRepository;
import ge.join2play.join2playback.repository.interfaces.TournamentRegistrationRepository;
import ge.join2play.join2playback.repository.interfaces.TournamentRepository;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TournamentRegistrationService {
    private final TournamentRegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final TournamentRegistrationNotificationRepository notificationRepository;

    @Autowired
    public TournamentRegistrationService(TournamentRegistrationRepository registrationRepository,
                                       TournamentRepository tournamentRepository,
                                       UserRepository userRepository,
                                       TournamentRegistrationNotificationRepository notificationRepository) {
        this.registrationRepository = registrationRepository;
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    public TournamentRegistrationResponse registerForTournament(TournamentRegistrationRequest request, UUID userId) {
        Tournament tournament = tournamentRepository.getById(request.getTournamentId());
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        var userOptional = userRepository.getById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        TournamentRegistration existingRegistration = registrationRepository.getByTournamentIdAndUserId(
                request.getTournamentId(), userId);
        
        if (existingRegistration != null && existingRegistration.getStatus() == RegistrationStatus.REGISTERED) {
            throw new RuntimeException("User is already registered for this tournament");
        }

        int currentParticipants = registrationRepository.countByTournamentId(request.getTournamentId());
        if (currentParticipants >= tournament.getMaxParticipants()) {
            throw new RuntimeException("Tournament is full");
        }

        if (Instant.now().isAfter(tournament.getRegistrationDeadline())) {
            throw new RuntimeException("Registration deadline has passed");
        }

        TournamentRegistration registration;
        if (existingRegistration != null && existingRegistration.getStatus() == RegistrationStatus.WITHDRAWN) {
            registration = existingRegistration;
            registration.setStatus(RegistrationStatus.PENDING);
            registration.setRegisteredAt(Instant.now());
        } else {
            registration = new TournamentRegistration();
            registration.setTournamentId(request.getTournamentId());
            registration.setUserId(userId);
            registration.setRegistrationType(request.getRegistrationType());
            registration.setTeamId(request.getTeamId());
            registration.setStatus(RegistrationStatus.PENDING);
            registration.setRegisteredAt(Instant.now());
        }

        if (request.getParticipantInfo() != null) {
            var participantInfo = request.getParticipantInfo();
            registration.setFullName(participantInfo.getFullName());
            registration.setAge(participantInfo.getAge());
            registration.setEmail(participantInfo.getEmail());
            registration.setPhoneNumber(participantInfo.getPhoneNumber());
            registration.setAddress(participantInfo.getAddress());
            registration.setPreviousExperience(participantInfo.getPreviousExperience());
            registration.setSkillLevel(participantInfo.getSkillLevel());
            registration.setPreviousAchievements(participantInfo.getPreviousAchievements());

            if (participantInfo.getEmergencyContact() != null) {
                var emergencyContact = participantInfo.getEmergencyContact();
                registration.setEmergencyContactName(emergencyContact.getName());
                registration.setEmergencyContactRelationship(emergencyContact.getRelationship());
                registration.setEmergencyContactPhone(emergencyContact.getPhone());
                registration.setEmergencyContactEmail(emergencyContact.getEmail());
            }
        }

        TournamentRegistration savedRegistration = registrationRepository.save(registration);
        
        TournamentRegistrationNotification notification = new TournamentRegistrationNotification(
            tournament.getId(),
            tournament.getName(),
            user.getId(),
            user.getName(),
            tournament.getHostId(),
            tournament.getHostName(),
            savedRegistration.getId()
        );
        notificationRepository.save(notification);
        
        return convertToResponse(savedRegistration);
    }

    public TournamentRegistrationResponse withdrawFromTournament(UUID tournamentId, UUID userId) {
        TournamentRegistration registration = registrationRepository.getByTournamentIdAndUserId(tournamentId, userId);
        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        if (registration.getStatus() == RegistrationStatus.WITHDRAWN) {
            throw new RuntimeException("Registration is already withdrawn");
        }

        registration.setStatus(RegistrationStatus.WITHDRAWN);
        registration.setUpdatedAt(Instant.now());

        TournamentRegistration updatedRegistration = registrationRepository.update(registration);
        return convertToResponse(updatedRegistration);
    }

    public TournamentRegistrationResponse getUserRegistration(UUID tournamentId, UUID userId) {
        TournamentRegistration registration = registrationRepository.getByTournamentIdAndUserId(tournamentId, userId);
        if (registration == null) {
            return null;
        }
        return convertToResponse(registration);
    }

    public List<TournamentRegistrationResponse> getTournamentRegistrations(UUID tournamentId) {
        List<TournamentRegistration> registrations = registrationRepository.getByTournamentId(tournamentId);
        return registrations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TournamentRegistrationResponse> getUserRegistrations(UUID userId) {
        List<TournamentRegistration> registrations = registrationRepository.getByUserId(userId);
        return registrations.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TournamentRegistrationResponse getRegistrationById(UUID registrationId) {
        TournamentRegistration registration = registrationRepository.getById(registrationId);
        if (registration == null) {
            return null;
        }
        return convertToResponse(registration);
    }

    public boolean canRegisterForTournament(UUID tournamentId, UUID userId) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        if (tournament == null) {
            return false;
        }

        if (Instant.now().isAfter(tournament.getRegistrationDeadline())) {
            return false;
        }

        int currentParticipants = registrationRepository.countByTournamentId(tournamentId);
        if (currentParticipants >= tournament.getMaxParticipants()) {
            return false;
        }

        TournamentRegistration existingRegistration = registrationRepository.getByTournamentIdAndUserId(tournamentId, userId);
        if (existingRegistration != null && (existingRegistration.getStatus() == RegistrationStatus.REGISTERED || 
                                            existingRegistration.getStatus() == RegistrationStatus.PENDING)) {
            return false;
        }

        return true;
    }

    public TournamentRegistrationResponse approveRegistration(UUID registrationId, UUID hostId) {
        TournamentRegistration registration = registrationRepository.getById(registrationId);
        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        Tournament tournament = tournamentRepository.getById(registration.getTournamentId());
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        if (!tournament.getHostId().equals(hostId)) {
            throw new RuntimeException("Only tournament host can approve registrations");
        }

        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new RuntimeException("Registration is not pending approval");
        }

        int currentParticipants = registrationRepository.countByTournamentId(tournament.getId());
        if (currentParticipants >= tournament.getMaxParticipants()) {
            throw new RuntimeException("Tournament is full");
        }

        registration.setStatus(RegistrationStatus.REGISTERED);
        registration.setUpdatedAt(Instant.now());
        
        TournamentRegistration savedRegistration = registrationRepository.update(registration);
        
        tournament.setCurrentParticipants(currentParticipants + 1);
        tournamentRepository.update(tournament);
        
        TournamentRegistrationNotification notification = notificationRepository.getByRegistrationId(registrationId);
        if (notification != null) {
            notificationRepository.delete(notification.getId());
        }
        
        return convertToResponse(savedRegistration);
    }

    public TournamentRegistrationResponse rejectRegistration(UUID registrationId, UUID hostId) {
        TournamentRegistration registration = registrationRepository.getById(registrationId);
        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        Tournament tournament = tournamentRepository.getById(registration.getTournamentId());
        if (tournament == null) {
            throw new RuntimeException("Tournament not found");
        }

        if (!tournament.getHostId().equals(hostId)) {
            throw new RuntimeException("Only tournament host can reject registrations");
        }

        if (registration.getStatus() != RegistrationStatus.PENDING) {
            throw new RuntimeException("Registration is not pending approval");
        }

        registration.setStatus(RegistrationStatus.WITHDRAWN);
        registration.setUpdatedAt(Instant.now());
        
        TournamentRegistration savedRegistration = registrationRepository.update(registration);
        
        TournamentRegistrationNotification notification = notificationRepository.getByRegistrationId(registrationId);
        if (notification != null) {
            notificationRepository.delete(notification.getId());
        }
        
        return convertToResponse(savedRegistration);
    }

    private TournamentRegistrationResponse convertToResponse(TournamentRegistration registration) {
        return new TournamentRegistrationResponse(
                registration.getId(),
                registration.getTournamentId(),
                registration.getUserId(),
                registration.getRegistrationType(),
                registration.getTeamId(),
                registration.getStatus(),
                registration.getRegisteredAt(),
                registration.getUpdatedAt(),
                registration.getFullName(),
                registration.getAge(),
                registration.getEmail(),
                registration.getPhoneNumber(),
                registration.getAddress(),
                registration.getEmergencyContactName(),
                registration.getEmergencyContactRelationship(),
                registration.getEmergencyContactPhone(),
                registration.getEmergencyContactEmail(),
                registration.getPreviousExperience(),
                registration.getSkillLevel(),
                registration.getPreviousAchievements()
        );
    }
} 