package com.wk.manage_course.client;

import com.wk.framework.client.WKServiceList;
import com.wk.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(WKServiceList.WK_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
    @GetMapping("/cms/page/get/{id}")
    public CmsPage findById(@PathVariable("id") String id);
}
