<template>
  <div class="kkfile-view">
    <el-input type="text" v-model="fileUrl" placeholder="输入要预览的url"></el-input>
    <el-button type="success" @click="previewFile">预览</el-button>
  </div>
</template>

<script>
  import {Base64} from 'js-base64'

  export default {
    name: "TestKkfileview",
    data(){
      return{
        fileUrl:"",
      }
    },
    methods:{
      previewFile(){
        window.open('http://127.0.0.1:8012/onlinePreview?url='+Base64.encode(this.fileUrl));
      },
    },
    created() {
      console.log(Base64.encode('潘高'));
      console.log(Base64.decode('5r2Y6auY'));
    }
  }
</script>

<style scoped>

</style>
