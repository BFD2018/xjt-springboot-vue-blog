<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  require('echarts/theme/macarons') // echarts theme
  import { debounce } from '@/utils'

  const animationDuration = 6000;

  export default {
    name:"MyBarChart",
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
            text: '博客分类',
            subtext: '数量统计',
            left: 'left'
          },
          xAxis: {
            type: 'category',
            axisLabel: {
              interval:0,
              rotate:40
            },
            data: chartData.map(item => {
              return item.name
            }),
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: chartData.map(item => {
                return item.value
              }),
              type: 'bar',
              showBackground: true,
              backgroundStyle: {
                color: 'rgba(180, 180, 180, 0.2)'
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
        //按type类型统计博客数量
        this.$getRequest("/blog/counts/byType").then(res => {
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
