<template>
	<div class="v-emoji-picker-view">
		<el-col>
			<el-button type="text" size="mini" @click="showDialog = !showDialog">😃</el-button>
		</el-col>

		<el-col :span="12">
			<el-col :span="16">
				<el-input
						id="input"
						v-model="inputText"
						type="textarea"
						:autosize="{ minRows: 2, maxRows: 2}"
						resize="none"
						placeholder="请输入内容">
				</el-input>
			</el-col>
			<el-col :span="6">
				<el-button @click="sumitComment" style="height: 54px;color: white;" type="success">发表评论</el-button>
			</el-col>
		</el-col>

		<el-col>
			<VEmojiPicker v-show="showDialog" @select="selectEmoji"/>
		</el-col>
	</div>
</template>

<script>
	export default {
		name: "TestVueEmojiPicker",
		data(){
			return{
				showDialog:false,
				inputText:"",
			}
		},
		methods:{
			selectEmoji(emoji){
				console.log(emoji);
				let input = document.getElementById("input")
				let startPos = input.selectionStart
				let endPos = input.selectionEnd
				let resultText = input.value.substring(0, startPos) + emoji.data + input.value.substring(endPos)
				input.value = resultText
				input.focus()
				input.selectionStart = startPos + emoji.data.length
				input.selectionEnd = startPos + emoji.data.length
				this.inputText = resultText
			},
			sumitComment(){
				console.log(this.inputText);
				this.$emit("sumitComment",this.inputText);
			}
		},
	}
</script>

<style scoped>
	.v-emoji-picker-view{
		width: 100%;
	}
</style>