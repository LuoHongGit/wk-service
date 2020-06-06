package com.wk.manage_cms;

import com.wk.manage_cms.ManageCmsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest(classes=ManageCmsApplication.class)
@RunWith(SpringRunner.class)
public class RestTemplateTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test1(){
        ResponseEntity<Map> entity = restTemplate.getForEntity("http://127.0.0.1:31001/cms/config/get/5a791725dd573c3574ee333f", Map.class);

        System.out.println(entity);
    }
}
