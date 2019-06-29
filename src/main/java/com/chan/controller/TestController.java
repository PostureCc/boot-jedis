package com.chan.controller;

import com.chan.mapper.TestMapper;
import com.chan.model.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Chan
 * @Date: 2019/6/29 09:47
 * @Description:
 */
@RestController
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insert(@RequestBody UserVo userVo) {
        testMapper.insert(userVo);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find(@RequestParam(value = "id", required = false) Long id) {
        return testMapper.find(id).toString();
    }

}
