package com.wk.manage_cms.controller;

import com.wk.api.cms.CmsConfigControllerApi;
import com.wk.framework.domain.cms.CmsConfig;
import com.wk.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {
    @Autowired
    private CmsConfigService cmsConfigService;

    @GetMapping("/get/{id}")
    public CmsConfig findById(@PathVariable("id") String id) {
        return cmsConfigService.findById(id);
    }
}
