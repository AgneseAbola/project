package com.example.project.service;

import com.example.project.mapper.ContactMapper;
import com.example.project.model.Contact;
import com.example.project.repository.ContactDAO;
import com.example.project.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository repo;

    @Override
    public List<Contact> getAllContacts() {
        List<ContactDAO> contactDAOList = repo.findAll();
        return contactDAOList.stream().map(ContactMapper::mapFromDAO).collect(Collectors.toList());
    }

    @Override
    public Contact saveContact(Contact contact) {
        ContactDAO contactDAO = repo.save(ContactMapper.mapToDAO(contact));
        return ContactMapper.mapFromDAO(contactDAO);
    }

    @Override
    public Optional<Contact> getContactById(int id) {
        return repo.findById(id)
                .flatMap(contact -> Optional.ofNullable(ContactMapper.mapFromDAO(contact)));
    }

    @Override
    public void deleteContact(int id) {
        repo.deleteById(id);
    }

    @Override
    public Contact editContactById(Contact contact) {
        ContactDAO contactDAO = ContactMapper.mapToDAO(contact);
        ContactDAO updateContact = repo.save(contactDAO);
        return ContactMapper.mapFromDAO(updateContact);
    }
}
