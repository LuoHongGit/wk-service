package com.wk.manage_cms.dao;

import com.wk.framework.domain.cms.CmsConfig;
import com.wk.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsPage实体的持久层接口
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {

}
