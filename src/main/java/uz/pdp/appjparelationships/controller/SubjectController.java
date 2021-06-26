package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.SubjectDto;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    //CREATE
    @PostMapping
    public String addSubject(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }

    //READ
//    @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Integer id) {
        Optional<Subject> byId = subjectRepository.findById(id);
        return byId.orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deleteSubjeect(@PathVariable Integer id) {
        Optional<Subject> byId = subjectRepository.findById(id);
        if (byId.isPresent()) {
            subjectRepository.deleteById(id);
            return "Deleted !";
        }
        return "Subject not found";
    }

    @PostMapping("/{id}")
    public String editSubject(@PathVariable Integer id, @RequestBody SubjectDto subjectDto) {
        Optional<Subject> byId = subjectRepository.findById(id);
        if (!byId.isPresent()) return "Subject not found";
        Subject subject = byId.get();
        subject.setName(subjectDto.getName());
        subjectRepository.save(subject);
        return "Edited";
    }

}
