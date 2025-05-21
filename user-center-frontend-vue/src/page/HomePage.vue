<template>
  <div class="welcome-container">
    <div class="welcome-content">
      <h1 class="welcome-title">欢迎来到我们的平台</h1>
      <p class="welcome-subtitle">探索无限可能，开启精彩旅程</p>
      <a-button type="primary" size="large" @click="goToHome">
        <template #icon>
          <ArrowRightOutlined />
        </template>
        开始探索
      </a-button>
    </div>
    <div class="welcome-image">
      <img src="../assets/logo.png" alt="Logo" />
    </div>
  </div>
</template>

<script>
import { ArrowRightOutlined } from "@ant-design/icons-vue";
import { Button } from "ant-design-vue";

export default {
  components: {
    ArrowRightOutlined,
    "a-button": Button,
  },
  methods: {
    goToHome() {
      this.$router.push("/user/information");
    },
  },
};
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.welcome-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 88vh;
  background: linear-gradient(135deg, #74ebd5, #acb6e5);
  color: white;
  text-align: center;
}

.welcome-content {
  margin-bottom: 20px;
}

.welcome-title {
  font-size: 3rem;
  margin-bottom: 10px;
}

.welcome-subtitle {
  font-size: 1.2rem;
  margin-bottom: 20px;
}

.welcome-image {
  width: 100%;
  max-width: 400px;
  margin-top: 20px;
}

.welcome-image img {
  width: 100%;
  height: auto;
}
</style>
