package com.example.studentmanagement.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "class_groups")
public class ClassGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "请输入班级名称")
    @Size(max = 100, message = "班级名称不能超过 100 个字符")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Size(max = 30, message = "年级不能超过 30 个字符")
    @Column(length = 30)
    private String grade;

    @Size(max = 80, message = "班主任不能超过 80 个字符")
    @Column(length = 80)
    private String advisor;

    @Size(max = 60, message = "教室不能超过 60 个字符")
    @Column(length = 60)
    private String room;

    @Size(max = 500, message = "班级描述不能超过 500 个字符")
    @Column(length = 500)
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getAdvisor() { return advisor; }
    public void setAdvisor(String advisor) { this.advisor = advisor; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
