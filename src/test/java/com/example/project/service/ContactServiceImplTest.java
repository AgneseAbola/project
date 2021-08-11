package com.example.project.service;

import com.example.project.mapper.ContactMapper;
import com.example.project.model.Contact;
import com.example.project.repository.ContactDAO;
import com.example.project.repository.ContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(ContactServiceImpl.class)
class ContactServiceImplTest {

    @Autowired
    private ContactServiceImpl service;

    @MockBean
    private ContactRepository repo;

    private static Contact getContactModel(int id, String name, String surname, String email, int phone) {
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(name);
        contact.setSurname(surname);
        contact.setEmail(email);
        contact.setPhone(phone);
        return contact;
    }

    public static ContactDAO getContactDAO(int id, String name, String surname, String email, int phone) {
        ContactDAO contactDAO = new ContactDAO();
        contactDAO.setId(id);
        contactDAO.setName(name);
        contactDAO.setSurname(surname);
        contactDAO.setEmail(email);
        contactDAO.setPhone(phone);
        return contactDAO;
    }

    private final ContactDAO contactDAO = getContactDAO(1, "name1", "surname1", "email1", 111);
    private final Contact contact = getContactModel(1, "name1", "surname1", "email1", 111);
    private final List<ContactDAO> contactDAOList = new ArrayList<>();

    @Test
    void getContactListPositiveTest() {
        contactDAOList.add(contactDAO);
        when(repo.findAll()).thenReturn(contactDAOList);
        List<Contact> list = service.getAllContacts();
        assertEquals(1, list.size());
        assertEquals("name1", list.get(0).getName());
        verify(repo, times(1)).findAll();
    }

    @Test
    void getContactListNegativeTest() {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertTrue(service.getAllContacts().isEmpty());
        verify(repo, times(1)).findAll();
    }

    @Test
    void getContactByIdTestPositive() {
        when(repo.findById(anyInt())).thenReturn(Optional.of(contactDAO));
        Optional<Contact> result = service.getContactById(anyInt());
        assertTrue(result.isPresent());
        assertEquals(result.get().getName(), contactDAO.getName());
    }

    @Test
    void getContactByIdTestNegative() {
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        Optional<Contact> result = service.getContactById(anyInt());
        assertFalse(result.isPresent());
    }

    @Test
    void updateContactByIdTestPositive() {
        when(repo.save(any(ContactDAO.class))).thenReturn(contactDAO);
        Contact result = service.editContactById(contact);
        assertEquals(result.getName(), contactDAO.getName());
    }

    @Test
    void updateContactByIdTestNegative() {
        when(repo.save(contactDAO)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(HttpClientErrorException.class, () ->
                service.editContactById(ContactMapper.mapFromDAO(contactDAO)));
    }

    @Test
    void deleteContactByIdTestPositive() {
        doNothing().when(repo).deleteById(anyInt());
        service.deleteContact(anyInt());
        verify(repo, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteContactByIdTestNegative() {
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)).when(repo).deleteById(1);
        assertThrows(HttpClientErrorException.class, () ->
                service.deleteContact(1));
    }

    @Test
    void postContactTestPositive() {
        when(repo.save(any(ContactDAO.class))).thenReturn(contactDAO);
        service.saveContact(contact);
        assertEquals(contact.getName(), ContactMapper.mapFromDAO(contactDAO).getName());
    }

    @Test
    void postContactTestNegative() {
        when(service.saveContact(contact)).thenThrow(HttpClientErrorException.class);
        assertThrows(HttpClientErrorException.class, () ->
                service.saveContact(ContactMapper.mapFromDAO(contactDAO)));
    }
}