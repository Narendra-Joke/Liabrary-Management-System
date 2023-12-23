package org.gfg.minor1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30)
    private String name;
    private int cost;

    @ManyToOne
    @JoinColumn
//    @JsonIgnoreProperties("bookList")
    @JsonIgnore
    private Author author;

    private String bookNo; // just to keep a track which student took which book

    @Enumerated(value = EnumType.STRING) // it keeps the data in value type
    private BookType type;

    @JoinColumn // annotation is used to specify that the foreign key column for this relationship
    @ManyToOne
    @JsonIgnoreProperties("bookList")
    private Student student; // in the book table by default student_id stored.
}
