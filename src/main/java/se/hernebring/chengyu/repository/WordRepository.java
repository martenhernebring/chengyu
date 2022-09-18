package se.hernebring.chengyu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.hernebring.chengyu.model.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
}
