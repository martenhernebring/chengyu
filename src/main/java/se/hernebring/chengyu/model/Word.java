package se.hernebring.chengyu.model;

import jakarta.persistence.*;

import java.util.List;

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

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", length=" + length +
                '}';
    }

    public Long getId() {
        return id;
    }

    public int getLength() {
        return length;
    }
}
