package com.uap.repository;

import com.uap.model.Contact;
import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    List<Contact> findAll();
    Optional<Contact> findById(String id);
    Contact save(Contact contact); 
    void delete(String id);
    List<Contact> search(String query); 
    List<Contact> filterByCategory(String category);
    List<Contact> favorites();
}