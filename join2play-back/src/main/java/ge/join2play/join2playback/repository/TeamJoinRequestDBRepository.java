package ge.join2play.join2playback.repository;

import ge.join2play.join2playback.model.TeamJoinRequest;
import ge.join2play.join2playback.model.enums.TeamJoinRequestStatus;
import ge.join2play.join2playback.repository.interfaces.TeamJoinRequestRepository;
import ge.join2play.join2playback.repository.jpa_interfaces.TeamJoinRequestJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("teamJoinRequestRepositoryJPA")
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jpa")
public class TeamJoinRequestDBRepository implements TeamJoinRequestRepository {

    private final TeamJoinRequestJPARepository teamJoinRequestJPARepository;

    @Autowired
    public TeamJoinRequestDBRepository(TeamJoinRequestJPARepository teamJoinRequestJPARepository) {
        this.teamJoinRequestJPARepository = teamJoinRequestJPARepository;
    }

    @Override
    public TeamJoinRequest saveRequest(UUID teamId, UUID fromUserId) {
        TeamJoinRequest request = new TeamJoinRequest(
                UUID.randomUUID(),
                teamId,
                fromUserId,
                LocalDateTime.now(),
                TeamJoinRequestStatus.PENDING
        );
        return teamJoinRequestJPARepository.save(request);
    }

    @Override
    public List<TeamJoinRequest> getPendingRequestsForTeam(UUID teamId) {
        return teamJoinRequestJPARepository.findByTeamIdAndStatus(teamId, TeamJoinRequestStatus.PENDING);
    }

    @Override
    public List<TeamJoinRequest> getAllPendingRequests() {
        return teamJoinRequestJPARepository.findByStatus(TeamJoinRequestStatus.PENDING);
    }

    @Override
    public List<TeamJoinRequest> getAllRequestsForTeam(UUID teamId) {
        return teamJoinRequestJPARepository.findByTeamId(teamId);
    }

    @Override
    public List<TeamJoinRequest> getAllRequests() {
        return teamJoinRequestJPARepository.findAll();
    }

    @Override
    public Optional<TeamJoinRequest> findById(UUID requestId) {
        return teamJoinRequestJPARepository.findById(requestId);
    }

    @Override
    public void updateStatus(UUID requestId, TeamJoinRequestStatus status) {
        Optional<TeamJoinRequest> request = teamJoinRequestJPARepository.findById(requestId);
        if (request.isPresent()) {
            TeamJoinRequest req = request.get();
            req.setStatus(status);
            teamJoinRequestJPARepository.save(req);
        }
    }

    @Override
    public Optional<TeamJoinRequest> findRequestBetweenTeamAndUser(UUID teamId, UUID userId) {
        return teamJoinRequestJPARepository.findByTeamIdAndFromUserId(teamId, userId);
    }
}
