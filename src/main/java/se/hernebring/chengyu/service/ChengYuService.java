package se.hernebring.chengyu.service;

import org.springframework.stereotype.Service;
import se.hernebring.chengyu.model.Word;
import se.hernebring.chengyu.repository.ChengYuRepository;

import java.util.*;

@Service
public class ChengYuService {

    private ChengYuRepository repository;

    private static int unit;
    private final HashMap<String,Integer> counter = new LinkedHashMap<>();

    public ChengYuService(ChengYuRepository repository) {
        this.repository = repository;
    }

    public Map<String,Integer> createMap(String text) {
        unit = 10;
        if(text.length() < unit)
            return counter;
        for(; unit > 3; unit--)
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
        var iterator = repository.findAll().iterator();
        Set<Integer> alreadyTaken = new HashSet<>();
        while(iterator.hasNext()) {
            var word = iterator.next();
            var id = word.getId();
            for(int i = 0; i < word.getLength(); i++)
                alreadyTaken.add((int) (id + i));

        }
        int[] additional = new int[unit - 1];
        Arrays.fill(additional, -1);
        char[] chars = new char[unit];
        char[] exclude = {'、', '，', '。', '《','》','（','）','「','」','？', '：', '；', '…', '！'};
        final HashMap<String,Integer> firstTime = new HashMap<>();
        for(int i = 0; i <= text.length() - unit; i++) {
            if(alreadyTaken.contains(i))
                continue;

            for(int j = 0; j<chars.length; j++)
                chars[j] = text.charAt(i+j);

            if(charsNotEqualTo(chars, exclude) && charsNotWhitespace(chars) && indexNot(additional, i)) {
                String chengYu = text.substring(i, i + unit);
                Integer index = firstTime.get(chengYu);
                if(index == null) {
                    firstTime.put(chengYu, i);
                    continue;
                } else {
                    Integer frequency = counter.get(chengYu);
                    if (frequency == null)
                        counter.put(chengYu, -i);
                    else if (frequency < 3) {
                        repository.save(new Word(firstTime.get(chengYu), unit));
                        repository.save(new Word(-frequency, unit));
                        counter.put(chengYu, 3);
                        repository.save(new Word(i, unit));
                    } else {
                        counter.put(chengYu, ++frequency);
                        repository.save(new Word(i, unit));
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
