package se.hernebring.chengyu.service;

import org.springframework.stereotype.Service;
import se.hernebring.chengyu.model.Word;
import se.hernebring.chengyu.repository.WordRepository;

import java.util.HashSet;
import java.util.Map;
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

    public Integer secondOccurrence(int unit, int i, int first, int frequency) {
        if (frequency < 3) {
            if(first + unit > -frequency) {
                return -i;
            }
            if(-frequency + unit <= i) {
                repository.save(new Word(first, unit));
                repository.save(new Word(-frequency, unit));
                frequency = 3;
            } else
                return null;

        } else
            ++frequency;

        repository.save(new Word(i, unit));
        return frequency;
    }
}
