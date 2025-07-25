-- Insert Users
INSERT INTO users (id, name, email, phone_number, birth_date, description, password)
VALUES ('13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', 'Kakha Salukvadze', 'kakha@example.com', '+995555123456', 803750400000,
        'Sports enthusiast and backend developer.', 'securepassword123'),
       ('24fb6e3f-2dac-4eac-8df1-abc456789012', 'John Doe', 'john.doe@example.com', '+995555987654', 637977600000,
        'Loves football and tennis.', 'john123'),
       ('35ac7d4e-3fac-5fab-9ed2-cba987654321', 'Jane Smith', 'jane.smith@example.com', '+995599123789', 594345600000,
        'Basketball player and outdoor enthusiast.', 'jane123'),
       ('46bd8e5f-4abc-6a1c-8ef3-dcba87654321', 'Mike Johnson', 'mike.j@example.com', '+995591234567', 713664000000,
        'Runner and sports event organizer.', 'mike123'),
       ('57ce9f60-5bcd-7b2d-9f04-edcba7654321', 'Emily Davis', 'emily.d@example.com', '+995592345678', 728083200000,
        'Volleyball fan and team leader.', 'emily123');

-- Insert Teams
INSERT INTO teams (id, name, description, sport_type, captain_id, max_members, min_age, max_age, created_at, updated_at)
VALUES ('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'Vake Runners', 'A running team for weekend enthusiasts in Vake park',
        'RUNNING', '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', 15, 18, 45, '2025-01-25 10:00:00+00',
        '2025-01-26 10:00:00+00'),
       ('b2c3d4e5-f6a7-8901-2345-678901bcdef0', 'City Basketball Club',
        'Competitive basketball team looking for skilled players', 'BASKETBALL', '24fb6e3f-2dac-4eac-8df1-abc456789012',
        12, 20, 35, '2025-01-25 10:00:00+00', '2025-01-26 10:00:00+00'),
       ('c3d4e5f6-a7b8-9012-3456-789012cdef01', 'Elite Football Squad', 'Semi-professional football team', 'FOOTBALL',
        '46bd8e5f-4abc-6a1c-8ef3-dcba87654321', 22, 18, 30, '2025-01-25 10:00:00+00', '2025-01-26 10:00:00+00');

-- Insert Team Members
INSERT INTO team_members (id, team_id, user_id, role, joined_at)
VALUES
-- Vake Runners team members
(gen_random_uuid(), 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', 'CAPTAIN',
 '2025-01-25 10:00:00+00'),
(gen_random_uuid(), 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '35ac7d4e-3fac-5fab-9ed2-cba987654321', 'MEMBER',
 '2025-01-25 10:00:00+00'),
(gen_random_uuid(), 'a1b2c3d4-e5f6-7890-1234-567890abcdef', '57ce9f60-5bcd-7b2d-9f04-edcba7654321', 'MEMBER',
 '2025-01-26 10:00:00+00'),
-- City Basketball Club team members
(gen_random_uuid(), 'b2c3d4e5-f6a7-8901-2345-678901bcdef0', '24fb6e3f-2dac-4eac-8df1-abc456789012', 'CAPTAIN',
 '2025-01-25 10:00:00+00'),
(gen_random_uuid(), 'b2c3d4e5-f6a7-8901-2345-678901bcdef0', '46bd8e5f-4abc-6a1c-8ef3-dcba87654321', 'MEMBER',
 '2025-01-25 10:00:00+00'),
-- Elite Football Squad team members
(gen_random_uuid(), 'c3d4e5f6-a7b8-9012-3456-789012cdef01', '46bd8e5f-4abc-6a1c-8ef3-dcba87654321', 'CAPTAIN',
 '2025-01-25 10:00:00+00'),
(gen_random_uuid(), 'c3d4e5f6-a7b8-9012-3456-789012cdef01', '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', 'MEMBER',
 '2025-01-26 10:00:00+00');

-- Insert Events
INSERT INTO events (id, host_id, host_email, host_phone, min_age, max_age, description, event_time, latitude, longitude,
                    location, number_of_participants_total, number_of_participants_registered, sport_type)
VALUES ('e5d1d580-a4cf-4677-8220-a50c5decccfa', '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', 'kakha.salukvadze@example.com',
        '+995 555 123 456', 18, 35, 'Weekend Football Match - Join us for a friendly game!', '2025-04-16 10:15:30+00',
        41.725788, 44.727753, 'Vake Park, Tbilisi', 10, 3, 'FOOTBALL');

-- Insert Event Participants
INSERT INTO event_participants (id, event_id, participant_id)
VALUES (gen_random_uuid(), 'e5d1d580-a4cf-4677-8220-a50c5decccfa', '24fb6e3f-2dac-4eac-8df1-abc456789012'),
       (gen_random_uuid(), 'e5d1d580-a4cf-4677-8220-a50c5decccfa', '35ac7d4e-3fac-5fab-9ed2-cba987654321'),
       (gen_random_uuid(), 'e5d1d580-a4cf-4677-8220-a50c5decccfa', '46bd8e5f-4abc-6a1c-8ef3-dcba87654321');

-- Insert User Permissions to allow hosting tournaments
INSERT INTO user_permissions (user_id, can_host_tournaments, granted_by, granted_at, reason)
VALUES ('13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', true, 'system', '2024-01-01',
        'Initial admin user - Sports enthusiast and backend developer'),
       ('46bd8e5f-4abc-6a1c-8ef3-dcba87654321', true, 'admin', '2024-01-15',
        'Experienced organizer - Runner and sports event organizer');

-- Insert Tournaments
INSERT INTO tournaments (id, name, description, sport_type, format, tournament_type, status, host_id, host_name,
                         location, latitude, longitude, start_date, end_date, registration_deadline, max_participants,
                         current_participants, entry_fee, prize_pool, min_age, max_age, rules, created_at, updated_at)
VALUES (gen_random_uuid(), 'Tbilisi Football Championship',
        'Annual football tournament featuring the best teams from Tbilisi. Join us for an exciting competition with great prizes!',
        'FOOTBALL', 'SINGLE_ELIMINATION', 'team', 'UPCOMING', '13fa5e4e-1d9e-4a2a-9a20-7385f24e9097', 'Kakha Salukvadze',
        'Tbilisi, Georgia', 41.7151, 44.8271, '2025-06-15 09:00:00+00', '2025-06-20 18:00:00+00',
        '2025-06-10 23:59:59+00', 16, 8, 50.0, 1000.0, 18, 35,
        'All participants must follow fair play rules.
Teams must have 11 players.
Matches are 90 minutes long.', NOW(), NOW()),
       (gen_random_uuid(), 'Basketball 3v3 Tournament',
        'Fast-paced 3v3 basketball tournament. Perfect for teams looking for quick, exciting games!', 'BASKETBALL',
        'DOUBLE_ELIMINATION', 'team', 'UPCOMING', '46bd8e5f-4abc-6a1c-8ef3-dcba87654321', 'Mike Johnson',
        'Tbilisi Sports Complex', 41.7151, 44.8271, '2025-07-01 10:00:00+00', '2025-07-03 18:00:00+00',
        '2025-06-25 23:59:59+00', 32, 12, 25.0, 500.0, 16, 40,
        '3v3 format
First to 21 points wins
Shot clock: 12 seconds', NOW(), NOW());

