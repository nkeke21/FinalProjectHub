<template>
  <div class="tournament-list-page">
    <div class="page-header">
      <h1>Tournaments</h1>
      <p>Discover and join exciting tournaments in your area</p>
    </div>

    <div class="tournament-filters">
      <n-button type="primary" color="orange">
        <template #icon>
          <n-icon><FilterOutline /></n-icon>
        </template>
        Filter Tournaments
      </n-button>
    </div>

    <div class="tournament-grid">
      <div class="tournament-card" v-for="tournament in tournaments" :key="tournament.id">
        <div class="tournament-header">
          <div class="tournament-badge">
            <n-icon size="20" color="#f97316">
              <FootballOutline v-if="tournament.sportType === 'Football'" />
              <BasketballOutline v-else-if="tournament.sportType === 'Basketball'" />
              <TennisballOutline v-else-if="tournament.sportType === 'Tennis'" />
              <FitnessOutline v-else />
            </n-icon>
            <span class="sport-type">{{ tournament.sportType }}</span>
          </div>
          <div class="tournament-status" :class="tournament.status.toLowerCase()">
            {{ tournament.status }}
          </div>
        </div>
        
        <h3 class="tournament-title">{{ tournament.name }}</h3>
        
        <div class="tournament-meta">
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <PersonOutline />
            </n-icon>
            <span>{{ tournament.hostName }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <LocationOutline />
            </n-icon>
            <span>{{ tournament.location }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <CalendarOutline />
            </n-icon>
            <span>{{ formatDateRange(tournament.startDate, tournament.endDate) }}</span>
          </div>
          <div class="meta-item">
            <n-icon size="16" color="#64748b">
              <PeopleOutline />
            </n-icon>
            <span>{{ tournament.ageRange.min }}-{{ tournament.ageRange.max }} years</span>
          </div>
        </div>

        <div class="tournament-participants">
          <span class="participant-count">{{ tournament.currentParticipants }}/{{ tournament.maxParticipants }} participants</span>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: (tournament.currentParticipants / tournament.maxParticipants * 100) + '%' }"></div>
          </div>
        </div>

        <div class="tournament-actions">
          <n-button type="primary" color="orange" class="view-details-btn">
            View Details
          </n-button>
        </div>
      </div>
    </div>

    <div class="empty-state" v-if="tournaments.length === 0">
      <n-icon size="64" color="#cbd5e1">
        <TrophyOutline />
      </n-icon>
      <h3>No tournaments available</h3>
      <p>Check back later for upcoming tournaments in your area</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { 
  NButton, 
  NIcon 
} from 'naive-ui'
import { 
  FilterOutline,
  FootballOutline,
  BasketballOutline,
  TennisballOutline,
  FitnessOutline,
  LocationOutline,
  CalendarOutline,
  PeopleOutline,
  PersonOutline,
  TrophyOutline
} from '@vicons/ionicons5'
import { mockTournaments } from '@/data/mockTournaments'
import type { Tournament } from '@/models/Tournament'

const tournaments = ref<Tournament[]>(mockTournaments)

const formatDateRange = (startDate: string, endDate: string) => {
  const start = new Date(startDate).toLocaleDateString();
  const end = new Date(endDate).toLocaleDateString();
  return `${start} - ${end}`;
};
</script>

<style scoped>
.tournament-list-page {
  padding: 2rem;
  width: 90%;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 2rem;
  text-align: center;
}

.page-header h1 {
  font-size: 2.5rem;
  color: #1e293b;
  margin-bottom: 0.5rem;
}

.page-header p {
  color: #64748b;
  font-size: 1.1rem;
}

.tournament-filters {
  margin-bottom: 2rem;
  display: flex;
  justify-content: flex-end;
}

.tournament-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.tournament-card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
  cursor: pointer;
}

.tournament-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.tournament-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.tournament-badge {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: #fff7ed;
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
}

.sport-type {
  font-weight: 600;
  color: #f97316;
  font-size: 0.875rem;
}

.tournament-status {
  padding: 0.1075rem 0.775rem;
  border-radius: 20px;
  font-size: 0.7rem;
  font-weight: 600;
  text-transform: uppercase;
  white-space: nowrap;
}

.tournament-status.registration-open {
  background: #fef3c7;
  color: #92400e;
}

.tournament-status.in-progress {
  background: #dbeafe;
  color: #1e40af;
}

.tournament-status.completed {
  background: #dcfce7;
  color: #166534;
}

.tournament-status.registration-closed {
  background: #fee2e2;
  color: #991b1b;
}

.tournament-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 1rem;
  line-height: 1.4;
}

.tournament-meta {
  margin-bottom: 1rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  color: #64748b;
  font-size: 0.875rem;
}

.tournament-participants {
  margin-bottom: 1.5rem;
}

.participant-count {
  display: block;
  color: #f97316;
  font-weight: 600;
  font-size: 0.875rem;
  margin-bottom: 0.5rem;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: #f97316;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.tournament-actions {
  display: flex;
  justify-content: center;
}

.view-details-btn {
  width: 100%;
  height: 40px;
  border-radius: 8px;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #64748b;
}

.empty-state h3 {
  margin: 1rem 0 0.5rem 0;
  color: #475569;
}

.empty-state p {
  font-size: 1rem;
}

@media (max-width: 768px) {
  .tournament-list-page {
    padding: 1rem;
  }
  
  .tournament-grid {
    grid-template-columns: 1fr;
  }
  
  .page-header h1 {
    font-size: 2rem;
  }
}
</style> 