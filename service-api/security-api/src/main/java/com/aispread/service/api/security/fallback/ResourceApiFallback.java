package com.aispread.service.api.security.fallback;

import com.aispread.service.api.security.ResourceApi;
import com.aispread.manager.security.bean.ResourceInfo;
import com.redimybase.framework.model.datamodel.ztree.Ztree;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Vim 2018/11/24 11:51
 *
 * @author Vim
 */
@Service
public class ResourceApiFallback implements ResourceApi {
    @Override
    public List<String> getResKeyList(String userId) {
        return null;
    }

    @Override
    public List<String> getResNameList(String userId) {
        return null;
    }

    @Override
    public List<ResourceInfo> getMenuByUserId(String currentUserId) {
        return null;
    }

    @Override
    public List<Ztree> menuNodeList() {
        return null;
    }
}
