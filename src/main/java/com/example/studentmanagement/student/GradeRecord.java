package com.example.studentmanagement.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "grade_records")
public class GradeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "请输入科目名称")
    @Size(max = 100, message = "科目名称不能超过 100 个字符")
    @Column(nullable = false, length = 100)
    private String subject;

    @NotBlank(message = "请输入学期")
    @Size(max = 20, message = "学期不能超过 20 个字符")
    @Column(nullable = false, length = 20)
    private String term;

    @NotNull(message = "请输入成绩")
    @DecimalMin(value = "0.0", inclusive = true, message = "成绩不能小于 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "成绩不能大于 100")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(precision = 4, scale = 1)
    private BigDecimal credit;

    @Size(max = 200, message = "备注不能超过 200 个字符")
    @Column(length = 200)
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }

    public BigDecimal getCredit() { return credit; }
    public void setCredit(BigDecimal credit) { this.credit = credit; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
