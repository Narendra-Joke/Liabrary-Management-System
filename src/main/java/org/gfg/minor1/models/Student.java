package org.gfg.minor1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Student implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30)
    private String name;

    @Column(length = 30)
    private String address;

    @Column(nullable = false,unique = true)
    private String contact;

    @Column(length = 30,unique = true)
    private String email;

    // it will fetch from server time you don't need to require write new Date()
    @CreationTimestamp // this comes from hibernate
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

//    @JsonIgnore it ignores data on json

    @OneToMany(mappedBy = "student")
    private List<Book> booksList;

//    @Enumerated(value = EnumType.STRING)
    @Enumerated
    private StudentType studentType;

    @OneToOne
    @JoinColumn
    private User user;
}
