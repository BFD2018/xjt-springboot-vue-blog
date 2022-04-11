<template>
	<div class="all-blog-view">
		<!--搜索-->
		<el-row style="margin-top: 10px;margin-bottom: 10px;">
			<el-col :span="12">
				<el-input placeholder="按照博客标题搜索" v-model="inputSearchKeyWord">
					<el-button slot="append" type="success" @click="searchByTitle" icon="el-icon-search"></el-button>
				</el-input>
			</el-col>
		</el-row>

		<el-menu :default-active="activeTab" class="el-menu-demo" mode="horizontal" @select="handleNavTagSelect">
			<el-menu-item index="0">全部</el-menu-item>
			<el-menu-item index="1">原创</el-menu-item>
			<el-menu-item index="2">转载</el-menu-item>
			<el-menu-item index="3">草稿</el-menu-item>
			<el-menu-item index="4">公开</el-menu-item>
			<el-menu-item index="5">私密</el-menu-item>
			<el-menu-item index="6">回收站</el-menu-item>
		</el-menu>

		<el-table
				v-loading="loading"
				:data="tableData"
				style="width: 100%">
			<el-table-column
					label="文章列表"
					width="1000">
				<template slot-scope="scope">
					<el-card class="box-card">
						<el-row type="flex" :gutter="10">
							<el-col :span="6">
								<el-image
										style="width: 100%;height: 80px;border: 1px solid #eee;"
										:src="scope.row.firstPicture"
										fit="cover"></el-image>
							</el-col>

							<el-col :span="18">
								<el-row style="font-size: 18px;">
									<!-- 根据shareStatement属性判断文章是否为草稿 -->
									<el-button size="mini" v-if="!scope.row.shareStatement" type="warning" icon="el-icon-edit"
														 circle></el-button>
									{{scope.row.title}}
								</el-row>

								<div style="margin-top: 15px;" class="my-flex">
									<div style="margin-right: 10px;" v-if="scope.row.isDelete">
										<el-tag size="mini"   type="danger">已删除</el-tag>
									</div>
									<div style="margin-right: 10px;" v-if="!scope.row.shareStatement">
										<el-tag size="mini"   type="warning">草稿
										</el-tag>
									</div>
									<div style="margin-right: 10px;"  v-if="scope.row.shareStatement" >
										<el-tag size="mini">{{scope.row.flag}}
										</el-tag>
									</div>
									<div style="margin-right: 20px;"  v-if="scope.row.shareStatement" >
										<el-tag size="mini" type="info">
											{{scope.row.published == '0' ? '私密' : '公开'}}
										</el-tag>
									</div>
									<div style="margin-right: 20px;"><i class="el-icon-view"> {{scope.row.views}} </i>
									</div>
									<div style="margin-right: 20px;"><i class="el-icon-chat-square">
										{{scope.row.commentCount}} </i>
									</div>
									<div class="my-flex-span1" style="margin-right: 20px;" ><i class="el-icon-date"> {{scope.row.createTime | formatTime}}</i>
									</div>

									<div style="margin-right: 20px;">
                                        <el-button v-if="!scope.row.isDelete" type="primary" size="mini"
                                                   @click="$router.push('/admin/editblog/'+scope.row.id)">编辑</el-button>
									</div>
									<div style="margin-right: 20px;" v-if="!scope.row.isDelete">
										<el-popconfirm
												@confirm="handleDelete(scope.$index,scope.row.id)"
												confirm-button-text='好的'
												cancel-button-text='不用了'
												icon="el-icon-info"
												icon-color="red"
												title="确定删除博客吗？">
											<el-button  slot="reference" type="danger" size="mini">删除</el-button>
										</el-popconfirm>
									</div>
									<div style="margin-right: 20px;">
										<el-button type="warning" size="mini" @click="handlePreviewContent(scope.$index,scope.row)">预览</el-button>
									</div>
									<div style="margin-right: 20px;" v-if="scope.row.isDelete">
										<el-button  @click="recoveryBlog(scope.row.id)" type="primary" size="mini">还原</el-button>
									</div>
								</div>
							</el-col>
						</el-row>
					</el-card>
				</template>
			</el-table-column>
		</el-table>

		<!--dialog预览博客内容-->
		<el-dialog title="预览博客内容" :visible.sync="previewBlogDialogVisible">
			<mavon-editor
					:value="currentBlog.content"
					defaultOpen="preview"
					:boxShadow="false"
					style="z-index:1;height:70vh"
					:editable="false"
					:subfield="false"
					:toolbarsFlag="false">
			</mavon-editor>
		</el-dialog>


		<!-- 分页 -->
		<div style="margin-top: 20px;" class="my-border-padding">
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
		name: "write-blog",
		data() {
			return {
				inputSearchKeyWord: "",
				activeName: "first",   //当前选项卡的label
				tabindex: "0",   //选项卡index
				hidden_search_comp: false,		//false隐藏搜索子组件,true显示

				//分页
				currentPage: 1,
				pageSizes: [3, 6, 9, 12],
				pageSize: 6,
				totalBlogs: 0,		//总数
				tableData: [],		//查到的博客列表
				loading: false, 		//table加载样式是否显示

				activeTab: "0",		//当前选择tab

				currentBlog: {},		//当前选中的博客
				previewBlogDialogVisible: false,
			}
		},
		created() {
			this.initBlogs();
		},
		methods: {
			//初始化【全部】博客的数据
			initBlogs() {
				this.loading = true;
				//通用路由
				let baseUrl = `/blog/getByPage?current=${this.currentPage}&size=${this.pageSize}`;
				if (this.activeTab === "0") {		//全部
					baseUrl += "&is_delete=0 "
				} else if (this.activeTab === "1") {	//原创
					baseUrl += "&flag=原创&share_statement=1&is_delete=0 "
				} else if (this.activeTab === "2") {	//转载
					baseUrl += "&flag=转载&share_statement=1&is_delete=0 "
				} else if (this.activeTab === "3") {	//草稿
					baseUrl += "&share_statement=0&is_delete=0 "
				} else if (this.activeTab === "4") {	//公开
					baseUrl += "&published=1&share_statement=1&is_delete=0 "
				} else if (this.activeTab === "5") {	//私密
					baseUrl += "&published=0&share_statement=1&is_delete=0 "
				} else if (this.activeTab === "6") {	//回收站
					baseUrl += "&is_delete=1 "
				}
				//网络请求
				this.$getRequest(baseUrl).then(res => {
					console.log(res);
					if (res.data.status === 200) {
            this.tableData.splice(0);
						this.totalBlogs = Number(res.data.obj.total);
						this.tableData.push(...res.data.obj.records);
						this.loading = false;
					}else{
            this.loading = false;
					  this.$message.warning(res.data.msg)
          }
				})
			},
			//根据标题栏搜索
			searchByTitle() {
				if (this.inputSearchKeyWord === "") {
					this.$message.error("请输入搜索关键字")
					this.hidden_search_comp = false;
				} else {
					this.hidden_search_comp = true;
				}
				//根据博客标题关键字搜索
				this.$request("/blog/findByLikeTitle?title=" + this.inputSearchKeyWord).then(res => {
					console.log(res);
					if(res.data.status === 200){
					  this.tableData.splice(0);
            this.tableData.push(...res.data.obj);
					  this.totalBlogs = res.data.obj.length;
          }
				})
			},

			//表格操作-删除
			handleDelete(index,bid) {
				console.log(index,bid);
				//网络请求
				this.$deleteRequest("/blog/delete/?bid=" + bid).then(res => {
					console.log(res);
					this.$message.success("删除成功!");

					this.tableData.splice(index,1);
				})
			},

			//预览博客内容
			handlePreviewContent(index,row) {
				// console.log(index);
				this.currentBlog = row;
				this.previewBlogDialogVisible = true;
			},

			//nav改变
			handleNavTagSelect(key, keypath) {
				this.activeTab = key;
				this.initBlogs();
			},

			/*分页事件*/
			handleSizeChange(val) {
				console.log(`每页 ${val} 条`);
				this.pagesize = val
				this.initBlogs()
			},
			handleCurrentChange(val) {
				console.log(`当前页: ${val}`);
				this.currentPage = val
				this.initBlogs()
			},
		}
	}
</script>

<style>

</style>
