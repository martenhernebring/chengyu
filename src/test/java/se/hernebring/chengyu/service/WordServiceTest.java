package se.hernebring.chengyu.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.hernebring.chengyu.dto.WordMeta;
import se.hernebring.chengyu.model.Word;
import se.hernebring.chengyu.repository.WordRepository;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @Mock
    private WordRepository repository;

    @InjectMocks
    private WordService service;

    @Test
    void sextupleThinkEveryoneCountsOnce() {
        when(repository.findAll()).thenReturn(List.of(
                new Word(0L, 10), new Word(10L, 10), new Word(20L, 10)
        ));
        service.findAlreadyTaken();
        verify(repository, times(1)).findAll();
    }

    @Test
    void secondOccurrenceReturnsNullWhen4charsAndFirstWas0AndSecondWas5AndThirdWas8() {
        WordMeta wm = new WordMeta(4, 0, -5, 8);
        var result = service.secondOccurrence(wm);
        assertNull(result);
    }

}
