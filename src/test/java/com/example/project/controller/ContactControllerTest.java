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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
    void getContactListPositiveTest() throws Exception {
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
    void deleteContactByIdTest() throws Exception {
        doNothing().when(service).deleteContact(anyInt());
        mockMvc.perform(delete("http://localhost:8080/" + anyInt()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void getContactListEmptyTest() throws Exception{
        when(service.getAllContacts()).thenReturn(EMPTY_LIST);
        this.mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"));
    }

    @Test
    void getContactByIdPositiveTest() throws Exception {
        when(service.getContactById(anyInt())).thenReturn(Optional.of(contactModel));
        mockMvc.perform(get("http://localhost:8080/" + anyInt()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("contact", contactModel))
                .andExpect(view().name("updateAndDeleteContact"));
    }

    @Test
    void editContactPositiveTest() throws Exception {
        when(service.getContactById(anyInt())).thenReturn(Optional.of(contactModel));
        mockMvc.perform(put("http://localhost:8080/" + contactModel.getId())
                        .param("contact.id", String.valueOf(contactModel.getId()))
                        .param("contact.name", contactModel.getName())
                        .param("contact.surname", contactModel.getSurname())
                        .param("contact.email", contactModel.getEmail())
                        .param("contact.phone", String.valueOf(contactModel.getPhone())))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void saveContactPositiveTest() throws Exception {
        when(service.saveContact(contactModel)).thenReturn(contactModel);
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/")
                        .param("contact", contactModel.getName()))
                .andExpect(status().is3xxRedirection())
               // .andExpect(MockMvcResultMatchers.flash().attribute(successMessages, successMessage))
                .andExpect(redirectedUrl("/"));
    }
}