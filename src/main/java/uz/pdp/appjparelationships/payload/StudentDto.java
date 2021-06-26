package uz.pdp.appjparelationships.payload;

import lombok.Data;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Subject;

import java.util.List;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;
    private Group group;
    private Address address;
    private List<Subject> subjects;
}
