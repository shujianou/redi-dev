package com.wwdj.admin.controller.attachment;

import com.redimybase.framework.web.BaseController;
import com.wwdj.manager.file.entity.AttachmentEntity;
import com.wwdj.manager.file.mapper.AttachmentMapper;
import com.wwdj.manager.file.service.impl.AttachmentServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redimybase.framework.bean.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 附件相关接口
 * Created by Vim 2019/1/18 14:43
 *
 * @author Vim
 */
@RestController
@RequestMapping("attachment")
@Api(tags = "附件相关接口")
public class AttachmentController extends BaseController<String, AttachmentEntity, AttachmentMapper, AttachmentServiceImpl> {

    /**
     * 根据ID集合查询附件信息
     */
    @PostMapping("detailById")
    @ApiOperation(value = "根据ID集合查询附件信息")
    public R<?> detailById(@ApiParam(value = "附件id集合") String ids) {
        return R.ok(service.list(new QueryWrapper<AttachmentEntity>().in("id", ids).select("name,path,suffix_type")));
    }

    @Override
    public R<?> deleteBatchIds(String ids) {
        return R.fail("接口权限受限");
    }

    @Override
    public R<?> delete(String s) {
        return R.fail("接口权限受限");
    }

    @Autowired
    private AttachmentServiceImpl service;

    @Override
    protected AttachmentServiceImpl getService() {
        return service;
    }
}
