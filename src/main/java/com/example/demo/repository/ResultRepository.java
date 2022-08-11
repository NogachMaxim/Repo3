package com.example.demo.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Learner;
import com.example.demo.model.Result;
import com.example.demo.model.Test;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
	Result findByLearnerAndTest(Learner learner, Test test);
}
