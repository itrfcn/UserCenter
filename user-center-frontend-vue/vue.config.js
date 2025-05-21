const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8080", // 后端服务地址
        changeOrigin: true, // 必须设置为 true，否则会出现跨域问题
        pathRewrite: {
          "^/api": "", // 将 /api 重写为空字符串，例如 /api/user/current 会被代理为 http://localhost:8080/user/current
        },
      },
    },
  },
  lintOnSave: "warning",
});
