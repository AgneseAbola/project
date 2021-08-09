package com.example.project.service;

import com.example.project.model.Contact;
import com.example.project.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository repo;

    @Override
    public List<Contact> getAllContacts() {
        return repo.findAll();
    }

    @Override
    public void saveContact(Contact contact) {
        repo.save(contact);
    }

    @Override
    public Optional<Contact> getContactById(int id) {
        return repo.findById(id);
    }

    @Override
    public void deleteContact(int id) {
        repo.deleteById(id);
    }
}
