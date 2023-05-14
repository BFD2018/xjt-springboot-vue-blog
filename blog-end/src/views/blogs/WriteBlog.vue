<template>
	<div class="write-blog-view">
		<el-form ref="editForm"
						 status-icon
						 label-width="80px"
						 label-position="left"
						 :model="editForm"
						 :rules="rules1">
			<el-form-item label="标题" prop="title">
				<el-input v-model="editForm.title"></el-input>
			</el-form-item>

			<el-form-item label="描述" prop="description">
				<el-input v-model="editForm.description"></el-input>
			</el-form-item>

			<el-form-item label="首页图片">
				<el-input v-model="editForm.first_picture"></el-input>
			</el-form-item>

			<el-form-item label="文章标签">
				<el-select v-model="editForm.tags" multiple placeholder="请选择Tag标签">
					<el-option
							v-for="(item,index) in tagList"
							:key="index"
							:label="item.name"
							:value="item.id">
					</el-option>
				</el-select>
			</el-form-item>

			<el-form-item label="分类专栏">
				<el-select v-model="editForm.type" placeholder="请选择文章分类Category">
					<el-option
							v-for="(item,index) in typeList"
							:key="index"
							:label="item.name"
							:value="item.id">
					</el-option>
				</el-select>
			</el-form-item>

			<el-form-item label="文章类型">
				<el-radio-group v-model="editForm.flag">
					<el-radio :label="0">转载</el-radio>
					<el-radio :label="1">原创</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item label="发布形式">
				<el-radio-group v-model="editForm.published">
					<el-radio :label="0">私密</el-radio>
					<el-radio :label="1">公开</el-radio>
				</el-radio-group>
			</el-form-item>

			<el-form-item>
				<el-switch
						style="display: block"
						v-model="editForm.share_statement"
						active-color="#13ce66"
						inactive-color="#ff4949"
						active-text="文章发布"
						inactive-text="暂存草稿">
				</el-switch>
			</el-form-item>

			<el-form-item label="内容" prop="content">
				<mavon-editor
						ref="md"
						placeholder="请输入文档内容..."
						:boxShadow="false"
						@imgAdd="handleMavonEditorImgAdd"
						@imgDel="handleMavonEditorImgDel"
						style="z-index:1;border: 1px solid #d9d9d9;height:60vh"
						v-model="editForm.content"/>
			</el-form-item>

			<el-form-item style="margin: auto;">
				<el-button type="info" @click="reset">重置</el-button>
				<el-button type="primary" @click="submitBlogForm">提交</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>

	export default {
		name: "write-blog",
		components: {},
		data() {
			return {
				editForm: {
					title: "",
					description: "",
					first_picture: "",
					content: "",
					type: "",			//类型
					flag: 1,		//原创
					published: 1,			//公开
					share_statement:true,		//文章是否完成
					tags: [],				//选中的tag列表
				},
				rules1: {
					title: [
						{required: true, message: '请输入标题', trigger: 'blur'},
						{min: 3, max: 100, message: '长度在 3 到 100 个字符', trigger: 'blur'}
					],
					description: [
						{required: true, message: '请输入摘要', trigger: 'blur'}
					],
					content: [
						{required: true, message: '请输入文章内容', trigger: 'blur'}
					],
				},
				tagList:[],		//文章标签列表
				typeList:[],		//文章类型列表
			}
		},
		methods: {
			//初始化
			init(){
			  /*初始化mavon-editor*/
        //this.$refs.md.value = "";

				/*获取所有tags*/
				this.$getRequest("/tag/all").then(res =>{
					console.log(res);
					if(res.data.status === 200){
						//每次请求时清空tableData数据
						this.tagList.splice(0);
						this.tagList.push(...res.data.obj)
					}
				})

				/*获取所有type*/
				this.$getRequest("/type/all").then(res =>{
					console.log(res);
					if(res.data.status === 200){
						//每次请求时清空tableData数据
						this.typeList.splice(0);
						this.typeList.push(...res.data.obj)
					}
				})
			},
			//提交博客
			submitBlogForm() {
				console.log(this.editForm);
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$postRequest("/blog/save",this.editForm).then(res =>{
							if(res.data.status ===200){
								this.$notify.success(res.data.msg);
								this.$router.push('/admin/allblog')
							}
						})
					} else {
						this.$notify.error('error submit!!');
						return false;
					}
				});
			},
			//重置（清空输入）
			reset(){
				this.$refs.editForm.resetFields();
			},

			/*mavon-editor图片上传*/
			handleMavonEditorImgAdd(pos, $file){
				// console.log(pos);
				// console.log($file);
				// 1、将图片上传到服务器.
				var formdata = new FormData();
				formdata.append('file', $file);
				this.$request({
					url: '/file/aliyun/oss',
					method: 'post',
					data: formdata,
					headers: { 'Content-Type': 'multipart/form-data' },
				}).then((res) => {
					// 2、将返回的url替换到文本原位置![...](0) -> ![...](url)
					console.log(res);
          if(res.data.status === 200){
            this.$message.success("图片上传成功");
            this.$refs.md.$img2Url(pos, res.data.obj);
          }
				})
			},
			/*mavon-editor图片删除*/
			handleMavonEditorImgDel(pos, $file){
				console.log(pos);
				console.log($file);
				delete this.img_file[pos];
			},

		},
		created() {
			this.init();
		}
	}
</script>

<style scoped>
	.write-blog-view{
		width: 98%;
	}
</style>
