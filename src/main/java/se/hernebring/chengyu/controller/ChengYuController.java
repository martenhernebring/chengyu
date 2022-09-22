package se.hernebring.chengyu.controller;

import org.springframework.stereotype.Controller;
import se.hernebring.chengyu.dto.WordDto;
import se.hernebring.chengyu.service.WordService;

import java.util.*;

@Controller
public class ChengYuController {

    private final WordService wordService;

    private final Map<String,Integer> counter = new LinkedHashMap<>();

    public ChengYuController(WordService articleService) {
        this.wordService = articleService;
    }

    public Map<String,Integer> createMap(String text) {
        int unit = 10;
        if(text.length() < unit)
            return counter;
        for(; unit > 3; unit--)
            count(text, unit);

        counter.entrySet().removeIf(entry -> entry.getValue() < 3);
        return counter;
    }

    void count(String text, int unit) {
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

            if(charsNotEqualTo(chars, exclude) &&
                    charsNotWhitespace(chars) &&
                    indexNot(additional, index)) {
                String chengYu = text.substring(index, index + unit);
                Integer first = firstTime.get(chengYu);
                if(first == null)
                    firstTime.put(chengYu, index);
                else {
                    WordDto wm = new WordDto(chengYu, unit, first, counter.get(chengYu), index);
                    secondOrMore(wm, additional);
                }
            }
        }
    }

    private void secondOrMore(WordDto wd, int[] additional) {
        if (wd.frequencyOrSecond() == null)
            counter.put(wd.chengYu(), -wd.latest());
        else {
            Integer second = wordService.secondOccurrence(wd);
            if(second != null)
                counter.put(wd.chengYu(), second);
            for (int j = 0; j < additional.length; j++)
                additional[j] = wd.latest() + j + 1;
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
