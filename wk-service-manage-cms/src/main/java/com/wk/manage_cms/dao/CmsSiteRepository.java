package com.wk.manage_cms.dao;

import com.wk.framework.domain.cms.CmsPage;
import com.wk.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CmsPage实体的持久层接口
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {

}
