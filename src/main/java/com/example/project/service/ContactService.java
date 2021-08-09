package com.example.project.service;

import com.example.project.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    List<Contact> getAllContacts();
    void saveContact(Contact contact);
    Optional<Contact> getContactById(int id);
    void deleteContact(int id);
}
