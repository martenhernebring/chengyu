package se.hernebring.chengyu.engine;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChengYuEngineTest {

    private ChengYuEngine chengYuEngine;

    @BeforeEach
    void init() {
        chengYuEngine = new ChengYuEngine();
    }

    @Test
    void singleChengYuReturnEmpty() {
        var result = chengYuEngine.createMap("高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void doubleChengYuReturnsChengYuCount() {
        var result = chengYuEngine.createMap("高山流水高山流水");
        assertEquals(2, result.get("高山流水"));
        assertEquals(1, result.size());
    }

    @Test
    void differentDoubleChengYuReturnsDifferentChengYuWithCount() {
        var result = chengYuEngine.createMap("明辨是非明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void singleChengYusReturnEmpty() {
        var result = chengYuEngine.createMap("明辨是非高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void tripleChengYuReturnsChengYuCountWithoutFakes() {
        var result = chengYuEngine.createMap("開天闢地開天闢地開天闢地");
        assertEquals(3, result.get("開天闢地"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithPrefixReturnsChengYuWithCount() {
        var result = chengYuEngine.createMap("高明辨是非明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithSuffixReturnsChengYuWithCount() {
        var result = chengYuEngine.createMap("明辨是非明辨是非高");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithDoublePrefixYuReturnsChengYuWithCount() {
        var result = chengYuEngine.createMap("高高明辨是非明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithInBetweenReturnsChengYuWithCount() {
        var result = chengYuEngine.createMap("明辨是非高明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithDoublePrefixAndDoubleSuffixReturnsChengYuWithCount() {
        var result = chengYuEngine.createMap("高高明辨是非明辨是非高高");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void twoDoubleChengYuReturnsTwoChengYuCount() {
        var result = chengYuEngine.createMap("高山流水高山流水明辨是非明辨是非");
        assertEquals(2, result.get("高山流水"));
        assertEquals(2, result.get("明辨是非"));
        assertEquals(2, result.size());
    }

    @Test
    void tripleDoubleChengYuAndDoubleChengYuContainingStartingChar() {
        var result = chengYuEngine.createMap("畫龍點睛畫蛇添足畫龍點睛畫蛇添足畫龍點睛");
        assertEquals(3, result.get("畫龍點睛"));
        assertEquals(2, result.get("畫蛇添足"));
        assertEquals(2, result.size());
    }

    @Test
    void integration() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/stringtoolong.txt");
        String stringTooLong = IOUtils.toString(fis, StandardCharsets.UTF_8);
        var result = chengYuEngine.createMap(stringTooLong);
        System.out.println("ChengYu,Count");
        var sr = ChengYuEngineTest.sortByValue((HashMap<String, Integer>) result);
        sr.forEach((key, value) -> System.out.println(key + "," + value));
    }

    public static HashMap<String, Integer>
    sortByValue(HashMap<String, Integer> hm) {
        return hm.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
    }

}
