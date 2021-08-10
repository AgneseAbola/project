package com.example.project.mapper;

import com.example.project.model.Contact;
import com.example.project.repository.ContactDAO;

public class ContactMapper {

    public static Contact mapFromDAO(ContactDAO contactDAO) {
        if (contactDAO == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(contactDAO.getId());
        contact.setName(contactDAO.getName());
        contact.setSurname(contactDAO.getSurname());
        contact.setEmail(contactDAO.getEmail());
        contact.setPhone(contactDAO.getPhone());
        return contact;
    }

    public static ContactDAO mapToDAO(Contact contact){
        if (contact == null) {
            return null;
        }
        ContactDAO contactDAO = new ContactDAO();
        contactDAO.setId(contact.getId());
        contactDAO.setName(contact.getName());
        contactDAO.setSurname(contact.getSurname());
        contactDAO.setEmail(contact.getEmail());
        contactDAO.setPhone(contact.getPhone());
        return contactDAO;
    }
}
