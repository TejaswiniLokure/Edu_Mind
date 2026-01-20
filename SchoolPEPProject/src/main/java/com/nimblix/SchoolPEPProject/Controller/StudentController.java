package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.StudentRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.StudentDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;


    /*
    In this API we are registering the student. It will help to onboard the student, In this we are storing the  student
    fullName,emailId and password.
     */
    @PostMapping("/register")
    public ResponseEntity<?> studentRegistration(@RequestBody StudentRegistrationRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            studentService.registerStudent(request);

            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
            response.put(SchoolConstants.MESSAGE, "Student Registration Successful!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

 /*
       This API is used to fetch the student details by using the student I'd.
 */

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getStudentsBySchoolId(
            @RequestParam Long schoolId) {

        List<StudentDetailsResponse> students =
                studentService.getStudentsBySchoolId(schoolId);

        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
        response.put(SchoolConstants.MESSAGE, "Students fetched successfully");
        response.put("data", students);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/update")
    public ResponseEntity<?> updateStudent(
            @RequestParam Long studentId,
            @RequestBody StudentRegistrationRequest request) {

        Map<String, Object> response = new HashMap<>();

        try {
            studentService.updateStudentDetails(studentId, request);
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
            response.put(SchoolConstants.MESSAGE, "Student updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
            response.put(SchoolConstants.MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



        @PostMapping("/delete")
        public ResponseEntity<?> deleteStudent(@RequestParam Long studentId) {
            Map<String, Object> response = new HashMap<>();

            try {
                studentService.deleteStudent(studentId);
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student deleted successfully");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }



        @PutMapping("/update")
        public ResponseEntity<?> updateStudentPut(
                @RequestParam Long studentId,
                @RequestBody StudentRegistrationRequest request) {

            Map<String, Object> response = new HashMap<>();

            try {
                studentService.updateStudentDetails(studentId, request);
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student updated successfully (PUT)");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }



        @DeleteMapping("/delete")
        public ResponseEntity<?> deleteStudentDelete(@RequestParam Long studentId) {
            Map<String, Object> response = new HashMap<>();

            try {
                studentService.deleteStudent(studentId);
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS);
                response.put(SchoolConstants.MESSAGE, "Student deleted successfully (DELETE)");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                response.put(SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE);
                response.put(SchoolConstants.MESSAGE, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }


