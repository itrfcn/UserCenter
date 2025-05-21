<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style></style>
<script setup lang="ts">
import { useLoginUserStore } from "@/store/user";
import BasicLayout from "@/layout/BasicLayout.vue";
const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();
</script>
