<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  require('echarts/theme/macarons') // echarts theme
  import { debounce } from '@/utils'

  const animationDuration = 6000;

  export default {
    name:"MyPieChart",
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
      setChartOption(pieData){
        let option = {
          title: {
            text: '用户-角色',
            subtext: '数量分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '角色占比',
              type: 'pie',
              radius: '70%',
              data: pieData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };

        this.chart.setOption(option);
      },

      initChart() {
        this.chart = this.$echarts.init(this.$el, 'macarons')

        this.setChartOption(this.chartData);
      },

      initChartData(){
        //用户（按性别）统计占比
        this.$getRequest("/user/counts/byRole").then(res => {
          console.log(res);
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
