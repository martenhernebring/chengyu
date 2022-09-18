package se.hernebring.chengyu.controller;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.hernebring.chengyu.service.WordService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class ChengYuControllerIT {

    private ChengYuController chengYuController;

    @Autowired
    WordService service;

    @BeforeEach
    void init() {
        chengYuController = new ChengYuController(service);
    }

    @Test
    void integration() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/stringtoolong.txt");
        String stringTooLong = IOUtils.toString(fis, StandardCharsets.UTF_8);
        var result = chengYuController.createMap(stringTooLong);
        System.out.println("ChengYu,Count");
        ChengYuControllerIT.sortByValue((HashMap<String, Integer>) result)
                .forEach((key, value) -> System.out.println(key + "," + value));
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        return hm.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
}
