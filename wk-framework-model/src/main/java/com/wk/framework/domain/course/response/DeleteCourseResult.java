package com.wk.framework.domain.course.response;

import com.wk.framework.model.response.ResponseResult;
import com.wk.framework.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/20.
 */
@Data
@ToString
public class DeleteCourseResult extends ResponseResult {
    public DeleteCourseResult(ResultCode resultCode, String courseId) {
        super(resultCode);
        this.courseid = courseid;
    }
    private String courseid;

}
