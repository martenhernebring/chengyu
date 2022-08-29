package se.hernebring.chengyu.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ChengYuServiceTest {

    @Autowired
    ChengYuService chengYuService;

    @Test
    void doubleChengYuReturnsChengYuCount() {
        String result = chengYuService.find("高山流水高山流水");
        assertEquals("Count,ChengYu\n2,高山流水", result);
        System.out.println(result);
    }
}
