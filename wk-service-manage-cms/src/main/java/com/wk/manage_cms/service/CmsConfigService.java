package com.wk.manage_cms.service;

import com.wk.framework.domain.cms.CmsConfig;
import com.wk.manage_cms.dao.CmsConfigRepository;
import org.bouncycastle.cms.CMSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfig findById(String id){
        //获取查询结果
        Optional<CmsConfig> opt = cmsConfigRepository.findById(id);

        //判空
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;
    }
}
