package org.xjt.blog;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.xjt.blog.service.TBlogService;
import org.xjt.blog.utils.MyMailUtil;
import org.xjt.blog.utils.MyUtils;
import org.xjt.blog.utils.RespBean;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

@SpringBootTest
public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue() throws FileNotFoundException {
//        String path = ResourceUtils.getURL("classpath:static").getPath().replace("%20", "").replace("/", "\\");
//
//        System.out.println(path.substring(1));

        //System.out.println(request.getServletPath());
        //
        //System.out.println(request.getRequestURI());
        //
        //System.out.println(request.getProtocol()+"://"+request.getRemoteHost());

        String localContainer = "fileContainer";
        String uploadPath = ResourceUtils.getURL("/classpath").getPath() + localContainer;
        System.out.println(uploadPath);

    }

    @Autowired
    private TBlogService tBlogService;

    @Test
    public void test01(){
        List<HashMap<String, Object>> mapList = new ArrayList<>();

        HashMap<String, Object> map = new HashMap<>();
        map.put("tag_name","vue");
        map.put("tag_color","primary");
        map.put("blog_num",4);
        System.out.println("map==========>");
        System.out.println(map);
        mapList.add(map);
    }

    @Test
    public void testJieba1(){
        JiebaSegmenter segmenter = new JiebaSegmenter();
        /*单词*/
        System.out.println(segmenter.sentenceProcess("小明硕士毕业于中国科学院计算所，后在日本京都大学深造"));
        System.out.println(segmenter.sentenceProcess("这是一个伸手不见五指的黑夜"));
        System.out.println(segmenter.sentenceProcess("我是你爸爸，你个傻逼"));
    }

    @Test
    public void testJieba2(){
        JiebaSegmenter segmenter = new JiebaSegmenter();
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。", "我不喜欢日本和服。", "雷猴回归人间。",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        ArrayList<Map<String, Object>> objects = new ArrayList<>();
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX));
            List<SegToken> tokenList = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);

            tokenList.forEach(item ->{
                String s = item.toString().substring(1, item.toString().length() - 1);
                String name = s.split(",")[0];
                String startOffset = s.split(",")[1].trim();
                String endOffset = s.split(",")[2].trim();
                Integer val = (Integer.valueOf(startOffset) + Integer.valueOf(endOffset)) / 2;
                System.out.println(name);
                System.out.println(val);

                Map<String, Object> map = new HashMap<>();
                map.put("name",name);
                map.put("value",val);
                objects.add(map);
            });
        }

        System.out.println(objects);
    }

    @Test
    public void test03(){
        String ss = "[这是, 0, 2]";
        String substring = ss.substring(1, ss.length() - 1);
        String name = substring.split(",")[0];
        String startOffset = substring.split(",")[1].trim();
        String endOffset = substring.split(",")[2].trim();
        Integer val = (Integer.valueOf(startOffset) + Integer.valueOf(endOffset)) / 2;
        System.out.println(name);
        System.out.println(val);
    }

    @Test
    public void test04(){
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        // 可以直接从文件中读取
        //List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(getInputStream("D:\\citydo-one\\技术\\Java_Note-master\\python\\tp\\Trump.txt"));
        List<WordFrequency> wordFrequencies = new ArrayList<>();
        // 用词语来随机生成词云
        List<String> test = Arrays.asList("你好","谢谢");
        String strValue = "铁路投资=20, 步伐=20, 国际证券=10, 新开工=15, 加快=15, 逆势增长=15, 增速=7, 万亿元=7, 5月份=6, 同增=5, 水利=5, 相比=5, 跌幅=5, 公路=4, 建筑行业=4, 基建=4, 投资额=4, 本周=4, 高铁=4, 铁路=4, 市政=4, 项目=4, 板块=4, 减少=3, 表现=3, 铁路建设=3, 亿元=3, 新线=3, 回落=3, 开通=3, 下跌=3, 中标=3, 上市公司=3, 6月份=3, 细分=2, 六保=2, 三大=2, 单相=2, 单月=2, 六稳=2, 建筑板块=2, 2项=2, 0.11=2, 洪涝灾害=2, 增长=2, 基建投资=2, 全国铁路=2, 最小=2, 投资=2, 作用=2, 稳步复苏=2, 行业=2, 相关=2, 提升=2, 地产=2, 火车头=2, 预计=2, 央企=2, 建筑=2, 施工=2, 来源=2, 地产投资=2, 签订=2, 比上周=2, 区间=2, 不及预期=2, 34.6=2, 中的=2, 8.4=2, 同比分别=2, 大幅提升=1, 持续=1, 建设步伐=1, 环比=1, 口径=1, 115=1, 其次是=1, 公布=1, 当月=1, 建筑指数=1, 24个=1, 2.85=1, 据介绍=1, 2020年=1, 产业链=1, 行业投资=1, 投资增速=1, 10.8=1, 较上周=1, 亮眼=1, 电热=1, 多个=1, 29.2=1, 欠佳=1, 装配式建筑=1, 收盘=1, ppp=1, 总体=1, 复工=1, 建中=1, 中国=1, 6.3=1, 6.5=1, 下挫=1, 发行=1, 0.07=1, 合计=1, 跑赢大盘=1, 稳步增长=1, 大盘=1, 下半年=1, 3项=1, 计为=1, 气水=1, 0.14=1, 好于=1, 概念板块=1, 金融界=1, 分别为=1, 专项=1, 明显加快=1, 上涨=1, 订单金额=1, 上半年=1, 增速放缓=1, 0.44=1, 开工=1, 1.08=1, 超过=1, 30.1=1, 整体估值=1, 4.5=1, 1.8pct=1, 0.020=1, 八大=1, 1178=1, epc=1, 主要风险=1, 0.60=1, 公用=1, 申万=1, 连镇=1, 2084.43=1, 显现=1, 有望=1, 跌幅为=1, 投资数据=1, 合并=1, 改善=1, 复苏=1, 0.87=1, 0.950=1, 0.85=1, 新开工项目=1, 评级=1, 北段=1, 影响=1, 房地产开发投资=1, 影响下=1, 主要是=1, 0.90=1, 园林工程=1, 2.36=1, 1.68=1, 0.93=1, 涨幅=1, 二级=1, 6月=1, 建筑节能=1, 建筑工程=1, 情况=1, 交运=1, 四大=1, 网站=1, 下降=1, 邮储=1, 开始显现=1, 设计=1, 格库铁路=1, 12.1=1, 2.39=1, 微涨=1, 3.05=1, 所致=1, 继续保持=1, 5000=1, 认为是=1, 3.19=1, 4400公里=1, 新疆=1, 2300公里=1, 订单=1, 分支=1, 公里=1";
        String[] split = strValue.split(", ");
        String word = "";
        int count = 0;

        for (int i = 0; i < split.length; i++) {
            String[] wordInfo = split[i].split("=");
            word = wordInfo[0];
            count = Integer.valueOf(wordInfo[1]);
            wordFrequencies.add(new WordFrequency(word, count));
        }
        //加入分词并随机生成权重，每次生成得图片都不一样
        //test.stream().forEach(e-> wordFrequencies.add(new WordFrequency(e,new Random().nextInt(test.size()))));
        //此处不设置会出现中文乱码
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 18);
        //设置图片分辨率
        Dimension dimension = new Dimension(1280, 720);
        //此处的设置采用内置常量即可，生成词云对象
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        //设置边界及字体
        wordCloud.setPadding(2);
        //因为我这边是生成一个圆形,这边设置圆的半径
        wordCloud.setBackground(new CircleBackground(255));
        wordCloud.setFontScalar(new SqrtFontScalar(12, 42));
        //设置词云显示的三种颜色，越靠前设置表示词频越高的词语的颜色
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setKumoFont(new KumoFont(font));
        wordCloud.setBackgroundColor(new Color(255, 255, 255));
        //因为我这边是生成一个圆形,这边设置圆的半径
        wordCloud.setBackground(new CircleBackground(255));
        wordCloud.build(wordFrequencies);
        //生成词云图路径
        wordCloud.writeToFile("E:\\wordCount.png");
    }

    @Test
    public void test05(){
        String s = MyMailUtil.sendMail("1351655382@qq.com");
        System.out.println(s);
    }
}
