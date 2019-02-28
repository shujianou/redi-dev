package com.wwdj.manager.test.service.impl;

import com.wwdj.manager.test.entity.TestEntity;
import com.wwdj.manager.test.mapper.TestMapper;
import com.wwdj.manager.test.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vim
 * @since 2018-09-14
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestEntity> implements TestService {

}
