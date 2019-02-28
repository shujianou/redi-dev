package com.redimybase.framework.model.echarts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Echarts 树状图模型
 * Created by Irany 2018/8/16 14:39
 */
@Getter
@Setter
public class EchartsTreeModel extends EchartsModel {

    public EchartsTreeModel(String name) {
        this.setName(name);
    }

    public EchartsTreeModel() {
    }

    public EchartsTreeModel append(EchartsTreeModel children) {
        this.children.add(children);
        return this;
    }

    public EchartsTreeModel append(List<EchartsTreeModel> children) {
        this.children.addAll(children);
        return this;
    }


    /**
     * 高亮的节点样式
     */
    private Emphasis emphasis;

    /**
     * 本系列每个数据项中特定的 tooltip 设定。
     */
    private Tooltip tooltip;

    /**
     * 是否开启动画。
     */
    private boolean animation;

    /**
     * 是否开启动画的阈值，当单个系列显示的图形数量大于这个阈值时会关闭动画。
     */
    private Integer animationThreshold;

    /**
     * 初始动画的时长，支持回调函数，可以通过每个数据返回不同的 delay 时间实现更戏剧的初始动画效果：
     * <p>
     * animationDuration: function (idx) {
     * // 越往后的数据延迟越大
     * return idx * 100;
     * }
     */
    private Integer animationDuration;

    /**
     * 初始动画的缓动效果。
     */
    private String animationEasing;

    /**
     * 初始动画的延迟，支持回调函数，可以通过每个数据返回不同的 delay 时间实现更戏剧的初始动画效果。
     * <p>
     * 如下示例：
     * <p>
     * animationDelay: function (idx) {
     * // 越往后的数据延迟越大
     * return idx * 100;
     * }
     */
    private Integer animationDelay;

    /**
     * 数据更新动画的时长。
     * <p>
     * 支持回调函数，可以通过每个数据返回不同的 delay 时间实现更戏剧的更新动画效果：
     * <p>
     * animationDurationUpdate: function (idx) {
     * // 越往后的数据延迟越大
     * return idx * 100;
     * }
     */
    private Integer animationDurationUpdate;

    /**
     * 数据更新动画的缓动效果。
     */
    private String animationEasingUpdate;

    /**
     * 数据更新动画的延迟，支持回调函数，可以通过每个数据返回不同的 delay 时间实现更戏剧的更新动画效果。
     * <p>
     * 如下示例：
     * <p>
     * animationDelayUpdate: function (idx) {
     * // 越往后的数据延迟越大
     * return idx * 100;
     * }
     */
    private Integer animationDelayUpdate;


    /**
     * 子集
     */
    private List<EchartsTreeModel> children;
}
