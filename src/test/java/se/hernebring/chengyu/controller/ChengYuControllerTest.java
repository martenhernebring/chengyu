package se.hernebring.chengyu.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    void thinkEveryoneCountsOnce() {
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
    @Disabled
    void menIsOnlyAtTheEnd() {
        var result = controller.createMap("我們可以我們可以我們可以");
        assertTrue(result.isEmpty());
    }

    @Test
    @Disabled
    void ICanOnlyBe2ndToLast() {
        //從此以後, 就可以了, 從今以後, 走出世間法修煉以後, 都可以做, 是可以的 is ok
        //以為自己, 我們就可以, 都可以看的, 出世間法以, 有的人可以, 我們可以 is not ok
        //所以我們, 可以看到, 所以我們講, 所以這個, 所以往往, 所以大家, 所以有的人, 所以不能, 可以無條件的幫, 可以直接, 所以你不,
        // 可以給你, 所以就不, 可以產生, 所以這種, 所以他就, 所以一旦, 可以達到, 可以修煉, 所以大家千萬注意, 所以有些人,
        // 所以我們這,可以顯現出,, 所以我們要, 所以人的, 可以改變, 可以長功, 可以得到, 可以透過, 所以你看, 所以它能, 所以他的,
        // 可以延長, 所以他講, 所以他要 is not ok
    }

}
