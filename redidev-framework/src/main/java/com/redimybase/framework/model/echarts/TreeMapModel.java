package com.redimybase.framework.model.echarts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Irany 2018/9/5 11:03
 */
@Getter
@Setter
public class TreeMapModel extends EchartsModel {

    private String path;

    private List<TreeMapModel> children;

}
