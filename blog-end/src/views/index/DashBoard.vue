<template>
  <div class="dashboard-view">
    <github-corner class="github-corner" />

    <panel-group class="panel-group"/>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <my-two-linechart class="chart-item"/>
    </el-row>

    <div class="echarts-group">
      <my-pie-chart class="chart-item"/>

      <my-bar-chart class="chart-item"/>

      <my-wordcloud-chart class="chart-item"/>

      <my-user-comments-chart class="chart-item"/>
    </div>
  </div>
</template>

<script>
  import GithubCorner from '@/components/GithubCorner'

  import PanelGroup from './PanelGroup'

  import MyPieChart from "./charts/MyPieChart"
  import MyBarChart from "./charts/MyBarChart"
  import MyWordcloudChart from "./charts/MyWordcloudChart"
  import MyTwoLinechart from "./charts/MyTwoLinechart"
  import MyUserCommentsChart from "./charts/MyUserCommentsChart"

  import 'echarts-wordcloud';

  export default {
    name: "DashBoard",
    components: {
      GithubCorner,
      PanelGroup,
      MyPieChart,
      MyBarChart,
      MyWordcloudChart,
      MyTwoLinechart,
      MyUserCommentsChart,
    },
    data() {
      return {

      }
    },
    methods: {

    },
    mounted(){

    },
    created() {

    }
  }
</script>

<style lang="less" scoped>
  .dashboard-view {
    position: relative;
    width: 95%;
    margin: auto;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .panel-group{
      margin-top: 32px;
    }

    .echarts-group {
      width: 100%;
      overflow: hidden;
      margin-top: 20px;
      margin-bottom: 20px;
      display: grid;
      grid-template-columns: 50% 50%;
      grid-row-gap: 30px;

      .chart-item {
        width: auto;
        background-color: #fff;
        min-height: 300px;
        padding: 10px;
      }

      .chart-item:nth-child(even) {
        margin-left: 30px;
      }
    }
  }

</style>
