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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "award_records")
public class AwardRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "请输入获奖名称")
    @Size(max = 200, message = "获奖名称不能超过 200 个字符")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "请输入获奖级别")
    @Size(max = 50, message = "获奖级别不能超过 50 个字符")
    @Column(nullable = false, length = 50)
    private String level;

    @NotNull(message = "请选择获奖日期")
    @Column(nullable = false)
    private LocalDate awardDate;

    @Size(max = 500, message = "说明不能超过 500 个字符")
    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public LocalDate getAwardDate() { return awardDate; }
    public void setAwardDate(LocalDate awardDate) { this.awardDate = awardDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
