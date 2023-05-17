<template>
  <div class="blog-list-view">
    <div class="layout-left">
      <div class="main-title">
        <div class="my-left">博客</div>
        <div class="my-right">共 {{totalBlogs}} 篇</div>
      </div>

      <div class="main-content">
        <div v-for="(item,index) in blogsList" :key="index" class="blog-item">
          <BlogComp :blogInfo="item"></BlogComp>
        </div>
      </div>

      <!--分页-->
      <div style="margin-top: 20px;" class="my-border-padding">
        <el-pagination
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalBlogs">
        </el-pagination>
      </div>
    </div>

    <div class="layout-right" v-if="$store.state.login_user">
      <div class="card">
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

      <el-card class="box-card">
        <div class="clearfix my-title" slot="header">用户信息</div>
        <div class="my-middle">
          <el-image
            class="my-border"
            style="width: 200px;height: 200px;"
            :src="$store.state.login_user.avatar || '' "
            fit="cover"></el-image>
          <p>{{$store.state.login_user.username}}</p>
          <p>{{$store.state.login_user.description}}</p>
        </div>
      </el-card>

      <el-card class="box-card my-marginTop10">
        <div slot="header" class="clearfix">
          <nav-bar>
            <div slot = "left-slot">分类专栏</div>
            <div slot = "right-slot">
              <i class="el-icon-more" @click="blogTypeMore"></i>
            </div>
          </nav-bar>
        </div>
        <div class="tag-group">
          <el-tag v-for="(item,index) in typesList" :key="index" effect="plain">
            {{ item.name }}
          </el-tag>
        </div>
      </el-card>


      <el-card class="box-card my-marginTop10">
        <div slot="header" class="clearfix">
          <nav-bar>
            <div slot = "left-slot">热门文章</div>
            <div slot = "right-slot">
              <el-button type="text">TOP5</el-button>
            </div>
          </nav-bar>
        </div>
        <div v-for="(item,index) in hotBlogs" :key="index">
          <div class="my-marginTop10">
            <el-tag effect="plain">{{ index + 1 }}</el-tag>
            <span class="my-marginLeft5"><i class="el-icon-view"></i> {{item.views}}</span>
            <br/>
            <span class="my-mini-text">{{item.title}}</span>
          </div>
        </div>
      </el-card>
    </div>

  </div>
</template>

<script>
  import BlogComp from "./BlogComp";
  import NavBar from "@/components/navbar/NavBar.vue"

  export default {
    name: "BlogList",
    components: {
      BlogComp,
      NavBar,
    },
    data() {
      return {
        blogsList: [],		//博客
        typesList: [],		//分类
        hotBlogs: [],		//热门博客
        todayHistoryEventList:[],

        //分页
        totalBlogs: 0,
        currentPage: 1,
        pageSizes: [3, 6, 9, 12],
        pageSize: 6,
      }
    },
    methods: {
      initGetToadyHistroy(){
        this.$getRequest("/blog/front/juhe/todayevent").then(res => {
          console.log(res);
          this.todayHistoryEventList = res.data.obj.result
        })
      },
      initBlogs() {
        let baseUrl = `/blog/getByPageHelper?current=${this.currentPage}&size=${this.pageSize}`;

        this.$getRequest(baseUrl).then(res => {
          console.log(res);
          if (res.data.status === 200) {
            this.blogsList.splice(0);
            this.blogsList.push(...res.data.obj.list);

            this.totalBlogs = Number(res.data.obj.total);

            this.initHotBlog();
          } else {
            this.$message.error(res.data.msg);
          }
        })
      },
      initType() {
        this.$getRequest("/type/all").then(res => {
          console.log(res);
          if (res.data.status === 200) {
            this.typesList.push(...res.data.obj);
            //只展示前五个
            this.typesList.splice(5);
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

      /*分页*/
      handleSizeChange(pageSize) {
        //console.log(`每页 ${pageSize} 条`);
        this.pageSize = pageSize;
        this.initBlogs()
      },
      handleCurrentChange(current) {
        //console.log(`当前页: ${current}`);
        this.currentPage = current;
        this.initBlogs()
      },
    },
    created() {
      this.initBlogs();
      this.initType();

      this.initGetToadyHistroy();

      console.log(this.$store.state.login_user);
    }
  }
</script>

<style lang="less" scoped>
  .blog-list-view {
    display: flex;

    .layout-left {
      flex: 1;
      box-shadow: 0 0 3px #dddddd;

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
