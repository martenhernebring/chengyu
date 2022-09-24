package se.hernebring.chengyu.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import se.hernebring.chengyu.service.WordService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChengYuControllerTest {

    private ChengYuController controller;

    @Autowired
    WordService service;

    @BeforeEach
    void init() {
        controller = new ChengYuController(service);
    }

    @Test
    void betweenPeopleCountsOnce() {
        String thinkEveryone = "人與人之間人與人之間人與人之間";
        var result = controller.createMap(thinkEveryone);
        assertEquals(3, result.get("人與人之間"));
        assertEquals(1, result.size());
    }

    @Test
    void quadrupleDoubleChengYuAndTripleChengYuContainingStartingChar() {
        var result = controller.createMap("畫龍點睛畫龍點睛畫蛇添足畫龍點睛畫蛇添足畫蛇添足畫龍點睛");
        assertEquals(4, result.get("畫龍點睛"));
        assertEquals(3, result.get("畫蛇添足"));
        assertEquals(2, result.size());
    }

    @Test
    void singleChengYuReturnEmpty() {
        var result = controller.createMap("高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void tripleChengYuReturnsChengYuCount() {
        var result = controller.createMap("高山流水高山流水高山流水");
        assertEquals(3, result.get("高山流水"));
        assertEquals(1, result.size());
    }

    @Test
    void differentTripleChengYuReturnsDifferentChengYuWithCount() {
        var result = controller.createMap("明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYusReturnEmpty() {
        var result = controller.createMap("明辨是非高山流水明辨是非");
        assertTrue(result.isEmpty());
    }

    @Test
    void quadrupleChengYuReturnsChengYuCountWithoutFakes() {
        var result = controller.createMap("開天闢地開天闢地開天闢地開天闢地");
        assertEquals(4, result.get("開天闢地"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithPrefixReturnsChengYuWithCount() {
        var result = controller.createMap("高明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithSuffixReturnsChengYuWithCount() {
        var result = controller.createMap("明辨是非明辨是非明辨是非高");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithDoublePrefixYuReturnsChengYuWithCount() {
        var result = controller.createMap("高高明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithInBetweenReturnsChengYuWithCount() {
        var result = controller.createMap("明辨是非高明辨是非高明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithDoublePrefixAndDoubleSuffixReturnsChengYuWithCount() {
        var result = controller.createMap("高高明辨是非明辨是非明辨是非高高");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void twoTripleChengYuReturnsTwoChengYuCount() {
        var result = controller.createMap("高山流水高山流水高山流水明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("高山流水"));
        assertEquals(3, result.get("明辨是非"));
        assertEquals(2, result.size());
    }

    @Test
    void dongXiShouldNotAppear() {
        var result = controller.createMap("這些東西這些東西這些東西");
        assertTrue(result.isEmpty());
    }

    @Test
    void menIsOnlyAtTheEnd() {
        var result = controller.createMap("我們可以大覺者們大覺者們我們可以大覺者們我們可以");
        assertEquals(3, result.get("大覺者們"));
        assertEquals(1, result.size());
    }

    @Test
    void iCanOnlyBe2ndToLast() {
        var result = controller.createMap("從今以後所以我們所以我們從今以後所以我們從今以後");
        assertEquals(3, result.get("從今以後"));
        assertEquals(1, result.size());
    }

}
