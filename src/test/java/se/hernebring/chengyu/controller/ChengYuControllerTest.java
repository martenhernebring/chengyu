package se.hernebring.chengyu.controller;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import se.hernebring.chengyu.service.WordService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChengYuControllerTest {

    private ChengYuController chengYuController;

    @Autowired
    WordService service;

    @BeforeEach
    void init() {
        chengYuController = new ChengYuController(service);
    }

    @Test
    void thinkEveryoneCountsOnce() {
        String thinkEveryone = "大家想一想大家想一想大家想一想";
        var result = chengYuController.createMap(thinkEveryone);
        assertEquals(1, result.size());
    }

    @Test
    void quadrupleDoubleChengYuAndTripleChengYuContainingStartingChar() {
        var result = chengYuController.createMap("畫龍點睛畫龍點睛畫蛇添足畫龍點睛畫蛇添足畫蛇添足畫龍點睛");
        assertEquals(4, result.get("畫龍點睛"));
        assertEquals(3, result.get("畫蛇添足"));
        assertEquals(2, result.size());
    }

    @Test
    void singleChengYuReturnEmpty() {
        var result = chengYuController.createMap("高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void tripleChengYuReturnsChengYuCount() {
        var result = chengYuController.createMap("高山流水高山流水高山流水");
        assertEquals(3, result.get("高山流水"));
        assertEquals(1, result.size());
    }

    @Test
    void differentTripleChengYuReturnsDifferentChengYuWithCount() {
        var result = chengYuController.createMap("明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYusReturnEmpty() {
        var result = chengYuController.createMap("明辨是非高山流水明辨是非");
        assertTrue(result.isEmpty());
    }

    @Test
    void quadrupleChengYuReturnsChengYuCountWithoutFakes() {
        var result = chengYuController.createMap("開天闢地開天闢地開天闢地開天闢地");
        assertEquals(4, result.get("開天闢地"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithPrefixReturnsChengYuWithCount() {
        var result = chengYuController.createMap("高明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithSuffixReturnsChengYuWithCount() {
        var result = chengYuController.createMap("明辨是非明辨是非明辨是非高");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithDoublePrefixYuReturnsChengYuWithCount() {
        var result = chengYuController.createMap("高高明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithInBetweenReturnsChengYuWithCount() {
        var result = chengYuController.createMap("明辨是非高明辨是非高明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithDoublePrefixAndDoubleSuffixReturnsChengYuWithCount() {
        var result = chengYuController.createMap("高高明辨是非明辨是非明辨是非高高");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void twoTripleChengYuReturnsTwoChengYuCount() {
        var result = chengYuController.createMap("高山流水高山流水高山流水明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("高山流水"));
        assertEquals(3, result.get("明辨是非"));
        assertEquals(2, result.size());
    }

}
