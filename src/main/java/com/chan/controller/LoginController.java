package com.chan.controller;

import com.chan.context.Token;
import com.chan.mapper.TestMapper;
import com.chan.model.dto.UserDto;
import com.chan.model.vo.UserVo;
import com.chan.service.JWTService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: Chan
 * @Date: 2019/6/29 15:00
 * @Description:
 */
@RestController
public class LoginController {

    /**
     * 流程
     * 1.用户登录
     * 2.
     */

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private JWTService jwtService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestBody UserVo userVo) {
        UserDto userDto = testMapper.find(userVo.getId());
        Token token = new Token();
        BeanUtils.copyProperties(userDto, token);
        String str = jwtService.produceToken(token);
        return str;
    }

}
