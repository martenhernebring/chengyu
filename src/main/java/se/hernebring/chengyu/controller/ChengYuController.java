package se.hernebring.chengyu.controller;

import org.springframework.stereotype.Controller;
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
        for(int i = 0; i <= text.length() - unit; i++) {
            if(alreadyTaken.contains(i))
                continue;

            for(int j = 0; j<chars.length; j++)
                chars[j] = text.charAt(i+j);

            if(charsNotEqualTo(chars, exclude) && charsNotWhitespace(chars) && indexNot(additional, i)) {
                String chengYu = text.substring(i, i + unit);
                Integer index = firstTime.get(chengYu);
                if(index == null)
                    firstTime.put(chengYu, i);
                else {
                    Integer frequency = counter.get(chengYu);
                    if (frequency == null) {
                        counter.put(chengYu, -i);
                    } else {
                        int first = firstTime.get(chengYu);
                        Integer second = wordService.secondOccurrence(unit, i, first, frequency);
                        if(second != null)
                            counter.put(chengYu, second);
                        for (int j = 0; j < additional.length; j++)
                            additional[j] = i + j + 1;
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
