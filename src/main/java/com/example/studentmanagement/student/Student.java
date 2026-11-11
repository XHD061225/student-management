package com.example.studentmanagement.student;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @Size(max = 50, message = "生日不能超过 50 个字符")
    @Column(length = 50)
    private String birthday;

    @Size(max = 80, message = "专业不能超过 80 个字符")
    @Column(length = 80)
    private String major;

    @Size(max = 200, message = "家庭住址不能超过 200 个字符")
    @Column(length = 200)
    private String address;

    @Size(max = 80, message = "籍贯不能超过 80 个字符")
    @Column(length = 80)
    private String hometown;

    @Size(max = 500, message = "备注不能超过 500 个字符")
    @Column(length = 500)
    private String remark;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GradeRecord> grades = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AwardRecord> awards = new ArrayList<>();

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

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getHometown() { return hometown; }
    public void setHometown(String hometown) { this.hometown = hometown; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public List<GradeRecord> getGrades() { return grades; }
    public void setGrades(List<GradeRecord> grades) { this.grades = grades; }

    public List<AwardRecord> getAwards() { return awards; }
    public void setAwards(List<AwardRecord> awards) { this.awards = awards; }
}
