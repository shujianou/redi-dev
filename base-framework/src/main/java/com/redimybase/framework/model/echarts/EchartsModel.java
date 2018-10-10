package com.redimybase.framework.model.echarts;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Echarts 数据模型
 * Created by Irany 2018/8/16 13:45
 */
@Getter
@Setter
public class EchartsModel {

    /**
     * 树节点的名称，用来标识每一个节点。
     */
    private String name;

    /**
     * 节点的值，在 tooltip 中显示。
     */
    private String value;

    /**
     * 该节点的样式。
     */
    private ItemStyle itemStyle;

    /**
     * 节点标签
     */
    private Label label;




    /**
     * 节点样式。
     */
    @Getter
    @Setter
    public static class ItemStyle implements Serializable {

        /**
         * 图形的颜色。 默认从全局调色盘 option.color 获取颜色
         */
        private String color;

        /**
         * 图形的描边颜色。支持的颜色格式同 color，不支持回调函数。
         */
        private String borderColor;

        /**
         * 描边线宽。为 0 时无描边。
         */
        private Integer borderWidth;

        /**
         * 柱条的描边类型，默认为实线，支持 'solid', 'dashed', 'dotted'。
         */
        private String borderType;

        /**
         * 图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。
         */
        private String shadowBlur;

        /**
         * 阴影颜色。支持的格式同color。
         */
        private String shadowColor;

        /**
         * 阴影水平方向上的偏移距离。
         */
        private Integer shadowOffsetX = 0;

        /**
         * 阴影垂直方向上的偏移距离。
         */
        private Integer shadowOffsetY = 0;

        public static ItemStyle INSTANCE = new ItemStyle();

        public static String SOLID = "solid";

        public static String DASHED = "dashed";

        public static String DOTTED = "dotted";
    }


    /**
     * 节点标签
     */
    @Getter
    @Setter
    public static class Label implements Serializable{

        /**
         * 是否显示标签。
         */
        private boolean show;

        /**
         * 标签的位置。
         */
        private Object[] position;

        /**
         * 距离图形元素的距离。当 position 为字符描述值（如 'top'、'insideRight'）时候有效。
         */
        private Integer distance;

        /**
         * 标签旋转。从 -90 度到 90 度。正值是逆时针。
         */
        private Integer rotate;

        /**
         * 是否对文字进行偏移。默认不偏移。例如：[30, 40] 表示文字在横向上偏移 30，纵向上偏移 40。
         */
        private Integer[] offset;

        /**
         * 文字的颜色。
         */
        private String color;

        /**
         * 文字字体的风格
         */
        private String fontStyle;

        /**
         * 文字字体的粗细
         */
        private String fontWeight;

        /**
         * 文字的字体系列
         */
        private String fontFamily;

        /**
         * 文字的字体大小
         */
        private Integer fontSize;

        /**
         * 文字水平对齐方式，默认自动。
         */
        private String align;

        /**
         * 文字垂直对齐方式，默认自动。
         */
        private String verticalAlign;

        /**
         * 行高。
         */
        private Integer lineHeight;

        /**
         * 文字块背景色。
         */
        private String backgroundColor;

        /**
         * 文字块边框颜色。
         */
        private String borderColor;

        /**
         * 文字块边框宽度。
         */
        private Integer borderWidth;

        /**
         * 文字块的圆角。
         */
        private Integer borderRadius;

        /**
         * 文字块的内边距。例如：
         *
         * padding: [3, 4, 5, 6]：表示 [上, 右, 下, 左] 的边距。
         * padding: 4：表示 padding: [4, 4, 4, 4]。
         * padding: [3, 4]：表示 padding: [3, 4, 3, 4]。
         */
        private Integer padding;

        /**
         * 文字块的背景阴影颜色。
         */
        private String shadowColor;

        /**
         * 文字块的背景阴影长度。
         */
        private Integer shadowBlur;

        /**
         * 文字块的背景阴影 X 偏移。
         */
        private Integer shadowOffsetX;

        /**
         * 文字块的背景阴影 Y 偏移。
         */
        private Integer shadowOffsetY;

        /**
         * 文字块的宽度。一般不用指定，不指定则自动是文字的宽度。在想做表格项或者使用图片（参见 backgroundColor）时，可能会使用它。
         */
        private String width;

        /**
         * 文字块的高度。一般不用指定，不指定则自动是文字的高度。在使用图片（参见 backgroundColor）时，可能会使用它。
         */
        private String height;

