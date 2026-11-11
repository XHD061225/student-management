package com.example.studentmanagement.student;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRecordRepository extends JpaRepository<AwardRecord, Long> {
    List<AwardRecord> findAllByOrderByAwardDateDesc();
    List<AwardRecord> findByStudentOrderByAwardDateDesc(Student student);
}
