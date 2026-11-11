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
public class AwardRecordController {
    private final AwardRecordRepository awardRecords;
    private final StudentRepository students;

    public AwardRecordController(AwardRecordRepository awardRecords, StudentRepository students) {
        this.awardRecords = awardRecords;
        this.students = students;
    }

    @GetMapping("/awards")
    public String list(Model model) {
        model.addAttribute("awardRecords", awardRecords.findAllByOrderByAwardDateDesc());
        return "awards/list";
    }

    @GetMapping("/awards/new")
    public String newAward(Model model) {
        model.addAttribute("awardRecord", new AwardRecord());
        model.addAttribute("students", students.findAllByOrderByIdDesc());
        model.addAttribute("pageTitle", "新增获奖记录");
        return "awards/form";
    }

    @GetMapping("/awards/{id}/edit")
    public String editAward(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return awardRecords.findById(id).map(record -> {
            model.addAttribute("awardRecord", record);
            model.addAttribute("students", students.findAllByOrderByIdDesc());
            model.addAttribute("pageTitle", "编辑获奖记录");
            return "awards/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("notice", "��有找到该获奖记录。");
            return "redirect:/awards";
        });
    }

    @PostMapping("/awards/save")
    public String saveAward(@Valid @ModelAttribute("awardRecord") AwardRecord awardRecord,
                            BindingResult result, Model model, RedirectAttributes redirect) {
        if (awardRecord.getStudent() == null || awardRecord.getStudent().getId() == null) {
            result.rejectValue("student", "required", "请选择学生");
        } else {
            students.findById(awardRecord.getStudent().getId()).ifPresent(awardRecord::setStudent);
        }

        if (result.hasErrors()) {
            model.addAttribute("students", students.findAllByOrderByIdDesc());
            model.addAttribute("pageTitle", awardRecord.getId() == null ? "新增获奖记录" : "编辑获奖记录");
            return "awards/form";
        }

        awardRecords.save(awardRecord);
        redirect.addFlashAttribute("notice", "获奖记录已保存。");
        return "redirect:/awards";
    }

    @PostMapping("/awards/{id}/delete")
    public String deleteAward(@PathVariable Long id, RedirectAttributes redirect) {
        if (awardRecords.existsById(id)) {
            awardRecords.deleteById(id);
            redirect.addFlashAttribute("notice", "获奖记录已删除。");
        }
        return "redirect:/awards";
    }
}
