<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  require('echarts/theme/macarons') // echarts theme
  import { debounce } from '@/utils'

  const animationDuration = 6000;

  export default {
    name:"MyTwoLinechart",
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
        default: '450px'
      }
    },
    data() {
      return {
        chart: null,
      }
    },
    methods: {
      initChart(lineData) {
        this.chart = this.$echarts.init(this.$el, 'macarons');

        let legendData = Object.keys(lineData);
        let seriesData = [];
        for (const [key, value] of Object.entries(lineData)) {
          let obj = {
            name: key,
            smooth: true,
            type: 'line',
            data: value,
            animationDuration: 2800,
            animationEasing: 'cubicInOut'
          };

          seriesData.push(obj);
        }

        let option = {
          title: {
            text: '用户文件统计'
          },
          xAxis: {
            data: ['图片', '文档', '音频', '视频', '压缩包', '其他'],
            boundaryGap: false,
            axisTick: {
              show: false
            }
          },
          grid: {
            left: 10,
            right: 10,
            bottom: 20,
            top: 30,
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            },
            padding: [5, 10]
          },
          yAxis: {
            axisTick: {
              show: false
            }
          },
          legend: {
            data: legendData
          },
          series: seriesData
        };

        this.chart.setOption(option);
      },

      initChartData(){
        this.$getRequest("/file/getCountsByUserAndType").then(res => {
          //console.log(res);
          if (res.data.status === 200) {
            let ret = {};
            //返回的数据结构是 {"朱小明":[2,3,4,6,1],"妹爷":[1,4,2,3,5]}

            res.data.obj.forEach((item, index) => {
              let flag = Object.keys(ret).some((val, index) => {
                return val === item['nickname']
              });

              if (!flag) {
                ret[item['nickname']] = [0, 0, 0, 0, 0, 0];
              }
            });

            res.data.obj.forEach((item, index) => {
              if (item["file_type"] === 1) {
                ret[item['nickname']][0] = Number(item["num"]);
              } else if (item["file_type"] === 2) {
                ret[item['nickname']][1] = Number(item["num"]);
              } else if (item["file_type"] === 3) {
                ret[item['nickname']][2] = Number(item["num"]);
              } else if (item["file_type"] === 4) {
                ret[item['nickname']][3] = Number(item["num"]);
              } else if (item["file_type"] === 5) {
                ret[item['nickname']][4] = Number(item["num"]);
              } else if (item["file_type"] === 6) {
                ret[item['nickname']][5] = Number(item["num"]);
              }
            });

            this.$nextTick(() =>{
              this.initChart(ret);
            })
          }
        });
      },
    },
    created() {
      this.initChartData();
    }
  }
</script>
