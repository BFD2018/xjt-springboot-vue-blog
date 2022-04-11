import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

import Config from '@/settings'
import {getToken,setToken,removeToken} from "@/utils/auth.js"

export default new Vuex.Store({
  state: {
    HOST: Config.HOST,

    login_user: getToken() || {},    //后台管理员登录的信息
  },
  mutations: {
    updateLoginUser(state, payload) {
      setToken(payload);
      state.login_user = payload;
    },
    removeLoginUser(state){
      removeToken();
      state.login_user = {};
    },
  },
  actions: {
  },
  modules: {
  }
})
