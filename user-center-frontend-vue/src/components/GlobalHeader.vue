<template>
  <div id="GlobalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.png" alt="logo" />
          <div class="title">用户管理中心</div>
        </div>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <a-col flex="200px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser?.id > -1" class="userconter">
            <!--            <a-avatar :size="50" :src="loginUserStore.loginUser.avatarUrl">-->
            <!--              <template #icon><UserOutlined /></template>-->
            <!--            </a-avatar>-->
            <!--            {{ loginUserStore.loginUser.username ?? "无名" }}-->
            <!--            <a-button type="primary" @click="layout">退出</a-button>-->
            <a-menu @click="handleMenuClick">
              <a-dropdown-button>
                {{ loginUserStore.loginUser.username ?? "无名" }}
                <template #overlay>
                  <a-menu @click="handleMenuClick">
                    <a-menu-item key="1" @click="goInfomation">
                      <!--                    <UserOutlined />-->
                      信息设置
                    </a-menu-item>
                    <a-menu-item key="2" @click="layout">
                      <!--                    <UserOutlined />-->
                      退出登录
                    </a-menu-item>
                  </a-menu>
                </template>
                <template #icon>
                  <a-avatar
                    :size="25"
                    :src="loginUserStore.loginUser.avatarUrl"
                  >
                    <template #icon><UserOutlined /></template> </a-avatar
                ></template>
              </a-dropdown-button>
            </a-menu>
          </div>
          <div v-else>
            <router-link to="/user/login">
              <a-button type="primary">登录</a-button>
            </router-link>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { h, ref } from "vue";
import {
  CrownOutlined,
  HomeOutlined,
  UserOutlined,
} from "@ant-design/icons-vue";
import { MenuProps } from "ant-design-vue";
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/store/user";
import { userLogout } from "@/api/user";

const loginUserStore = useLoginUserStore();
const router = useRouter();

const doMenuClick = ({ key }: { key: string }) => {
  if (key.startsWith("http")) {
    window.open(key, "_blank");
  } else {
    router.push({ path: key });
  }
};

const current = ref<string[]>([]);
router.afterEach((to, from) => {
  current.value = [to.path];
});

const items = ref<MenuProps["items"]>([
  {
    key: "/",
    icon: () => h(HomeOutlined),
    label: "主页",
    title: "主页",
  },
  {
    key: "/admin/userManage",
    icon: () => h(CrownOutlined),
    label: "用户管理",
    title: "用户管理",
  },
  {
    key: "https://itrf.cn",
    label: h("a", { href: "https://itrf.cn", target: "_blank" }, "IT睿行"),
    title: "IT睿行",
  },
]);
// const handleMenuClick: MenuProps["onClick"] = (e) => {
//   console.log("click", e);
//   layout;
// };

const layout = async () => {
  try {
    // 执行登出逻辑
    await userLogout();
    // 清除本地存储中的 token
    localStorage.removeItem("token");
    // 清空 Pinia Store 中的用户信息
    loginUserStore.setLoginUser(null);
    // 重定向到登录页面
    router.replace("/user/login");
  } catch (error) {
    console.error("登出失败:", error);
  }
};
const goInfomation = async () => {
  router.replace("/user/information");
};
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}
.demo-dropdown-wrap :deep(.ant-dropdown-button) {
  margin-right: 8px;
  margin-bottom: 8px;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>
