package ge.join2play.join2playback.service;

import ge.join2play.join2playback.config.EventTableConfig;
import ge.join2play.join2playback.config.FilterConfig;
import ge.join2play.join2playback.model.*;
import ge.join2play.join2playback.model.enums.SportType;
import ge.join2play.join2playback.model.exceptions.InvalidEventDateException;
import ge.join2play.join2playback.model.exceptions.SportTypeDoesNotExistException;
import ge.join2play.join2playback.model.exceptions.UserAgeNotInRangeException;
import ge.join2play.join2playback.model.exceptions.UserDoesNotExistException;
import ge.join2play.join2playback.repository.interfaces.EventParticipantsRepository;
import ge.join2play.join2playback.repository.interfaces.EventRepository;
import ge.join2play.join2playback.repository.interfaces.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static ge.join2play.join2playback.model.enums.SportType.getSportTypeOptions;

@Service
public class ApplicationService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventParticipantsRepository eventParticipantsRepository;
    private final FilterConfig filterConfig;
    private final EventTableConfig eventTableConfig;

    public ApplicationService(EventRepository eventRepository, UserRepository userRepository, EventParticipantsRepository eventParticipantsRepository, FilterConfig filterConfig, EventTableConfig eventTableConfig) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventParticipantsRepository = eventParticipantsRepository;
        this.filterConfig = filterConfig;
        this.eventTableConfig = eventTableConfig;
    }

    public SetupResponse getSetupInfo() {
        List<TableHeader> columns = eventTableConfig.getTableColumns();
        List<Option> sportTypeOptions = getSportTypeOptions();
        List<Option> filterOptions = filterConfig.getFilterOptions();
        List<EventResponse> events = getAllEvents();
        return new SetupResponse(columns, sportTypeOptions, filterOptions, events);
    }

    public Event convertEventRequestToEvent(EventRequest eventRequest) {
        SportType sportType;
        try {
            sportType = SportType.valueOf(eventRequest.getSportType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SportTypeDoesNotExistException("Invalid sport type " + eventRequest.getSportType() + " provided.");
        }
        
        Instant eventTime = Instant.ofEpochMilli(eventRequest.getEventTime());
        Instant currentTime = Instant.now();
        if (eventTime.isBefore(currentTime) || eventTime.equals(currentTime)) {
            throw new InvalidEventDateException("Event date and time must be in the future.");
        }
        
        Optional<User> hostUser = userRepository.getById(eventRequest.getHostId());
        if (hostUser.isEmpty()) {
            throw new UserDoesNotExistException(
                    String.format("Host user with id %s does not exist.", eventRequest.getHostId()));
        }
        
        return new Event(
                UUID.randomUUID(), eventRequest.getHostId(), hostUser.get().getEmail(), hostUser.get().getPhoneNumber(), 
                eventRequest.getMinAge(), eventRequest.getMaxAge(), eventRequest.getDescription(), eventTime,
                eventRequest.getLatitude(), eventRequest.getLongitude(), eventRequest.getLocation(), eventRequest.getNumberOfParticipantsTotal(),
                eventRequest.getNumberOfParticipantsRegistered(), sportType
        );
    }

    public EventResponse convertEventToEventResponse(Event event) {
        String ageRange = event.getMinAge() + "-" + event.getMaxAge();
        Optional<User> hostUser = userRepository.getById(event.getHostId());
        if (hostUser.isEmpty()) {
            throw new UserDoesNotExistException(
                    String.format("Host user with id %s does not exist.", event.getHostId()));
        }
        String hostName = hostUser.get().getName();
        
        List<EventParticipant> eventParticipants = eventParticipantsRepository.getAllParticipantsOf(event.getId());
        List<ParticipantInfo> participantsList = eventParticipants.stream()
                .map(ep -> {
                    Optional<User> optionalUser = userRepository.getById(ep.getParticipantId());
                    if (optionalUser.isEmpty()) {
                        throw new UserDoesNotExistException(
                                String.format("Participant with id %s does not exist.", ep.getParticipantId()));
                    }
                    User participant = optionalUser.get();
                    int age = calculateAge(participant.getBirthDate());
                    return new ParticipantInfo(participant.getId(), participant.getName(), participant.getEmail(), age);
                })
                .collect(Collectors.toList());
        
        return new EventResponse(event.getId(), event.getHostId(), hostName, hostUser.get().getEmail(), hostUser.get().getPhoneNumber(), ageRange, event.getDescription(), event.getEventTime().toEpochMilli(),
                event.getLatitude(), event.getLongitude(), event.getLocation(), event.getNumberOfParticipantsTotal(), event.getNumberOfParticipantsRegistered(),
                event.getSportType().toString(), participantsList);
    }

    private int calculateAge(long birthDateMillis) {
        LocalDate birthDate = Instant.ofEpochMilli(birthDateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear() - birthDate.getYear() - 
               (currentDate.getDayOfYear() < birthDate.getDayOfYear() ? 1 : 0);
    }

    public EventResponse createEvent(EventRequest eventRequest) {
        return convertEventToEventResponse(eventRepository.save(convertEventRequestToEvent(eventRequest)));
    }

    public EventResponse getEvent(UUID id) {
        return convertEventToEventResponse(eventRepository.getById(id));
    }

    public EventResponse updateEvent(EventRequest eventRequest, UUID id) {
        Event event = convertEventRequestToEvent(eventRequest);
        event.setId(id);
        return convertEventToEventResponse(eventRepository.update(event));
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.getAll().stream().map(this::convertEventToEventResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getUserHostedEvents(UUID id) {
        if(userRepository.getById(id).isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("User with id %s does not exist.", id));
        }
        return eventRepository.getAllHostedBy(id).stream().map(this::convertEventToEventResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getUserRegisteredEvents(UUID id) {
        if(userRepository.getById(id).isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("User with id %s does not exist.", id));
        }
        List<EventParticipant> eventParticipants = eventParticipantsRepository.getAllEventsParticipating(id);
        return eventParticipants.stream().map(ep -> eventRepository.getById(ep.getEventId())).map(this::convertEventToEventResponse).collect(Collectors.toList());
    }

    public EventResponse joinEvent(UUID eventId, UUID userId) {
        Optional<User> user = userRepository.getById(userId);
        if(user.isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("User with id %s does not exist.", userId));
        }
        
        Event event = eventRepository.getById(eventId);
        if(event == null){
            throw new RuntimeException("Event with id " + eventId + " does not exist.");
        }
        
        int userAge = calculateAge(user.get().getBirthDate());
        if (userAge < event.getMinAge() || userAge > event.getMaxAge()) {
            throw new UserAgeNotInRangeException(
                String.format("User age %d is outside the event's age range (%d-%d).", 
                    userAge, event.getMinAge(), event.getMaxAge()));
        }
        
        List<EventParticipant> existingParticipants = eventParticipantsRepository.getAllParticipantsOf(eventId);
        boolean alreadyParticipating = existingParticipants.stream()
                .anyMatch(ep -> ep.getParticipantId().equals(userId));
        
        if(alreadyParticipating) {
            throw new RuntimeException("User is already participating in this event.");
        }
        
        if(event.getNumberOfParticipantsRegistered() >= event.getNumberOfParticipantsTotal()) {
            throw new RuntimeException("Event is already full.");
        }
        
        EventParticipant eventParticipant = new EventParticipant(eventId, UUID.randomUUID(), userId);
        eventParticipantsRepository.save(eventParticipant);
        
        event.setNumberOfParticipantsRegistered(event.getNumberOfParticipantsRegistered() + 1);
        eventRepository.update(event);
        
        return convertEventToEventResponse(event);
    }

    public boolean isUserParticipating(UUID eventId, UUID userId) {
        if(userRepository.getById(userId).isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("User with id %s does not exist.", userId));
        }
        
        Event event = eventRepository.getById(eventId);
        if(event == null){
            throw new RuntimeException("Event with id " + eventId + " does not exist.");
        }
        
        List<EventParticipant> existingParticipants = eventParticipantsRepository.getAllParticipantsOf(eventId);
        return existingParticipants.stream()
                .anyMatch(ep -> ep.getParticipantId().equals(userId));
    }

    public EventResponse removeParticipant(UUID eventId, UUID participantId, UUID requestingUserId) {
        // Verify the requesting user exists
        if(userRepository.getById(requestingUserId).isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("Requesting user with id %s does not exist.", requestingUserId));
        }
        
        // Verify the participant to be removed exists
        if(userRepository.getById(participantId).isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("Participant with id %s does not exist.", participantId));
        }
        
        // Get the event
        Event event = eventRepository.getById(eventId);
        if(event == null){
            throw new RuntimeException("Event with id " + eventId + " does not exist.");
        }
        
        // Check if the requesting user is the host of the event
        if(!event.getHostId().equals(requestingUserId)){
            throw new RuntimeException("Only the event host can remove participants.");
        }
        
        // Find the participant record
        EventParticipant participantRecord = eventParticipantsRepository.getByEventIdAndParticipantId(eventId, participantId);
        if(participantRecord == null){
            throw new RuntimeException("User is not participating in this event.");
        }
        
        // Don't allow host to remove themselves
        if(participantId.equals(event.getHostId())){
            throw new RuntimeException("Event host cannot be removed from their own event.");
        }
        
        // Remove the participant
        eventParticipantsRepository.delete(participantRecord.getId());
        
        // Update the registered participants count
        event.setNumberOfParticipantsRegistered(event.getNumberOfParticipantsRegistered() - 1);
        eventRepository.update(event);
        
        return convertEventToEventResponse(event);
    }

    public void deleteEvent(UUID eventId, UUID requestingUserId) {
        if(userRepository.getById(requestingUserId).isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("Requesting user with id %s does not exist.", requestingUserId));
        }
        
        Event event = eventRepository.getById(eventId);
        if(event == null){
            throw new RuntimeException("Event with id " + eventId + " does not exist.");
        }
        
        if(!event.getHostId().equals(requestingUserId)){
            throw new RuntimeException("Only the event host can delete the event.");
        }
        
        eventRepository.delete(eventId);
    }

    public UserDetailsResponse getUserDetails(UUID id) {
        Optional<User> user = userRepository.getById(id);
        if(user.isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("User with id %s does not exist.", id));
        }
        return convertUserToUserDetailsResponse(user.get());
    }

    public List<UserResponse> searchUsers(String query) {
        List<User> matchedUsers = userRepository
                .findByNameStartsWithIgnoreCaseOrEmailStartsWithIgnoreCase(query);

        return matchedUsers.stream()
                .map(user -> new UserResponse(
                        user.getId().toString(),
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    private UserDetailsResponse convertUserToUserDetailsResponse(User user) {
        LocalDate birthDate = null;
        if (user.getBirthDate() != null) {
            birthDate = Instant.ofEpochMilli(user.getBirthDate())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return new UserDetailsResponse(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(), birthDate, user.getDescription());
    }

    public UserUpdateDTO updateUserDetails(UUID id, UserUpdateDTO userUpdateDTO) {
        Optional<User> optionalUser = userRepository.getById(id);
        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistException(
                    String.format("User with id %s does not exist.", id));
        }
        User user = optionalUser.get();
        user.setName(userUpdateDTO.getName());
        user.setDescription(userUpdateDTO.getDescription());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        return convertUserUserUpdateDTO(userRepository.update(user));
    }

    private UserUpdateDTO convertUserUserUpdateDTO(User user) {
        return new UserUpdateDTO(user.getName(), user.getPhoneNumber(), user.getDescription());
    }
}
