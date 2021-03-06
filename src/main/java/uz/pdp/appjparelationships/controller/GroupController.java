package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

    /**
     * FOR MINISTRY
     * @return
     */
    @GetMapping
    public List<Group> getGroups() {
        return groupRepository.findAll();
    }


    // FOR UNIVERSITY
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId) {
        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(universityId);
        List<Group> groupsByUniversityId = groupRepository.getGroupsByUniversityId(universityId);
        List<Group> groupsByUniversityIdNative = groupRepository.getGroupsByUniversityIdNative(universityId);
        return allByFaculty_universityId;
    }

    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto) {

        Group group = new Group();
        group.setName(groupDto.getName());

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()) {
            return "Such faculty not found";
        }

        group.setFaculty(optionalFaculty.get());

        groupRepository.save(group);
        return "Group added";
    }

    @DeleteMapping("/{id}")
    public String deleteGroupById(@PathVariable Integer id) {
        try {
            Optional<Group> byId = groupRepository.findById(id);
            if (byId.isPresent())
                groupRepository.deleteById(id);
            return "Group deleted!";
        } catch (Exception e) {
            return "Error or Group no found";
        }
    }

    @PutMapping("/{id}")
    public String deleteByGroupId(@PathVariable Integer id, @RequestBody GroupDto groupDto) {
        Optional<Group> byId = groupRepository.findById(id);
        if (!byId.isPresent())
            return "Group not found!";
        Group group = byId.get();
        Optional<Faculty> facultyById = facultyRepository.findById(groupDto.getFacultyId());
        if (facultyById.isPresent()) {
            Faculty faculty = facultyById.get();
            group.setFaculty(faculty);
            group.setName(groupDto.getName());
            groupRepository.save(group);
            return "Group edited!";
        }
        return "Wrong Faculty ID!";
    }
}

