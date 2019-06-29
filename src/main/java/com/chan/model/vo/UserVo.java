package com.chan.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: Chan
 * @Date: 2019/6/29 09:35
 * @Description:
 */
@Data
public class UserVo implements Serializable {

    private Long id;

    private String name;

    private Integer age;

    private Byte sex;

    private String createTime;

    private String updateTime;

}
