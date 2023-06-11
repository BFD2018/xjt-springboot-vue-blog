import cache from './cache'

export default {
  install(Vue) {
    // 缓存对象
    Vue.prototype.$cache = cache
  }
}
