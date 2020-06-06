package com.wk.manage_cms;

import com.wk.manage_cms.service.CmsPageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest(classes = ManageCmsApplication.class)
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void getHtmlPageByIdTest(){
        String content = cmsPageService.getHtmlPageById("5eb17d736834f332f496c204");
        System.out.println(content);
    }
}
