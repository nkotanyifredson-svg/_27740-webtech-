package auca.ac.rw.RestfullApi.controller.studentRegistration;

import auca.ac.rw.RestfullApi.model.studentregistration.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> students = new ArrayList<>();
    private Long nextId = 6L;
   
    public StudentController() {
        
        students.add(new Student(1L, "John", "Doe", "john.doe@email.com", "Computer Science", 3.8));
        students.add(new Student(2L, "Jane", "Smith", "jane.smith@email.com", "Computer Science", 3.5));
        
        
        students.add(new Student(3L, "Bob", "Johnson", "bob.johnson@email.com", "Mechanical Engineering", 3.2));
        students.add(new Student(4L, "Alice", "Williams", "alice.williams@email.com", "Electrical Engineering", 3.9));
        
      
        students.add(new Student(5L, "Charlie", "Brown", "charlie.brown@email.com", "Business Administration", 2.8));
    }

    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

 
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Student student = findStudentById(studentId);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> studentsByMajor = students.stream()
                .filter(student -> student.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
        
        if (!studentsByMajor.isEmpty()) {
            return new ResponseEntity<>(studentsByMajor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGpa(@RequestParam Double gpa) {
        List<Student> filteredStudents = students.stream()
                .filter(student -> student.getGpa() >= gpa)
                .collect(Collectors.toList());
        
        if (!filteredStudents.isEmpty()) {
            return new ResponseEntity<>(filteredStudents, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        student.setStudentId(nextId++);
        students.add(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    
    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student updatedStudent) {
        Student existingStudent = findStudentById(studentId);
        
        if (existingStudent != null) {
           
            existingStudent.setFirstName(updatedStudent.getFirstName());
            existingStudent.setLastName(updatedStudent.getLastName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setMajor(updatedStudent.getMajor());
            existingStudent.setGpa(updatedStudent.getGpa());
            
            return new ResponseEntity<>(existingStudent, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    private Student findStudentById(Long studentId) {
        return students.stream()
                .filter(student -> student.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }
}