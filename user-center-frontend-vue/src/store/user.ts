import { defineStore } from "pinia";
import { ref } from "vue";
import { getCurrentUser, userLogout } from "@/api/user";

export const useLoginUserStore = defineStore(
  "loginUser",
  () => {
    const loginUser = ref<any>({
      username: "未登录",
    });

    async function fetchLoginUser() {
      try {
        const res = await getCurrentUser();
        if (res.data.code === 0 && res.data.data) {
          loginUser.value = res.data.data;
        } else {
          console.warn("获取用户信息失败:", res.data.message || "未知错误");
          // 执行登出逻辑
          await userLogout();
          // 清除本地存储中的 token
          localStorage.removeItem("token");
          // 清空 Pinia Store 中的用户信息
          setLoginUser(null);
        }
      } catch (error) {
        console.error("获取用户信息失败:", error);
      }
    }

    function setLoginUser(newLoginUser: any) {
      loginUser.value = newLoginUser;
    }

    return { loginUser, setLoginUser, fetchLoginUser };
  },
  {
    persist: true,
  }
);
