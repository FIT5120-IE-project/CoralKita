<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  mounted() {
    // 初始化时恢复用户认证状态
    this.$store.dispatch('initAuth')
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
