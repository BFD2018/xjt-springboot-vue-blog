<template>
	<div class="user-detail-view">
		<el-card class="box-card">
			<div slot="header" class="clearfix">
				<div class="my-title">用户信息</div>
			</div>

			<el-form ref="editForm"
							 status-icon
							 label-width="80px"
							 label-position="left"
							 :model="editForm">
				<el-form-item label="id">
					<el-input v-model="editForm.id" disabled></el-input>
				</el-form-item>

				<el-form-item label="用户名">
					<el-input v-model="editForm.username"></el-input>
				</el-form-item>

				<el-form-item label="密码">
					<el-input v-model="editForm.password"></el-input>
				</el-form-item>

				<el-form-item label="昵称">
					<el-input v-model="editForm.nickname"></el-input>
				</el-form-item>

				<el-form-item label="邮箱">
					<el-input v-model="editForm.email"></el-input>
				</el-form-item>

				<el-form-item label="描述">
					<el-input
							type="textarea"
							:rows="4"
							v-model="editForm.description">
					</el-input>
				</el-form-item>

				<el-upload
						class="avatar-uploader"
						:action="uploadAvatarUrl"
						:data="uploadAvatarParams"
						:show-file-list="false"
						:on-success="handleAvatarSuccess"
						:before-upload="beforeAvatarUpload">
					<img v-if="editForm.avatar" :src="editForm.avatar |filterImgUrl(that)" class="avatar my-border" width="120px" height="120px">
					<i v-else class="el-icon-plus avatar-uploader-icon"></i>
				</el-upload>

				<el-form-item class="my-marginTop10">
					<el-button type="primary" @click="submitBlogForm">提交</el-button>
				</el-form-item>
			</el-form>
		</el-card>
	</div>
</template>

<script>
	import {_updateUserInfo} from "@/api/api.js"

	export default {
		name: "UserDetail",
		data(){
			return{
				uploadAvatarParams:{
					"id":"",
				},

				editForm:{
					id: "1",
					username:"",
					password:"",
					nickname: "",
					avatar: "",
					email: "",
					description: "",
				},

			}
		},
		methods:{
			submitBlogForm(){
				console.log(this.editForm);
				_updateUserInfo(this.editForm).then(res =>{
					console.log(res);
					if(res.data.status === 200){
						this.$notify.success(res.data.msg);
						this.$router.push("/admin/user/list")
					}else{
						this.$notify.error(res.data.msg);
					}
				})
			},

			/*上传头像*/
			beforeAvatarUpload(file) {
				const isJPG = file.type.startsWith("image");
				const isLt10M = file.size / 1024 / 1024 < 10;
				if (!isJPG) {
					this.$message.error('上传头像图片只能是 JPG 格式!');
				}
				if (!isLt10M) {
					this.$message.error('上传头像图片大小不能超过 10MB!');
				}
				return isJPG && isLt10M;
			},
			handleAvatarSuccess(res, file){
				if(res.status === 200){
					this.editForm.avatar = res.obj.path;
					this.$message.success(res.msg);
				}
			},
		},
		computed:{
			uploadAvatarUrl(){
			  return '/api/file/avatar/upload';
			}
		},
		created() {
			console.log(this.$route);
			this.editForm = this.$route.query;
			this.uploadAvatarParams.id = this.$route.query.id;
		}
	}
</script>

<style scoped>
	.box-card{
		margin-top: 20px;
		width: 70vw;
	}

	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}
	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}
	.avatar-uploader-icon {
		font-size: 28px;
		border: 1px dashed #d9d9d9;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}
	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}
</style>
