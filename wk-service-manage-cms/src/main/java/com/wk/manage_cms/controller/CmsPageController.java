package com.wk.manage_cms.controller;

import com.wk.api.cms.CmsPageControllerApi;
import com.wk.framework.domain.cms.CmsPage;
import com.wk.framework.domain.cms.request.QueryPageRequest;
import com.wk.framework.domain.cms.response.CmsPageResult;
import com.wk.framework.domain.course.CourseBase;
import com.wk.framework.model.response.QueryResponseResult;
import com.wk.framework.model.response.ResponseResult;
import com.wk.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private CmsPageService cmsPageService;

    /**
     * 分页查询页面
     * @param page
     * @param size
     * @param request
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CourseBase> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest request) {
        return cmsPageService.findList(page, size, request);
    }

    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    @PostMapping("/add")
    public CmsPageResult addPage(@RequestBody CmsPage cmsPage) {
        //调用Service层
        return cmsPageService.addPage(cmsPage);
    }

    /**
     * 根据id查询页面
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.findById(id);
    }

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    @PutMapping("/edit/{id}")
    public CmsPageResult editPage(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return cmsPageService.editPage(id, cmsPage);
    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public ResponseResult delPage(@PathVariable("id") String id) {
        return cmsPageService.delPage(id);
    }

    /**
     * 页面发布
     * @param id
     * @return
     */
    @PostMapping("/postPage/{id}")
    public ResponseResult postPage(@PathVariable("id") String id) {
        return cmsPageService.postPage(id);
    }

    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.saveOrUpdate(cmsPage);
    }
}
