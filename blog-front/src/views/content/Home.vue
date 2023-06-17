<template>
  <div class="home-view">
    <div class="layout-left">
      <!-- 轮播图      -->
      <div class="card card-default card-outline">
        <el-carousel height="360px" direction="vertical">
          <el-carousel-item v-for="(item,index) in carouselList" :key="index">
            <div class="carouselItem" @click="gotoBlogDetail(item)">
              <img v-lazy="item.firstPicture" alt="" width="100%" height="100%">
              <div class="title">{{item.title}}</div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 博客列表     -->
      <div class="card card-default card-outline">
        <div class="card-header main-title">
          <div class="my-left">博客</div>
          <div class="my-right">共 {{ totalBlogs }} 篇</div>
        </div>

        <div class="card-body main-content">
          <div v-for="(item,index) in blogsList" :key="index" class="blog-item">
            <BlogComp :blogInfo="item"></BlogComp>
          </div>
        </div>
        <!--加载更多 -->
        <div class="loadmoreBlog" style="width: 120px;margin: 20px auto;">
          <el-button ref="loadmoreBtn" plain type="success" size="medium" round
                     :disabled="currentPage >= totalPages"
                     @click="loadmore">加载更多</el-button>
        </div>
      </div>
    </div>

    <div class="layout-right" v-if="$store.state.login_user">
      <!--      历史上的今天-->
      <div class="card" style="height: 1000px;overflow-y: scroll;">
        <div class="card-header">
          <div class="card-title">历史上的今天</div>
          <div class="card-tools">
            <button type="button" class="btn btn-tool" data-card-widget="collapse">
              <i class="fas fa-minus"></i>
            </button>
          </div>
        </div>
        <div class="card-body">
          <el-timeline :reverse="true">
            <el-timeline-item
                v-for="(activity, index) in todayHistoryEventList"
                :key="index"
                :timestamp="activity.date">
              {{ activity.title }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>

      <div class="card card-outline card-info">
        <div class="card-header">
          <div class="card-title">用户信息</div>
        </div>
        <div class="card-body">
          <el-image
              class="my-border"
              style="width: 200px;height: 200px;"
              :src="$store.state.login_user.avatar || '' "
              fit="cover"></el-image>
          <p>{{ $store.state.login_user.username }}</p>
          <p>{{ $store.state.login_user.description }}</p>
        </div>
      </div>

      <div class="card card-outline card-info">
        <div class="card-header">
          <div class="card-title">
            分类标签
          </div>
        </div>

        <div class="card-body" style="margin-top: 120px;">
          <tag-cloud/>
        </div>
      </div>


      <div class="card card-outline card-success">
        <div class="card-header">
          <div class="card-title">
            热门文章
          </div>
          <div class="card-tools">
            TOP5
          </div>
        </div>
        <div class="card-body">
          <div class="my-marginTop10" v-for="(item,index) in hotBlogs" :key="index">
            <el-tag effect="plain">{{ index + 1 }}</el-tag>
            <span class="my-marginLeft5"><i class="el-icon-view"></i> {{ item.views }}</span>
            <br/>
            <span class="my-mini-text">{{ item.title }}</span>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import BlogComp from "./BlogComp";
import NavBar from "@/components/navbar/NavBar.vue"
import TagCloud from "../../components/tagcloud/TagCloud";

export default {
  name: "Home",
  components: {
    BlogComp,
    NavBar,
    TagCloud,
  },
  data() {
    return {
      carouselList:[],      //轮播列表（按照评论数 浏览量排序 默认4个）
      blogsList: [],		//博客
      typesList: [],		//分类
      hotBlogs: [],		//热门博客
      todayHistoryEventList: [],

      //总博客数
      totalBlogs: 0,
      //总页数
      totalPages: 0,
      //当前页
      currentPage: 1,
      //每页显示条目
      pageSize:6,
    }
  },
  methods: {
    initCarouselListData(){
      this.$getRequest("/blog/front/carousel").then(res => {
        //console.log(res);
        this.carouselList = res.data.obj
      })
    },
    initGetToadyHistroy() {
      this.$getRequest("/blog/front/juhe/todayevent").then(res => {
        //console.log(res);
        this.todayHistoryEventList = res.data.obj.result
      })
    },
    initBlogs() {
      let baseUrl = `/blog/getByPage?current=${this.currentPage}&size=${this.pageSize}`;
      this.$getRequest(baseUrl).then(res => {
        //console.log(res);
        if (res.data.status === 200) {
          this.blogsList.push(...res.data.obj.records);
          this.totalPages = res.data.obj.pages;
          this.totalBlogs = Number(res.data.obj.total);
        } else {
          this.$message.error(res.data.msg);
        }
      })
    },
    initType() {
      this.$getRequest("/type/all").then(res => {
        //console.log(res);
        if (res.data.status === 200) {
          let arr = res.data.obj;
          for (let i = 0; i < arr.length; i++) {
            let item = arr[i]
            item.url = "/blog/type/?id="+ item.id + "&name=" + item.name;
            //console.log(item);
            this.typesList.push(item);
          }
        }
      })
    },
    /*根据blog.views排序*/
    initHotBlog() {
      if (this.blogsList != null) {
        this.hotBlogs.splice(0);
        this.hotBlogs.push(...this.blogsList);
        this.hotBlogs.sort(function (a, b) {
          return b.views - a.views;
        });
        this.hotBlogs.splice(5);
      }
    },
    blogTypeMore() {

    },
    gotoBlogDetail(clickBlogInfo){
      this.$cache.session.setJSON("currentBlogDetail",clickBlogInfo)
      this.$router.push(`/blog/detail/${clickBlogInfo.id}`)
    },

    loadmore(){
      this.currentPage++;
      // if(Number(this.currentPage) > Number(this.totalPages)){
      //   this.$message.warning("已经到底啦 我也是有底线的~~~")
      // }
      this.initBlogs();
    }
    /*分页*/
    // handleSizeChange(pageSize) {
    //   //console.log(`每页 ${pageSize} 条`);
    //   this.pageSize = pageSize;
    //   this.initBlogs()
    // },
    // handleCurrentChange(current) {
    //   //console.log(`当前页: ${current}`);
    //   this.currentPage = current;
    //   this.initBlogs()
    // },
  },
  created() {
    this.initCarouselListData();
    this.initBlogs();
    //this.initType();

    this.initGetToadyHistroy();

    //console.log(this.$store.state.login_user);
  }
}
</script>

<style lang="less" scoped>
.home-view {
  display: flex;

  .layout-left {
    flex: 1;
    box-shadow: 0 0 3px #dddddd;
    .carouselItem{
      position: relative;
      .title{
        font-size: 22px;
        font-weight: lighter;
        position: absolute;
        left: 50px;
        top: 20px;
        color: rgba(255,255,255,0.8);
      }
    }
    .main-title {
      margin: 10px 20px 0 20px;
      height: 50px;
      border-bottom: 2px solid darkcyan;
      font-size: 26px;
      color: darkcyan;
      font-weight: 500;
    }

    .main-content {
      .blog-item {
        padding: 20px;
        border-bottom: 1px solid #ddd;
      }
    }
  }

  .layout-right {
    width: 420px;
    margin-left: 20px;

    .tag-group {
      .el-tag {
        margin-left: 10px;
        margin-top: 10px;
      }
    }
  }
}
</style>
