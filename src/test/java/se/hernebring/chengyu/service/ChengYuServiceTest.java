package se.hernebring.chengyu.service;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.hernebring.chengyu.repository.ChengYuRepository;

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
class ChengYuServiceTest {

    private ChengYuService chengYuService;

    @Autowired
    ChengYuRepository repository;

    @BeforeEach
    void init() {
        chengYuService = new ChengYuService(repository);
    }

    @Test
    void singleChengYuReturnEmpty() {
        var result = chengYuService.createMap("高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void doubleChengYuReturnsChengYuCount() {
        var result = chengYuService.createMap("高山流水高山流水");
        assertEquals(2, result.get("高山流水"));
        assertEquals(1, result.size());
    }

    @Test
    void differentDoubleChengYuReturnsDifferentChengYuWithCount() {
        var result = chengYuService.createMap("明辨是非明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void singleChengYusReturnEmpty() {
        var result = chengYuService.createMap("明辨是非高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void tripleChengYuReturnsChengYuCountWithoutFakes() {
        var result = chengYuService.createMap("開天闢地開天闢地開天闢地");
        assertEquals(3, result.get("開天闢地"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithPrefixReturnsChengYuWithCount() {
        var result = chengYuService.createMap("高明辨是非明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithSuffixReturnsChengYuWithCount() {
        var result = chengYuService.createMap("明辨是非明辨是非高");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithDoublePrefixYuReturnsChengYuWithCount() {
        var result = chengYuService.createMap("高高明辨是非明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithInBetweenReturnsChengYuWithCount() {
        var result = chengYuService.createMap("明辨是非高明辨是非");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYuWithDoublePrefixAndDoubleSuffixReturnsChengYuWithCount() {
        var result = chengYuService.createMap("高高明辨是非明辨是非高高");
        assertEquals(2, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void twoDoubleChengYuReturnsTwoChengYuCount() {
        var result = chengYuService.createMap("高山流水高山流水明辨是非明辨是非");
        assertEquals(2, result.get("高山流水"));
        assertEquals(2, result.get("明辨是非"));
        assertEquals(2, result.size());
    }

    @Test
    void tripleDoubleChengYuAndDoubleChengYuContainingStartingChar() {
        var result = chengYuService.createMap("畫龍點睛畫蛇添足畫龍點睛畫蛇添足畫龍點睛");
        assertEquals(3, result.get("畫龍點睛"));
        assertEquals(2, result.get("畫蛇添足"));
        assertEquals(2, result.size());
    }

    @Test
    void integration() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/stringtoolong.txt");
        String stringTooLong = IOUtils.toString(fis, StandardCharsets.UTF_8);
        var result = chengYuService.createMap(stringTooLong);
        System.out.println("ChengYu,Count");
        var sr = ChengYuServiceTest.sortByValue((HashMap<String, Integer>) result);
        sr.forEach((key, value) -> System.out.println(key + "," + value));
        var db = repository.findAll();
        db.forEach(System.out::println);
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
