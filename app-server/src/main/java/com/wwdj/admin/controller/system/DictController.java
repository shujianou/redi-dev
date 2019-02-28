package com.wwdj.admin.controller.system;

import com.redimybase.framework.web.BaseController;
import com.wwdj.manager.dict.entity.DictEntity;
import com.wwdj.manager.dict.mapper.DictMapper;
import com.wwdj.manager.dict.service.impl.DictServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典Controller
 * Created by Vim 2018/12/27 14:15
 *
 * @author Vim
 */
@RestController
@RequestMapping("dict")
@Api(tags = "数据字典接口")
public class DictController extends BaseController<String, DictEntity, DictMapper, DictServiceImpl> {


    @Autowired
    private DictServiceImpl service;

    @Override
    protected DictServiceImpl getService() {
        return service;
    }
}
