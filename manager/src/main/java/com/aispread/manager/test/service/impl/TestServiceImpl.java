package com.aispread.manager.test.service.impl;

import com.aispread.manager.test.entity.TestEntity;
import com.aispread.manager.test.mapper.TestMapper;
import com.aispread.manager.test.service.TestService;
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
