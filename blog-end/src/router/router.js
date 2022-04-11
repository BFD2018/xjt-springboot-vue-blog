import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Login from "@/views/Login.vue"
import Register from "@/views/Register.vue"
import Home from "@/views/Home.vue"

//首页
import DashBoard from "@/views/index/DashBoard.vue"

//博客
import WriteBlog from "@/views/blogs/WriteBlog.vue"
import EditBlog from "@/views/blogs/EditBlog.vue"
import AllBlog from "@/views/blogs/AllBlog.vue"
import CategoryBlog from "@/views/blogs/CategoryBlog.vue"
import TagBlog from "@/views/blogs/TagBlog.vue"
import CommentBlog from "@/views/blogs/CommentBlog.vue"
import LinksBlog from "@/views/blogs/LinksBlog.vue"

//用户
import UserList from "@/views/users/UserList.vue"
import UserCollection from "@/views/users/UserCollection.vue"
import AddUser from "@/views/users/AddUser.vue"
import EditUser from "@/views/users/EditUser.vue"

//测试
import TestAplayer from "@/views/test/TestAplayer.vue"
import TestVueEmojiPicker from "@/views/test/TestVueEmojiPicker.vue"
import TestKkfileview from "@/views/test/TestKkfileview.vue"

const routes = [
  {
    path: '/',
    name: 'Login',
    component:Login,
    hidden:true
  },
  {
    path: '/login',
    name: 'Login',
    component:Login,
    hidden:true
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
    hidden:true
  },
  {
    path: '/admin/dashboard',
    name: 'register',
    component: Home,
    hidden:true,
    children:[
      {path:"/",name:"写博客",component:DashBoard},
    ]
  },

  {
    path: '/admin/home',
    name: '博客管理',
    component: Home,
    myIcon:"el-icon-s-management",
    children: [
      {path:"/admin/blog/write",name:"写博客",component:WriteBlog,myIcon:"el-icon-edit"},
      { path: '/admin/editblog/:id', name: '编辑博客',  component: EditBlog, props: true, hidden: true },
      { path: '/admin/allblog', name: '文章管理',  component: AllBlog ,myIcon:"el-icon-notebook-1" },
      { path: '/admin/categoryblog', name: '分类专栏',  component: CategoryBlog, myIcon:"el-icon-document" },
      { path: '/admin/tagblog', name: '标签管理',  component: TagBlog,myIcon:"el-icon-collection-tag"  },
      { path: '/admin/commentblog', name: '评论管理',  component: CommentBlog,myIcon:"el-icon-tickets"  },
      { path: '/admin/linksblog', name: '友链管理',  component: LinksBlog ,myIcon:"el-icon-paperclip" },
    ]
  },

  {
    path: '/admin/user',
    name: '用户管理',
    component: Home,
    myIcon:"el-icon-user-solid",
    children: [
      {path:"/admin/user/list",name:"用户列表",component:UserList,myIcon:"el-icon-user"},
      {path:"/admin/user/collection/:uid",name:"用户收藏",component:UserCollection,hidden: true},
      {path:"/admin/user/add",name:"新增用户",component:AddUser,myIcon:"el-icon-plus"},
      {path:"/admin/user/update",name:"编辑用户",component:EditUser,hidden: true},
    ]
  },

  {
    path: '/test',
    name: '测试模块',
    component: Home,
    myIcon:"el-icon-setting",
    children: [
      {path:"/test/vue-aplayer",name:"aplayer播放器",component:TestAplayer,myIcon:"el-icon-s-data"},
      {path:"/test/v-emoji-picker",name:"emoji测试",component:TestVueEmojiPicker,myIcon:"el-icon-s-data"},
      {path:"/test/kkfileview",name:"Kkfileview测试",component:TestKkfileview,myIcon:"el-icon-s-data"},
    ]
  },
];

const router = new VueRouter({
  routes,
  mode:"history",
});

export default router
