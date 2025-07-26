-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS event_invitations CASCADE;
DROP TABLE IF EXISTS event_participants CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS tournament_registration_notifications CASCADE;
DROP TABLE IF EXISTS tournament_registrations CASCADE;
DROP TABLE IF EXISTS tournaments CASCADE;
DROP TABLE IF EXISTS friend_requests CASCADE;
DROP TABLE IF EXISTS team_join_requests CASCADE;
DROP TABLE IF EXISTS team_members CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS user_permissions CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create Users table
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(320) NOT NULL UNIQUE,
                       phone_number VARCHAR(20),
                       birth_date BIGINT,
                       description TEXT,
                       password VARCHAR(255) NOT NULL
);

-- Create indexes for users table
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_name ON users(name);

-- Create User Permissions table
CREATE TABLE user_permissions (
                                  user_id UUID PRIMARY KEY,
                                  can_host_tournaments BOOLEAN DEFAULT FALSE NOT NULL,
                                  granted_by VARCHAR(255),
                                  granted_at VARCHAR(255),
                                  reason TEXT,
                                  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create Teams table
CREATE TABLE teams (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       sport_type VARCHAR(50) NOT NULL,
                       captain_id UUID NOT NULL,
                       max_members INTEGER NOT NULL,
                       min_age INTEGER NOT NULL,
                       max_age INTEGER NOT NULL,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       FOREIGN KEY (captain_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for teams table
CREATE INDEX idx_teams_name ON teams(name);
CREATE INDEX idx_teams_sport_type ON teams(sport_type);
CREATE INDEX idx_teams_captain_id ON teams(captain_id);

-- Create Team Members table
CREATE TABLE team_members (
                              id UUID PRIMARY KEY,
                              team_id UUID NOT NULL,
                              user_id UUID NOT NULL,
                              role VARCHAR(50) NOT NULL,
                              joined_at TIMESTAMP WITH TIME ZONE NOT NULL,
                              FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                              UNIQUE(team_id, user_id)
);

-- Create indexes for team_members table
CREATE INDEX idx_team_members_team_id ON team_members(team_id);
CREATE INDEX idx_team_members_user_id ON team_members(user_id);

-- Create Team Join Requests table
CREATE TABLE team_join_requests (
                                    request_id UUID PRIMARY KEY,
                                    team_id UUID NOT NULL,
                                    from_user_id UUID NOT NULL,
                                    sent_at TIMESTAMP NOT NULL,
                                    status VARCHAR(20) NOT NULL,
                                    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
                                    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for team_join_requests table
CREATE INDEX idx_team_join_requests_team_id ON team_join_requests(team_id);
CREATE INDEX idx_team_join_requests_from_user_id ON team_join_requests(from_user_id);
CREATE INDEX idx_team_join_requests_status ON team_join_requests(status);

-- Create Friend Requests table
CREATE TABLE friend_requests (
                                 request_id UUID PRIMARY KEY,
                                 from_user_id UUID NOT NULL,
                                 to_user_id UUID NOT NULL,
                                 sent_at TIMESTAMP NOT NULL,
                                 status VARCHAR(20) NOT NULL,
                                 FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
                                 FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for friend_requests table
CREATE INDEX idx_friend_requests_from_user_id ON friend_requests(from_user_id);
CREATE INDEX idx_friend_requests_to_user_id ON friend_requests(to_user_id);
CREATE INDEX idx_friend_requests_status ON friend_requests(status);

-- Create Tournaments table
CREATE TABLE tournaments (
                             id UUID PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             description TEXT,
                             sport_type VARCHAR(50) NOT NULL,
                             format VARCHAR(50) NOT NULL,
                             tournament_type VARCHAR(20) NOT NULL,
                             host_id UUID NOT NULL,
                             host_name VARCHAR(255) NOT NULL,
                             location VARCHAR(500) NOT NULL,
                             latitude DOUBLE PRECISION NOT NULL,
                             longitude DOUBLE PRECISION NOT NULL,
                             start_date TIMESTAMP WITH TIME ZONE NOT NULL,
                             end_date TIMESTAMP WITH TIME ZONE NOT NULL,
                             registration_deadline TIMESTAMP WITH TIME ZONE NOT NULL,
                             max_participants INTEGER NOT NULL,
                             current_participants INTEGER NOT NULL,
                             entry_fee DOUBLE PRECISION NOT NULL,
                             prize_pool DOUBLE PRECISION NOT NULL,
                             min_age INTEGER NOT NULL,
                             max_age INTEGER NOT NULL,
                             rules TEXT,
                             created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                             updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                             FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for tournaments table
CREATE INDEX idx_tournaments_sport_type ON tournaments(sport_type);
CREATE INDEX idx_tournaments_start_date ON tournaments(start_date);
CREATE INDEX idx_tournaments_host_id ON tournaments(host_id);

-- Create Tournament Registrations table
CREATE TABLE tournament_registrations (
                                          id UUID PRIMARY KEY,
                                          tournament_id UUID NOT NULL,
                                          user_id UUID NOT NULL,
                                          registration_type VARCHAR(20) NOT NULL,
                                          team_id UUID,
                                          status VARCHAR(20) NOT NULL,
                                          registered_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                          updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                          full_name VARCHAR(255),
                                          age INTEGER,
                                          email VARCHAR(320),
                                          phone_number VARCHAR(20),
                                          address TEXT,
                                          emergency_contact_name VARCHAR(255),
                                          emergency_contact_relationship VARCHAR(100),
                                          emergency_contact_phone VARCHAR(20),
                                          emergency_contact_email VARCHAR(320),
                                          previous_experience TEXT,
                                          skill_level VARCHAR(50),
                                          previous_achievements TEXT,
                                          FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE,
                                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                          FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL
);

-- Create indexes for tournament_registrations table
CREATE INDEX idx_tournament_registrations_tournament_id ON tournament_registrations(tournament_id);
CREATE INDEX idx_tournament_registrations_user_id ON tournament_registrations(user_id);
CREATE INDEX idx_tournament_registrations_status ON tournament_registrations(status);

-- Create Tournament Registration Notifications table
CREATE TABLE tournament_registration_notifications (
                                                       id UUID PRIMARY KEY,
                                                       tournament_id UUID NOT NULL,
                                                       tournament_name VARCHAR(255) NOT NULL,
                                                       requester_id UUID NOT NULL,
                                                       requester_name VARCHAR(255) NOT NULL,
                                                       host_id UUID NOT NULL,
                                                       host_name VARCHAR(255) NOT NULL,
                                                       registration_id UUID NOT NULL,
                                                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                                       is_read BOOLEAN NOT NULL,
                                                       FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE,
                                                       FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
                                                       FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE CASCADE,
                                                       FOREIGN KEY (registration_id) REFERENCES tournament_registrations(id) ON DELETE CASCADE
);

-- Create indexes for tournament_registration_notifications table
CREATE INDEX idx_tournament_notifications_host_id ON tournament_registration_notifications(host_id);
CREATE INDEX idx_tournament_notifications_is_read ON tournament_registration_notifications(is_read);

-- Create Events table
CREATE TABLE events (
                        id UUID PRIMARY KEY,
                        host_id UUID NOT NULL,
                        host_email VARCHAR(320) NOT NULL,
                        host_phone VARCHAR(20),
                        min_age INTEGER NOT NULL,
                        max_age INTEGER NOT NULL,
                        description TEXT,
                        event_time TIMESTAMP WITH TIME ZONE NOT NULL,
                        latitude DOUBLE PRECISION NOT NULL,
                        longitude DOUBLE PRECISION NOT NULL,
                        location VARCHAR(500) NOT NULL,
                        number_of_participants_total INTEGER NOT NULL,
                        number_of_participants_registered INTEGER NOT NULL,
                        sport_type VARCHAR(50) NOT NULL,
                        FOREIGN KEY (host_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for events table
CREATE INDEX idx_events_host_id ON events(host_id);
CREATE INDEX idx_events_sport_type ON events(sport_type);
CREATE INDEX idx_events_event_time ON events(event_time);

-- Create Event Participants table
CREATE TABLE event_participants (
                                    id UUID PRIMARY KEY,
                                    event_id UUID NOT NULL,
                                    participant_id UUID NOT NULL,
                                    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
                                    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE,
                                    UNIQUE(event_id, participant_id)
);

-- Create indexes for event_participants table
CREATE INDEX idx_event_participants_event_id ON event_participants(event_id);
CREATE INDEX idx_event_participants_participant_id ON event_participants(participant_id);

-- Create Event Invitations table
CREATE TABLE event_invitations (
                                   invitation_id UUID PRIMARY KEY,
                                   event_id UUID NOT NULL,
                                   from_user_id UUID NOT NULL,
                                   to_user_id UUID NOT NULL,
                                   sent_at TIMESTAMP NOT NULL,
                                   status VARCHAR(20) NOT NULL,
                                   FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
                                   FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE,
                                   FOREIGN KEY (to_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for event_invitations table
CREATE INDEX idx_event_invitations_event_id ON event_invitations(event_id);
CREATE INDEX idx_event_invitations_from_user_id ON event_invitations(from_user_id);
CREATE INDEX idx_event_invitations_to_user_id ON event_invitations(to_user_id);
CREATE INDEX idx_event_invitations_status ON event_invitations(status);
