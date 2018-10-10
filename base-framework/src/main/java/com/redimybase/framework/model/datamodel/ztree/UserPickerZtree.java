package com.redimybase.framework.model.datamodel.ztree;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * UserPicker ZTree
 * Created by Irany 2018/6/29 10:58
 */
@Getter
@Setter
public class UserPickerZtree<ID extends Serializable> extends Ztree<ID> {

    private Integer childCount; //子节点数目
}
