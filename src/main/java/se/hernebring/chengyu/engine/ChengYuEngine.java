package se.hernebring.chengyu.engine;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class ChengYuEngine {
    private final static int ONE = 4;
    private final HashMap<String,Integer> counter = new LinkedHashMap<>();

    public Map<String,Integer> createMap(String text) {
        if(text.length() < ONE)
            return counter;
        count(text);
        counter.entrySet().removeIf(entry -> entry.getValue() < 2);
        remove(new String[]{"人類", "不同", "來說", "層次"});
        return counter;
    }

    private void remove(String[] toBeExcluded) {
        for (String word : toBeExcluded) {
            counter.entrySet().removeIf(
                    entry -> entry.getKey().contains(word));
        }
    }

    private void count(String text) {
        int[] additional = new int[ONE - 1];
        for(int ch: additional) {
            additional[ch] = -1;
        }
        char[] chars = new char[ONE];
        char[] exclude = {'、', '，', '。', '的', '是', '們', '有', '這', '在', '？', '：', '；'};
        for(int i = 0; i <= text.length() - ONE - 1; i ++) {
            chars[0] = text.charAt(i);
            chars[1] = text.charAt(i + 1);
            chars[2] = text.charAt(i + 2);
            chars[3] = text.charAt(i + 3);
            if(charsNotEqualTo(chars, exclude)
                    && (!Character.isWhitespace(chars[0])  & !Character.isWhitespace(chars[1]) &
                    !Character.isWhitespace(chars[2])  & !Character.isWhitespace(chars[3]))
                    && (i != additional[0] & i != additional[1] & i != additional[2])
            ) {
                String chengYu = text.substring(i, i + ONE);
                Integer value = counter.get(chengYu);
                if(value == null) {
                    counter.put(chengYu, -i);
                    continue;
                } else if(value <= 0)
                    counter.put(chengYu, 2);
                else
                    counter.put(chengYu, ++value);
                additional[0] = i + 1;
                additional[1] = i + 2;
                additional[2] = i + 3;
            }
        }
    }

    private boolean charsNotEqualTo(char[] chars1, char[] chars2) {
        boolean result = true;
        for(char i : chars1) {
            for(char j : chars2) {
                result = result & (i != j);
            }
        }
        return result;
    }
}
