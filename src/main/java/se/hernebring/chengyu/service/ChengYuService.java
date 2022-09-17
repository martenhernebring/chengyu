package se.hernebring.chengyu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.hernebring.chengyu.model.Word;
import se.hernebring.chengyu.repository.ChengYuRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ChengYuService {

    private ChengYuRepository repository;

    private final static int ONE = 10;
    private final HashMap<String,Integer> counter = new LinkedHashMap<>();

    public ChengYuService(ChengYuRepository repository) {
        this.repository = repository;
    }

    public Map<String,Integer> createMap(String text) {
        if(text.length() < ONE)
            return counter;
        count(text);
        counter.entrySet().removeIf(entry -> entry.getValue() < 3);
        //remove(new String[]{"人類", "不同", "來說", "層次"});
        return counter;
    }

//    private void remove(String[] toBeExcluded) {
//        for (String word : toBeExcluded) {
//            counter.entrySet().removeIf(
//                    entry -> entry.getKey().contains(word));
//        }
//    }

    private void count(String text) {
        int[] additional = new int[ONE - 1];
        Arrays.fill(additional, -1);
        char[] chars = new char[ONE];
        //,'是', '要','到','們', '一', '這', '高', '之', '走', '有', '個', '家', '何', '就','的'
        char[] exclude = {'、', '，', '。', '《','》','（','）','「','」','？', '：', '；'};
        final HashMap<String,Integer> firstTime = new HashMap<>();
        for(int i = 0; i < text.length() - ONE - 1; i++) {
            for(int j = 0; j<chars.length; j++)
                chars[j] = text.charAt(i+j);

            if(charsNotEqualTo(chars, exclude) && charsNotWhitespace(chars) && indexNot(additional, i)) {
                String chengYu = text.substring(i, i + ONE);
                Integer index = firstTime.get(chengYu);
                if(index == null) {
                    firstTime.put(chengYu, i);
                    continue;
                } else {
                    if (counter.get(chengYu) == null)
                        counter.put(chengYu, -i);
                    else {
                        Integer frequency = counter.get(chengYu);
                        if (frequency < 3) {
                            repository.save(new Word(firstTime.get(chengYu), ONE));
                            repository.save(new Word(-counter.get(chengYu), ONE));
                            counter.put(chengYu, 3);
                        }
                        else
                            counter.put(chengYu, ++frequency);

                        repository.save(new Word(i, ONE));
                    }
                    for (int j = 0; j < additional.length; j++) {
                        additional[j] = i + j + 1;
                    }
                }
            }
        }
    }

    private boolean indexNot(int[] additional, int i) {
        boolean result = true;
        for(int a : additional) {
            result = result & (i != a);
        }
        return result;
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

    private boolean charsNotWhitespace(char[] chars) {
        boolean result = true;
        for (char aChar : chars) {
            result = result & (!Character.isWhitespace(aChar));
        }
        return result;
    }
}
