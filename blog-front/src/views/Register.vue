<template>
  <div class="register-view">
    <el-card class="login-card">
      <div slot="header" class="clearfix">
        <div class="my-title">小熊博客注册</div>
      </div>

      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">注册</el-button>

          <el-button type="success" style="margin-left: 50px;" @click="$router.push('/toLogin')">去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!--粒子特效-->
    <vue-particles
      color="#dedede"
      :particleOpacity="0.7"
      :particlesNumber="50"
      shapeType="circle"
      :particleSize="4"
      linesColor="#dedede"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="3"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push">
    </vue-particles>
  </div>
</template>

<script>
  export default {
    name: "Login",
    components: {},
    data() {
      return {
        ruleForm: {
          username: "root",
          password: "123456",
        },
        rules: {
          username: [
            {required: true, message: "请输入用户名", trigger: "blur"},
            {min: 1, max: 18, message: "长度在 1 到 18 个字符", trigger: "blur"}
          ],
          password: [
            {required: true, message: "请输入密码", trigger: "blur"},
            {min: 1, max: 18, message: "长度在 1 到 18 个字符", trigger: "blur"}
          ],
        }
      };
    },
    methods: {
      submitForm() {
        this.$refs.ruleForm.validate(valid => {
          if (valid) {
            this.$postRequest("/user/register", this.ruleForm).then(res => {
              if (res.data.status === 200) {
                this.$notify.success(res.data.msg);
                setTimeout(() => {
                  this.$router.replace("/toLogin")
                }, 1500)
              } else {
                this.$notify.error(res.data.msg);
              }
            })
          } else {
            this.$notify.error("error submit!!");
            return false;
          }
        });
      }
    }
  };
</script>

<style scoped>
  .register-view {
    position: relative;
    width: 100vw;
    height: 100vh;
    background-image: url('~@/assets/images/common/bg.jpg');
    background-size: cover;
  }

  .login-card {
    width: 420px;
    height: 320px;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;
  }
</style>
