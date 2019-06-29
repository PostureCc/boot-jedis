package com.chan.mapper;

import com.chan.model.dto.UserDto;
import com.chan.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {

    int insert(UserVo userVo);

    UserDto find(@Param("id") Long id);

}
