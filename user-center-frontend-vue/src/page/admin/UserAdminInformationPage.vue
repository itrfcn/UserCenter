<template>
  <div class="user-profile-container">
    <a-card title="个人信息" class="user-profile-card">
      <a-form
        :model="form"
        :rules="rules"
        ref="formRef"
        @finish="onSubmit"
        layout="vertical"
      >
        <a-form-item label="用户名" name="username">
          <a-input v-model:value="form.username" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="账号" name="userAccount">
          <a-input v-model:value="form.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item label="性别" name="gender">
          <a-radio-group v-model:value="form.gender">
            <a-radio :value="0">男</a-radio>
            <a-radio :value="1">女</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="QQ号" name="QQNumber">
          <a-input v-model:value="form.QQNumber" placeholder="请输入QQ号" />
        </a-form-item>
        <a-form-item label="手机号" name="phone">
          <a-input v-model:value="form.phone" placeholder="请输入手机号" />
        </a-form-item>
        <a-form-item label="邮箱" name="email">
          <a-input v-model:value="form.email" placeholder="请输入邮箱" />
        </a-form-item>
        <a-form-item label="密码" name="userPassword">
          <a-input-password
            v-model:value="form.userPassword"
            placeholder="请输入密码，不填则不更新"
          />
        </a-form-item>
        <a-form-item label="状态" name="userStatus">
          <a-radio-group v-model:value="form.userStatus">
            <a-radio :value="0">正常</a-radio>
            <a-radio :value="1">封禁</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="权限" name="userRole">
          <a-radio-group v-model:value="form.userRole">
            <a-radio :value="0">普通用户</a-radio>
            <a-radio :value="1">管理员</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">保存修改</a-button>
          <a-button type="primary" @click="router().go(-1)">返回</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { searchUsers, updateUserAdmin } from "@/api/user";
import { useRoute } from "vue-router";
import router from "@/router";

export default {
  methods: {
    router() {
      return router;
    },
  },
  setup() {
    const form = reactive({
      id: "",
      username: "",
      userAccount: "",
      gender: "",
      avatar: "",
      phone: "",
      email: "",
      userPassword: "",
      confirmPassword: "",
      avatarUrl: "",
      QQNumber: "",
      userStatus: "",
      userRole: "",
    });

    const rules = {
      username: [
        { required: true, message: "请输入用户名", trigger: "blur" },
        {
          min: 2,
          max: 8,
          message: "用户名长度必须在 2 到 8 位之间",
          trigger: "blur",
        },
      ],
      userAccount: [
        { required: true, message: "请输入账号", trigger: "blur" },
        {
          min: 4,
          max: 8,
          message: "账号长度必须在 4 到 8 位之间",
          trigger: "blur",
        },
      ],
      gender: [{ required: true, message: "请选择性别", trigger: "blur" }],
      phone: [
        { required: true, message: "请输入手机号", trigger: "blur" },
        {
          pattern: /^1[3-9]\d{9}$/,
          message: "请输入有效的手机号",
          trigger: "blur",
        },
      ],
      QQNumber: [
        { required: true, message: "请输入QQ号", trigger: "blur" },
        {
          pattern: /^[1-9]\d{4,10}$/,
          message: "请输入有效的QQ号",
          trigger: "blur",
        },
      ],
      email: [
        { required: true, message: "请输入邮箱", trigger: "blur" },
        { type: "email", message: "请输入有效的邮箱地址", trigger: "blur" },
      ],
      userPassword: [
        {
          min: 4,
          max: 20,
          message: "密码长度必须在 4 到 20 位之间",
          trigger: "blur",
        },
        {
          pattern:
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).*$/,
          message: "密码必须包含大小写字母、数字和特殊符号",
          trigger: "blur",
        },
      ],
      userStatus: [
        { required: true, message: "请选择状态", trigger: "blur" },
        {
          validator: (rule, value) => {
            if (![0, 1].includes(value)) {
              return Promise.reject(new Error("状态参数错误"));
            }
            return Promise.resolve();
          },
          trigger: "blur",
        },
      ],
      userRole: [
        { required: true, message: "请选择权限", trigger: "blur" },
        {
          validator: (rule, value) => {
            if (![0, 1].includes(value)) {
              return Promise.reject(new Error("权限参数错误"));
            }
            return Promise.resolve();
          },
          trigger: "blur",
        },
      ],
    };

    const formRef = ref(null);
    const route = useRoute();
    const userId = ref(route.params.id);
    console.log(userId.value);
    const fetchUserProfile = async () => {
      try {
        const res = await searchUsers({ id: userId.value });
        if (res.data.code === 0 && res.data.data && res.data.data.length > 0) {
          const userData = res.data.data[0]; // 提取第一个元素
          form.id = userData.id;
          form.username = userData.username;
          form.userAccount = userData.userAccount;
          form.gender = userData.gender;
          form.avatar = userData.avatar;
          form.phone = userData.phone;
          form.email = userData.email;
          form.avatarUrl = userData.avatarUrl;
          form.QQNumber = userData.QQNumber;
          form.userStatus = userData.userStatus;
          form.userRole = userData.userRole;
          // 从 avatarUrl 中提取 QQ 号码
          if (form.avatarUrl) {
            const match = form.avatarUrl.toString().match(/dst_uin=(\d+)/);
            if (match && match[1]) {
              form.QQNumber = match[1]; // 提取 QQ 号码
            }
          }
        } else {
          message.error("未找到用户信息");
        }
      } catch (error) {
        message.error("获取个人信息失败");
      }
    };

    const onSubmit = async () => {
      try {
        await formRef.value.validate();
        // 拼接 QQ 头像 URL
        form.avatarUrl = `http://q.qlogo.cn/headimg_dl?dst_uin=${form.QQNumber}&spec=640&img_type=jpg`;
        const res = await updateUserAdmin(form);
        if (res.data.code === 0) {
          message.success("个人信息更新成功");
          await fetchUserProfile();
        } else {
          message.error("个人信息更新失败:" + res.data.description);
        }
      } catch (error) {
        message.error("表单验证失败");
      }
    };

    fetchUserProfile();

    return {
      form,
      rules,
      formRef,
      onSubmit,
    };
  },
};
</script>

<style scoped>
.user-profile-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.user-profile-card {
  width: 100%;
  max-width: 600px;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  overflow: hidden;
  text-align: center;
  line-height: 100px;
  cursor: pointer;
}

.avatar-uploader:hover {
  border-color: #40a9ff;
}

.avatar-uploader img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
</style>
