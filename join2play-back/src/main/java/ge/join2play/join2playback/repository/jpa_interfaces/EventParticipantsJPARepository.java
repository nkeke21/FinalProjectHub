package ge.join2play.join2playback.repository.jpa_interfaces;

import ge.join2play.join2playback.model.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventParticipantsJPARepository extends JpaRepository<EventParticipant, UUID> {

    @Query("SELECT ep FROM EventParticipant ep WHERE ep.participantId = :userId")
    List<EventParticipant> findByParticipantId(@Param("userId") UUID userId);

    @Query("SELECT ep FROM EventParticipant ep WHERE ep.eventId = :eventId")
    List<EventParticipant> findByEventId(@Param("eventId") UUID eventId);
}
