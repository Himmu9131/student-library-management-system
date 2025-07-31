package com.demo.example.student_library_management_system.repository;

import com.demo.example.student_library_management_system.dto.RecentActivityDto;
import com.demo.example.student_library_management_system.model.Book;
import com.demo.example.student_library_management_system.model.Card;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.Transaction;
import com.demo.example.student_library_management_system.response.TransactionResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT t.* FROM `transaction` t " +
            "JOIN book b ON t.book_id = b.id " +
            "WHERE DATEDIFF(CURDATE(), t.transaction_date) > 0 " +
            "AND t.is_issue_operation = 1 " +
            "AND b.issued_to_student = 1", nativeQuery = true)
    List<Transaction> findOverdueTransactions();

    @Query("SELECT new com.demo.example.student_library_management_system.response.TransactionResponseDto(" +
            "t.id, t.book.name, t.card.student.name, t.card.id, " +
            "t.isIssueOperation, t.transactionStatus, t.fine, t.transactionDate) " +
            "FROM Transaction t")
    List<TransactionResponseDto> findAllTransactions();

//    @Query("SELECT new com.demo.example.student_library_management_system.response.RecentActivityDto(" +
//            "t.card.student.name, " +
//            "t.book.name, " +
//            "t.transactionStatus.name(), " +
//            "t.transactionDate) " +
//            "FROM Transaction t ORDER BY t.transactionDate DESC")
//    List<RecentActivityDto> findRecentActivities(Pageable pageable);

    @Query("SELECT t.book.name " +
            "FROM Transaction t " +
            "WHERE t.isIssueOperation = true " +
            "GROUP BY t.book.name " +
            "ORDER BY COUNT(t.id) DESC")
    List<String> findMostPopularBooks(Pageable pageable);


    Optional<Transaction> findTopByBookAndCardAndIsIssueOperationOrderByTransactionDateDesc(Book book, Card card, boolean b);

    List<Transaction> findByCard_Student_IdAndIsIssueOperationOrderByTransactionDateDesc(int studentId, boolean b);

    @Query("SELECT COALESCE(SUM(t.fine), 0) FROM Transaction t WHERE t.card.student.id = :studentId")
    double getTotalFineByStudentId(@Param("studentId") int studentId);

    @Query("SELECT COUNT(t) FROM Transaction t " +
            "WHERE t.card.student.id = :studentId " +
            "AND t.isIssueOperation = true " +
            "AND t.fine > 0")
    int countOverdueByStudentId(@Param("studentId") int studentId);

//    @Query("SELECT new com.demo.example.student_library_management_system.dto.RecentActivityDto(" +
//            "t.id, t.transactionDate, t.transactionStatus, b.name) " +
//            "FROM Transaction t JOIN t.book b " +
//            "WHERE t.card.student.id = :studentId " +
//            "ORDER BY t.transactionDate DESC")
//    List<RecentActivityDto> getRecentActivitiesByStudentId(@Param("studentId") int studentId);

    @Query("SELECT new com.demo.example.student_library_management_system.dto.RecentActivityDto(" +
            "t.card.student.name, t.book.name, t.isIssueOperation) " +
            "FROM Transaction t ORDER BY t.transactionDate DESC")
    List<RecentActivityDto> findRecentActivities(Pageable pageable);

    @Query(value = "SELECT COUNT(DISTINCT c.student_id) FROM transaction t JOIN card c ON t.card_id = c.id WHERE t.is_issue_operation = true", nativeQuery = true)
    int totalBorrower();


//    @Query(value = "select * from student s join card c on s.student_id=c.id join transaction t on c.card_id=t.id where " +
//            "t.transaction_date between "" and ""; ",nativeQuery = true)
//     Optional<Student> getTotalStudentsBasedOnRange();
}
