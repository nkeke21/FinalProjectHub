package ge.join2play.join2playback.service;

import ge.join2play.join2playback.config.EventTableConfig;
import ge.join2play.join2playback.config.FilterConfig;
import ge.join2play.join2playback.model.*;
import ge.join2play.join2playback.model.errors.SportTypeDoesNotExist;
import ge.join2play.join2playback.model.errors.UserDoesNotExistError;
import ge.join2play.join2playback.repository.EventParticipantsRepository;
import ge.join2play.join2playback.repository.EventRepository;
import ge.join2play.join2playback.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ge.join2play.join2playback.model.SportType.getSportTypeOptions;

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
        String ageRange = eventRequest.getAgeRange();
        String[] ages = ageRange.split("-");
        int minAge = Integer.parseInt(ages[0]);
        int maxAge = Integer.parseInt(ages[1]);
        SportType sportType;
        try {
            sportType = SportType.valueOf(eventRequest.getSportType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SportTypeDoesNotExist("Invalid sport type " + eventRequest.getSportType() + " provided.");
        }
        return new Event(
                UUID.randomUUID(), eventRequest.getHostId(), minAge, maxAge, eventRequest.getDescription(), Instant.ofEpochMilli(eventRequest.getEventTime()),
                eventRequest.getLatitude(), eventRequest.getLongitude(), eventRequest.getLocation(), eventRequest.getNumberOfParticipantsTotal(),
                eventRequest.getNumberOfParticipantsRegistered(), sportType
        );
    }

    public EventResponse convertEventToEventResponse(Event event) {
        String ageRange = event.getMinAge() + "-" + event.getMaxAge();
        String hostName = userRepository.getById(event.getHostId()).getName();
        return new EventResponse(event.getId(), event.getHostId(), hostName, ageRange, event.getDescription(), event.getEventTime().toEpochMilli(),
                event.getLatitude(), event.getLongitude(), event.getLocation(), event.getNumberOfParticipantsTotal(), event.getNumberOfParticipantsRegistered(),
                event.getSportType().toString());
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
//        if(userRepository.getById(id) == null){
//            throw new UserDoesNotExistError(
//                    String.format("User with id %s does not exist.", id));
//        }
        return eventRepository.getAllHostedBy(id).stream().map(this::convertEventToEventResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getUserRegisteredEvents(UUID id) {
//        if(userRepository.getById(id) == null){
//            throw new UserDoesNotExistError(
//                    String.format("User with id %s does not exist.", id));
//        }
        List<EventParticipant> eventParticipants = eventParticipantsRepository.getAllEventsParticipating(id);
        return eventParticipants.stream().map(ep -> eventRepository.getById(ep.getEventId())).map(this::convertEventToEventResponse).collect(Collectors.toList());
    }

    public UserDetailsResponse getUserDetails(UUID id) {
        if(userRepository.getById(id) == null){
            throw new UserDoesNotExistError(
                    String.format("User with id %s does not exist.", id));
        }
        return convertUserToUserDetailsResponse(userRepository.getById(id));
    }

    private UserDetailsResponse convertUserToUserDetailsResponse(User user) {
        return new UserDetailsResponse(user.getName(), user.getEmail(), user.getPhoneNumber(), user.getBirthDate(), user.getDescription());
    }

    public UserUpdateDTO updateUserDetails(UUID id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.getById(id);
        if(user == null){
            throw new UserDoesNotExistError(
                    String.format("User with id %s does not exist.", id));
        }
        user.setName(userUpdateDTO.getName());
        user.setDescription(userUpdateDTO.getDescription());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        return convertUserUserUpdateDTO(userRepository.update(user));
    }

    private UserUpdateDTO convertUserUserUpdateDTO(User user) {
        return new UserUpdateDTO(user.getName(), user.getPhoneNumber(), user.getDescription());
    }
}
