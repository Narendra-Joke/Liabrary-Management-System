package org.gfg.minor1.service;

import org.gfg.minor1.models.*;
import org.gfg.minor1.repository.AuthorRepository;
import org.gfg.minor1.repository.BookRepository;
import org.gfg.minor1.request.CreateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String BOOK_PREFIX_KEY = "book:";

    public void create(CreateBookRequest createBookRequest){
        Book book = createBookRequest.to();
        // condition then we need to check whether we need to save it in our db or not

        // here u need to have some method which will tell you whether author present or not
        Author authorFromDB = authorRepository.findByEmail(book.getAuthor().getEmail());
        if(authorFromDB == null){
            authorFromDB = authorRepository.save(book.getAuthor());
        }
        List<Book> list = new ArrayList<>();
        book.setAuthor(authorFromDB);
        // we are saving data to the db
        book = bookRepository.save(book);
        // I will save the same data to redis
        pushDataToRedisByBookNo(book);
        list.add(book);
        pushDataToRedisByAuthorName(list);
        pushDataToRedisByCost(book);
        pushDataToRedisByType(book);
    }

    private void pushDataToRedisByBookNo(Book book){
        if(book != null){
            redisTemplate.opsForValue().set(BOOK_PREFIX_KEY+book.getBookNo(),book,10, TimeUnit.MINUTES);
        }
    }

    private void pushDataToRedisByAuthorName(List<Book> book){
        if(!book.isEmpty()){
            String authorName = book.get(0).getAuthor().getName();
            redisTemplate.opsForList().leftPush(BOOK_PREFIX_KEY + authorName,book);
            redisTemplate.expire(BOOK_PREFIX_KEY + authorName,10,TimeUnit.MINUTES);
        }
    }

    private void pushDataToRedisByCost(Book book){
            redisTemplate.opsForList().leftPush(BOOK_PREFIX_KEY+book.getCost(),book);
            redisTemplate.expire(BOOK_PREFIX_KEY+book.getCost(),10,TimeUnit.MINUTES);
    }

    private void pushDataToRedisByType(Book book){
            redisTemplate.opsForList().leftPush(BOOK_PREFIX_KEY+book.getType(),book);
            redisTemplate.expire(BOOK_PREFIX_KEY+book.getType(),10,TimeUnit.MINUTES);
    }

    public Book createUpdate(Book book){
        return bookRepository.save(book);
    }

    public List<Book> findBooks(BookFilterType bookFilterType, String value, OperationType operationType){
        switch (operationType){
            case EQUALS:
                switch (bookFilterType){
                    case BOOK_NO:
                        // before hitting directly to the database
                        // you need to check first at redis
                        Book bookWithPrefix = (Book) redisTemplate.opsForValue().get(BOOK_PREFIX_KEY+value);
                        if(bookWithPrefix != null){
                            List<Book> list = new ArrayList<>();
                            list.add(bookWithPrefix);
                            return list;
                        }
                        List<Book> bookList = bookRepository.findByBookNo(value);
                        // kept it in the redis cache
                        pushDataToRedisByBookNo(bookList != null  ? bookList.get(0) : null);
                        return bookList;
                    case AUTHOR_NAME:
                        List<Book> bookWithAuthorName = (List<Book>) redisTemplate.opsForList().range(BOOK_PREFIX_KEY+value, 0, -1);
                        if(!bookWithAuthorName.isEmpty()){
                            return bookWithAuthorName;
                        }
                        bookWithAuthorName = bookRepository.findByAuthor_Name(value);
                        // kept it in the redis cache
                        pushDataToRedisByAuthorName(!bookWithAuthorName.isEmpty() ? bookWithAuthorName : null);
                        return bookWithAuthorName;
                    case COST:
                        return bookRepository.findByCost(Integer.valueOf(value));
                    case BOOKTYPE:
                        return bookRepository.findByType(BookType.valueOf(value));
                }
            case LESS_THAN:
                switch(bookFilterType){
                    case COST:
                        return bookRepository.findByCostLessThan(Integer.valueOf(value));
                }
            case GREATER_THAN:
                switch (bookFilterType){
                    case COST:
                        return bookRepository.findByCostGreaterThan(Integer.valueOf(value));
                }
            default:
                return new ArrayList<>();
        }
    }
}
