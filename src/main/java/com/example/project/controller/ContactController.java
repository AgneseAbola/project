package com.example.project.controller;

import com.example.project.model.Contact;
import com.example.project.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class ContactController {

    @Autowired
    private ContactService service;

    private static final String CONTACT_STRING = "contact";
    private static final String REDIRECT = "redirect:/";
    private static final String ID = "/{id}";

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewList(Model model) {
        List<Contact> list = service.getAllContacts();
        model.addAttribute("contacts", list);
        return "contacts";
    }

    @GetMapping(value = "/newContactForm", produces = MediaType.APPLICATION_JSON_VALUE)
    public String newContactForm(Model model) {
        Contact contact = new Contact();
        model.addAttribute(CONTACT_STRING, contact);
        return "newContact";
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveContact(@Valid @ModelAttribute(CONTACT_STRING) Contact contact) {
        service.saveContact(contact);
        return REDIRECT;
    }

    @GetMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getContactById(Model model, @PathVariable("id") int id) {
        Optional<Contact> contact = service.getContactById(id);
        if (contact.isEmpty()) {
            return "errorContact";
        }
        model.addAttribute(CONTACT_STRING, contact.get());
        return "updateAndDeleteContact";
    }

    @PutMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public String editContactById(@PathVariable("id") int id, @ModelAttribute("contact") Contact contact) {
        Optional<Contact> contactById = service.getContactById(id);
        if (contactById.isPresent()) {
            contact.setId(id);
            service.saveContact(contact);
        }
        return REDIRECT;
    }

    @DeleteMapping(value = ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteContactById(@PathVariable("id") int id) {
        service.deleteContact(id);
        return REDIRECT;
    }
}
