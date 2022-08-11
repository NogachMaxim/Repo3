package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Learner;
import com.example.demo.model.Message;
import com.example.demo.model.Role;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	Message findByText(String text);
	ArrayList<Message> findAllByDateTime(Date dateTime);
	ArrayList<Message> findAllByLearner(Learner learner);
	ArrayList<Message> findAllByText(String text);
	ArrayList<Message> findByTextAndLearnerAndRole(String text, Learner learner, Role roleFrom);
}
