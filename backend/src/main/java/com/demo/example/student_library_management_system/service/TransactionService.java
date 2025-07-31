package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.converters.TransactionConverter;
import com.demo.example.student_library_management_system.dto.BorrowedBookDto;
import com.demo.example.student_library_management_system.dto.RecentActivityDto;
import com.demo.example.student_library_management_system.dto.TransactionRequestDto;
import com.demo.example.student_library_management_system.enums.TransactionStatus;
import com.demo.example.student_library_management_system.model.Book;
import com.demo.example.student_library_management_system.model.Card;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.Transaction;
import com.demo.example.student_library_management_system.repository.BookRepository;
import com.demo.example.student_library_management_system.repository.CardRepository;
import com.demo.example.student_library_management_system.repository.TransactionRepository;
import com.demo.example.student_library_management_system.response.TransactionResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final int MAX_ALLOWED_DAYS = 5;
    private static final double FINE_PER_DAY = 2.0;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RecentActivityService recentActivityService;

    public String addTransaction(TransactionRequestDto transactionRequestDto){
        Transaction transaction = TransactionConverter.convertTransactionRequestDtoToTransaction(transactionRequestDto);

        Book book = bookRepository.findById(transactionRequestDto.getBookId()).get();
        transaction.setBook(book);

        Card card = cardRepository.findById(transactionRequestDto.getCardId()).get();
        transaction.setCard(card);

        transactionRepository.save(transaction);
        return "Transaction saved successfully";
    }

    public List<TransactionResponseDto> getAll() {

            List<TransactionResponseDto> transactionList = transactionRepository.findAllTransactions();
            if(transactionList.isEmpty()){
                throw new RuntimeException("Transactions are not present");
            }
            return transactionList;

    }

    public List<Transaction> overDueTransactions() {
        List<Transaction> overdueTransactions=transactionRepository.findOverdueTransactions();
        return overdueTransactions;
    }



    public Transaction issueBook(int bookId, int cardId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        Card card = cardRepository.findById(cardId).orElseThrow();

        if (book.getCopies() <= 0 && card.getBooksIssuedToCard().contains(book)) {
            throw new RuntimeException("No copies available to issue.");
        }

        card.getBooksIssuedToCard().add(book);
        book.setCopies(book.getCopies() - 1); // ✅ reduce copy
        book.setIssuedToStudent(true);
        book.setCard(card);
        bookRepository.save(book);

        Transaction transaction = Transaction.builder()
                .book(book)
                .card(card)
                .isIssueOperation(true)
                .transactionDate(new Date())
                .fine(0.0)
                .transactionStatus(TransactionStatus.ISSUED)
                .build();

        return transactionRepository.save(transaction);
    }

    public Transaction returnBook(int bookId, int cardId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        Card card = cardRepository.findById(cardId).orElseThrow();

        Transaction lastIssue = transactionRepository
                .findTopByBookAndCardAndIsIssueOperationOrderByTransactionDateDesc(book, card, true)
                .orElseThrow(() -> new RuntimeException("No previous issue transaction found"));

        long diff = new Date().getTime() - lastIssue.getTransactionDate().getTime();
        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        double fine = days > MAX_ALLOWED_DAYS ? (days - MAX_ALLOWED_DAYS) * FINE_PER_DAY : 0.0;

        book.setCopies(book.getCopies() + 1); // ✅ return = +1
        book.setIssuedToStudent(false);
        bookRepository.save(book);

        Transaction returnTransaction = Transaction.builder()
                .book(book)
                .card(card)
                .isIssueOperation(false)
                .transactionDate(new Date())
                .fine(fine)
                .transactionStatus(TransactionStatus.RETURNED)
                .build();

        return transactionRepository.save(returnTransaction);
    }

    public List<String> findMostPopularBooks(){
        Pageable top3 = PageRequest.of(0, 4);
            return transactionRepository.findMostPopularBooks(top3);


    }

    public List<BorrowedBookDto> getBorrowedBooksByStudentId(int studentId) {
        List<Transaction> transactions = transactionRepository
                .findByCard_Student_IdAndIsIssueOperationOrderByTransactionDateDesc(studentId, true);

        List<BorrowedBookDto> dtoList = new ArrayList<>();
        for (Transaction t : transactions) {
            String bookTitle = t.getBook().getName();
            Date issuedDate = t.getTransactionDate();

            // Assuming MAX_DAYS is defined (e.g., 15)
            int MAX_DAYS = 15;
            Date dueDate = new Date(issuedDate.getTime() + MAX_DAYS * 24L * 60 * 60 * 1000);
            boolean isOverdue = new Date().after(dueDate);

            dtoList.add(new BorrowedBookDto(bookTitle, issuedDate, dueDate, isOverdue));
        }
        return dtoList;
    }


    public int countOverdueByStudentId(int id) {
        return transactionRepository.countOverdueByStudentId(id);
    }


    public double getTotalFineByStudentId(int studentId) {
        Double fine = transactionRepository.getTotalFineByStudentId(studentId);
        double totalFine = (fine >0) ? fine : 0.0;
        return totalFine;
    }

    public int totalBorrower() {
        System.out.println("📘 Total Borrowers: " + transactionRepository.totalBorrower());
        return transactionRepository.totalBorrower();
    }


}
