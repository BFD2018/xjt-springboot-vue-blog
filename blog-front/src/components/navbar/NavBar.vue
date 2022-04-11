<template>
  <div id="navBar">
    <div class="left">
      <slot name="left-slot"></slot>
    </div>
    <div class="middle">
      <slot name="middle-slot"></slot>
    </div>
    <div class="right">
      <slot name="right-slot"></slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: "NavBar",
  }
</script>

<style scoped>
  #navBar {
    display: flex;
    height: 50px;
    line-height: 50px;
  }
  .left{
    width: 150px;
    font-size: 24px;
    text-align: left;
  }

  .middle {
    flex: 1;
    text-align: center;
  }

  .right {
    width: 150px;
    font-size: 22px;
    text-align: right;
  }
</style>
