<template>
  <div class="xjt-about-view">
    <div class="left">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <h2>关于我</h2>
        </div>
        <div class="content1">
          <div class="left-avatar">
            <el-image
              class="hvr-grow"
              style="width: 100%; height: 100%"
              :src="userInfo.avatar"
              fit="fill"></el-image>
          </div>
          <div class="right-info">
            <div class="username">用户名：
              <span style="background-color: #eeeeee;padding: 5px">{{userInfo.username}}</span>
            </div>
            <div class="nickname">昵称：
              <span style="background-color: #eeeeee;padding: 5px">{{userInfo.nickname}}</span>
            </div>
            <div class="desc">描述：
              <span style="background-color: #eeeeee;padding: 5px">{{userInfo.description}}</span>
            </div>
            <div class="email">邮箱：
              <span style="background-color: #eeeeee;padding: 5px">{{userInfo.email}}</span>
            </div>
            <div>
              <el-tag type="info">{{userInfo.updateTime | filterFormatDate(that)}}</el-tag>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="box-card" style="margin-top: 30px;">
        <div slot="header" class="clearfix">
          <h2>收藏语录</h2>
        </div>
        <div class="content2">
          <div class="hvr-fade my-border-padding">在不幸的源头，总有一桩意外。在幸福的源头，总有一桩巧合</div>
          <div class="hvr-fade my-border-padding">孤独，不一定不快乐；得到，不一定能长久；失去，不一定不再拥有</div>
          <div class="hvr-fade my-border-padding">有的人对你好，是因为你对他好，有的人对你好，是因为懂得你的好</div>
          <div class="hvr-fade my-border-padding">绝望是在每一次的失望中累积出来的，而爱则是被不知感恩的人消耗殆尽的</div>
          <div class="hvr-fade my-border-padding">小时候就觉得酒很苦，为什么大人们还是那么爱喝，长大后才发现比起生活，酒确实甜的许多</div>
          <div class="hvr-fade my-border-padding">随口答应，还不如认真拒绝。承诺有限，精力紧张，大家可以学习另外两种技能：随口拒绝讨厌鬼，认真答应心上人</div>
        </div>
      </el-card>
    </div>

    <div class="right">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <h2>本周热点</h2>
        </div>
        <div class="content">
          <div>
            <el-image
              style="width: 100%; height: auto"
              src="https://ae02.alicdn.com/kf/H55a29ac2fe644ee0a80f6cb93f6fe005t.png"
              fit="fill"></el-image>
          </div>
          <div>
            <el-image
              style="width: 100%; height: auto"
              src="https://ae05.alicdn.com/kf/H1342120bc2b245b6974b3d1b69cd5b9aG.png"
              fit="fill"></el-image>
          </div>
        </div>
      </el-card>
    </div>

  </div>
</template>

<script>
  export default {
    name: "About",
    data() {
      return {
        userInfo: this.$store.state.login_user
      }
    },
  }
</script>

<style lang="less" scoped>
  .xjt-about-view {
    width: 100%;
    display: flex;

    .left {
      flex: 1;

      .content1 {
        display: flex;

        .left-avatar {
          width: 250px;
          height: 250px;
          padding: 10px;
          border-radius: 8px;
          border: 1px solid #ddd;
          overflow: hidden;
        }

        .right-info {
          flex: 1;
          margin-left: 20px;
          box-shadow: -2px 0 3px #ddd;
          padding: 0 26px;

          div {
            color: #606266;
            font-size: 20px;
            line-height: 50px;
          }
        }

      }
    }

    .right {
      width: 360px;
      margin-left: 20px;
      padding: 0 10px;
    }
  }

</style>
