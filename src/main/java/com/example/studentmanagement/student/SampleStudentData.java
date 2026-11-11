package com.example.studentmanagement.student;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Adds fictional, clearly labeled demo students and grades to a fresh local database. */
@Component
public class SampleStudentData implements CommandLineRunner {
    private final StudentRepository students;
    private final GradeRecordRepository grades;

    public SampleStudentData(StudentRepository students, GradeRecordRepository grades) {
        this.students = students;
        this.grades = grades;
    }

    @Override
    public void run(String... args) {
        List<StudentSample> samples = List.of(
                new StudentSample("DEMO2026001", "小李", "男", "计算机 1 班", "软件工程", "数学", "88.50"),
                new StudentSample("DEMO2026002", "小王", "女", "计算机 1 班", "计算机科学与技术", "数学", "92.00"),
                new StudentSample("DEMO2026003", "小张", "男", "计算机 2 班", "数据科学与大数据技术", "Java 程序设计", "85.00"),
                new StudentSample("DEMO2026004", "小刘", "女", "计算机 2 班", "网络工程", "Java 程序设计", "95.50"),
                new StudentSample("DEMO2026005", "小陈", "男", "软件工程 1 班", "软件工程", "数据库原理", "79.00"),
                new StudentSample("DEMO2026006", "小赵", "女", "软件工程 1 班", "人工智能", "数据库原理", "90.00")
        );

        for (StudentSample sample : samples) {
            Student student = students.findByStudentNo(sample.studentNo())
                    .orElseGet(() -> students.save(createStudent(sample)));

            if (!grades.existsByStudentAndSubjectAndTerm(student, sample.subject(), "2026-2027 第 1 学期")) {
                GradeRecord grade = new GradeRecord();
                grade.setStudent(student);
                grade.setSubject(sample.subject());
                grade.setTerm("2026-2027 第 1 学期");
                grade.setScore(new BigDecimal(sample.score()));
                grade.setCredit(new BigDecimal("3.0"));
                grade.setRemark("示例成绩，仅供演示");
                grades.save(grade);
            }
        }
    }

    private Student createStudent(StudentSample sample) {
        Student student = new Student();
        student.setStudentNo(sample.studentNo());
        student.setName(sample.name());
        student.setGender(sample.gender());
        student.setClassName(sample.className());
        student.setMajor(sample.major());
        return student;
    }

    private record StudentSample(String studentNo, String name, String gender, String className,
                                 String major, String subject, String score) { }
}
