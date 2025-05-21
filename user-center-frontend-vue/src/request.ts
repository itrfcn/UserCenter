import axios from "axios";
import router from "@/router";

const myAxios = axios.create({
  // 区分开发环境和线上环境
  baseURL:
    process.env.NODE_ENV === "development"
      ? "http://localhost:3000/"
      : "http://user.z7z.me",
  timeout: 10000,
  withCredentials: true,
});

// Add a request interceptor
myAxios.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
myAxios.interceptors.response.use(
  function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    console.log(response);

    const { data } = response;
    console.log(data);
    // 未登录
    // if (data.code === 40100) {
    //   if (
    //     !response.request.responseURL.includes("user/current") &&
    //     window.location.pathname !== "/user/login" &&
    //     window.location.pathname !== "/"
    //   ) {
    //     router.push(`/user/login?redirect=${window.location.pathname}`);
    //   }
    // }
    return response;
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
  }
);

export default myAxios;
