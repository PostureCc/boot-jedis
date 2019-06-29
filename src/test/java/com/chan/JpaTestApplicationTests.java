package com.chan;

import com.chan.service.JedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaTestApplicationTests {

    @Autowired
    private JedisService jedisService;

    @Test
    public void testRedis(){
        jedisService.setJedis("jedis","jedis",100);
    }

    @Test
    public void contextLoads() {
    }

}
