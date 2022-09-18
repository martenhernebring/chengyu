package se.hernebring.chengyu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Word {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private int length;

    public Word() {}

    public Word(long id, int length) {
        this.id = id;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public int getLength() {
        return length;
    }
}
