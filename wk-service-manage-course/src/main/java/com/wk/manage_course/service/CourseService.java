package com.wk.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wk.framework.domain.cms.CmsPage;
import com.wk.framework.domain.cms.response.CmsPageResult;
import com.wk.framework.domain.cms.response.CmsPostPageResult;
import com.wk.framework.domain.course.*;
import com.wk.framework.domain.course.ext.CategoryNode;
import com.wk.framework.domain.course.ext.CourseInfo;
import com.wk.framework.domain.course.ext.TeachplanNode;
import com.wk.framework.domain.course.request.CourseListRequest;
import com.wk.framework.domain.course.response.AddCourseResult;
import com.wk.framework.domain.course.response.CourseCode;
import com.wk.framework.domain.course.response.CoursePublishResult;
import com.wk.framework.domain.course.response.CourseView;
import com.wk.framework.exception.ExceptionCast;
import com.wk.framework.model.response.CommonCode;
import com.wk.framework.model.response.QueryResponseResult;
import com.wk.framework.model.response.QueryResult;
import com.wk.framework.model.response.ResponseResult;
import com.wk.manage_course.client.CmsPageClient;
import com.wk.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.ServiceMode;
import java.util.Optional;

/**
 * 教学计划业务层实现类
 */
@Service
public class CourseService {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;
    
    /**
     * 查询课程计划
     *
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //获取课程id
        String courseid = teachplan.getCourseid();

        //获取父节点id
        String parentid = teachplan.getParentid();

        //校验课程id和课程计划名称
        if (teachplan == null ||
                StringUtils.isEmpty(courseid) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //初始化父节点
        Teachplan rootTeachplan = null;

        //判断是否选择了父节点id
        if (StringUtils.isEmpty(parentid)) {
            //没有选择父节点 默认为根节点
            rootTeachplan = this.getRootTeachplan(courseid);
        } else {
            //选择了父节点
            Optional<Teachplan> parentOpt = teachplanRepository.findById(parentid);

            //判空
            if (!parentOpt.isPresent()) {
                ExceptionCast.cast(CommonCode.INVALID_PARAM);
            }

            rootTeachplan = parentOpt.get();
        }

        //给当前节点设置父节点
        teachplan.setParentid(rootTeachplan.getId());

        //设置状态为未发布
        teachplan.setStatus("0");

        //获取父节点级别
        String parentGrade = rootTeachplan.getGrade();

        //根据父节点级别设置该节点级别
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        } else if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }

        //设置课程id
        teachplan.setCourseid(rootTeachplan.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程计划根节点
     *
     * @param courseid
     * @return
     */
    private Teachplan getRootTeachplan(String courseid) {
        //查询该课程的根节点
        Teachplan teachplan = teachplanRepository.findByCourseidAndParentid(courseid, "0");

        //判空
        if (teachplan == null) {
            //通过id查询课程
            Optional<CourseBase> courseOpt = courseBaseRepository.findById(courseid);

            //判空
            if (!courseOpt.isPresent()) {
                ExceptionCast.cast(CommonCode.INVALID_PARAM);
            }

            //获取课程信息
            CourseBase courseBase = courseOpt.get();

            //创建新的根节点
            teachplan = new Teachplan();

            //设置属性
            teachplan.setCourseid(courseid);
            teachplan.setPname(courseBase.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");//1级
            teachplan.setStatus("0");//未发布
            teachplanRepository.save(teachplan);
        }
        return teachplan;
    }

    /**
     * 分页查询课程列表
     *
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult<CourseInfo> findCourseListPage(int page, int size, CourseListRequest courseListRequest) {
        //参数校验
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 10;
        }

        //设置分页
        PageHelper.startPage(page, size);

        //调用持久层获取查询结果
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);

        //创建响应结果对象
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(courseListPage.getResult());
        queryResult.setTotal(courseListPage.getTotal());
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, queryResult);
    }

    /**
     * 添加课程基本信息
     *
     * @param courseBase
     * @return
     */
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //参数判断
        if (courseBase == null) {
            ExceptionCast.cast(CourseCode.COURSE_PARAM_INVALID);
        }

        //设置课程默认状态为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    /**
     * 通过课程id查询课程基本信息
     *
     * @param courseId
     * @return
     */
    public CourseBase findCourseBaseById(String courseId) {
        //调用持久层获取查询结果
        Optional<CourseBase> opt = courseBaseRepository.findById(courseId);

        //判空
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;
    }

    /**
     * 更新课程基本信息
     *
     * @param courseId
     * @param courseBase
     * @return
     */
    @Transactional
    public ResponseResult updateCourseBase(String courseId, CourseBase courseBase) {
        //通过id查询课程信息
        CourseBase one = this.findCourseBaseById(courseId);

        //判空
        if (one == null) {
            ExceptionCast.cast(CourseCode.COURSE_PARAM_INVALID);
        }

        //修改课程信息
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());

        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 通过id查询课程营销信息
     *
     * @param courseId
     * @return
     */
    public CourseMarket findCourseMarketById(String courseId) {
        //调用持久层获取查询结果
        Optional<CourseMarket> opt = courseMarketRepository.findById(courseId);

        //判空
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;
    }

    /**
     * 更新课程营销信息
     *
     * @param course
     * @param courseMarket
     * @return
     */
    @Transactional
    public CourseMarket updateCourseMarket(String course, CourseMarket courseMarket) {
        //通过id查询课程营销信息
        CourseMarket one = this.findCourseMarketById(course);

        if (one != null) {
            //原本有数据
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());
            one.setEndTime(courseMarket.getEndTime());
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
        } else {
            //第一次添加
            //添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            one.setId(course);
        }

        courseMarketRepository.save(one);

        return one;
    }

    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        //查询课程图片
        Optional<CoursePic> opt = coursePicRepository.findById(courseId);

