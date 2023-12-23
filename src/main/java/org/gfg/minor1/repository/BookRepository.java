package org.gfg.minor1.repository;

import org.gfg.minor1.models.Book;
import org.gfg.minor1.models.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findByBookNo(String bookNo);
    List<Book> findByAuthor_Name(String authorName);

    List<Book> findByCost(Integer cost);

    List<Book> findByType(BookType type);

    List<Book> findByCostLessThan(Integer cost);

    List<Book> findByCostGreaterThan(Integer cost);


}