        /**
         * 文字本身的描边颜色。
         */
        private String textBorderColor;

        /**
         * 文字本身的描边宽度。
         */
        private Integer textBorderWidth;

        /**
         * 文字本身的阴影颜色。
         */
        private String textShadowColor;

        /**
         * 文字本身的阴影长度。
         */
        private Integer textShadowBlur;

        /**
         * 文字本身的阴影 X 偏移。
         */
        private Integer textShadowOffsetX;

        /**
         * 文字本身的阴影 Y 偏移。
         */
        private Integer textShadowOffsetY;
    }


    /**
     * 高亮的节点样式
     */
    @Getter
    @Setter
    public static  class Emphasis implements Serializable{
        private Label label;
        private ItemStyle itemStyle;
    }

    /**
     * 提示框浮层的文本样式。
     */
    @Getter
    @Setter
    public static class TextStyle implements  Serializable{

        /**
         * 文字的颜色。
         */
        private String color;

        /**
         * 文字字体的风格
         */
        private String fontStyle;

        /**
         * 文字字体的粗细
         */
        private String fontWeight;

        /**
         * 文字的字体系列
         */
        private String fontFamily;

        /**
         * 文字的字体大小
         */
        private Integer fontSize;

        /**
         * 行高。
         */
        private Integer lineHeight;

        /**
         * 文字块的宽度。一般不用指定，不指定则自动是文字的宽度。在想做表格项或者使用图片（参见 backgroundColor）时，可能会使用它。
         */
        private Object width;

        /**
         * 文字块的高度。一般不用指定，不指定则自动是文字的高度。在使用图片（参见 backgroundColor）时，可能会使用它。
         */
        private Object height;

        /**
         * 文字本身的描边颜色。
         */
        private String textBorderColor;

        /**
         * 文字本身的描边宽度。
         */
        private String textBorderWidth;

        /**
         * 文字本身的阴影颜色。
         */
        private String textShadowColor;

        /**
         * 文字本身的阴影长度。
         */
        private Integer textShadowBlur;

        /**
         * 文字本身的阴影 X 偏移。
         */
        private Integer textShadowOffsetX;

        /**
         * 文字本身的阴影 Y 偏移。
         */
        private Integer textShadowOffsetY;

    }

    /**
     * 本系列每个数据项中特定的 tooltip 设定。
     */
    @Getter
    @Setter
    public static class Tooltip implements Serializable{

        /**
         * 提示框浮层的位置，默认不设置时位置会跟随鼠标的位置。
         */
        private Object[] position;

        /**
         * 提示框浮层内容格式器，支持字符串模板和回调函数两种形式。
         * 模板变量有 {a}, {b}，{c}，{d}，{e}，分别表示系列名，数据名，数据值等。 在 trigger 为 'axis' 的时候，会有多个系列的数据，此时可以通过 {a0}, {a1}, {a2} 这种后面加索引的方式表示系列的索引。 不同图表类型下的 {a}，{b}，{c}，{d} 含义不一样。 其中变量{a}, {b}, {c}, {d}在不同图表类型下代表数据含义为：
         *
         * 折线（区域）图、柱状（条形）图、K线图 : {a}（系列名称），{b}（类目值），{c}（数值）, {d}（无）
         *
         * 散点图（气泡）图 : {a}（系列名称），{b}（数据名称），{c}（数值数组）, {d}（无）
         *
         * 地图 : {a}（系列名称），{b}（区域名称），{c}（合并数值）, {d}（无）
         *
         * 饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
         *
         * 更多其它图表模板变量的含义可以见相应的图表的 label.formatter 配置项。
         *
         * 示例：
         *
         * formatter: '{b0}: {c0}<br />{b1}: {c1}'
         */
        private String formatter;

        /**
         * 提示框浮层的背景颜色。
         */
        private String backgroundColor;

        /**
         * 提示框浮层的边框颜色。
         */
        private String borderColor;

        /**
         * 提示框浮层的边框宽。
         */
        private Integer borderWidth;

        /**
         * 提示框浮层内边距，单位px，默认各方向内边距为5，接受数组分别设定上右下左边距。
         */
        private Integer padding;

        /**
         * 提示框浮层的文本样式。
         */
        private TextStyle textStyle;

        /**
         * 额外附加到浮层的 css 样式。如下为浮层添加阴影的示例：
         *
         * extraCssText: 'box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);'
         */
        private String extraCssText;
    }
}
