<template>
	<div class="type-blog-view">
    <div class="typeTabs">
      <el-tabs v-model="currentTypeId" @tab-click="handleClick" tab-position="left" stretch>
        <el-tab-pane v-for="(item,index) in typesList" :key="item.id" :label="item.name" :name="item.id ">
          <div class="blog-content">
            <div class="main-content">
              <div v-for="(blogInfo,index) in blogsList" :key="index" class="blog-item">
                <div class="card card-outline card-gray my-marginTop20">
                  <div class="card-body">
                    <el-row type="flex" :gutter="30">
                      <el-col :span="8" style="width: 320px;height: 180px;border:1px solid #ddd;overflow: hidden;border-radius: 8px;padding: 10px;">
                        <img v-lazy="blogInfo.firstPicture"  style="width: 100%;height: 100%;" alt="">
                      </el-col>

                      <el-col :span="15">
                        <el-row>
                          <!-- 根据shareStatement属性判断文章是否为草稿 -->
                          <el-button size="mini" v-if="!blogInfo.shareStatement"
                                     type="warning" icon="el-icon-edit" circle></el-button>
                          <span @click="gotoBlogDetail(blogInfo.id)"
                                class="my-medium-title">{{blogInfo.title}}</span>
                        </el-row>

                        <div style="margin-top: 15px;" class="my-flex">
                          <div style="margin-right: 10px;" v-if="blogInfo.isDelete">
                            <el-tag size="mini"   type="danger">已删除</el-tag>
                          </div>
                          <div style="margin-right: 10px;" v-if="!blogInfo.shareStatement">
                            <el-tag size="mini"   type="warning">草稿
                            </el-tag>
                          </div>
                          <div style="margin-right: 10px;"  v-if="blogInfo.shareStatement" >
                            <el-tag size="mini">{{blogInfo.flag}}
                            </el-tag>
                          </div>
                          <div style="margin-right: 20px;"  v-if="blogInfo.shareStatement" >
                            <el-tag size="mini" type="info">
                              {{blogInfo.published == 'false' ? '私密' : '公开'}}
                            </el-tag>
                          </div>
                          <div style="margin-right: 20px;"><i class="el-icon-view"> {{blogInfo.views}} </i>
                          </div>
                          <div style="margin-right: 20px;"><i class="el-icon-chat-square">
                            {{blogInfo.commentCount}} </i>
                          </div>

                          <div class="my-flex-span1" style="margin-right: 20px;" ><i class="el-icon-date"> {{blogInfo.create_time | filterFormatDate(that)}}</i></div>
                        </div>

                        <el-row type="flex" class="my-marginTop20">
                          <el-col style="align-items: flex-start;font-size: 12px;color: rgba(0,0,0,0.8);" class="my-more3LineEscapeText">{{blogInfo.description}}</el-col>
                        </el-row>
                      </el-col>
                    </el-row>
                  </div>
                </div>
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
                  :total="total">
              </el-pagination>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>


	</div>
</template>

<script>
	import BlogComp from "./BlogComp";

	export default {
		name: "TypeBlog",
		components:{
			BlogComp,
		},
		data(){
			return{
				typesList:[
          {
            "id":"-1",
            "name":"全部"
          }
        ],
				blogsList:[],
        currentTypeId:"-1",		//当前选中typeId

				//分页
				currentPage: 1,
				pageSize: 6,
				pageSizes: [3, 6, 9, 12],
        total:0,      //总数
			}
		},
		methods:{
			_getAllTypes(){
				//获取所有Type
				this.$getRequest("/type/all").then(res =>{
					//console.log(res);
					if(res.data.status === 200){
						this.typesList.push(...res.data.obj)
					}
				})
			},

			_getBlogsByTypeId(){
				//获取所有type对应的blog
				let params = {
					"current":this.currentPage,
					"size":this.pageSize,
					"typeId":this.currentTypeId,
				}
				this.$getRequest("/blog/getByPage?"+this.$qs.stringify(params)).then(res =>{
					//console.log(res);
					if(res.data.status === 200){
            this.blogsList = [];
						this.blogsList.push(...res.data.obj.records);
						this.total = parseInt(res.data.obj.total);
					}else{
						this.$message.error(res.data.msg);
					}
				})
			},

      handleClick(tab, event) {
        this.currentTypeId = tab.name;
        this.currentPage = 1;
        this.pageSize = 6;
        this._getBlogsByTypeId();
      },
      gotoBlogDetail(blogid){
        this.$cache.session.setJSON("currentBlogDetail",this.blogInfo)
        //this.$cache.session.set("currentBlogDetail",this.blogInfo)
        this.$router.push(`/blog/detail/${blogid}`)
      },

			/*分页*/
			handleSizeChange(pageSize){
				console.log(`每页 ${pageSize} 条`);
				this.pagesize = pageSize
				this._getBlogsByTypeId()
			},
			handleCurrentChange(current){
				console.log(`当前页: ${current}`);
				this.currentPage = current
				this._getBlogsByTypeId()
			},
		},
		created() {
			this._getAllTypes();

			setTimeout(() =>{
				this._getBlogsByTypeId();
			},1000);
		}
	}
</script>

<style scoped>

</style>
