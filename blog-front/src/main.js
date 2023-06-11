import Vue from 'vue'
import App from './App.vue'
import routers from './router/routers.js'
import store from './store/store.js'

//router权限控制
import './router/index.js'

Vue.config.productionTip = false;

//使用element-ui
import ElementUI from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
Vue.use(ElementUI);

import less from 'less'
Vue.use(less);

//粒子特效
import VueParticles from 'vue-particles'
Vue.use(VueParticles);

/*qs*/
import qs from "qs"
Vue.prototype.$qs = qs;

/*日期时间格式化*/
import Moment from 'moment'
Vue.prototype.$moment = Moment;

/*使用 mavonEditor*/
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
Vue.use(mavonEditor);

/*v-emoji-picker组件*/
import VEmojiPicker from 'v-emoji-picker';
Vue.use(VEmojiPicker);

/*使用hover.css*/
import hover from "hover.css"
Vue.use(hover);

/*定义常量*/
import * as Myconst from "@/utils/Myconst.js"
Vue.prototype.Myconst = Myconst;

//全局注册混入
import mymixin from "@/utils/mymixin"
Vue.use(mymixin);

import {$request,$getRequest,$postRequest,$getRequestBlob,$postKeyValueRequest} from "./network/request";
Vue.prototype.$request = $request;
Vue.prototype.$getRequest = $getRequest;
Vue.prototype.$postRequest = $postRequest;

//AdminLTE
import 'admin-lte/dist/js/adminlte.min.js'
import 'admin-lte/dist/css/adminlte.min.css'

//图片懒加载
import VueLazyload from 'vue-lazyload'
Vue.use(VueLazyload, {		  //options配置项
  preLoad: 1.3,
  error: require('@/assets/images/common/404.jpg'),
  loading: require('@/assets/images/common/loading.gif'),
  attempt: 3
})

import plugins from './plugins/index.js' // plugins
Vue.use(plugins)

new Vue({
  router: routers,
  store,
  render: h => h(App)
}).$mount('#app');
