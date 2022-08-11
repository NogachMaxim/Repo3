package com.example.demo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Answer;
import com.example.demo.model.Learner;
import com.example.demo.model.Material;
import com.example.demo.model.MesRole;
import com.example.demo.model.Message;
import com.example.demo.model.Question;
import com.example.demo.model.ResponseServ;
import com.example.demo.model.Teacher;
import com.example.demo.model.Test;
import com.example.demo.model.TextMesAndIdLWith;
import com.example.demo.service.TLServiceImpl;

@Controller
public class MainController {

	private TLServiceImpl tLService;

	public MainController(TLServiceImpl tLService) {
		super();
		this.tLService = tLService;
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String index(Model model) {
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString()
				.equals("TEACHER")) {
			Teacher teacher = this.tLService
					.teacherBySurname(SecurityContextHolder.getContext().getAuthentication().getName());
			if (model.getAttribute("surnameL") != null) {
				model.addAttribute("messages", this.tLService.messagesL(model.getAttribute("surnameL").toString()));
				model.addAttribute("idLearnerWith",
						this.tLService.learnerBySurname(model.getAttribute("surnameL").toString()).getId());

			} else {
				model.addAttribute("messages", null);
				model.addAttribute("idLearnerWith", null);
			}
			model.addAttribute("learnersAskJoin", this.tLService.learnersAskJoin(teacher.getId()));
			model.addAttribute("teacher", teacher);
			model.addAttribute("learners", teacher.getLearners());
			model.addAttribute("allTest", this.tLService.allTest());
			model.addAttribute("materials", this.tLService.allMaterials());
			return "ProfilT";
		} else {
			Learner learner = this.tLService
					.learnerBySurname(SecurityContextHolder.getContext().getAuthentication().getName());
			model.addAttribute("learner", learner);
			if (learner.getTeacher() == null) {
				model.addAttribute("teachers", this.tLService.allTeachers());
			} else {
				model.addAttribute("teacherJoin", learner.getTeacher());
			}
			model.addAttribute("materials", learner.getMaterials());
			model.addAttribute("allTest", this.tLService.allTest());
			ArrayList<Message> messages = new ArrayList<Message>();
			messages.addAll(learner.getMessages());
			messages.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
			model.addAttribute("messages", messages);
			model.addAttribute("statistic", this.tLService.formStatistics(learner.getId()));
			return "ProfilL";
		}
	}

	/*
	 * @GetMapping("/getBooks") public ResponseEntity<Object> getAllBooks() {
	 * ServiceResponse<List<Book>> response = new ServiceResponse<>("success",
	 * bookStore); return new ResponseEntity<Object>(response, HttpStatus.OK); }
	 */

	@PostMapping("/deleteTeacher/{id}")
	public String deleteTeacher(Model model, @PathVariable("id") Long id) {
		this.tLService.deleteTeacher(id);
		return "redirect:/login";
	}

	@PostMapping("/deleteLearner/{id}")
	public String deleteLearner(Model model, @PathVariable("id") Long id) {
		this.tLService.deleteLearner(id);
		return "redirect:/login";
	}

	@GetMapping("/registrTeacher")
	public String registrationTeacher(Model model) {
		model.addAttribute("newTeacher", new Teacher());
		return "RegistrationTeacher";
	}

	@GetMapping("/registrLearner")
	public String registrationLearner(Model model) {
		model.addAttribute("newLearner", new Learner());
		return "RegistrationLearner";
	}

	@PostMapping("/registrTeacher")
	public String registTeacher(@ModelAttribute("newTeacher") Teacher teacher) {
		this.tLService.registrTeacher(teacher.getSurname(), teacher.getName(), teacher.getPatronymic(),
				teacher.getPassword());
		return "redirect:/login";
	}

	@PostMapping("/registrLearner")
	public String registLearner(@ModelAttribute("newLearner") Learner learner) {
		this.tLService.registrLearner(learner.getSurname(), learner.getName(), learner.getPatronymic(),
				learner.getPassword());
		return "redirect:/login";
	}

	@PostMapping("/joinTeacher/{id}")
	public String askJoinTeacher(@PathVariable("id") Long learnerId, @RequestParam("teacher") String surnameT) {
		this.tLService.saveMessage("Ask join to" + surnameT, "LEARNER", learnerId);
		return "redirect:/";
	}

	@PostMapping("/joinLearner/{id}")
	public String joinLearner(@PathVariable("id") Long teacherId, @RequestParam("learner") String surnameL) {
		this.tLService.accessionL(surnameL, teacherId);
		return "redirect:/";
	}

	@GetMapping("/chooseLearner")
	public String chooseLearner(Model model, @RequestParam("learner") String surnameL) {
		model.addAttribute("surnameL", surnameL);
		return index(model);
	}

	@PostMapping("/addMessageFromT")
	public ResponseEntity<Object> addMessageFromT(
			/*
			 * Model model, @RequestParam("addingMessage") String messageText,
			 * 
			 * @RequestParam("IdLearnerWith") Long IdLearnerWith
			 */ @RequestBody TextMesAndIdLWith textMesAndIdLWith) {
		this.tLService.saveMessage(textMesAndIdLWith.getTextMes(), "TEACHER", textMesAndIdLWith.getIdLWith());
		List<Message> messages = this.tLService.messagesLByIdL(textMesAndIdLWith.getIdLWith());
		List<MesRole> messRoles=new ArrayList<MesRole>();
		for(Message mes : messages) {
			messRoles.add(new MesRole(mes, mes.getRole().getName()));
		}
		ResponseServ<List<MesRole>> responseServ = new ResponseServ<List<MesRole>>("success", messRoles);
		return new ResponseEntity<Object>(responseServ, HttpStatus.OK);
	}

