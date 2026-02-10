package auca.ac.rw.question2_student_api.controller.student;
import auca.ac.rw.question2_student_api.model.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> students = new ArrayList<>();

    public StudentController() {
        // Initialize with sample students
        students.add(new Student(1L, "John", "Doe", "john.doe@email.com", "Computer Science", 3.8));
        students.add(new Student(2L, "Jane", "Smith", "jane.smith@email.com", "Mathematics", 3.9));
        students.add(new Student(3L, "Bob", "Johnson", "bob.johnson@email.com", "Physics", 3.2));
        students.add(new Student(4L, "Alice", "Brown", "alice.brown@email.com", "Computer Science", 3.7));
        students.add(new Student(5L, "Charlie", "Wilson", "charlie.wilson@email.com", "Biology", 3.1));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Optional<Student> student = students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();

        if (student.isPresent()) {
            return new ResponseEntity<>(student.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> studentsByMajor = students.stream()
                .filter(student -> student.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());

        return new ResponseEntity<>(studentsByMajor, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGPA(@RequestParam Double gpa) {
        List<Student> filteredStudents = students.stream()
                .filter(student -> student.getGpa() >= gpa)
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredStudents, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        // Generate new ID
        long newId = students.stream()
                .mapToLong(Student::getStudentId)
                .max()
                .orElse(0) + 1;
        student.setStudentId(newId);

        students.add(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student updatedStudent) {
        Optional<Student> existingStudent = students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();

        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            student.setEmail(updatedStudent.getEmail());
            student.setMajor(updatedStudent.getMajor());
            student.setGpa(updatedStudent.getGpa());

            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


