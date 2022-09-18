package se.hernebring.chengyu.service;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import se.hernebring.chengyu.repository.ChengYuRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChengYuServiceIT {

    @Autowired
    private ChengYuRepository repository;

    private ChengYuService chengYuService;

    @BeforeEach
    void init() {
        chengYuService = new ChengYuService(repository);
    }

    @Test
    void thinkEveryoneCountsOnce() {
        String thinkEveryone = "大家想一想大家想一想大家想一想";
        var result = chengYuService.createMap(thinkEveryone);
        assertEquals(1, result.size());
    }

    @Test
    void quadrupleDoubleChengYuAndTripleChengYuContainingStartingChar() {
        var result = chengYuService.createMap("畫龍點睛畫龍點睛畫蛇添足畫龍點睛畫蛇添足畫蛇添足畫龍點睛");
        assertEquals(4, result.get("畫龍點睛"));
        assertEquals(3, result.get("畫蛇添足"));
        assertEquals(2, result.size());
    }

    @Test
    void integration() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/stringtoolong.txt");
        String stringTooLong = IOUtils.toString(fis, StandardCharsets.UTF_8);
        var result = chengYuService.createMap(stringTooLong);
        System.out.println("ChengYu,Count");
        ChengYuServiceIT.sortByValue((HashMap<String, Integer>) result)
                .forEach((key, value) -> System.out.println(key + "," + value));
    }

    public static HashMap<String, Integer>
    sortByValue(HashMap<String, Integer> hm) {
        return hm.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

}
