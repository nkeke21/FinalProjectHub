<template>
    <div class="about-page">
        <n-page-header title="About Join2Play" subtitle="Discover what we do and how it benefits you" />

        <n-space vertical size="large">
        <n-grid x-gap="24" y-gap="24" cols="1 s:1 m:2" responsive>
            <n-gi>
                <n-card hoverable>
                    <template #header>
                        <n-icon :size="22"><i class="material-icons">info</i></n-icon>
                        <span style="margin-left: 8px; font-weight: 600;">What is Join2Play?</span>
                    </template>
                    <n-text>
                        <strong>Join2Play</strong> is a modern social sports platform that makes it easy to discover, join, and host local sporting events. Whether you're planning a football game, attending a running group, or organizing a tennis match, our platform connects sports enthusiasts of all skill levels.
                    </n-text>
                </n-card>
            </n-gi>

            <n-gi>
                <n-card hoverable>
                    <template #header>
                        <n-icon :size="22"><i class="material-icons">emoji_events</i></n-icon>
                        <span style="margin-left: 8px; font-weight: 600;">What We Offer</span>
                    </template>
                    <n-list bordered>
                    <n-list-item>
                        <n-thing title="Create Events" content="Host your own events with full control over sport type, location, time, and participant capacity." />
                    </n-list-item>
                    <n-list-item>
                        <n-thing title="Discover & Join" content="Easily find nearby events filtered by sport, time, or distance — and join in seconds." />
                    </n-list-item>
                    <n-list-item>
                        <n-thing title="Teams & Communities" content="Build or join sport communities, manage team members, and stay engaged." />
                    </n-list-item>
                    <n-list-item>
                        <n-thing title="Tournaments" content="Create or join structured tournaments with automated brackets and progress tracking." />
                    </n-list-item>
                    <n-list-item>
                        <n-thing title="Notifications" content="Never miss a game — receive timely alerts before events start." />
                    </n-list-item>
                    </n-list>
                </n-card>
            </n-gi>

            <n-gi span="2">
                <n-card hoverable>
                    <template #header>
                    <n-icon :size="22"><i class="material-icons">public</i></n-icon>
                    <span style="margin-left: 8px; font-weight: 600;">Our Mission</span>
                    </template>
                    <n-text>
                    We believe in making sports more accessible, inclusive, and social. Join2Play exists to break down barriers so that anyone, regardless of background or experience, can find like-minded players, get active, and form lasting connections.
                    </n-text>
                </n-card>
            </n-gi>
        </n-grid>
        </n-space>
    </div>
</template>
  
<script setup>
import { NPageHeader, NCard, NGrid, NGi, NText, NDivider, NList, NListItem, NThing, NIcon, NSpace } from 'naive-ui'
</script>
  
<style scoped>
.about-page {
    padding: 40px;
    max-width: 1000px;
    margin: 0 auto;
}
.material-icons {
    vertical-align: middle;
}
</style>
