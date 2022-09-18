package se.hernebring.chengyu.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.hernebring.chengyu.model.Word;
import se.hernebring.chengyu.repository.ChengYuRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChengYuServiceTest {

    @Mock
    private ChengYuRepository repository;

    @InjectMocks
    private ChengYuService chengYuService;

    @Test
    void singleChengYuReturnEmpty() {
        var result = chengYuService.createMap("高山流水");
        assertTrue(result.isEmpty());
    }

    @Test
    void tripleChengYuReturnsChengYuCount() {
        var result = chengYuService.createMap("高山流水高山流水高山流水");
        assertEquals(3, result.get("高山流水"));
        assertEquals(1, result.size());
    }

    @Test
    void differentTripleChengYuReturnsDifferentChengYuWithCount() {
        var result = chengYuService.createMap("明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void doubleChengYusReturnEmpty() {
        var result = chengYuService.createMap("明辨是非高山流水明辨是非");
        assertTrue(result.isEmpty());
    }

    @Test
    void quadrupleChengYuReturnsChengYuCountWithoutFakes() {
        var result = chengYuService.createMap("開天闢地開天闢地開天闢地開天闢地");
        assertEquals(4, result.get("開天闢地"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithPrefixReturnsChengYuWithCount() {
        var result = chengYuService.createMap("高明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithSuffixReturnsChengYuWithCount() {
        var result = chengYuService.createMap("明辨是非明辨是非明辨是非高");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithDoublePrefixYuReturnsChengYuWithCount() {
        var result = chengYuService.createMap("高高明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithInBetweenReturnsChengYuWithCount() {
        var result = chengYuService.createMap("明辨是非高明辨是非高明辨是非");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void tripleChengYuWithDoublePrefixAndDoubleSuffixReturnsChengYuWithCount() {
        var result = chengYuService.createMap("高高明辨是非明辨是非明辨是非高高");
        assertEquals(3, result.get("明辨是非"));
        assertEquals(1, result.size());
    }

    @Test
    void twoTripleChengYuReturnsTwoChengYuCount() {
        var result = chengYuService.createMap("高山流水高山流水高山流水明辨是非明辨是非明辨是非");
        assertEquals(3, result.get("高山流水"));
        assertEquals(3, result.get("明辨是非"));
        assertEquals(2, result.size());
    }

    @Test
    void sextupleThinkEveryoneCountsOnce() {
        String thinkEveryone = "大家想一想大家想一想大家想一想大家想一想大家想一想大家想一想";
        when(repository.findAll()).thenReturn(List.of(
                new Word(0L, 10), new Word(10L, 10), new Word(20L, 10)
        ));
        chengYuService.count(thinkEveryone, 5);
        verify(repository, times(1)).findAll();
    }

}
