<template>
  <div id="app">
    <router-view />
    <!-- 全局聊天机器人组件 -->
    <ChatWidget />
  </div>
</template>

<script>
import ChatWidget from './component/ChatWidget.vue'

export default {
  name: 'App',
  components: {
    ChatWidget
  },
  mounted() {
    // 应用初始化，移除了认证初始化
    console.log('CoralKita应用已启动')
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: 'Arial', 'Microsoft YaHei', sans-serif;
}

#app {
  height: 100vh;
  width: 100%;
}
</style>
