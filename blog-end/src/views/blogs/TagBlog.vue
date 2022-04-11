<template>
  <div class="tag-blog-view">
    <!--搜索-->
    <el-row style="margin-top: 10px;margin-bottom: 10px;">
      <el-col :span="8">
        <el-input placeholder="搜索/添加" v-model="inputTagName">
        </el-input>
      </el-col>
      <el-col :span="1">
        <el-button type="primary" @click="searchByInput">
          <i class="el-icon-search"></i>
          搜索
        </el-button>
      </el-col>
      <el-col :span="1" :offset="1">
        <el-button type="warning" @click="handleAddNewTag">
          <i class="el-icon-plus"></i>
          增加
        </el-button>
      </el-col>
    </el-row>

    <!--修改分类的对话框-->
    <el-dialog title="编辑Tag信息" :visible.sync="updateTagDialogFormVisible">
      <div class="updateType" v-if="currentEditTag!=null">
        id：
        <br/>
        <br/>
        <el-input v-model="currentEditTag.id" disabled></el-input>
        <br/>
        <br/>
        Tag名：
        <br/>
        <br/>
        <el-input v-model="currentEditTag.name"></el-input>
        <br/>
        <br/>
        选择tag颜色：
        <br/>
        <br/>
        <el-select v-model="currentEditTag.color" placeholder="请选择Tag颜色">
          <el-option
            v-for="item in selectTagOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            <el-button size="mini" :type="item.value" circle></el-button>
            <span style="margin-left: 20px; color: #8492a6; font-size: 13px">{{ item.label }}</span>
          </el-option>
        </el-select>

        <br/>
        <br/>
        <el-button type="primary" @click="handleConfirmUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        label="ID"
        width="200">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="名称"
        width="300">
        <template slot-scope="scope">
          <el-tag :type="scope.row.color == ''?'success':scope.row.color">{{ scope.row.name }}</el-tag>
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
    name: "TagBlog",
    components: {},
    data() {
      return {
        inputTagName: "",
        tableData: [],

        updateTagDialogFormVisible: false,		//编辑type对话框是否显示
        currentEditTag: {
          id: "",
          name: "",
          color: "",
        },		//当前编辑的tag

        selectTagOptions: [
          {
            value: '',
            label: '默认'
          },
          {
            value: 'primary',
            label: '主要'
          },
          {
            value: 'success',
            label: '成功'
          },
          {
            value: 'info',
            label: '信息'
          },
          {
            value: 'warning',
            label: '警告'
          },
          {
            value: 'danger',
            label: '危险'
          },
        ],
      }
    },
    methods: {
      //网络请求---获取所有分类
      _getAllTag() {
        this.$request("/tag/all").then(res => {
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
          this.$deleteRequest("/tag/delete?tid=" + row.id).then(res => {
            console.log(res);
            if (res.data.status === 200) {
              this.$message.success('删除成功!');
            } else {
              this.$message.error(res.data.msg);
            }
          })
        }).catch(() => {
          this.$message.info('已取消删除');
        });
      },

      //表格操作-编辑
      handleEdit(index, row) {
        this.updateTagDialogFormVisible = true;
        this.currentEditTag = row;
      },
      //确认修改
      handleConfirmUpdate() {
        console.log(this.currentEditTag);
        this.$postRequest("/tag/update", this.currentEditTag).then(res => {
          console.log(res);
          if (res.data.status === 200) {
            this.$message.success(res.data.msg);
            this.updateTagDialogFormVisible = false;
            //window.location.reload();				//强制刷新页面（相当于浏览器的刷新按钮）
          } else {
            this.$message.error(res.data.msg);
          }
        })
      },

      /*点击搜索--根据tag.name模糊搜索*/
      searchByInput() {
        if (this.inputTagName == '') {
          this.$message.error("请输入tag名")
        } else {
          this.$getRequest("/tag/findByName/like?name=" + this.inputTagName).then(res => {
            console.log(res);
            if (res.data.status === 200) {
              this.tableData.splice(0);
              this.tableData.push(...res.data.obj);
            } else {
              this.$message.error(res.data.msg);
            }
          })
        }

      },
      //新增tag
      handleAddNewTag() {
        if (this.inputTagName == '') {
          this.$message.error("请输入tag名")
        } else {
          let isExist = this.tableData.some((item) => {
            return item.name.toLowerCase() === this.inputTagName.toLowerCase()
          });
          if (isExist) {
            this.$message.error("tag已存在")
          } else {
            let postData = {
              "tagName": this.inputTagName
            };
            this.$postRequest("/tag/save/", postData).then(res => {
              // console.log(res);
              if (res.data.status === 200) {
                this.$message.success("Tag创建成功");
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
      this._getAllTag();
    }
  }
</script>

<style>

</style>
