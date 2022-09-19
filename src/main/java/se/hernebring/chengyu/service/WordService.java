package se.hernebring.chengyu.service;

import org.springframework.stereotype.Service;
import se.hernebring.chengyu.dto.WordMeta;
import se.hernebring.chengyu.model.Word;
import se.hernebring.chengyu.repository.WordRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class WordService {

    private final WordRepository repository;

    public WordService(WordRepository repository) {
        this.repository = repository;
    }

    public Set<Integer> findAlreadyTaken() {
        var iterator = repository.findAll().iterator();
        Set<Integer> alreadyTaken = new HashSet<>();
        while(iterator.hasNext()) {
            var word = iterator.next();
            var id = word.getId();
            for(int i = 0; i < word.getLength(); i++)
                alreadyTaken.add((int) (id + i));

        }
        return alreadyTaken;
    }

    public Integer secondOccurrence(WordMeta wordMeta) {
        int unit = wordMeta.unit();
        int frequency = wordMeta.frequencyOrSecond();
        if (frequency < 3) {
            int first = wordMeta.first();
            if(first + unit > -frequency)
                return -wordMeta.latest();

            if(-frequency + unit <= wordMeta.latest()) {
                repository.save(new Word(first, unit));
                repository.save(new Word(-frequency, unit));
                frequency = 3;
            } else
                return null;

        } else
            ++frequency;

        repository.save(new Word(wordMeta.latest(), unit));
        return frequency;
    }
}
