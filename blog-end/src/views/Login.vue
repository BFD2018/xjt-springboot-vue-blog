<template>
  <div class="login-view">
    <el-card class="login-card">
      <div slot="header" class="clearfix logo-title">
        <div class="logo-image"><img src="@/assets/maotouying.png" alt="" width="100px" height="90px"></div>
        <div style="font-size: 32px;font-weight: bold;height: 90px;line-height: 90px;margin-left: 20px">小熊博客后台管理登录</div>
      </div>

      <el-form
        v-if="isUsernamePasswordLogin"
        :model="usernamePasswordForm"
        :rules="rules"
        ref="usernamePasswordForm"
        label-width="100px"
        label-position="left"
        class="usernamePasswordFormClass">

        <el-form-item label="用户名" prop="username">
          <el-input v-model="usernamePasswordForm.username"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="usernamePasswordForm.password"></el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="verify_code">
          <el-row>
            <el-col :span="15">
              <el-input v-model="usernamePasswordForm.verify_code"></el-input>
            </el-col>
            <el-col :span="8" :offset="1">
              <el-image id="verify_captcha" @click="updateVerifyCode"
                        style="width: 100%; height: 48px"
                        src="/api/user/getCaptcha"
                        fit="scale-down"></el-image>
            </el-col>
          </el-row>
        </el-form-item>

        <el-row style="padding: 15px;font-size: 18px;">
          <el-col :offset="18" :span="6">
            <el-button type="default" icon="el-icon-message" @click="isUsernamePasswordLogin = !isUsernamePasswordLogin">邮箱登录</el-button>
          </el-col>
        </el-row>
      </el-form>


      <el-form
        v-if="!isUsernamePasswordLogin"
        :model="emailForm"
        ref="emailForm"
        :rules="emailRules"
        label-width="100px"
        label-position="left"
        class="emailFormClass">

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="emailForm.email"></el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <el-row>
            <el-col :span="15">
              <el-input v-model="emailForm.code"></el-input>
            </el-col>

            <el-col :span="8" :offset="1">
              <el-button type="default" :loading="isSendEmailing" @click="sendCode2Email">发送验证码</el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-row style="padding: 15px;font-size: 18px;">
          <el-col :offset="16" :span="6">
            <el-button type="warning" icon="el-icon-user" @click="isUsernamePasswordLogin = !isUsernamePasswordLogin">用户名密码登录</el-button>
          </el-col>
        </el-row>
      </el-form>


      <el-row>
        <el-col :offset="16" :span="4">
          <el-button type="primary" v-if="isUsernamePasswordLogin" @click="submitForm('usernamePasswordForm')">登录
          </el-button>
          <el-button type="primary" v-else @click="submitForm('emailForm')">登录</el-button>
        </el-col>

        <el-col :span="4">
          <el-button type="danger" @click="$router.push('/register')">注册</el-button>
        </el-col>
      </el-row>
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
        isUsernamePasswordLogin: true,   //默认使用用户名密码登录
        isSendEmailing:false,   //是否正在发送邮件
        usernamePasswordForm: {
          username: "admin",
          password: "123456",
          verify_code: "",
        },
        emailForm: {
          email: "1351655382@qq.com",
          code: ""
        },
        rules: {
          username: [
            {required: true, message: "请输入用户名", trigger: "blur"},
            {min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur"}
          ],
          password: [
            {required: true, message: "请输入密码", trigger: "blur"},
            {min: 3, max: 18, message: "长度在 3 到 18 个字符", trigger: "blur"}
          ],
          verify_code: [
            {required: true, message: "请输入验证码", trigger: "blur"},
          ]
        },
        emailRules:{
          email:[
            {required: true, message: "请输入邮箱", trigger: "blur",validator:this.emailValidate},
          ],
          code:[
            {required: true, message: "请输入邮箱", trigger: "blur"},
          ]
        }
      };
    },
    methods: {
      //邮箱验证
      emailValidate(rule, value, callback){
        //rule 对应使用emailValida自定义验证的 对象
        //value 对应使用emailValida自定义验证的 数值
        //callback 回调函数
        const regEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (regEmail.test(value)) {
          return callback();
        }
        callback(new Error("邮箱格式有误"));
      },
      submitForm(formName) {
        if (formName == "usernamePasswordForm") {
          this.$refs[formName].validate(valid => {
            if (valid) {
              this.$postRequest("/user/login", this.usernamePasswordForm).then(res => {
                //console.log(res);
                if (res.data.status === 200) {
                  this.$notify.success("登录成功！");

                  //直接修改store的state数据Vuex跟踪不到，建议不要直接修改
                  this.$store.commit("updateLoginUser", res.data.obj);

                  setTimeout(() => {
                    this.$router.replace("/admin/dashboard")
                  }, 500)
                } else {
                  this.$notify.error(res.data.msg);
                }
              })
            } else {
              console.log("error submit!!");
              return false;
            }
          });
        }
        else if (formName == "emailForm") {
          this.$postRequest("/user/login", this.emailForm).then(res => {
            if (res.data.status === 200) {
              console.log(res);
              this.$notify.success("登录成功！");

              //直接修改store的state数据Vuex跟踪不到，建议不要直接修改
              this.$store.commit("updateLoginUser", res.data.obj);

              setTimeout(() => {
                this.$router.replace("/admin/dashboard")
              }, 500)
            } else {
              this.$notify.error(res.data.msg);
            }
          })
        }
      },
      //更新验证码
      updateVerifyCode(e) {
        document.getElementById("verify_captcha").setAttribute("src", "/api/user/getCaptcha?time=" + new Date());
      },
      //向邮箱发送验证码
      sendCode2Email() {
        this.isSendEmailing = true;
        this.$getRequest("/user/sendMailCode?email=" + this.emailForm.email).then(res => {
          if(res.data.status === 200){
            this.isSendEmailing = false;
            this.$notify.success(res.data.msg);
          }

        })
      }
    }
  };
</script>

<style scoped>
  .login-view {
    position: relative;
    background-image: url('~@/assets/images/common/bg.jpg');
    background-size: cover;

  }

  .login-card {
    width: 550px;
    height: 480px;
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    margin: auto;


  }

  .login-card .logo-title {
    display: flex;
  }
</style>