        //初始化图片对象
        CoursePic coursePic = null;

        //判空
        if (opt.isPresent()) {
            coursePic = opt.get();
            coursePic.setPic(pic);
        } else {
            coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
        }

        //保存
        coursePicRepository.save(coursePic);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findCoursePicById(String courseId) {
        Optional<CoursePic> opt = coursePicRepository.findById(courseId);

        //判空
        if (opt.isPresent()) {
            return opt.get();
        }

        return null;
    }

    @Transactional
    public ResponseResult deleteCoursePicById(String courseId) {
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public CourseView getCoruseView(String id) {
        CourseView courseView = new CourseView();

        //查询课程基本信息
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            courseView.setCourseBase(courseBase);
        }
        Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
        if (courseMarketOptional.isPresent()) {
            CourseMarket courseMarket = courseMarketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if (picOptional.isPresent()) {
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(picOptional.get());
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    /**
     * 通过课程id生成cmspage
     * @param id
     * @return
     */
    public CoursePublishResult preview(String id){
        //通过id查询课程
        CourseBase one = this.findCourseBaseById(id);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(id+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+id);

        //保存页面
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);

        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //页面id
        String pageId = cmsPageResult.getCmsPage().getPageId();

        //页面url
        String pageUrl = previewUrl+pageId;
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String courseId){
        //课程信息
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程详情页面
        CmsPostPageResult cmsPostPageResult = publish_page(courseId);
        if(!cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }

        //更新课程状态
        CourseBase courseBase = saveCoursePubState(courseId);
        //课程索引...
        //课程缓存...
        //页面url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //更新课程发布状态
    private CourseBase saveCoursePubState(String courseId){
        CourseBase courseBase = this.findCourseBaseById(courseId);
        //更新发布状态
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }

    //发布课程正式页面
    public CmsPostPageResult publish_page(String courseId){
        CourseBase one = this.findCourseBaseById(courseId);
        //发布课程预览页面
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId+".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);
        //发布页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        return cmsPostPageResult;
    }
}
