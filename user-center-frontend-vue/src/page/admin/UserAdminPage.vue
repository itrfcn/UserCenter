<template>
  <div id="UserAdminPage">
    <div class="components-input-demo-size">
      <a-input
        v-model:value="searchValue.username"
        size="large"
        placeholder="用户名"
      />
      <a-input
        v-model:value="searchValue.userAccount"
        size="large"
        placeholder="账号"
      />
      <a-input
        v-model:value="searchValue.phone"
        size="large"
        placeholder="手机号"
      />
      <a-input
        v-model:value="searchValue.email"
        size="large"
        placeholder="邮箱"
      />
      <a-radio-group v-model:value="searchValue.gender" size="large">
        <a-radio-button value="">空</a-radio-button>
        <a-radio-button value="0">男</a-radio-button>
        <a-radio-button value="1">女</a-radio-button>
      </a-radio-group>
      <a-radio-group v-model:value="searchValue.userStatus" size="large">
        <a-radio-button value="">空</a-radio-button>
        <a-radio-button value="0">正常</a-radio-button>
        <a-radio-button value="1">封禁</a-radio-button>
      </a-radio-group>
      <a-radio-group v-model:value="searchValue.userRole" size="large">
        <a-radio-button value="">空</a-radio-button>
        <a-radio-button value="0">普通用户</a-radio-button>
        <a-radio-button value="1">管理员</a-radio-button>
      </a-radio-group>
      <a-button
        type="primary"
        size="large"
        style="margin-left: 20px"
        @click="onSearch"
        >搜索</a-button
      >
    </div>

    <a-table :columns="columns" :data-source="data">
      <template #headerCell="{ column }">
        <template v-if="column.key === 'name'">
          <span>
            <smile-outlined />
            Name
          </span>
        </template>
      </template>

      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'avatarUrl'">
          <a-image :src="record.avatarUrl" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 1">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'userStatus'">
          <div v-if="record.userStatus === 1">
            <a-tag color="red">封禁</a-tag>
          </div>
          <div v-else>
            <a-tag color="green">正常</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'gender'">
          <div v-if="record.gender === 0">
            <a-tag>男</a-tag>
          </div>
          <div v-else-if="record.gender === 1">
            <a-tag>女</a-tag>
          </div>
          <div v-else>
            <a-tag>未知</a-tag>
          </div>
        </template>
        <template v-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button type="primary" @click="doUpdate(record.id)">修改</a-button>
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { SmileOutlined } from "@ant-design/icons-vue";
import { reactive, ref } from "vue";
import { deleteUser, searchUsers } from "@/api/user";
import { message } from "ant-design-vue";
import dayjs from "dayjs";
import { useRouter } from "vue-router";
const searchValue = reactive({
  userAccount: null,
  username: null,
  phone: null,
  email: null,
  gender: null,
  userRole: null,
  userStatus: null,
});

const onSearch = async () => {
  console.log(searchValue);
  if (searchValue === null) {
    await fetchData();
  } else {
    const res = await searchUsers(searchValue);
    if (res.data.data) {
      data.value = res.data.data || [];
    } else {
      message.error("获取数据失败:" + res.data.description);
    }
  }
};

const doDelete = async (id: number) => {
  if (!id) {
    return;
  }
  const res = await deleteUser(id);
  if (res.data.code === 0) {
    message.success("删除成功");
    fetchData();
  } else {
    message.error("删除失败:" + res.data.description);
  }
};
const router = useRouter();

const doUpdate = (id: number) => {
  router.push({
    name: "用户详细管理",
    params: { id: id },
  });
};

const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "用户名",
    dataIndex: "username",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "头像",
    dataIndex: "avatarUrl",
  },
  {
    title: "性别",
    dataIndex: "gender",
  },
  {
    title: "手机号",
    dataIndex: "phone",
  },
  {
    title: "邮箱",
    dataIndex: "email",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "状态",
    dataIndex: "userStatus",
  },
  {
    title: "用户角色",
    dataIndex: "userRole",
  },
  {
    title: "操作",
    key: "action",
  },
];

const data = ref([]);
const fetchData = async () => {
  const res = await searchUsers({});
  if (res.data.data) {
    data.value = res.data.data || [];
  } else {
    message.error("获取数据失败:" + res.data.description);
  }
};

fetchData();
</script>
<style>
.components-input-demo-size .ant-input {
  width: 200px;
  margin: 0 8px 8px 0;
}
</style>
