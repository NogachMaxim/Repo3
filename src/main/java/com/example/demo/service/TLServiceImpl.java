package com.example.demo.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Answer;
import com.example.demo.model.Learner;
import com.example.demo.model.Material;
import com.example.demo.model.Message;
import com.example.demo.model.Question;
import com.example.demo.model.Result;
import com.example.demo.model.Role;
import com.example.demo.model.Teacher;
import com.example.demo.model.Test;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.LearnerRepository;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.ResultRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.TestRepository;

@Service
public class TLServiceImpl implements TLService, UserDetailsService {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private LearnerRepository learnerRepository;

	@Autowired
	private MaterialRepository materialRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private TestRepository testRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private ResultRepository resultRepository;

	@Lazy
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Teacher registrTeacher(String surname, String name, String patronymic, String password) {
		Teacher teacher = new Teacher(surname, name, patronymic, passwordEncoder.encode(password));
		return this.teacherRepository.save(teacher);
	}

	@Override
	public Teacher teacherById(Long teacherId) {
		return this.teacherRepository.getById(teacherId);
	}

	@Override
	public Teacher teacherBySurname(String surname) {
		return this.teacherRepository.findBySurname(surname);
	}

	@Override
	public List<Teacher> allTeachers() {
		return this.teacherRepository.findAll();
	}

	@Override
	public void updateTeacher(Teacher modifyTeacher) {
		this.teacherRepository.save(modifyTeacher);
	}

	@Override
	public void deleteTeacher(Long teacherId) {
		Teacher teacher = this.teacherRepository.getById(teacherId);
		if (teacher != null) {
			List<Learner> learners = (List<Learner>) teacher.getLearners();
			for (Learner learner : learners) {
				List<Message> messages = (List<Message>) learner.getMessages();
				learner.setMessages(null);
				for (Message message : messages) {
					this.messageRepository.delete(message);
				}
				learner.setTeacher(null);
				this.learnerRepository.save(learner);
			}
			this.teacherRepository.delete(teacher);
		}
	}

	@Override
	public void accessionL(String surnameL, Long teacherId) {
		Teacher teacher = this.teacherRepository.getById(teacherId);
		Learner learner = this.learnerRepository.findBySurname(surnameL);
		Collection<Learner> learners = teacher.getLearners();
		learners.add(learner);
		teacher.setLearners(learners);
		learner.setTeacher(teacher);
		this.learnerRepository.save(learner);
		this.teacherRepository.save(teacher);
	}

	@Override
	public Material addMaterial(String name, String materialText) {
		Material material = new Material(name, materialText);
		return this.materialRepository.save(material);
	}

	@Override
	public Material materialById(Long materialId) {
		return this.materialRepository.getById(materialId);
	}

	@Override
	public void updateMaterial(Long materialId, String modifyText) throws IOException {
		Material material = this.materialRepository.getById(materialId);
		material.setMaterialText(modifyText);
		this.materialRepository.save(material);
	}

	@Override
	public void deleteMaterial(Long materialId) {
		Material material = this.materialRepository.getById(materialId);
		if (material != null) {
			List<Learner> learners = this.learnerRepository.findAll();
			for (Learner learner : learners) {
				Collection<Material> materials = learner.getMaterials();
				materials.removeIf(material1 -> (material1.getId() == materialId));
				learner.setMaterials(materials);
			}
			this.materialRepository.delete(material);
		}
	}

	@Override
	public List<Learner> learnersAskJoin(Long teacherId) {
		Teacher teacher = this.teacherRepository.getById(teacherId);
		List<Message> messages = this.messageRepository.findAllByText("Ask join to" + teacher.getSurname());
		List<Learner> learnersAsk = new ArrayList();
		for (Message message : messages) {
			learnersAsk.add(message.getLearner());
		}
		return learnersAsk;
	}

	@Override
	public void giveMaterialL(Long materialId, String surnameL) {
		Material material = this.materialRepository.getById(materialId);
		Learner learner = this.learnerRepository.findBySurname(surnameL);
		Collection<Material> materials = learner.getMaterials();
		if (!materials.contains(material)) {
			materials.add(material);
		}
		learner.setMaterials(materials);
		this.learnerRepository.save(learner);
	}

