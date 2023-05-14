<template>
	<div class="user-info-view">
		<el-steps :active="active" finish-status="success">
			<el-step title="基本信息"></el-step>
			<el-step title="详细信息"></el-step>
			<el-step title="展示用户信息"></el-step>
		</el-steps>

		<div class="step-form">
			<div v-show="active === 0" class="step1">
				<el-form ref="step1Form"
								 status-icon
								 label-width="100px"
								 label-position="left"
								 :model="step1Form"
								 :rules="step1FormRules">
					<el-form-item label="username" prop="username">
						<el-input v-model="step1Form.username" placeholder="请输入用户名"></el-input>
					</el-form-item>

					<el-form-item label="password" prop="password">
						<el-input placeholder="请输入密码" show-password v-model="step1Form.password"></el-input>
					</el-form-item>
				</el-form>
			</div>

			<div v-show="active === 1" class="step2">
				<el-form
						status-icon
						label-width="100px"
						label-position="left"
						:model="step2Form">
					<el-form-item label="nickname">
						<el-input placeholder="请输入昵称" v-model="step2Form.nickname"></el-input>
					</el-form-item>

					<el-form-item label="email">
						<el-input v-model="step2Form.email"></el-input>
					</el-form-item>

					<el-form-item label="用户角色">
						<el-select v-model="step2Form.role" placeholder="请选择用户角色">
							<el-option
									v-for="(item,index) in rolesList"
									:key="index"
									:label="item.nameZh"
									:value="item.id">
								<span style="float: left">{{ item.nameZh }}</span>
								<span style="float: right; color: #8492a6; font-size: 13px">{{ item.name }}</span>
							</el-option>
						</el-select>
					</el-form-item>

					<el-form-item label="description">
						<el-input
								type="textarea"
								:rows="4"
								placeholder="请输入用户描述信息"
								v-model="step2Form.description">
						</el-input>
					</el-form-item>

					<el-form-item label="avatar">
						<el-upload
								class="avatar-uploader"
								action="/api/file/aliyun/oss"
								:show-file-list="false"
								:on-success="handleUploadSuccess"
								:before-upload="beforeUpload">
							<img v-if="this.step2Form.avatar" :src="this.step2Form.avatar" class="avatar">
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</el-form-item>
				</el-form>
			</div>

			<div v-if="active === 2" class="active1">
				<el-card class="box-card">
					<div class="user-card">
						<div class="avatar">
							<el-image
									style="width: 100%; height: 100%"
									:src="this.step2Form.avatar"
									fit="cover"></el-image>
						</div>
						<div class="info">
							<div class="my-title">用户名：</div>
							<p class="my-marginLeft10">{{this.step1Form.username}}</p>
							<div class="my-title">昵称：</div>
							<p class="my-marginLeft10">{{this.step2Form.nickname}}</p>
							<div class="my-title">邮箱：</div>
							<p class="my-marginLeft10">{{this.step2Form.email}}</p>
							<div class="my-title">用户角色：</div>
							<p class="my-marginLeft10">{{this.step2Form.role}}</p>
							<div class="my-title">描述：</div>
							<p class="my-marginLeft10">{{this.step2Form.description}}</p>
						</div>
					</div>
				</el-card>
			</div>

			<el-button v-if="active > 0" style="margin-top: 12px" @click="pre">上一步</el-button>
			<el-button v-if="active < 2" style="margin-top: 12px" @click="next">下一步</el-button>
			<a href="/admin/user/list" v-if="active === 2">
				<el-button type="success" style="margin-top: 12px">完成</el-button>
			</a>
		</div>
	</div>
</template>

<script>
	import {_userRegister,_updateUserMoreInfo,_getAllRoles} from "@/api/api.js"

	export default {
		name: "AddUser",
		data() {
			return {
				dialogAvatarUrl: '',
				dialogAvatarVisible: false,
				rolesList: [],			//所有的角色列表
				active: 0,

				//用户基本信息
				step1Form: {
					username: "",
					password: "",
				},
				step1FormRules: {
					username: [
						{required: true, message: '请输入用户名', trigger: 'blur'},
					],
					password: [
						{required: true, message: '请输入密码', trigger: 'blur'},
					],
				},
				//用户详细信息
				step2Form: {
					id: "",
					nickname: "",
					avatar: "",
					email: "",
					role: "",
					description: "",
				},
			}
		},
		methods: {
			//获取所有角色
			getAllRoles() {
				_getAllRoles().then(res => {
					console.log(res);
					if (res.data.status === 200) {
						this.rolesList.push(...res.data.obj);
					}
				})
			},

			/*用户头像上传*/
			beforeUpload(file){
				//console.log(file)
				const isJPG = file.type.startsWith("image");
				const isLt50M = file.size / 1024 / 1024 < 50;

				if (!isJPG) {
					this.$message.error('上传头像图片只能是Image!');
				}
				if (!isLt50M) {
					this.$message.error('上传头像图片大小不能超过 50MB!');
				}
				return isJPG && isLt50M;
			},
			handleUploadSuccess(res, file){
				console.log(res);
				if(res.status === 200){
          this.step2Form.avatar = res.obj;
				}
			},

			//步骤条下一步的方法
			next() {
				if (this.active === 0) {
					//提交
					this.$refs.step1Form.validate((valid) => {
						let self = this;
						if (valid) {
							console.log(this.step1Form);
							_userRegister(this.step1Form).then(res => {
								console.log(res);
								if (res.data.status === 200) {
									this.$notify.success(res.data.msg);
									this.step2Form.id = res.data.obj.id;
									setTimeout(()=>{
										self.active++;
									},1000)
								}
							})
						} else {
							this.$notify.error('error submit!!');
							return false;
						}
					});
				}
				if (this.active === 1) {
					let self = this;
					console.log(this.step2Form);
					_updateUserMoreInfo(this.step2Form).then(res => {
						console.log(res);
						if (res.data.status === 200) {
							this.$notify.success(res.data.msg);
							setTimeout(()=>{
								self.active++;
							},1000)
						} else {
							this.$notify.error('error submit!!');
							return false;
						}
					})
				}
				if (this.active === 2) {
					this.$router.replace("/admin/user/list")
				}
			},
			//步骤条上一步的方法
			pre() {
				this.active--;

			},
		},
		created() {
			this.getAllRoles();
		},
		filters:{
			filterRoles(val){
				return val.nameZh + "-" + item.name;
			}
		}
	}
</script>

<style scoped>
	.step-form {
		width: 40vw;
		padding: 20px;
		margin: 20px auto;
		box-shadow: 0 0 2px #ddd;
	}

	.user-card {
		display: flex;
		flex-wrap: nowrap;
	}

	.user-card .avatar {
		width: 300px;
		height: 260px;
	}

	.user-card .info {
		flex: 1;
		margin-left: 10px;
		font-size: 15px;
		color: #8492a6;
		padding: 10px;
		border-left: 1px solid #dddddd;
	}

	/*头像上传*/
	.avatar-uploader .el-upload {
		border: 1px solid #dbdada;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}
	.avatar-uploader .el-upload i{
		border: 1px solid #dbdada;
	}
	.avatar-uploader .el-upload i:hover {
		border-color: #409EFF;
	}
	.avatar-uploader-icon {
		font-size: 28px;
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
