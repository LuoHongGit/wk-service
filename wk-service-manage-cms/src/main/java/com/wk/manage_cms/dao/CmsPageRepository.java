package com.wk.manage_cms.dao;

import com.wk.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsPage实体的持久层接口
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    //通过页面名称查询页面
    CmsPage findByPageName(String pageName);

    //通过站点id和页面名称以及访问路径查询页面
    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId, String pageName, String pageWebPath);
}
