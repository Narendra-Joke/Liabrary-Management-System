package org.gfg.minor1.service;

import org.gfg.minor1.exception.TxnServiceException;
import org.gfg.minor1.models.*;
import org.gfg.minor1.repository.TxnRepository;
import org.gfg.minor1.request.CreateReturnTxnRequest;
import org.gfg.minor1.request.CreateTxnRequest;
import org.gfg.minor1.response.TxnSettlementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TxnService {
    @Autowired
    private StudentService studentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TxnRepository txnRepository;

    // @Value annotation is used fetch the anything value from application.properties
    @Value("${student.valid.days}")
    private String validDays;

    @Value("${student.perday.fine}")
    private Integer finePerDay;

    // it makes application slow. so we need to create another method for both save queries
    @Transactional
    public String create(CreateTxnRequest createTxnRequest) throws TxnServiceException{
        /*
            1. check if student present or not
            2. book is available or not
            3. create a transaction
            4. we need to make now that book is unavailable
         */
       Student student = findStudent(createTxnRequest.getStudentContact());
       Book book = findBook(createTxnRequest.getBookNo());

        Txn txn = Txn.builder()
                .student(student)
                .book(book)
                .txnId(UUID.randomUUID().toString())
                .paidCost(createTxnRequest.getPaidAmount())
                .txnStatus(TxnStatus.ISSUED)
                .build();

        book.setStudent(student);
        // saving the data book to book table. 1) Query
        bookService.createUpdate(book); // updating book table

        // trying to save the data inside the txn 2) Query
        return txnRepository.save(txn).getTxnId();
    }

    public Student findStudent(String studentContact) throws TxnServiceException{
        List<Student> studentList = studentService.findStudent(StudentFilterType.CONTACT,
                studentContact,
                OperationType.EQUALS);
        if(studentList == null || studentList.isEmpty()){
            throw new TxnServiceException("Student doesn't exist in the library");
        }
        return studentList.get(0);
    }

    public Book findBook(String bookNo) throws TxnServiceException{
        List<Book> bookList = bookService.findBooks(BookFilterType.BOOK_NO,
                bookNo,
                OperationType.EQUALS);

        if(bookList == null || bookList.isEmpty() || bookList.get(0).getStudent() != null){
            throw new TxnServiceException("Book doesn't exist in the library");
        }
        return bookList.get(0);
    }

    public Book findBookForReturn(String bookNo) throws TxnServiceException{
        List<Book> bookList = bookService.findBooks(BookFilterType.BOOK_NO,
                bookNo,
                OperationType.EQUALS);

        if(bookList == null || bookList.isEmpty()){
            throw new TxnServiceException("Book doesn't exist in the library");
        }
        return bookList.get(0);
    }

    @Transactional
    public TxnSettlementResponse returnBook(CreateReturnTxnRequest createReturnTxnRequest) throws TxnServiceException{
        Student student = findStudent(createReturnTxnRequest.getStudentContact());
        Book book = findBookForReturn(createReturnTxnRequest.getBookNo());

        Txn txn = txnRepository.findByStudent_ContactAndBook_BookNoAndTxnStatusOrderByCreatedOnDesc(
                student.getContact(),
                book.getBookNo(),
                TxnStatus.ISSUED
        );

        // new entry in txn table
        // object what was the amount and was it returned or fined

        // if settlement amount < txn.paidCost()  --> fined case
        // update the table txn
        int settlementAmount = calculateSettlementAmount(txn);
        txn.setPaidCost(settlementAmount);
        txn.setTxnStatus(settlementAmount == txn.getPaidCost() ? TxnStatus.RETURNED : TxnStatus.FINED);
        txnRepository.save(txn);

        // now after txn book made available to the other user
        book.setStudent(null);
        bookService.createUpdate(book);

        return TxnSettlementResponse.builder().
                txnId(txn.getTxnId()).
                settlementAmount(settlementAmount).
                build();
    }

    public int calculateSettlementAmount(Txn txn){
        // I can convert both dates into milliseconds
        // check the difference
        // convert it back to days

        long issueTime = txn.getCreatedOn().getTime();
        long returnTime = System.currentTimeMillis();

        long diff = returnTime-issueTime;
        int daysPassed = (int) TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);

        if(daysPassed > Integer.valueOf(validDays)){
            int amount = ((daysPassed-Integer.valueOf(validDays)) * finePerDay);
            return txn.getPaidCost()-amount;
        }
        return txn.getPaidCost();
    }

    // how can i update txn table from code
}
