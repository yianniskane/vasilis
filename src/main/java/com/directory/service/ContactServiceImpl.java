package com.directory.service;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.directory.entity.Contact;
import com.directory.repository.ContactRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class ContactServiceImpl {
    private final ContactRepository repo;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    EntityManager em;


    @Autowired
    public ContactServiceImpl(ContactRepository repo) {
        this.repo = repo;
    }

    public Contact findByContactNo(Integer contact_no) {
        return this.repo.findContactByContact_no(contact_no);
    }

    public List<Contact> getAllContacts() {
        return this.repo.findAllContacts();
    }

    public int updateContact(String date_recorded, String telephone, String email, String street, String area, String city, String nomos, String zip, String seller, String manager, String company, String afm, String comments, boolean completed, String followup, boolean ignored, String last_comm_date, String orders, Integer contact_no) {
        return this.repo.updateContact(date_recorded, telephone, email, street, area, city, nomos, zip, seller, manager, company, afm, comments, completed, followup, ignored, last_comm_date, orders, contact_no);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public List<Contact> findContactByCompany(String company) {
        return this.repo.findByCompanyStartingWithIgnoreCase(company);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public List<Contact> findContactByAddressCity(String company) {
        return this.repo.findByAddress_CityStartingWithIgnoreCase(company);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public List<Contact> findContactByAddressStreet(String company) {
        return this.repo.findByAddress_StreetStartingWithIgnoreCase(company);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public List<Contact> findContactByManager(String company) {
        return this.repo.findByManagerStartingWithIgnoreCase(company);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public List<Contact> findContactByTelephone(String company) {
        return this.repo.findByTelephoneStartingWithIgnoreCase(company);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    public List<Contact> findContactByAFM(String company) {
        return this.repo.findByAfmStartingWithIgnoreCase(company);
    }
}

