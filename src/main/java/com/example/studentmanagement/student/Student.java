package com.example.studentmanagement.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "请输入学号")
    @Size(max = 32, message = "学号不能超过 32 个字符")
    @Column(name = "student_no", nullable = false, unique = true, length = 32)
    private String studentNo;

    @NotBlank(message = "请输入姓名")
    @Size(max = 80, message = "姓名不能超过 80 个字符")
    @Column(nullable = false, length = 80)
    private String name;

    @Size(max = 10, message = "性别不能超过 10 个字符")
    @Column(length = 10)
    private String gender;

    @NotBlank(message = "请输入班级")
    @Size(max = 100, message = "班级不能超过 100 个字符")
    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Size(max = 30, message = "联系电话不能超过 30 个字符")
    @Column(length = 30)
    private String phone;

    @Email(message = "请输入有效的邮箱地址")
    @Size(max = 150, message = "邮箱不能超过 150 个字符")
    @Column(length = 150)
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
