package com.example.studentmanagement.student;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {
    private final StudentRepository students;
    private final GradeRecordRepository gradeRecords;
    private final AwardRecordRepository awardRecords;

    public StudentController(StudentRepository students,
                            GradeRecordRepository gradeRecords,
                            AwardRecordRepository awardRecords) {
        this.students = students;
        this.gradeRecords = gradeRecords;
        this.awardRecords = awardRecords;
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "") String q, Model model) {
        String keyword = q.trim();
        model.addAttribute("students", keyword.isEmpty()
                ? students.findAllByOrderByIdDesc()
                : students.findByNameContainingIgnoreCaseOrStudentNoContainingIgnoreCaseOrClassNameContainingIgnoreCaseOrderByIdDesc(
                        keyword, keyword, keyword));
        model.addAttribute("q", q);
        return "students/list";
    }

    @GetMapping("/students/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return students.findById(id).map(student -> {
            model.addAttribute("student", student);
            model.addAttribute("grades", gradeRecords.findByStudentOrderByTermDescSubjectAsc(student));
            model.addAttribute("awards", awardRecords.findByStudentOrderByAwardDateDesc(student));
            return "students/detail";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("notice", "没有找到这条学生记录。");
            return "redirect:/";
        });
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "新增学生");
        return "students/form";
    }

    @GetMapping("/students/{id}/edit")
    public String editStudent(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return students.findById(id).map(student -> {
            model.addAttribute("student", student);
            model.addAttribute("pageTitle", "编辑学生");
            return "students/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("notice", "没有找到这条学生记录。");
            return "redirect:/";
        });
    }

    @PostMapping("/students/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result, Model model, RedirectAttributes redirect) {
        if (student.getStudentNo() != null && !student.getStudentNo().isBlank()) {
            boolean duplicate = student.getId() == null
                    ? students.existsByStudentNo(student.getStudentNo())
                    : students.existsByStudentNoAndIdNot(student.getStudentNo(), student.getId());
            if (duplicate) {
                result.rejectValue("studentNo", "duplicate", "该学号已存在");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", student.getId() == null ? "新增学生" : "编辑学生");
            return "students/form";
        }

        if (student.getId() != null && !students.existsById(student.getId())) {
            redirect.addFlashAttribute("notice", "要编辑的学生记录不存在。");
            return "redirect:/";
        }

        students.save(student);
        redirect.addFlashAttribute("notice", "学生信息已保存。");
        return "redirect:/";
    }

    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirect) {
        if (students.existsById(id)) {
            students.deleteById(id);
            redirect.addFlashAttribute("notice", "学生信息已删除。");
        }
        return "redirect:/";
    }
}
