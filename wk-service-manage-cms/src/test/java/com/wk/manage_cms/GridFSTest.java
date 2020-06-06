package com.wk.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;

/**
 * GridFS测试类
 */
@SpringBootTest(classes = ManageCmsApplication.class)
@RunWith(SpringRunner.class)
public class GridFSTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    /**
     * 保存测试
     */
    @Test
    public void saveTest() throws Exception{
        //创建文件输入流
        FileInputStream fis = new FileInputStream(new File("D:\\Program Files\\IDEA2019Projects\\wk-service\\test-freemarker\\src\\main\\resources\\templates\\index_banner.ftl"));

        //向GridFS存储文件
        ObjectId objectId = gridFsTemplate.store(fis, "轮播图测试文件01", "");

        //输出文件id
        System.out.println(objectId.toString());
    }

    @Test
    public void readTest() throws Exception{
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5eb167f230a9f94168945b90")));

        //创建下载流对象
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());

        //创建资源对象
        GridFsResource resource = new GridFsResource(gridFSFile, downloadStream);

        //获取内容
        String content = IOUtils.toString(resource.getInputStream(), "utf-8");

        //输出
        System.out.println(content);
    }

    @Test
    public void deleteTest(){
        //根据id删除
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5eb167f230a9f94168945b90")));
    }
}
