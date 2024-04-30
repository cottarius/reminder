package ru.cotarius.reminder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.cotarius.reminder.entity.Reminder;
import ru.cotarius.reminder.entity.User;
import ru.cotarius.reminder.service.ReminderService;
import ru.cotarius.reminder.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
//@RequestMapping("/remind")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;
    private final UserService userService;

    @GetMapping("/index")
    public String getReminds(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Reminder> reminds = reminderService.findByUserId(user.getId());
        model.addAttribute("reminds", reminds);
        model.addAttribute("user", user);
        return "index";
    }

//    @RequestMapping("/")
//    public ModelAndView home() {
//        List<Customer> listCustomer = customerService.listAll();
//        ModelAndView mav = new ModelAndView("index");
//        mav.addObject("listCustomer", listCustomer);
//        return mav;
//    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable long id, Model model){
        Reminder reminder = reminderService.findById(id);
        model.addAttribute("reminder", reminder);
        return "edit-remind";
    }

    @PutMapping("/update/{id}")
    public String updateReminder(@PathVariable long id, Reminder reminder, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            reminder.setId(id);
            return "edit-remind";
        }

        reminderService.save(reminder);
        return "redirect:/index";
    }

    @GetMapping("/remind/new")
    public String newRemindForm(Model model) {
        Reminder reminder = new Reminder();
        model.addAttribute("reminder", reminder);
        return "new_remind";
    }
    @PostMapping(value = "/save")
    public String saveRemind(@ModelAttribute("reminder") Reminder reminder, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        reminder.setUser(user);
        reminderService.update(reminder);
        return "redirect:/index";
    }

//    @PostMapping("/new_remind")
//    public String addRemind(@ModelAttribute("reminder") Reminder reminder, Principal principal) {
//        User user = userService.findByUsername(principal.getName());
//        reminder.setUser(user);
//        reminderService.save(reminder);
//        return "redirect:/index";
//    }


}