	@Override
	public void saveMessage(String text, String nameRoleFrom, Long idWithWhatLMes) {
		Date dateTime = new Date();
		Message message = new Message(text, dateTime);
		// get need role from db
		// role can be or not be
		Role role = this.roleRepository.findByName(nameRoleFrom);
		if (role != null) {
			// need role be
			message.setRole(role);
		} else {
			// need role not be
			Role role1;
			if (nameRoleFrom.equals("TEACHER")) {
				// need role is teacher
				role1 = new Role("TEACHER");
			} else {
				role1 = new Role("LEARNER");
			}
			this.roleRepository.save(role1);
			message.setRole(role1);
		}
		// set learner new message
		Learner learner = this.learnerRepository.getById(idWithWhatLMes);
		Collection<Message> messages = learner.getMessages();
		// if (this.messageRepository.findByTextAndLearnerAndRole(text, learner,
		// role).isEmpty()) {
		if (!text.trim().equals("")) {
			messages.add(message);
			learner.setMessages(messages);
			message.setLearner(learner);
			this.messageRepository.save(message);
		}
	}

	@Override
	public void deleteMessage(String text, Date datetime, int idFrom, Long idWithWhatLMes) {
		// find need message in db
		ArrayList<Message> messages = this.messageRepository.findAllByDateTime(datetime);
		messages.removeIf(message -> (!message.getText().equals(text)));
		messages.removeIf(message -> (message.getLearner().getId() != idWithWhatLMes));
		messages.removeIf(message -> (message.getRole().getId() != idFrom));
		this.messageRepository.delete(messages.get(0));
	}

	@Override
	public void updateMessage(String text, String newText, Date datetime, int idFrom, Long idWithWhatLMes) {
		// find need message in db
		ArrayList<Message> messages = this.messageRepository.findAllByDateTime(datetime);
		messages.removeIf(message -> (!message.getText().equals(text)));
		messages.removeIf(message -> (message.getLearner().getId() != idWithWhatLMes));
		messages.removeIf(message -> (message.getRole().getId() != idFrom));
		messages.get(0).setText(newText);
		this.messageRepository.save(messages.get(0));

	}

	@Override
	public ArrayList messagesL(String surnameL) {
		// find need message in db
		Learner learner = this.learnerRepository.findBySurname(surnameL);
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.addAll(learner.getMessages());
		messages.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		return messages;
	}

	@Override
	public ArrayList messagesLByIdL(Long id) {
		// find need message in db
		Learner learner = this.learnerRepository.getById(id);
		ArrayList<Message> messages = new ArrayList<Message>();
		messages.addAll(learner.getMessages());
		messages.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		return messages;
	}

	@Override
	public ArrayList findNeedMessForT(Long learnerId) {
		// find need message in db
		Learner learner = this.learnerRepository.getById(learnerId);
		ArrayList<Message> messages = this.messageRepository.findAllByLearner(learner);
		messages.removeIf(message -> (message.getRole().getId() != 1));
		return messages;
	}

	@Override
	public Test addTest(Test test) {
		this.testRepository.save(test);
		for (int i = 0; i < test.getQuestions().size(); i++) {
			((Question) test.getQuestions().get(i)).setTest(test);
			this.questionRepository.save((Question) test.getQuestions().get(i));
			for (int j = 0; j < 4; j++) {
				((Answer) test.getQuestions().get(i).getAnswers().get(j))
						.setQuestion((Question) test.getQuestions().get(i));
				this.answerRepository.save((Answer) test.getQuestions().get(i).getAnswers().get(j));
			}
		}
		return test;
	}

	@Override
	public List<Test> allTest() {
		return this.testRepository.findAll();
	}

	@Override
	public Test testById(Long testId) {
		return this.testRepository.getById(testId);
	}

	@Override
	public void updateTest(Test modifyTest) {
		this.testRepository.save(modifyTest);
	}

	@Override
	public void deleteTest(Long testId) {
		Test test = this.testRepository.getById(testId);
		if (test != null) {
			for (int i = 0; i < test.getQuestions().size(); i++) {
				Question question = this.questionRepository.getById(((Question) test.getQuestions().get(i)).getId());
				if (question != null) {
					for (int j = 0; j < 4; j++) {
						Answer answer = this.answerRepository.getById(((Answer) question.getAnswers().get(j)).getId());
						if (answer != null) {
							this.answerRepository.delete(answer);
						}
					}
					this.questionRepository.delete(question);
				}
			}
			this.testRepository.delete(test);
		}
	}

