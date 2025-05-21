import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomePage from "@/page/HomePage.vue";
import UserLoginAndRegisterPage from "@/page/user/UserLoginAndRegisterPage.vue";
import UserAdminPage from "@/page/admin/UserAdminPage.vue";
import UserInformationPage from "@/page/user/UserInformationPage.vue";
import UserAdminInformationPage from "@/page/admin/UserAdminInformationPage.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomePage,
  },
  {
    path: "/user/login",
    name: "登录",
    component: UserLoginAndRegisterPage,
  },
  {
    path: "/user/information",
    name: "个人信息",
    component: UserInformationPage,
  },
  {
    path: "/admin/userManage",
    name: "用户管理",
    component: UserAdminPage,
  },
  {
    path: "/admin/userManageDetial/:id",
    name: "用户详细管理",
    component: UserAdminInformationPage,
  },
  {
    path: "/:pathMatch(.*)*", // 通配符路由
    redirect: "/", // 重定向到首页
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
