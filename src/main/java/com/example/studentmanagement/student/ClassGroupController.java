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
public class ClassGroupController {
    private final ClassGroupRepository classGroups;

    public ClassGroupController(ClassGroupRepository classGroups) {
        this.classGroups = classGroups;
    }

    @GetMapping("/classes")
    public String list(Model model) {
        model.addAttribute("classGroups", classGroups.findAllByOrderByNameAsc());
        return "classes/list";
    }

    @GetMapping("/classes/new")
    public String newClassGroup(Model model) {
        model.addAttribute("classGroup", new ClassGroup());
        model.addAttribute("pageTitle", "新增班级");
        return "classes/form";
    }

    @GetMapping("/classes/{id}/edit")
    public String editClassGroup(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        return classGroups.findById(id).map(group -> {
            model.addAttribute("classGroup", group);
            model.addAttribute("pageTitle", "编辑班级");
            return "classes/form";
        }).orElseGet(() -> {
            redirect.addFlashAttribute("notice", "没有找到该班级信息。");
            return "redirect:/classes";
        });
    }

    @PostMapping("/classes/save")
    public String saveClassGroup(@Valid @ModelAttribute("classGroup") ClassGroup classGroup,
                                BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", classGroup.getId() == null ? "新增班级" : "编辑班级");
            return "classes/form";
        }

        classGroups.save(classGroup);
        redirect.addFlashAttribute("notice", "班级信息已保存。");
        return "redirect:/classes";
    }

    @PostMapping("/classes/{id}/delete")
    public String deleteClassGroup(@PathVariable Long id, RedirectAttributes redirect) {
        if (classGroups.existsById(id)) {
            classGroups.deleteById(id);
            redirect.addFlashAttribute("notice", "班级信息已删除。");
        }
        return "redirect:/classes";
    }
}
