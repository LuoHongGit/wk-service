package com.wk.manage_cms;

import com.wk.framework.domain.cms.CmsPage;
import com.wk.framework.domain.cms.CmsPageParam;
import com.wk.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * CmsPageRepository测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 测试查询所有
     */
    @Test
    public void findAllTest(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    /**
     * 测试分页查询
     */
    @Test
    public void findByPage(){
        //创建pagerequest对象
        int page = 1;
        int size = 10;
        PageRequest request = PageRequest.of(page, size);

        //分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(request);

        System.out.println(all.getContent());
    }

    /**
     * 测试添加
     */
    @Test
    public void addTest(){
//定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        System.out.println(cmsPage);
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }

    /**
     * 测试删除
     */
    @Test
    public void updateTest(){
        //通过主键查询cmsPage
        Optional<CmsPage> optional = cmsPageRepository.findById("5e9bfb27affe691c94a516ea");

        //判空
        if (optional.isPresent()) {
            //获取数据
            CmsPage cmsPage = optional.get();

            //更新数据
            cmsPage.setPageName("测试页面01");

            //发送更新
            cmsPageRepository.save(cmsPage);

            System.out.println(cmsPage);
        }
    }

    /**
     * 测试自定义持久层方法
     */
    @Test
    public void findByPageNameTest(){
        CmsPage cmsPage = cmsPageRepository.findByPageName("测试页面01");

        System.out.println(cmsPage);
    }

    //自定义条件查询测试
    @Test
    public void testFindAll() {
        ///条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        //页面别名模糊查询，需要自定义字符串的匹配器实现模糊查询
        //ExampleMatcher.GenericPropertyMatchers.contains() 包含
        //ExampleMatcher.GenericPropertyMatchers.startsWith()//开头匹配
        //条件值
        CmsPage cmsPage = new CmsPage();
        //站点ID
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板ID
        //cmsPage.setTemplateId("5ad9a24d68db5239b8fef199");
        cmsPage.setPageAliase("首页");
        //创建条件实例
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Pageable pageable = PageRequest.of(0, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getContent());
        System.out.println(all.getContent().size());
    }

    @Test
    public void test1(){
        //创建分页参数
        PageRequest pageable = PageRequest.of(0, 10);

        //创建实例对象
        CmsPage cmsPage = new CmsPage();

        //属性赋值
        //cmsPage.setSiteId("s01");
        //cmsPage.setTemplateId("t01");
        cmsPage.setPageAliase("课程详情页面");

        //创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();

        //创建Example对象
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        //查询结果
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);

        System.out.println(all.getContent());
        System.out.println(all.getContent().size());
    }
}