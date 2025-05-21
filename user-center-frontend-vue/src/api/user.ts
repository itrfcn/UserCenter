import myAxios from "@/request";

/**
 * 用户注册
 * @param params
 */
export const userRegister = async (params: any) => {
  return myAxios.request({
    url: "/api/user/register",
    method: "POST",
    data: params,
  });
};

/**
 * 用户登录/
 * @param params
 */
export const userLogin = async (params: any) => {
  return myAxios.request({
    url: "/api/user/login",
    method: "POST",
    data: params,
  });
};

/**
 * 用户注销
 * @param params
 */
export const userLogout = async () => {
  return myAxios.request({
    url: "/api/user/logout",
    method: "GET",
  });
};

/**
 * 获取当前用户
 */
export const getCurrentUser = async () => {
  return myAxios.request({
    url: "/api/user/current",
    method: "GET",
  });
};

/**
 * 获取用户列表
 */
export const searchUsers = async (params: any) => {
  return myAxios.request({
    url: "/api/user/search",
    method: "POST",
    data: params,
  });
};

/**
 * 更新用户
 */
export const updateUser = async (params: any) => {
  return myAxios.request({
    url: "/api/user/update",
    method: "POST",
    data: params,
  });
};

/**
 * 管理员更新用户
 */
export const updateUserAdmin = async (params: any) => {
  return myAxios.request({
    url: "/api/user/updateAdmin",
    method: "POST",
    data: params,
  });
};

/**
 * 删除用户
 * @param id
 */
export const deleteUser = async (id: number) => {
  return myAxios.request({
    url: "/api/user/delete?id=" + id,
    method: "GET",
  });
};
