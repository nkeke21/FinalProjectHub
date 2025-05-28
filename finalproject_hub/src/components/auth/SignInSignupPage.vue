<template>
    <div class="auth-container">
        <div class="left-panel">
            <img src="@/assets/Join2Play.png" alt="Join2Play Logo" class="auth-icon" />
        </div>

        <div class="right-panel">
        
        </div>
    </div>
</template>
  
<script setup>
</script>
  
<style scoped>
.auth-container {
    display: flex;
    height: 100vh;
    width: 100vw;
}

.left-panel {
    background-color: var(--dark);
    width: 40%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.auth-icon {
    width: 160px;
    height: auto;
}

.right-panel {
    width: 60%;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: white;
}
</style>
  