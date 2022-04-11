<template>
  <div class="links-blog-view">
    <!--搜索-->
    <el-row style="margin-top: 10px;margin-bottom: 10px;">
      <el-col :span="8">
        <el-input placeholder="按照Link名称模糊搜索" v-model="inputName">
        </el-input>
      </el-col>
      <el-col :span="1">
        <el-button type="primary" @click="searchByInput">
          <i class="el-icon-search"></i>
          搜索
        </el-button>
      </el-col>
      <el-col :span="1" :offset="1">
        <el-button type="warning" @click="addLinkDialogFormVisible = true">
          <i class="el-icon-plus"></i>增加
        </el-button>
      </el-col>
    </el-row>

    <!--修改link的对话框-->
    <el-dialog title="修改Link信息" :visible.sync="updateLinkDialogFormVisible">
      <el-form :model="currentEditLink" :rules="linkRules">
        <el-form-item label="友链名称" prop="blogName">
          <el-input v-model="currentEditLink.blogName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链地址" prop="blogAddress">
          <el-input v-model="currentEditLink.blogAddress" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链图片">
          <el-input v-model="currentEditLink.pictureAddress" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateLinkDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleConfirmUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!--添加link的对话框-->
    <el-dialog title="添加Link" :visible.sync="addLinkDialogFormVisible">
      <el-form :model="currentEditLink" :rules="linkRules">
        <el-form-item label="友链名称" prop="blogName">
          <el-input v-model="currentEditLink.blogName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链地址" prop="blogAddress">
          <el-input v-model="currentEditLink.blogAddress" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="友链图片">
          <el-input v-model="currentEditLink.pictureAddress" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addLinkDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleConfirmAddLink">确 定</el-button>
      </div>
    </el-dialog>

    <!--点击外链地址弹出dialog-iframe展示内容-->
    <el-dialog :title="currentEditLink.blogName" width="70%" :visible.sync="iframeDialogVisible">
      <iframe :src="currentEditLink.blogAddress" frameborder="0" width="100%" height="700px"></iframe>
    </el-dialog>

    <el-table
        :data="tableData"
        border
        style="width: 99%">
      <el-table-column
          label="ID"
          width="200">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
          label="友链图片"
          width="250">
        <template slot-scope="scope">
          <el-image
              style="width: 250px; height: 120px"
              :src="scope.row.pictureAddress"
              fit="contain">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column
          label="友链名称"
          width="300">
        <template slot-scope="scope">
          <span>{{ scope.row.blogName }}</span>
        </template>
      </el-table-column>
      <el-table-column
          label="友链地址">
        <template slot-scope="scope">
          <div @click="handleOpenLinkInline(scope.row)" class="my-hover1 my-padding">{{scope.row.blogAddress}}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button
            type="warning"
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

    <!--分页-->
    <div class="pagination my-border-padding">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[3, 6, 10, 15]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  export default {
    name: "LinksBlog",
    components: {},
    data() {
      return {
        inputName:"",
        tableData:[],

        updateLinkDialogFormVisible:false,		//编辑link对话框是否显示
        addLinkDialogFormVisible:false,		//编辑link对话框是否显示
        iframeDialogVisible:false,    //是否显示iframe展示外链内容
        currentEditLink: {
          id:"",
          blogName:"",
          blogAddress:"",
          pictureAddress:"",
        },		//当前编辑的link

        /*form验证规则*/
        linkRules:{
          blogName: [
            { required: true, message: '请输入友链博客名称', trigger: 'blur' },
          ],
          blogAddress: [
            { required: true, message: '请输入友链博客地址', trigger: 'blur' },
          ],
        },

        /*分页*/
        currentPage:1,
        pageSize:6,
        total:0,

      }
    },
    methods: {
      //网络请求---获取所有友链
      _initGetAllLinks(){
        this.$getRequest(`/link/getByPage?current=${this.currentPage}&pageSize=${this.pageSize}`).then(res =>{
          console.log(res);
          if(res.data.status === 200){
            this.tableData.splice(0);
            this.tableData.push(...res.data.obj.records);

            this.total = Number(res.data.obj.total);
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
          this.$deleteRequest("/link/delete?lid="+row.id).then(res => {
            console.log(res);
            if(res.data.status === 200){
              this.$message.success('删除成功!');
              this.tableData.splice(index,1);
            }else{
              this.$message.error(res.data.msg);
            }
          })
        }).catch(() => {
          this.$message.info('已取消删除');
        });
      },

      //打开编辑Dialog
      handleEdit(index, row) {
        this.updateLinkDialogFormVisible = true;
        this.currentEditLink = row;
      },
      //确认修改
      handleConfirmUpdate(){
        this.$postRequest("/link/update",this.currentEditLink).then(res =>{
          //console.log(res);
          if(res.data.status === 200){
            this.$message.success(res.data.msg);
            this.updateLinkDialogFormVisible = false;
            this.currentEditLink = {};
          }else{
            this.$message.error(res.data.msg);
          }
        })
      },

      /*点击搜索--根据tag.name模糊搜索*/
      searchByInput(){
        if(this.inputNewTagName == ''){
          this.$message.error("请输入tag名")
        }else{
          this.$getRequest("/link/findByName/like?name="+this.inputName).then(res =>{
            console.log(res);
            if(res.data.status === 200){
              this.tableData.splice(0);
              this.tableData.push(...res.data.obj);
            }else{
              this.$message.error(res.data.msg);
            }
          })
        }

      },

      //新增link---弹框填写信息
      handleConfirmAddLink(){
        this.$postRequest("/link/save",this.currentEditLink).then(res =>{
          console.log(res);
          if(res.data.status === 200){
            this.$message.success(res.data.msg);
            this.addLinkDialogFormVisible = false;
            this.currentEditLink = {};

            this.tableData.unshift(res.data.obj);
          }else{
            this.$message.error(res.data.msg)
          }
        })
      },

      /*打开外链---iframe展示*/
      handleOpenLinkInline(row){
        this.currentEditLink = {};
        this.currentEditLink = row;
        this.iframeDialogVisible = true;
      },


      /*分页*/
      handleSizeChange(size){
        /*pageSize 改变时会触发*/
        this.pageSize = size;
        this._initGetAllLinks();
      },
      handleCurrentChange(current){
        /*currentPage 改变时会触发*/
        this.currentPage = current;
        this._initGetAllLinks();
      },
    },
    created() {
      this._initGetAllLinks();
    }
  }
</script>

<style>

</style>
