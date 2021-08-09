package com.example.project.controller;

import com.example.project.model.Contact;
import com.example.project.service.ContactService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private ContactController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService service;

    @Mock
    private Model model;

    private Contact getContact(int id, String name, String surname, String email, int phone) {
        Contact contact = new Contact();
        contact.setId(id);
        contact.setName(name);
        contact.setSurname(surname);
        contact.setEmail(email);
        contact.setPhone(phone);
        return contact;
    }

    private List<Contact> contactList() {
        List<Contact> contactList = new ArrayList<>();
        contactList.add(getContact(1, "name1", "surname1", "email1", 111));
        contactList.add(getContact(2, "name2", "surname2", "email2", 222));
        return contactList;
    }

    private final Contact contactModel = getContact(1, "name1", "surname1", "email1", 111);



    @Test
    void getContactList() throws Exception {
        when(service.getAllContacts()).thenReturn(contactList());
        mockMvc.perform(get("http://localhost:8080/"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"));

        String actual = controller.viewList(model);
        assertNotNull(actual);
    }

    @Test
    void showAddContactFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/newContactForm")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("newContact"));
    }

    @Test
    void deleteContactTest() throws Exception {
        int id=1;
        mockMvc.perform(get("http://localhost:8080/contactForDelete/" + id))
                .andExpect(status().isFound());
        String actual = controller.deleteContact(id);
        assertNotNull(actual);
    }

    @Test
    void updateContact() throws Exception {
        when(service.getContactById(anyInt())).thenReturn(Optional.of(contactModel));

        ResultActions mvcRes = mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/1"))
                      //  .content(asJsonString(employeeType))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1))
                .andExpect(status().isCreated());

        verify(service, times(1)).saveContact(contactModel);
    }
}