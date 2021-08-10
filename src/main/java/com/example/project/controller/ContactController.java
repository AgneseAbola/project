package com.example.project.controller;

import com.example.project.model.Contact;
import com.example.project.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ContactController {

    @Autowired
    private ContactService service;

    private static final String CONTACT_STRING = "contact";

    @GetMapping("/")
    public String viewList(Model model) {
        model.addAttribute("contacts", service.getAllContacts());
        return "contacts";
    }

    @GetMapping("/newContactForm")
    public String newContactForm(Model model) {
        Contact contact = new Contact();
        model.addAttribute(CONTACT_STRING, contact);
        return "newContact";
    }

    @PostMapping("/saveContact")
    public String saveContact(@ModelAttribute("contact") Contact contact) {
        service.saveContact(contact);
        return "redirect:/";
    }

    @GetMapping("/contactForUpdate/{id}")
    public String contactForUpdate(@PathVariable(value = "id") int id, Model model) {
        Optional<Contact> contact = service.getContactById(id);
        model.addAttribute(CONTACT_STRING, contact);
        return "updateContact";
    }

    @GetMapping("/contactForDelete/{id}")
    public String deleteContact(@PathVariable(value = "id") int id) {
        service.deleteContact(id);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getContactById(Model model, @PathVariable("id") int id) {
        Optional<Contact> contact = service.getContactById(id);
        if (contact.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute(CONTACT_STRING, contact.get());
        return "updateContact";
    }
}
