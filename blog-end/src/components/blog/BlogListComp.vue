<template>
	<div class="blog-list-comp-view">
		<el-table
				v-loading="loading"
				:data="tableData"
				style="width: 100%">
			<el-table-column
					label="文章列表"
					width="1000">
				<template slot-scope="scope">
					<el-card  class="box-card">
						<el-row type="flex" :gutter="10">
							<el-col :span="6">
								<el-image
										style="width: 100%;height: 80px;"
										:src="scope.row.firstPicture"
										fit="cover"></el-image>
							</el-col>
							<el-col :span="18">
								<div style="font-size: 18px;">
									<!-- 根据shareStatement属性判断文章是否为草稿 -->
									<el-button size="mini" v-if="!scope.row.shareStatement" type="warning" icon="el-icon-edit" circle></el-button>
									{{scope.row.title}}
								</div>
								<div style="margin-top: 10px;">
									<el-tag size="mini" style="margin-right: 10px;"  v-if="scope.row.isDelete" type="danger">已删除</el-tag>
									<el-tag size="mini" style="margin-right: 10px;"  v-if="!scope.row.shareStatement" type="warning">草稿</el-tag>
									<el-tag style="margin-right: 10px;" v-if="scope.row.shareStatement" size="mini">{{scope.row.flag}}</el-tag>
									<el-tag style="margin-right: 20px;" v-if="scope.row.shareStatement" size="mini" type="info">{{scope.row.published == '0' ? '私密' : '公开'}}</el-tag>
									<i style="margin-right: 20px;" class="el-icon-view"> {{scope.row.views}} </i>
									<i style="margin-right: 20px;" class="el-icon-chat-square"> {{scope.row.commentCount}} </i>
									<i style="margin-right: 20px;" class="el-icon-date"> {{scope.row.createTime}}</i>

									<router-link :to=" '/admin/editblog/' + scope.row.id ">
										<el-button v-if="!scope.row.isDelete" style="float: right; margin-right: 10px;" type="primary" size="mini">编辑</el-button>
									</router-link>
									<el-button v-if="!scope.row.isDelete" style="float: right;" type="danger" size="mini" @click="deleteBlog(scope.row.id)">删除</el-button>
									<el-button style="float: right;" type="warning" size="mini" @click="previewContent(scope.row.id)">预览内容</el-button>
									<el-button v-if="scope.row.isDelete" style="float: right; margin-right: 10px;" @click="recoveryBlog(scope.row.id)" type="primary" size="mini">还原</el-button>
								</div>
							</el-col>
						</el-row>
					</el-card>
				</template>
			</el-table-column>
		</el-table>

		<!-- 分页 -->
		<div style="margin-top: 20px;">
			<el-pagination
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
</template>

<script>
	export default {
		name: "BlogListComp",
		components:{

		},
		props:{
			tabindex:{
				type:String,
				default:"0",
			}
		},
		data(){
			return{
				//分页
				currentPage:1,
				pageSizes:[3,6,9,12],
				pageSize:6,
				totalBlogs:0,		//总数
				//表格数据
				tableData:[],		//查到的博客列表
				loading:false, 		//table加载样式是否显示
				t2index:"0",		//保留父组件传过来的tab值
			}
		},
		mounted() {
			this.initIndex();
			this.initBlogs();
		},
		methods:{
			initIndex(){
				this.t2index = this.tabindex;
			},
			//初始化【全部】博客的数据
			initBlogs(){
				this.loading = true;
				//通用路由
				let baseUrl = `/blog/getByPage?current=${this.currentPage}&size=${this.pageSize}`;
				if(this.t2index === "0"){		//全部
					baseUrl += "&is_delete=0 "
				}
				else if(this.t2index === "1"){	//原创
					baseUrl += "&flag=原创&share_statement=1&is_delete=0 "
				}
				else if(this.t2index === "2"){	//转载
					baseUrl += "&flag=转载&share_statement=1&is_delete=0 "
				}
				else if(this.t2index === "3"){	//草稿
					baseUrl += "&share_statement=0&is_delete=0 "
				}
				else if(this.t2index === "4"){	//公开
					baseUrl += "&published=1&share_statement=1&is_delete=0 "
				}
				else if(this.t2index === "5"){	//私密
					baseUrl += "&published=0&share_statement=1&is_delete=0 "
				}
				else if(this.t2index === "6"){	//回收站
					baseUrl += "&is_delete=1 "
				}
				//网络请求
				this.$getRequest(baseUrl).then(res =>{
					//先清空tableData
					this.tableData.splice(0);
					console.log(res);
					if(res.status === 200){
						this.totalBlogs = res.data.obj.total;
						this.tableData.push(...res.data.obj.records);
						this.loading = false;
					}
				})
			},
			handleSizeChange(){
				console.log(`每页 ${val} 条`);
				this.pagesize = val
				this.initBlogs()
			},
			handleCurrentChange(val){
				console.log(`当前页: ${val}`);
				this.currentPage = val
				this.initBlogs()
			},
			//删除博客(逻辑删除)
			deleteBlog(bid){},
			//彻底删除博客

			//还原博客
			recoveryBlog(bid){},
		}
	}
</script>

<style scoped>
	.box-card{
		width: 100%;
	}
</style>