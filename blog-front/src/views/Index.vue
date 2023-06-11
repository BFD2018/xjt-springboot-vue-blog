<template>
  <div class="index-view">
    <div id="header-outer">
      <el-row type="flex" justify="space-between">
        <el-col :span="3" class="logo-title-col">
          <div class="my-left">
            <img src="@/assets/images/common/xiong-blog-logo.jpg" class="logo" alt="" height="60px">
          </div>
        </el-col>

        <el-col :span="14">
          <el-menu
            :default-active="headerNavActive"
            router
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="#ffffff"
            text-color="#303133"
            active-text-color="#409EFF">
            <el-menu-item index="/blog/list">博客列表</el-menu-item>
            <el-menu-item index="/blog/type">分类专栏</el-menu-item>
            <el-menu-item index="/blog/tag">文章标签</el-menu-item>
            <el-menu-item index="/blog/messages">留言板</el-menu-item>
            <el-menu-item index="/blog/links">友链</el-menu-item>
            <el-menu-item index="/blog/about">关于我</el-menu-item>
            <el-menu-item index="/blog/netdisk">个人网盘</el-menu-item>
          </el-menu>
        </el-col>

        <el-col :span="5" style="line-height: 60px;">
          <el-input
              size="small"
              placeholder="请输入内容"
              suffix-icon="el-icon-search"
              @change="handleSearch"
              v-model="inputSearchKey">
          </el-input>
        </el-col>

        <el-col :span="1" :offset="1" style="text-align: right;line-height: 60px;">
          <div v-if="Object.keys($store.state.login_user).length > 0">
            <el-dropdown class="userinfo-dropdown" @command="handleCommand">
								<span class="el-dropdown-link">
									<el-image
                    style="width: 55px; height: 55px;border-radius:50%"
                    :src="$store.state.login_user.avatar|filterImgUrl(that)"
                    fit="cover">
									</el-image>
								</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
                <el-dropdown-item command="setting">设置</el-dropdown-item>
                <el-dropdown-item command="logout">注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>

          <div v-else>
            <el-button type="success" size="mini" @click="$router.push('/toLogin')">登录</el-button>
            <span style="margin-left: 10px"></span>
            <el-button type="warning" size="mini" @click="$router.push('/toRegister')">注册</el-button>
          </div>
        </el-col>

      </el-row>
    </div>

    <div class="middle-container">
      <router-view/>
    </div>

    <div id="footer-outer">
      <div class="footer-content">
        <div class="item contact-me">
          <h3>联系我</h3>
          <p>Email:1351655382@qq.com</p>
          <p>Phone:15990076961</p>
        </div>
        <div class="item focus-me">
          <h3>关注我</h3>
          <el-image
            style="width: 100px; height: 100px"
            src="https://ae01.alicdn.com/kf/Hc0d21f283e11463894c017c6b673b84bM.png"
            fit="contain"></el-image>
        </div>
        <div class="item talk-us">
          <h3>与我交流</h3>
          <el-image
            style="width: 100px; height: 100px"
            src="https://ae01.alicdn.com/kf/Hc0d21f283e11463894c017c6b673b84bM.png"
            fit="contain"></el-image>
        </div>
        <div class="item website-info">
          <h5>站点信息</h5>
          <p><b>文章总数：</b>13篇</p>
          <p><b>访问总数：</b>174次</p>
          <p><b>评论总数：</b>19条</p>
          <p><b>留言总数：</b>8条</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Index",
    data() {
      return {
        inputSearchKey: '',
        headerNavActive:"/blog/list",
      }
    },
    watch:{
      chartData: {
        deep: true,
        handler(val) {
          this.setChartOption(val)
        }
      }
    },
    methods: {
      handleSearch(val) {
        console.log(val);
      },
      handleCommand(command) {
        if (command === "userInfo") {
          this.$router.push("/user/info/")
        }
        if (command === "setting") {
          this.$router.push("/user/setting/")
        }
        if (command === "logout") {
          this.$getRequest("/user/logout").then(res => {
            console.log(res);
            if (res.data.status === 200) {
              this.$message.success("注销成功！");
              this.$router.push("/toLogin");

              //修改store状态
              this.$store.commit("removeLoginUser");
            }
          })
        }
      },

      handleSelect(key, keyPath) {
        this.headerNavActive = key;
      }
    },
    created() {

    }
  }
</script>

<style lang="less" scoped>
  .index-view {
    position: relative;
    width: 100vw;
    height: auto;
  }

  #header-outer {
    width: 100%;
    height: 60px;
    background-color: #ffffff;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
    border-bottom: 1px solid #dddddd;

    .el-row {
      margin: 0 20px;
    }

    .btn {
      line-height: 60px;
    }
  }

  .middle-container {
    width: 90vw;
    margin: 80px auto 20px auto;
  }

  #footer-outer {
    width: 100%;
    position: relative;
    background-color: rgb(84, 92, 100);

    .footer-content {
      color: aliceblue;
      height: 240px;
      margin: auto;
      width: 90vw;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .item {
        flex: 1;
        text-align: center;
        line-height: 32px;
      }
    }
  }
</style>
