package com.jack_wang.web;

import com.jack_wang.po.Contact;
import com.jack_wang.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ContactController {


    @Autowired
    private ContactService contactService;

    @PostMapping("/saveContent")
    public String saveContact(Contact contact) {

        Contact c = contactService.saveContact(contact);
        if (c.getId() != null) {
            contactService.sendEmail(contact);
        }
        return "redirect:/";
    }

}
