package com.example.candy.service.choice;

import com.example.candy.domain.choice.Choice;
import com.example.candy.repository.choice.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceService {

    @Autowired
    ChoiceRepository choiceRepository;

    private Choice save(Choice choice) {
        return choiceRepository.save(choice);
    }
}
