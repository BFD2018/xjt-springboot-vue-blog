import router from './routers.js'
import store from '@/store/store.js'

const whiteList = ['/toLogin'];        // no redirect whitelist

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - ' + "xjt"
  }
  if (store.state.login_user != null && store.state.login_user != {}) {
    // 已登录且要跳转的页面是登录页
    if (to.path === '/login') {
      next({ path: '/toLogin' })
    }else{
      next();
    }
  } else {
    // next(`/toLogin`)
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/toLogin?redirect=${to.fullPath}`)      // 否则全部重定向到登录页
    }
  }
});
