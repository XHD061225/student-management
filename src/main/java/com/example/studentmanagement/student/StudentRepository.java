package com.example.studentmanagement.student;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentNo(String studentNo);
    boolean existsByStudentNoAndIdNot(String studentNo, Long id);

    List<Student> findAllByOrderByIdDesc();

    List<Student> findByNameContainingIgnoreCaseOrStudentNoContainingIgnoreCaseOrClassNameContainingIgnoreCaseOrderByIdDesc(
            String name, String studentNo, String className);
}
