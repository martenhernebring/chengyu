package se.hernebring.chengyu.service;

import org.springframework.stereotype.Service;
import se.hernebring.chengyu.dto.WordDto;
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

    public Integer secondOccurrence(WordDto wordDto) {
        int unit = wordDto.unit();
        int frequency = wordDto.frequencyOrSecond();
        if (frequency < 3) {
            int first = wordDto.first();
            if(first + unit > -frequency)
                return -wordDto.latest();

            if(-frequency + unit <= wordDto.latest()) {
                repository.save(new Word(first, unit));
                repository.save(new Word(-frequency, unit));
                frequency = 3;
            } else
                return null;

        } else
            ++frequency;

        repository.save(new Word(wordDto.latest(), unit));
        return frequency;
    }
}
