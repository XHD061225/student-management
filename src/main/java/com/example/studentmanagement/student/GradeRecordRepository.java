package com.example.studentmanagement.student;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRecordRepository extends JpaRepository<GradeRecord, Long> {
    List<GradeRecord> findAllByOrderByTermDescSubjectAsc();
    List<GradeRecord> findByStudentOrderByTermDescSubjectAsc(Student student);
    boolean existsByStudentAndSubjectAndTerm(Student student, String subject, String term);
}