	@PostMapping("/addMessageFromL")
	public ResponseEntity<Object> addMessageFromL(@RequestBody TextMesAndIdLWith textMesAndIdLWith) {
		/*this.tLService.saveMessage(messageText, "LEARNER", IdLearner);
		/* return index(model); */
		this.tLService.saveMessage(textMesAndIdLWith.getTextMes(), "LEARNER", textMesAndIdLWith.getIdLWith());
		List<Message> messages = this.tLService.messagesLByIdL(textMesAndIdLWith.getIdLWith());
		List<MesRole> messRoles=new ArrayList<MesRole>();
		for(Message mes : messages) {
			messRoles.add(new MesRole(mes, mes.getRole().getName()));
		}
		ResponseServ<List<MesRole>> responseServ = new ResponseServ<List<MesRole>>("success", messRoles);
		return new ResponseEntity<Object>(responseServ, HttpStatus.OK);
		}

	@GetMapping("/addMaterial")
	public String addMaterial() {
		return "NewMaterial";
	}

	@PostMapping("/addMaterial")
	public String addMater(@RequestParam("materialName") String materialName,
			@RequestParam("materialText") String materialText) throws IOException {
		Material material = this.tLService.addMaterial(materialName, materialText);
		return "redirect:/";
	}

	@GetMapping("/showMaterials")
	public String showMaterials(Model model) {
		model.addAttribute("materials", this.tLService.allMaterials());
		return "redirect:/";
	}

	@GetMapping("/material/{id}")
	public String showMaterial(Model model, @PathVariable("id") Long id) throws IOException {
		Material material = this.tLService.materialById(id);
		model.addAttribute("materialId", material.getId());
		model.addAttribute("materialName", material.getName());
		model.addAttribute("material", material.getMaterialText());
		model.addAttribute("learners", this.tLService.allLearners());
		model.addAttribute("isTeacher",
				SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0].toString()
						.equals("TEACHER"));
		return "materialById";
	}

	@GetMapping("/updateMaterial/{id}")
	public String updateMaterial(Model model, @PathVariable("id") Long id) {
		Material material = this.tLService.materialById(id);
		model.addAttribute("materialId", material.getId());
		model.addAttribute("materialName", material.getName());
		model.addAttribute("editMaterialText", material.getMaterialText());
		return "EditMaterial";
	}

	@PostMapping("/materialUpdate/edit")
	public String materialUpdate(@RequestParam("materialId") Long materialId, @RequestParam("materialText") String text)
			throws IOException {
		this.tLService.updateMaterial(materialId, text);
		return "redirect:/showMaterials";
	}

	@PostMapping("/deleteMaterial/{id}")
	public String delete(Model model, @PathVariable("id") Long id) {
		this.tLService.deleteMaterial(id);
		model.addAttribute("materials", this.tLService.allMaterials());
		return "Materials";
	}

	@PostMapping("giveMaterialL/{id}")
	public String givaMaterialL(Model model, @PathVariable("id") Long id, @RequestParam("learner") String surnameL) {
		this.tLService.giveMaterialL(id, surnameL);
		return "redirect:/";
	}

	@GetMapping("/addTest")
	public String addTest(Model model, @RequestParam("count") Integer count) {
		Test test = new Test();
		ArrayList<Question> questions = new ArrayList<Question>(count);
		for (int i = 0; i < count; i++) {
			questions.add(i, new Question());
			ArrayList<Answer> answers = new ArrayList<Answer>(4);
			for (int j = 0; j < 4; j++) {
				answers.add(j, new Answer());
			}
			questions.get(i).setAnswers(answers);
		}
		test.setQuestions(questions);
		model.addAttribute("test", test);
		return "NewTest";
	}

	@PostMapping("/addTest")
	public String addTest(Model model, @ModelAttribute("test") Test test) throws IOException {
		this.tLService.addTest(test);
		model.addAttribute("allTest", this.tLService.allTest());
		model.addAttribute("materials", this.tLService.allMaterials());
		return "redirect:/ProfilT";
	}

	@GetMapping("/test/{id}")
	public String showTest(Model model, @PathVariable("id") Long id) throws IOException {
		model.addAttribute("test", this.tLService.testById(id));
		return "TestById";
	}

	@GetMapping("/passTest/{id}")
	public String passTest(Model model, @PathVariable("id") Long id, @ModelAttribute("learnerId") Long learnerId) {
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.addAll(this.tLService.testById(id).getQuestions());
		for (int i = 0; i < questions.size(); i++) {
			for (int j = 0; j < 4; j++) {
				questions.get(i).getAnswers().get(j).setIsTrue(false);
			}
		}
		Test test = this.tLService.testById(id);
		test.setQuestions(questions);
		model.addAttribute("test", test);
		model.addAttribute("learnerId", learnerId);
		return "TestPassById";
	}

	@PostMapping("/checkTest")
	public String checkTest(Model model, @ModelAttribute("test") Test test, @RequestParam("learnerId") Long learnerId) {
		Integer procentTrue = this.tLService.podschetTrue(test);
		this.tLService.saveMessage("You result is " + procentTrue, "TEACHER", learnerId);
		this.tLService.addResult(procentTrue, this.tLService.learnerById(learnerId), test);
		return "redirect:/";
	}

	@PostMapping("/deleteTest/{id}")
	public String deleteTest(Model model, @PathVariable("id") Long id) throws IOException {
		this.tLService.deleteTest(id);
		model.addAttribute("allTest", this.tLService.allTest());
		return "ProfilT";
	}

}
