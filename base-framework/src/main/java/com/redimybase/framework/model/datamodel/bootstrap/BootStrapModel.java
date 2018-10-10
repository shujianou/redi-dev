package com.redimybase.framework.model.datamodel.bootstrap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redimybase.framework.model.datamodel.table.TableModel;

/**
 * Created by Irany 2018/6/20 23:29
 */

public class BootStrapModel<T> extends TableModel<T> {
    /**
     * 获取总记录数.
     */
    @JsonProperty("total")
    public long getTotal() {
        return total;
    }

}
