module.exports = {
  devServer: {
    host: 'localhost',
    open: true,
    port: 8002,
    proxy: {
      '^/api': {
        target: 'http://localhost:8000/',
        ws: false,
        changOrigin: true,      //允许跨域
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
