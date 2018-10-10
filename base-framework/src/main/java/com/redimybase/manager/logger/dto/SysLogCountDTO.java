package com.redimybase.manager.logger.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统日志统计DTO
 * Created by Irany 2018/8/13 18:17
 */
@Getter
@Setter
public class SysLogCountDTO  implements Serializable {

    private String timeSlot;
    private Integer count;
}
