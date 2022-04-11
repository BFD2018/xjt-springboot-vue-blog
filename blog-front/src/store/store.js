import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

import Config from '@/settings'

export default new Vuex.Store({
  state: {
    HOST: Config.HOST,

    login_user: JSON.parse(window.sessionStorage.getItem(Config.TokenKey)) || {},

    //游客登录后保存信息（登录后可以留言、评论、回复...）
    login_guest: {
      // id: "1",
      // username: "",
      // password: "",
      // nickname: "",
      // avatar: "",
      // email: "",
      // description: "",
      // createTime: "",
      // updateTime: "",
      // salt: "",
      // roles: "",
    }
  },
  mutations: {
    updateLoginUser(state, payload) {
      window.sessionStorage.setItem(Config.TokenKey,JSON.stringify(payload));
      state.login_user = payload;
    },
    updateLoginGuest(state, payload) {
      state.login_guest = payload;
    },
    removeLoginUser(state){
      window.sessionStorage.removeItem(Config.TokenKey);
      state.login_user = {};
    },
  },
  actions: {},
  modules: {}
})
