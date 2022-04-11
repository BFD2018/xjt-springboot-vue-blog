<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  require('echarts/theme/roma')     // echarts theme
  import { debounce } from '@/utils'

  const animationDuration = 6000;

  export default {
    name:"MyUserCommentsChart.vue",
    props: {
      className: {
        type: String,
        default: 'chart'
      },
      width: {
        type: String,
        default: '100%'
      },
      height: {
        type: String,
        default: '300px'
      }
    },
    data() {
      return {
        chart: null,
        chartData:[],
      }
    },
    mounted() {
      this.initChart()
      this.__resizeHandler = debounce(() => {
        if (this.chart) {
          this.chart.resize()
        }
      }, 100)
      window.addEventListener('resize', this.__resizeHandler)
    },
    beforeDestroy() {
      if (!this.chart) {
        return
      }
      window.removeEventListener('resize', this.__resizeHandler)
      this.chart.dispose()
      this.chart = null
    },
    watch: {
      chartData: {
        deep: true,
        handler(val) {
          this.setChartOption(val)
        }
      }
    },
    methods: {
      setChartOption(chartData){
        let option = {
          title: {
            text: '用户评论',
            subtext: '评论数量统计图',
            left: 'left'
          },
          dataset: [
            {
              dimensions: ['user_id', 'username', 'nickname', 'num'],
              source: chartData
            },
            {
              transform: {
                type: 'sort',
                config: { dimension: 'num', order: 'desc' }
              }
            }
          ],
          xAxis: {
            type: 'category',
            axisLabel: { interval: 0, rotate: 30 }
          },
          yAxis: {},
          series: {
            type: 'bar',
            encode: { x: 'nickname', y: 'num' },
            datasetIndex: 1
          }
        };

        this.chart.setOption(option);
      },

      initChart() {
        this.chart = this.$echarts.init(this.$el, 'roma');

        this.setChartOption(this.chartData);
      },

      initChartData(){
        //按type类型统计博客数量
        this.$getRequest("/comment/back/allByUser").then(res => {
          //console.log(res);
          if (res.data.status === 200) {
            this.chartData.push(...res.data.obj);
          }
        })
      },
    },
    created() {
      this.initChartData();
    }
  }
</script>
