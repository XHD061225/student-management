package com.example.studentmanagement.student;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GradeRecordController {
    private final GradeRecordRepository gradeRecords;
    private final StudentRepository students;

    public GradeRecordController(GradeRecordRepository gradeRecords, StudentRepository students) {
        this.gradeRecords = gradeRecords;
        this.students = students;
    }

    @GetMapping("/grades")
    public String list(Model model) {
        model.addAttribute("gradeRecords", gradeRecords.findAllByOrderByTermDescSubjectAsc());
        return "grades/list";
    }

    @GetMapping("/grades/new")
    public String newGrade(Model model) {
        model.addAttribute("gradeRecord", new GradeRecord());
        model.addAttribute("students", students.findAllByOrderByIdDesc());
        model.addAttribute("pageTitle", "新增成绩");
        return "grades/form";
    }

    @GetMapping("/grades/{id}/edit")
    public String editGrade(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return gradeRecords.findById(id).map(record -> {
            model.addAttribute("gradeRecord", record);
            model.addAttribute("students", students.findAllByOrderByIdDesc());
            model.addAttribute("pageTitle", "编辑成绩");
            return "grades/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("notice", "没有找到该成绩记录。");
            return "redirect:/grades";
        });
    }

    @PostMapping("/grades/save")
    public String saveGrade(@Valid @ModelAttribute("gradeRecord") GradeRecord gradeRecord,
                            BindingResult result, Model model, RedirectAttributes redirect) {
        if (gradeRecord.getStudent() == null || gradeRecord.getStudent().getId() == null) {
            result.rejectValue("student", "required", "请选择学生");
        } else {
            students.findById(gradeRecord.getStudent().getId()).ifPresent(gradeRecord::setStudent);
        }

        if (result.hasErrors()) {
            model.addAttribute("students", students.findAllByOrderByIdDesc());
            model.addAttribute("pageTitle", gradeRecord.getId() == null ? "新增成绩" : "编辑成绩");
            return "grades/form";
        }

        gradeRecords.save(gradeRecord);
        redirect.addFlashAttribute("notice", "成绩信息已保存。");
        return "redirect:/grades";
    }

    @PostMapping("/grades/{id}/delete")
    public String deleteGrade(@PathVariable Long id, RedirectAttributes redirect) {
        if (gradeRecords.existsById(id)) {
            gradeRecords.deleteById(id);
            redirect.addFlashAttribute("notice", "成绩记录已删除。");
        }
        return "redirect:/grades";
    }
}
