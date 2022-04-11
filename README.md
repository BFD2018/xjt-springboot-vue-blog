# xiong-blog博客

xiong-blog是基于springboot+vue的前后端分离的博客系统，包括如下几个部分服务端（提供API服务）xiong-blog-server，后台管理界面xiong-blog-backend，前台界面xiong-blog-front。

## 后台xiong-blog-backend

https://github.com/BFD2018/xiong-blog-backend

**编译运行**

Project setup

```
npm install
```

Compiles and hot-reloads for development

```
npm run serve
```

Compiles and minifies for production

```
npm run build
```

### 功能&技术栈

- [x] 博客分类展示
- [x] 博客二级评论功能
- [x] 留言功能
- [x] 在线预览
    - [x] kkfileView实现文件预览
    - [x] 博客文章（dialog弹窗---mavon-editor展示博客内容）
- [x] Vue2.6 
- [x] element-ui
- [x] mavon-editor实现markdown格式文档
- [x] 

### 效果

#### 登录页

![1643890809504](typora-assets/1643890809504.png)

#### dashboard页

![1643891174017](typora-assets/1643891174017.png)

![1643891190187](typora-assets/1643891190187.png)

#### 写博客

![1643891210428](typora-assets/1643891210428.png)

#### 文章管理

![1643892060971](typora-assets/1643892060971.png)

#### 分类专栏

![1643892078598](typora-assets/1643892078598.png)

#### 标签管理

![1643892090054](typora-assets/1643892090054.png)

#### 评论管理

![1643892108452](typora-assets/1643892108452.png)

#### 友链管理

![1643892124128](typora-assets/1643892124128.png)

点击友链地址预览

![1643892146265](typora-assets/1643892146265.png)

#### 用户列表

![1643892167105](typora-assets/1643892167105.png)

#### 新增用户

![1643892595482](typora-assets/1643892595482.png)

#### 测试模块

vue-aplayer

![1643892612244](typora-assets/1643892612244.png)

v-emoji-picker

![1643892625903](typora-assets/1643892625903.png)

kkfileView文件预览

![1643892874548](typora-assets/1643892874548.png)

![1643892888419](typora-assets/1643892888419.png)

## 前台xiong-blog-front

https://github.com/BFD2018/-xiong-blog-front

### 技术栈

```js
"dependencies": {
    "axios": "^0.21.1",
    "core-js": "^3.6.5",
    "element-ui": "^2.15.1",
    "highlight.js": "^11.3.1",
    "hover.css": "^2.3.2",
    "js-base64": "^3.7.2",
    "less-loader": "^6.0.0",
    "mavon-editor": "^2.9.1",
    "moment": "^2.29.1",
    "qs": "^6.10.1",
    "v-emoji-picker": "^2.3.3",
    "vue": "^2.6.11",
    "vue-aplayer": "^1.6.1",
    "vue-echarts": "^6.0.0",
    "vue-particles": "^1.0.9",
    "vue-router": "^3.2.0",
    "vuex": "^3.4.0",
    "wangeditor": "^4.7.10"
  },
  "devDependencies": {
    "@vue/cli-plugin-babel": "~4.5.0",
    "@vue/cli-plugin-router": "~4.5.0",
    "@vue/cli-plugin-vuex": "~4.5.0",
    "@vue/cli-service": "~4.5.0",
    "@vue/composition-api": "^1.4.0",
    "less": "^4.1.1",
    "vue-template-compiler": "^2.6.11"
  }
```

### 效果

#### 登录页

![1643893040820](typora-assets/1643893040820.png)

#### 博客列表页

![1643893142868](typora-assets/1643893142868.png)

#### 分类专栏

![1643893231519](typora-assets/1643893231519.png)

#### 文章标签

![1643893245807](typora-assets/1643893245807.png)

#### 留言板

![1643893301016](typora-assets/1643893301016.png)

#### 友链

![1643893326285](typora-assets/1643893326285.png)

#### 关于我

![1643893345633](typora-assets/1643893345633.png)

#### 个人网盘

![1643893369527](typora-assets/1643893369527.png)

## 服务端xiong-blog-server

https://github.com/BFD2018/springboot-blog

技术栈：

- [x] springboot
- [x] mybatis-plus
- [x] pageHelper分页
- [x] shiro权限控制
- [x] fastjson
- [x] easy-captcha
- [x] hutool
- [x] jieba分词
- [x] kumo词云
- [x] springboot+redis缓存
- [x] 图片上传

### 项目结构

![1643893579677](typora-assets/1643893579677.png)

### 分页

返回的`IPage<>`对象包括  records、total、size、current、pages 

![image-20211021201109218](typora-assets/image-20211021201109218.png)

