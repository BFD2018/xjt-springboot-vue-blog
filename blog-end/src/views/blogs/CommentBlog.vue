<template>
  <div class="write-blog-view">
    <el-table
      :data="tableData"
      border
      style="width: calc(100vw - 200px)">
      <el-table-column
        label="ID"
        width="190">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="评论人"
        width="130">
        <template slot-scope="scope">
          {{scope.row.username}}
        </template>
      </el-table-column>

      <el-table-column
        label="博客ID"
        width="190">
        <template slot-scope="scope">
          {{scope.row.blog_id}}
        </template>
      </el-table-column>

      <el-table-column
        label="博客标题"
        width="300">
        <template slot-scope="scope">
          {{scope.row.title}}
        </template>
      </el-table-column>

      <el-table-column
        label="父评论ID"
        width="190">
        <template slot-scope="scope">
          {{scope.row.parent_comment_id}}
        </template>
      </el-table-column>

      <el-table-column
        label="评论内容">
        <template slot-scope="scope">
          {{scope.row.content}}
        </template>
      </el-table-column>

      <el-table-column
        label="评论时间"
        width="100">
        <template slot-scope="scope">
          {{scope.row.create_time | formatTimestamp(that)}}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="success"
            @click="replyCommentBtn(scope.$index, scope.row)">回复
          </el-button>

          <span style="margin-left: 10px"></span>

          <el-popconfirm
            @confirm="deleteCommentBtn(scope.$index, scope.row)"
            title="确定删除该条评论吗？">
            <el-button
              slot="reference"
              size="mini"
              type="danger">删除
            </el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="回复评论" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="博客标题：">
          {{currentReplyObj.title}}
        </el-form-item>

        <el-form-item label="评论人：">
          {{currentReplyObj.username}}
        </el-form-item>

        <el-form-item label="评论内容：">
          {{currentReplyObj.content}}
        </el-form-item>

        <el-form-item label="评论时间：">
          {{currentReplyObj.create_time | formatTimestamp(that)}}
        </el-form-item>

        <el-form-item
          :rules="[
            { required: true, message: '请输入回复内容', trigger: 'blur' },]"
          label="回复：">
          <el-input
            type="textarea"
            :rows="4"
            placeholder="请输入回复内容"
            v-model="inputReplyContent">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmReply">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  export default {
    name: "write-blog",
    components: {},
    data(){
      return{
        tableData:[],

        currentReplyObj:{},
        inputReplyContent:"",
        dialogFormVisible:false,
      }
    },
    methods:{
      initTableData(){
        this.$getRequest("/comment/getAll").then(res =>{
          console.log(res);
          if(res.data.status === 200){
            this.tableData.splice(0);
            this.tableData.push(...res.data.obj);
          }
        })
      },
      replyCommentBtn(idx,row){
        this.dialogFormVisible = true;
        this.currentReplyObj = row;
      },
      deleteCommentBtn(idx,row){
        let comment_id = row.id;
        this.$getRequest("/comment/delete?id="+comment_id).then(res =>{
          if(res.data.status === 200){
            this.$notify.success("删除成功！");
            this.tableData.splice(idx,1);
          }
        })
      },
      confirmReply(){
        let params = {
          "user_id": this.$store.state.login_user.id,
          "content": this.inputReplyContent,
          "blog_id": this.currentReplyObj.blog_id,
          "parent_comment_id": this.currentReplyObj.id,
        };
        console.log(params);
        this.$postRequest("/comment/save", params).then(res => {
          console.log(res);
          if (res.data.status === 200) {
            this.$notify.success("评论成功!");
            this.dialogFormVisible = false;
            document.location.reload();
          }
        })
      }
    },
    created() {
      this.initTableData();
    }
  }
</script>

<style>

</style>
