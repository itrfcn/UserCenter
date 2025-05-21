<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header"><GlobalHeader /></a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="footer"
        ><a href="https://itrf.cn" target="_blank"
          >©2025 IT睿行 版权所有</a
        ></a-layout-footer
      >
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
</script>
<style scoped>
#basicLayout {
}

#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
#basicLayout .content {
  background: linear-gradient(to right, #fefefe, #fff);
  margin-bottom: 30px;
  //padding: 16px;
}
#basicLayout .header {
  padding-inline: 20px;
  margin-bottom: 5px;
  color: unset;
  background: white;
}

</style>
