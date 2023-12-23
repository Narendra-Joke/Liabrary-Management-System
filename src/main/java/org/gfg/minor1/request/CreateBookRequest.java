package org.gfg.minor1.request;
// CreateBookRequest dto --> data transfer object

import lombok.*;
import org.gfg.minor1.models.Author;
import org.gfg.minor1.models.Book;
import org.gfg.minor1.models.BookType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateBookRequest {
    private String bookName;
    private int cost;
    private BookType type;

    private String bookNo;

    private String authorName;

    private String authorEmail;

    public Book to(){
        Author authorData = Author.builder().
                name(this.authorName).
                email(this.authorEmail).
                build();

        return Book.builder().
                bookNo(this.bookNo).
                name(this.bookName).
                type(this.type).
                cost(this.cost).
                author(authorData).
                build();
    }
}
