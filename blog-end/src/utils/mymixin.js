export default {
  install(Vue) {
    Vue.mixin({
      data() {
        return {
          that: this,
        }
      },
      created() {

      },
      methods: {},
      filters: {
        filterImgUrl(val, that) {
          let imgurl = val + "";
          if (imgurl.startsWith("http") || imgurl.startsWith("https")) {
            return val;
          } else {
            return that.$store.state.HOST + val;
          }
        },
        formatTime(val) {
          return val.year + "-" + val.monthValue + "-" + val.dayOfMonth
        },
        formatTimestamp(val,that){
          return that.$moment(val).format("YYYY-MM-DD")
        }
      }
    })
  }
}
