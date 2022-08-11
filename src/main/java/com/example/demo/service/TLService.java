package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.example.demo.model.Answer;
import com.example.demo.model.Learner;
import com.example.demo.model.Material;
import com.example.demo.model.Message;
import com.example.demo.model.Question;
import com.example.demo.model.Result;
import com.example.demo.model.Teacher;
import com.example.demo.model.Test;

public interface TLService {
	Teacher registrTeacher(String surname, String name, String patronymic, String password);

	Teacher teacherById(Long teacherId);

	Teacher teacherBySurname(String surname);
	
	List<Teacher> allTeachers();

	void updateTeacher(Teacher modifyTeacher);

	void deleteTeacher(Long teacherId);

	void accessionL(String surnameL, Long teacherId);

	Material addMaterial(String name, String materialText);

	List<Material> allMaterials();
	
	Material materialById(Long materialId);

	void updateMaterial(Long materialId, String modifyText) throws IOException;

	void deleteMaterial(Long materialId);

	void giveMaterialL(Long materialId, String surnameL);

	List<Learner> learnersAskJoin(Long teacherId);
	
	void saveMessage(String text, String nameRoleFrom, Long idWithWhatLMes);

	void deleteMessage(String text, Date datetime, int idFrom, Long idWithWhatLMes);

	void updateMessage(String text, String newText, Date datetime, int idFrom, Long idWithWhatLMes);

	ArrayList messagesL(String surnameL);
	
	ArrayList messagesLByIdL(Long id);

	ArrayList findNeedMessForT(Long learnerId);
	
	Test addTest(Test test);
	
	List<Test> allTest() ;

	Test testById(Long testId);

	void updateTest(Test modifyTest);

	void deleteTest(Long testId);
	
	Learner registrLearner(String surname, String name, String patronymic, String password);

	List<Learner> allLearners() ;
	
	Learner learnerById(Long learnerId);
	
	Learner learnerBySurname(String surname);

	void updateLearner(Learner modifyLearner);

	void deleteLearner(Long learnerId);
	
	Integer podschetTrue(Test test);

	Result addResult(Integer procent, Learner learner, Test test);

	Collection<Result> resultByLearnerId(Long learnerId);
	
	void updateResult(Result modifyResult);
	
	Integer formStatistics(Long learnerId);
	
}
