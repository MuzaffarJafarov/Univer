package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;


    //1. FOR MINISTRY
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAll(pageable);
    }

    //2. FOR UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
    }

    //3. FOR FACULTY
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable Integer facultyId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
    }

    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable Integer groupId, @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto) {
        Optional<Address> addressById = addressRepository.findById(studentDto.getAddress().getId());
        if (!addressById.isPresent())
            return "Wrong Address!";
        Boolean aBoolean = studentRepository.existsByAddressAndFirstNameAndLastName(studentDto.getAddress(), studentDto.getFirstName(), studentDto.getLastName());
        if (!aBoolean) {
            Student student = new Student();
            student.setAddress(studentDto.getAddress());
            student.setGroup(studentDto.getGroup());
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setSubjects(studentDto.getSubjects());
            studentRepository.save(student);
            return "Created new Student";
        } else return "This Student is already exist!";
    }

    @DeleteMapping("/{id}")
    public String deleteStudentById(@PathVariable Integer id) {
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isPresent()) {
            studentRepository.deleteById(id);
            return "Student deleted !";
        }
        return "Wrong Student ID !";
    }


    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (!studentOptional.isPresent()) return "Wrong student info";
        Student student = studentOptional.get();
        Optional<Address> addressById = addressRepository.findById(studentDto.getAddress().getId());
        boolean groupById = groupRepository.existsById(studentDto.getGroup().getId());
        if (addressById.isPresent() && groupById) {
            student.setGroup(studentDto.getGroup());
            student.setLastName(studentDto.getLastName());
            student.setFirstName(studentDto.getFirstName());
            student.setAddress(studentDto.getAddress());
            Boolean aBoolean = studentRepository.existsByAddressAndFirstNameAndLastNameAndGroup(studentDto.getAddress(), studentDto.getFirstName(), studentDto.getLastName(), studentDto.getGroup());
            if (!aBoolean)
                studentRepository.save(student);
            return "Edited";

        }
        return "Wrong new Address or Group";

    }

}
