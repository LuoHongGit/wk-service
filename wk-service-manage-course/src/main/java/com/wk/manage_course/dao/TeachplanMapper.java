package com.wk.manage_course.dao;

import com.wk.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachplanMapper {
    TeachplanNode selectList(String courseId);
}
