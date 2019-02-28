package com.wwdj.manager.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Vim 2019/1/25 17:06
 *
 * @author Vim
 */
@Data
@ApiModel("文件上传model")
public class FileUploadRequest implements Serializable {

    @ApiModelProperty("文件类型,什么业务就写什么类型")
    private String type;
    @ApiModelProperty("需要上传到哪个文件夹")
    private String folder;
}
