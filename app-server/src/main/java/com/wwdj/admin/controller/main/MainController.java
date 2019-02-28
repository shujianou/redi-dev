package com.wwdj.admin.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redimybase.framework.bean.R;
import com.wwdj.manager.banner.entity.AppBannerEntity;
import com.wwdj.manager.banner.service.AppBannerService;
import com.wwdj.manager.item.entity.ItemCategoryEntity;
import com.wwdj.manager.item.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页接口
 * Created by Vim 2019/2/15 18:07
 *
 * @author Vim
 */
@RestController
@RequestMapping("app/main")
public class MainController {

    /**
     * 首页主要数据
     */
    @PostMapping("data")
    public R<?> mainData() {
        Map<String, Object> data = new HashMap<>();

        //轮播图
        data.put("banners", appBannerService.page(
                new Page<AppBannerEntity>(1, 5).setDesc("sort"),
                new QueryWrapper<AppBannerEntity>().lambda().eq(AppBannerEntity::getStatus, AppBannerEntity.Status.正常).select(AppBannerEntity::getId, AppBannerEntity::getSrc, AppBannerEntity::getUrl)).getRecords()
        );

        //商品分类
        data.put("itemCategoryList", itemCategoryService.page(
                new Page<ItemCategoryEntity>(1, 20).setDesc("sort"),
                new QueryWrapper<ItemCategoryEntity>().lambda().eq(ItemCategoryEntity::getStatus, ItemCategoryEntity.Status.正常).select(ItemCategoryEntity::getId, ItemCategoryEntity::getImg, ItemCategoryEntity::getShortName)).getRecords()
        );
        return new R<>(data);
    }

    @Autowired
    private AppBannerService appBannerService;

    @Autowired
    private ItemCategoryService itemCategoryService;
}
