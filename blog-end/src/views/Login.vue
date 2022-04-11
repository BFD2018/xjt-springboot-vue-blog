<template>
  <div class="login-view">
    <el-card class="login-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 26px;font-weight: bold;">小熊博客后台管理登录</span>
      </div>

      <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="100px"
          label-position="left"
          class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password"></el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="verify_code">
          <el-row>
            <el-col :span="15">
              <el-input v-model="ruleForm.verify_code"></el-input>
            </el-col>
            <el-col :span="8" :offset="1">
              <el-image id="verify_captcha" @click="updateCaptcha"
                        style="width: 100%; height: 48px"
                        src="/api/user/getCaptcha"
                        fit="scale-down"></el-image>
            </el-col>
          </el-row>
        </el-form-item>

        <el-row>
          <el-col :offset="4" :span="8">
            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
          </el-col>

            <el-col :offset="4" :span="8">
                <el-button type="warning" @click="$router.push('/register')">注册</el-button>
            </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!--粒子特效-->
    <vue-particles
        color="#dedede"
        :particleOpacity="0.7"
        :particlesNumber="80"
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
  data() {
    return {
      ruleForm: {
        username: "admin",
        password: "123456",
        verify_code: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur" }
        ],
        verify_code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$postRequest("/user/login",this.ruleForm).then(res =>{
            //console.log(res);
            if(res.data.status === 200){
              this.$notify.success("登录成功！");

              //直接修改store的state数据Vuex跟踪不到，建议不要直接修改
              this.$store.commit("updateLoginUser", res.data.obj);

              setTimeout(()=>{
                this.$router.replace("/admin/dashboard")
              },500)
            }else{
              this.$notify.error(res.data.msg);
            }
          })
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    updateCaptcha(e){
      document.getElementById("verify_captcha").setAttribute("src","/api/user/getCaptcha?time=" + new Date());
    }
  }
};
</script>

<style scoped>
.login-view{
  position: relative;
  background-image: url('~@/assets/images/common/bg.jpg');
  background-size: cover;
  width: 100vw;
  height: 100vh;
}
.login-card {
  width: 500px;
  height: 400px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
</style>
