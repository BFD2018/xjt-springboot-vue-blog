import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

const Login = () => import('@/views/Login.vue')
const Register = () => import('@/views/Register.vue')

const Index = () => import('@/views/Index.vue')
const Home = () => import('@/views/content/Home.vue')
const TypeBlog = () => import('@/views/content/TypeBlog.vue')
const TagBlog = () => import('@/views/content/TagBlog.vue')
const Messages = () => import('@/views/content/Messages.vue')
//友链
const Links = () => import('@/views/content/Links.vue')
//关于我
const About = () => import('@/views/content/About.vue')
//博客详细内容
const BlogDetail = () => import('@/views/content/BlogDetail.vue')

//私人网盘
const NetDisk = () => import('@/views/content/Netdisk/NetDisk.vue')


const routes = [
  {
    path: '/',
    name: 'home',
    redirect:"/blog/home"
  },
  {
    path: '/toLogin',
    name: 'toLogin',
    component: Login
  },
  {
    path: '/toRegister',
    name: 'register',
    component: Register
  },
  {
    path: '/blog',
    name: '首页',
    component: Index,
    children: [
      {path: "/blog/home", name: "首页", component: Home},
      {path: "/blog/type", name: "分类", component: TypeBlog},
      {path: "/blog/tag", name: "标签", component: TagBlog},
      {path: "/blog/messages", name: "留言板", component: Messages},
      {path: "/blog/netdisk", name: "个人网盘", component: NetDisk,},
      {path: "/blog/links", name: "友链", component: Links},
      {path: "/blog/about", name: "关于我", component: About},
      {path: "/blog/detail/:blog_id", name: "博客详情页", component: BlogDetail},
    ]
  },

]

const routers = new VueRouter({
  routes,
  mode: "history"
})

export default routers
