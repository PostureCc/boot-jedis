package com.chan.model.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Auther: Chan
 * @Date: 2019/6/29 09:35
 * @Description:
 */
@Data
@Entity(name = "t_user")
public class UserDto implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "sex")
    private Byte sex;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;

}
