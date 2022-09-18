package se.hernebring.chengyu.service;

import org.apache.commons.io.IOUtils;
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

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChengYuServiceIT {

    @Autowired
    private ChengYuRepository repository;

    @Test
    void integration() throws IOException {
        ChengYuService chengYuService = new ChengYuService(repository);
        FileInputStream fis = new FileInputStream("src/test/resources/stringtoolong.txt");
        String stringTooLong = IOUtils.toString(fis, StandardCharsets.UTF_8);
        var result = chengYuService.createMap(stringTooLong);
        System.out.println("ChengYu,Count");
        ChengYuServiceIT.sortByValue((HashMap<String, Integer>) result)
                .forEach((key, value) -> System.out.println(key + "," + value));
        //var db = repository.findAll();
        //db.forEach(System.out::println);
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
