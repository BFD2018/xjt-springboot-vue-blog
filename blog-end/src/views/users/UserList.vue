<template>
  <div class="user-list-view">
    <!--搜索-->
    <el-row style="margin-top: 10px;margin-bottom: 10px;">
      <el-col :span="8">
        <el-input placeholder="按用户名搜索" v-model="inputKeyWord"></el-input>
      </el-col>
      <el-col :span="1">
        <el-button type="success" @click="handleSearchByName">
          <i class="el-icon-search"></i>
          搜索
        </el-button>
      </el-col>
      <el-col :span="1" :offset="1">
        <el-button type="warning" @click="$router.push('/admin/user/add')">
          <i class="el-icon-plus"></i>增加
        </el-button>
      </el-col>
    </el-row>

    <el-table
      border
      :data="tableData"
      style="width: 100%">
      <el-table-column
        label="ID"
        width="220">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="头像"
        width="110">
        <template slot-scope="scope">
          <el-image
            class="my-border"
            style="width: 100px; height: 100px"
            :src="scope.row.avatar |filterImgUrl(that)"
            fit="contain"></el-image>
        </template>
      </el-table-column>
      <el-table-column
        label="用户名"
        width="160">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column
        label="昵称"
        width="160">
        <template slot-scope="scope">
          {{ scope.row.nickname }}
        </template>
      </el-table-column>
      <el-table-column
        label="邮箱"
        width="250">
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>
      </el-table-column>
      <el-table-column
        label="个人签名">
        <template slot-scope="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: "UserList",
    data() {
      return {
        inputKeyWord: "",
        inputNewTypeName: "",
        postNewTypeLoading: false,
        tableData: [],

        currentUser: {
          id: "",
          name: ""
        },		//当前编辑的user
      }
    },
    methods: {
      //网络请求---获取所有分类
      _getAllUser() {
        this.$getRequest("/user/all").then(res => {
          console.log(res);
          if (res.data.status === 200) {
            //每次请求时清空tableData数据
            this.tableData.splice(0);
            this.tableData.push(...res.data.obj)
          }
        })
      },

      //表格操作-删除
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除该标签, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //网络请求
          this.$getRequest("/user/delete?id=" + row.id).then(res => {
            if (res.data.status === 200) {
              this.tableData.splice(index, 1);
              this.$message.success('删除成功!');
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },

      //表格操作-编辑
      handleEdit(index, row) {
        console.log(row);
        let obj = {
          id: row.id,
          username: row.username,
          password: row.password,
          nickname: row.nickname,
          avatar: row.avatar,
          email: row.email,
          description: row.description,
        };
        let stringfy_url = this.$qs.stringify(obj);
        this.$router.replace("/admin/user/update?" + stringfy_url)
      },
      //确认修改
      handleDialogForm() {
        this.$postRequest("/user/update", this.currentUser).then(res => {
          console.log(res);
          if (res.data.status === 200) {
            this.$message.success("修改成功");
            this.updateUserDialogFormVisible = false;
          } else {
            this.$message.error(res.data.msg);
          }
        })
      },

      /*搜索*/
      handleSearchByName() {
        this.$getRequest("/user/getByName?name=" + this.inputKeyWord.trim()).then(res => {
          console.log(res);
          if (res.data.status === 200) {
            //每次请求时清空tableData数据
            this.tableData.splice(0);
            this.tableData.push(...res.data.obj)
          }else{
            this.$message.error(res.data.msg);
          }
        })
      },
      //新增user
      handlePostNewUser() {
        this.$postRequest("/user/register/?typeName=").then(res => {
          //console.log(res);
          if (res.data.status === 200) {
            this.$message.success("新增分类Tag成功");
            window.location.reload();
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }
    },
    created() {
      this._getAllUser();
    }
  }
</script>

<style scoped>

</style>
