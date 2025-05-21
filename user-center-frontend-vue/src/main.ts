import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createPinia } from "pinia";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/reset.css";
import "./access";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
createApp(App).use(pinia).use(Antd).use(router).mount("#app");
