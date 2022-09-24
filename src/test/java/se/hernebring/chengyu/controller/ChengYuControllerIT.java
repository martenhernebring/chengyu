package se.hernebring.chengyu.controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import se.hernebring.chengyu.service.WordService;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChengYuControllerIT {

    private ChengYuController controller;

    @Autowired
    WordService service;

    @BeforeEach
    void init() {
        controller = new ChengYuController(service);
    }

    @Test
    void integrationFileDoesNotContainEmDashAndIsSortedByValue() throws IOException {
        //GIVEN
        FileInputStream fis = new FileInputStream("src/test/resources/stringtoolong.txt");
        String stringTooLong = IOUtils.toString(fis, StandardCharsets.UTF_8);
        //WHEN
        var result = controller.createMap(stringTooLong);
        result = ChengYuControllerIT.sortByValue((HashMap<String, Integer>) result);
        //THEN
        var it = result.keySet().iterator();
        if(it.hasNext()) {
            var first = it.next();
            assertTrue(first.length() < 6);
            assertTrue(first.indexOf('—') < 0);
        } else
            fail();

        while (it.hasNext())
            assertTrue(it.next().indexOf('—') < 0);

        printToFile(result);
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

    private void printToFile(Map<String, Integer> result) throws IOException {
        FileWriter out = new FileWriter("src/test/resources/zf.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(new String[] { "ChengYu", "Count"}))) {
            result.forEach((key, value) -> {
                try {
                    printer.printRecord(key, value);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
