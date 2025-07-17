<template>
	<n-message-provider>
		<n-notification-provider>
			<div class="app">
				<FriendRequestNotification />
				<Sidebar v-if="showSidebar" />
				<router-view />
			</div>
		</n-notification-provider>
	</n-message-provider>
</template>

<script setup>
import { NMessageProvider, NNotificationProvider } from 'naive-ui'
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from './components/Sidebar/Sidebar.vue'
import ProfileComponent from './components/profile/ProfileComponent.vue';
import FriendRequestNotification from '@/components/common/requests/FriendRequestNotification.vue'

const route = useRoute()
const showSidebar = computed(() => route.path !== '/')
</script>

<style lang="scss">
:root {
	--primary: #4ade80;
	--primary-alt: #22c55e;
	--grey: #64748b;
	--dark: #1e293b;
	--dark-alt: #334155;
	--light: #f1f5f9;
	--sidebar-width: 300px;
}

* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: 'Fira sans', sans-serif;
}

body {
	background: var(--light);
}

button {
	cursor: pointer;
	appearance: none;
	border: none;
	outline: none;
	background: none;
}

.app {
	display: flex;

	main {
		flex: 1 1 0;
		padding: 2rem;

		@media (max-width: 1024px) {
			padding-left: 6rem;
		}
	}
}
</style>