package com.example.candy.repository.choice;

import com.example.candy.domain.choice.Choice;
import com.example.candy.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
