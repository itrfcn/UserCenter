import { useLoginUserStore } from "@/store/user";
import { message } from "ant-design-vue";
import router from "@/router";

/**
 * 全局权限校验
 */
router.beforeEach(async (to, from, next) => {
  // 获取 Pinia Store
  const loginUserStore = useLoginUserStore();
  const loginUser = loginUserStore.loginUser;
  // 等待用户信息加载完成
  if (!loginUser) {
    await loginUserStore.fetchLoginUser();
  }
  // 获取目标 URL
  const toUrl = to.fullPath;

  if (toUrl.startsWith("/admin")) {
    if (!loginUser || loginUser.userRole !== 1) {
      message.error("无权限");
      next("/");
      return;
    }
  }
  if (toUrl.startsWith("/user/information")) {
    if (!loginUser) {
      message.error("未登录");
      const redirectUrl = encodeURIComponent(to.fullPath);
      next(`/user/login?redirect=${redirectUrl}`);
      return;
    }
  }
  if (toUrl.startsWith("/user/login")) {
    if (loginUser) {
      message.error("已登录, 请勿重复登录");
      next("/");
      return;
    }
  }

  // 如果通过校验，继续导航
  next();
});
