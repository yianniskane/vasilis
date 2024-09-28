package com.directory.service;

import com.directory.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
public class CardService {
    private final CardRepository repo;

    @Autowired
    public CardService(CardRepository repo) {
        this.repo = repo;
    }

    public void updateContactCard(String company, String profession, String email, String telephone, Long id) {
        this.repo.updateCard(company, profession, email, telephone, id);
    }

    public void deleteCard(Long id) {
        this.repo.deleteCard(id);
    }


}