	@Override
	public Learner registrLearner(String surname, String name, String patronymic, String password) {
		Learner learner = new Learner(surname, name, patronymic, passwordEncoder.encode(password));
		return this.learnerRepository.save(learner);
	}

	@Override
	public Learner learnerById(Long learnerId) {
		return this.learnerRepository.getById(learnerId);
	}

	@Override
	public Learner learnerBySurname(String surname) {
		return this.learnerRepository.findBySurname(surname);
	}

	@Override
	public void updateLearner(Learner modifyLearner) {
		this.learnerRepository.save(modifyLearner);
	}

	@Override
	public void deleteLearner(Long learnerId) {
		Learner learner = this.learnerRepository.getById(learnerId);
		if (learner != null) {
			learner.setMaterials(null);
			Teacher teacher = learner.getTeacher();
			List<Learner> learners = (List<Learner>) teacher.getLearners();
			learners.remove(learner);
			teacher.setLearners(learners);
			this.teacherRepository.save(teacher);
			List<Message> messages = (List<Message>) learner.getMessages();
			for (Message message : messages) {
				this.messageRepository.delete(message);
			}
			List<Result> results = (List<Result>) learner.getResults();
			for (Result result : results) {
				this.resultRepository.delete(result);
			}
			this.learnerRepository.delete(learner);
		}
	}

	@Override
	public Integer podschetTrue(Test test) {
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.addAll(test.getQuestions());
		Test trueTest = this.testRepository.getById(test.getId());
		ArrayList<Question> trueQuestions = new ArrayList<Question>();
		trueQuestions.addAll(trueTest.getQuestions());
		double sumAllParts = 0;
		double trueAns = 0;
		double nesovpAns = 0;
		for (int i = 0; i < questions.size(); i++) {
			for (int j = 0; j < 4; j++) {
				if ((questions.get(i).getAnswers().get(j).getIsTrue() == trueQuestions.get(i).getAnswers().get(j)
						.getIsTrue()) && questions.get(i).getAnswers().get(j).getIsTrue()) {
					trueAns++;
				}
				if (questions.get(i).getAnswers().get(j).getIsTrue() != trueQuestions.get(i).getAnswers().get(j)
						.getIsTrue()) {
					nesovpAns++;
				}
			}
			sumAllParts += trueAns / (nesovpAns + trueAns);
			trueAns = 0;
			nesovpAns = 0;
		}
		return (int) Math.round(sumAllParts * 100 / (questions.size()));
	}

	@Override
	public Result addResult(Integer procent, Learner learner, Test test) {
		Result result = this.resultRepository.findByLearnerAndTest(learner, test);
		if (result != null) {
			result.setProcent(procent);
		} else {
			result = new Result(procent);
			result.setLearner(learner);
			result.setTest(this.testRepository.getById(test.getId()));
		}
		this.resultRepository.save(result);
		return result;
	}

	@Override
	public Collection<Result> resultByLearnerId(Long learnerId) {
		return this.learnerRepository.getById(learnerId).getResults();
	}

	@Override
	public void updateResult(Result modifyResult) {
		this.resultRepository.save(modifyResult);
	}

	@Override
	public Integer formStatistics(Long learnerId) {
		Collection<Result> results = this.learnerRepository.getById(learnerId).getResults();
		double statistic = 0;
		for (Result result : results) {
			statistic += result.getProcent();
		}
		return (int) Math.round(statistic / results.size());
	}

	@Override
	public UserDetails loadUserByUsername(String surname) throws UsernameNotFoundException {
		Teacher teacher = teacherRepository.findBySurname(surname);
		if (teacher == null) {
			Learner learner = learnerRepository.findBySurname(surname);
			if (learner == null) {
				throw new UsernameNotFoundException("Invalid username or password.");
			}
			HashSet<GrantedAuthority> hga = new HashSet();
			hga.add(new SimpleGrantedAuthority("LEARNER"));
			return new org.springframework.security.core.userdetails.User(learner.getSurname(), learner.getPassword(),
					hga);
		}
		HashSet<GrantedAuthority> hga = new HashSet();
		hga.add(new SimpleGrantedAuthority("TEACHER"));
		return new org.springframework.security.core.userdetails.User(teacher.getSurname(), teacher.getPassword(), hga);
	}

	@Override
	public List<Material> allMaterials() {
		return this.materialRepository.findAll();
	}

	@Override
	public List<Learner> allLearners() {
		return this.learnerRepository.findAll();
	}

}
