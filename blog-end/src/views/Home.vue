<template>
  <div class="admin-home-view">
    <div class="header-container">
      <div class="logo">
        <el-image class="logo"
                  style="width: auto; height: 60px;"
                  :src="require('@/assets/images/common/xiong-blog-logo.png')"
                  fit="fill"></el-image>
      </div>

      <div class="title">后台管理系统</div>

      <div class="right">
        <div v-if="!(Object.keys($store.state.login_user).length > 0)">
          <el-button type="success" size="mini" @click="$router.push('/login')">登录</el-button>
          <span style="margin-left: 10px"></span>
          <el-button type="warning" size="mini" @click="$router.push('/register')">注册</el-button>
        </div>

        <div v-else>
          <el-dropdown class="userinfo-dropdown" @command="handleCommand">
								<span class="el-dropdown-link">
									<span>{{this.$store.state.username}}</span>
                  <img style="width: 55px; height: 55px;border-radius:50%;border: 1px solid #ddd;"
                      v-lazy="$store.state.login_user.avatar" alt="">
								</span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout">注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div class="main">
      <div class="main-aside">
        <el-menu class="home-aside"
                 :default-active="sidebarActiveIndex"
                 style="height: 100%;"
                 :default-openeds="['/admin/home','/admin/user']"
                 router
                 background-color="#545c64"
                 text-color="#fff"
                 active-text-color="#ffd04b">

          <el-menu-item index="/admin/dashboard">
            <i class="el-icon-menu"></i>
            <span slot="title">首页</span>
          </el-menu-item>

          <el-submenu :index="item.path" collapse="true"
                      v-for="(item,index) in this.$router.options.routes"
                      v-if="!item.hidden" :key="index">
            <template slot="title">
              <i :class="item.myIcon"></i>
              <span>{{item.name}}</span>
            </template>
            <el-menu-item :index="child.path" v-for="(child,idx) in item.children" v-if="!child.hidden" :key="idx">
              <i :class="child.myIcon?child.myIcon:item.myIcon"></i>
              <span>{{child.name}}</span>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <div class="main-container">
        <!-- 面包屑 -->
        <div class="breadcrumb">
          <el-breadcrumb separator="/" v-if="this.$router.currentRoute.path != '/admin/home'">
            <el-breadcrumb-item :to="{ path: '/admin/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <!--主要内容-->
        <router-view class="main-content"/>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Home",
    components: {},
    data() {
      return {
        sidebarActiveIndex:"/admin/dashboard"
      };
    },
    methods: {
      handleCommand(command) {
        if (command === "userInfo") {
          this.$router.push("/user/detail/")
        }
        if (command === "setting") {
          this.$router.push("/user/setting/")
        }
        if (command === "logout") {
          this.$getRequest("/user/logout").then(res =>{
            console.log(res);
            if(res.data.status === 200){
              this.$message.success("注销成功！");
              this.$router.push("/login");

              //修改store状态
              this.$store.commit("removeLoginUser");
            }
          })
        }
      }
    },
    created() {
      //console.log(this.$route);
    },
    watch:{
      $route(route){
        this.sidebarActiveIndex = route.path;
      }
    }
  };
</script>

<style lang="less" scoped>
  .admin-home-view{
    background-color: rgb(239, 241, 244);
  }
  .header-container {
    width: 100%;
    height: 60px;
    line-height: 60px;
    font-size: 24px;
    font-weight: bold;
    display: flex;
    box-shadow: 0 2px 3px #ddd;
    background-color: #545c64;
    color: white;

    .logo {
      margin-left: 15px;
    }

    .title {
      flex: 1;
      margin-left: 30px;
      line-height: 60px;
      font-size: 26px;
    }

    .right {
      display: flex;

      div {
        margin-left: 10px;
        margin-right: 10px;
      }
    }
  }

  .main {
    width: 100%;
    display: flex;

    .main-aside {
      width: 250px;
      height: 100%;
      background-color: rgb(84, 92, 100);
      .home-aside{
        width: 100%;
      }
    }

    .main-container {
      flex: 1;
      margin: 20px 0 0 10px;
      .main-content{
        padding: 20px;
      }
    }
  }
</style>
