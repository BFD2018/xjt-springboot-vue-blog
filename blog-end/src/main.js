import Vue from 'vue'
import App from './App.vue'
import router from './router/router.js'
import store from './store/store.js'

Vue.config.productionTip = false;

//使用element-ui
import ElementUI from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
Vue.use(ElementUI);

//懒加载
import VueLazyload from 'vue-lazyload'
Vue.use(VueLazyload, {
  preLoad: 1.3,
  error: require('@/assets/images/common/404.jpg'),
  loading: require('@/assets/images/common/loading.gif'),
  attempt: 1
});

// 使用 mavonEditor
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
Vue.use(mavonEditor);

//粒子特效
import VueParticles from 'vue-particles'
Vue.use(VueParticles);

/*qs*/
import qs from "qs"
Vue.prototype.$qs = qs;

/*vue-aplayer*/
import vueAplayer from 'vue-aplayer'
Vue.use(vueAplayer);

/*less*/
import less from 'less'
Vue.use(less);

/*v-emoji-picker组件*/
import VEmojiPicker from 'v-emoji-picker';
Vue.use(VEmojiPicker);

/*定义常量*/
import * as Myconst from "@/utils/Myconst.js"
Vue.prototype.Myconst = Myconst;

//全局注册混入
import mymixin from "./utils/mymixin"
Vue.use(mymixin);

/*JavaScript时间日期格式化*/
import Moment from 'moment'
Vue.prototype.$moment = Moment;

/*引入echarts*/
import * as echarts from 'echarts';
Vue.prototype.$echarts = echarts;


//将一些请求添加到全局Vue上
import {request,postKeyValueRequest,getRequest,postRequest,putRequest,deleteRequest} from "@/api/request.js"
Vue.prototype.$request = request;
Vue.prototype.$postKeyValueRequest = postKeyValueRequest;
Vue.prototype.$getRequest = getRequest;
Vue.prototype.$postRequest = postRequest;
Vue.prototype.$putRequest = putRequest;
Vue.prototype.$deleteRequest = deleteRequest;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
