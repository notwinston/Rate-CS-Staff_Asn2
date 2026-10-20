package com.cmpt276.ratecsstaff.controller;

import com.cmpt276.ratecsstaff.model.RoleType;
import com.cmpt276.ratecsstaff.model.StaffRating;
import com.cmpt276.ratecsstaff.service.StaffRatingService;
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
public class StaffRatingController {

    private final StaffRatingService service;

    public StaffRatingController(StaffRatingService service) {
        this.service = service;
    }

    @ModelAttribute("roles")
    public RoleType[] roles() {
        return RoleType.values();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("ratings", service.findAll());
        return "index";
    }

    @GetMapping("/ratings/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return service.findById(id)
                .map(rating -> {
                    model.addAttribute("rating", rating);
                    return "detail";
                })
                .orElseGet(() -> notFound(ra, id));
    }

    @GetMapping("/ratings/new")
    public String createForm(Model model) {
        model.addAttribute("rating", new StaffRating());
        model.addAttribute("formAction", "/ratings");
        model.addAttribute("formTitle", "Add a Staff Rating");
        return "form";
    }

    @PostMapping("/ratings")
    public String create(@Valid @ModelAttribute("rating") StaffRating rating,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        validateUniqueEmail(rating, null, result);
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/ratings");
            model.addAttribute("formTitle", "Add a Staff Rating");
            return "form";
        }
        StaffRating saved = service.save(rating);
        ra.addFlashAttribute("message", "Rating for \"" + saved.getName() + "\" was created.");
        return "redirect:/ratings/" + saved.getId();
    }

    @GetMapping("/ratings/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return service.findById(id)
                .map(rating -> {
                    model.addAttribute("rating", rating);
                    model.addAttribute("formAction", "/ratings/" + id);
                    model.addAttribute("formTitle", "Edit Staff Rating");
                    return "form";
                })
                .orElseGet(() -> notFound(ra, id));
    }

    @PostMapping("/ratings/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("rating") StaffRating rating,
                         BindingResult result,
                         Model model,
                         RedirectAttributes ra) {
        if (!service.existsById(id)) {
            return notFound(ra, id);
        }
        rating.setId(id);
        validateUniqueEmail(rating, id, result);
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/ratings/" + id);
            model.addAttribute("formTitle", "Edit Staff Rating");
            return "form";
        }
        StaffRating saved = service.save(rating);
        ra.addFlashAttribute("message", "Rating for \"" + saved.getName() + "\" was updated.");
        return "redirect:/ratings/" + saved.getId();
    }

    @GetMapping("/ratings/{id}/delete")
    public String deleteConfirm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        return service.findById(id)
                .map(rating -> {
                    model.addAttribute("rating", rating);
                    return "delete";
                })
                .orElseGet(() -> notFound(ra, id));
    }

    @PostMapping("/ratings/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (!service.existsById(id)) {
            return notFound(ra, id);
        }
        service.deleteById(id);
        ra.addFlashAttribute("message", "Rating was deleted.");
        return "redirect:/";
    }

    private void validateUniqueEmail(StaffRating rating, Long currentId, BindingResult result) {
        if (!result.hasFieldErrors("email")
                && service.isEmailTakenByOther(rating.getEmail(), currentId)) {
            result.rejectValue("email", "email.duplicate",
                    "That email already has a rating. Each staff member is rated once.");
        }
    }

    private String notFound(RedirectAttributes ra, Long id) {
        ra.addFlashAttribute("error", "No staff rating found with id " + id + ".");
        return "redirect:/";
    }
}
