<template>
  <div class="category-blog-view">
    <!--搜索-->
    <el-row style="margin-top: 10px;margin-bottom: 10px;">
      <el-col :span="8">
        <el-input placeholder="输入Type名称" v-model="inputKeyWord"></el-input>
      </el-col>
      <el-col :span="1">
        <el-button type="success" @click="searchByName">
          <i class="el-icon-search"></i>
          搜索
        </el-button>
      </el-col>
      <el-col :span="1" :offset="1">
        <el-button type="warning" @click="handlePostNewType">
          <i class="el-icon-plus"></i>
          增加
        </el-button>
      </el-col>
    </el-row>

    <!--修改分类的对话框-->
    <el-dialog title="编辑分类专栏" :visible.sync="updateTypeDialogFormVisible">
      <div class="updateType" v-if="currentEditType!=null">
        id：
        <br>
        <el-input v-model="currentEditType.id" disabled></el-input>
        <br>
        <br>
        分类专栏名：
        <br>
        <el-input v-model="currentEditType.name"></el-input>
        <br/>
        <br/>
        <el-button type="primary" @click="handleDialogForm">确 定</el-button>
      </div>
    </el-dialog>


    <el-table
      border
      :data="tableData"
      style="width: 100%">
      <el-table-column
        label="ID"
        width="300">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="名称"
        width="300">
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button
            size="mini"
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
    name: "CategoryBlog",
    components: {},
    data() {
      return {
        inputKeyWord: "",
        inputNewTypeName: "",
        tableData: [],

        updateTypeDialogFormVisible: false,		//编辑type对话框是否显示
        currentEditType: {
          id: "",
          name: ""
        },		//当前编辑的type
      }
    },
    methods: {
      //网络请求---获取所有分类
      _getAllType() {
        this.$getRequest("/type/all").then(res => {
          console.log(res);
          if (res.data.status === 200) {
            //每次请求时清空tableData数据
            this.tableData.splice(0);
            this.tableData.push(...res.data.obj)
          }
        })
      },


      //表格操作-删除分类
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除该标签, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //网络请求
          this.$request("/type/delete/" + row.id).then(res => {
            console.log(res);
            if (res.status === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
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
        this.updateTypeDialogFormVisible = true;
        this.currentEditType = row;
      },
      //确认修改
      handleDialogForm() {
        this.$postRequest("/type/update", this.currentEditType).then(res => {
          console.log(res);
          if (res.data.status === 200) {
            this.$message.success("修改成功");
            this.updateTypeDialogFormVisible = false;
            //window.location.reload();				//强制刷新页面（相当于浏览器的刷新按钮）
          } else {
            this.$message.error(res.data.msg);
          }
        })
      },

      /*搜索*/
      searchByName() {
        this.$getRequest("/type/getByName/"+this.inputKeyWord).then(res =>{
          console.log(res);
          if(res.data.status === 200){
            this.tableData.splice(0);
            this.tableData.push(...res.data.obj)
          }else{
            this.$message.warning(res.data.obj);
          }
        })
      },
      //新增分类
      handlePostNewType() {
        if (this.inputKeyWord == '') {
          this.$message.error("请输入Type名")
        } else {
          let isExist = this.tableData.some((item) => {
            return item.name.toLowerCase() === this.inputKeyWord.toLowerCase()
          });
          if (isExist) {
            this.$message.error("type已存在")
          } else {
            let postData = {
              "typeName": this.inputKeyWord
            };
            this.$postRequest("/type/save/", postData).then(res => {
              console.log(res);
              if (res.data.status === 200) {
                this.$message.success("Type创建成功");
                window.location.reload();
              } else {
                this.$message.error(res.data.msg);
              }
            })
          }
        }
      }
    },
    created() {
      this._getAllType();
    }
  }
</script>

<style>

</style>
