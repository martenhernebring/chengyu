package se.hernebring.chengyu.controller;

import org.springframework.stereotype.Controller;
import se.hernebring.chengyu.dto.WordMeta;
import se.hernebring.chengyu.service.WordService;

import java.util.*;

@Controller
public class ChengYuController {

    final WordService wordService;

    public ChengYuController(WordService articleService) {
        this.wordService = articleService;
    }

    public Map<String,Integer> createMap(String text) {
        int unit = 10;
        final Map<String,Integer> counter = new LinkedHashMap<>();
        if(text.length() < unit)
            return counter;
        for(; unit > 3; unit--)
            count(text, unit, counter);

        counter.entrySet().removeIf(entry -> entry.getValue() < 3);
        return counter;
    }

    void count(String text, int unit, Map<String,Integer> counter) {
        int[] additional = new int[unit - 1];
        Arrays.fill(additional, -1);
        char[] chars = new char[unit];
        char[] exclude = {'、', '，', '。', '《','》','（','）','「','」','？', '：', '；', '…', '！'};
        final HashMap<String,Integer> firstTime = new HashMap<>();
        Set<Integer> alreadyTaken = wordService.findAlreadyTaken();
        for(int index = 0; index <= text.length() - unit; index++) {
            if(alreadyTaken.contains(index))
                continue;

            for(int j = 0; j<chars.length; j++)
                chars[j] = text.charAt(index+j);

            if(charsNotEqualTo(chars, exclude) && charsNotWhitespace(chars) && indexNot(additional, index)) {
                String chengYu = text.substring(index, index + unit);
                Integer first = firstTime.get(chengYu);
                if(first == null)
                    firstTime.put(chengYu, index);
                else {
                    Integer frequency = counter.get(chengYu);
                    if (frequency == null) {
                        counter.put(chengYu, -index);
                    } else {
                        WordMeta wm = new WordMeta(unit, first, frequency, index);
                        Integer second = wordService.secondOccurrence(wm);
                        if(second != null)
                            counter.put(chengYu, second);
                        for (int j = 0; j < additional.length; j++)
                            additional[j] = index + j + 1;
                    }

                }
            }
        }
    }

    private boolean indexNot(int[] additional, int i) {
        boolean result = true;
        for(int a : additional)
            result = result & (i != a);

        return result;
    }

    private boolean charsNotEqualTo(char[] chars1, char[] chars2) {
        boolean result = true;
        for(char i : chars1) {
            for(char j : chars2)
                result = result & (i != j);

        }
        return result;
    }

    private boolean charsNotWhitespace(char[] chars) {
        boolean result = true;
        for (char aChar : chars)
            result = result & (!Character.isWhitespace(aChar));

        return result;
    }
}
