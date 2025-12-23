package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/teacherRegister")
    public Map<String, String> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
        return teacherService.registerTeacher(request);
    }

    @GetMapping("/getTeacher")
    public ResponseEntity<TeacherDetailsResponse> getTeacherDetails(
            @RequestParam Long teacherId) {

        TeacherDetailsResponse response =
                teacherService.getTeacherDetails(teacherId);

        return ResponseEntity.ok(response);
    }


    @PutMapping(
            value = "/updateTeacher",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> updateTeacherDetails(
            @RequestBody TeacherRegistrationRequest request,
            @RequestParam Long teacherId) {

        return teacherService.updateTeacherDetails(request, teacherId);
    }



    @DeleteMapping("/delete")
    public  Map<String,String> deleteTeacherRecord(@RequestParam Long teacherId, @RequestParam Long schoolId){
        return  teacherService.deleteTeacherDetails(teacherId,schoolId);
    }


    @PostMapping("/createClassroom")
    public ResponseEntity<Map<String, String>> createClassroom(@RequestBody ClassroomRequest request) {
        return teacherService.createClassroom(request);
    }

    @PostMapping(
            value = "/createAssignment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> createAssignment(
            @RequestPart String assignmentJson,
            @RequestPart MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateAssignmentRequest request =
                    objectMapper.readValue(assignmentJson, CreateAssignmentRequest.class);

            return ResponseEntity.ok(
                    teacherService.createAssignment(request, files)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS,SchoolConstants.STATUS_ERORR ,
                            SchoolConstants.MESSAGE, "Invalid assignment payload"
                    )
            );
        }
    }


    @PostMapping(
            value = "/updateAssignment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> updateAssignment(
            @RequestPart String assignmentJson,
            @RequestPart(required = false) MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateAssignmentRequest request =
                    objectMapper.readValue(assignmentJson, CreateAssignmentRequest.class);

            return ResponseEntity.ok(
                    teacherService.updateAssignment(request, files)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE, "Invalid assignment payload"
                    )
            );
        }
    }


    @PostMapping("/onboardSubject")
    public ResponseEntity<Map<String, String>> onboardSubject(
            @RequestBody OnboardSubjectRequest request
    ) {

        if (request == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE, "Request body is missing"
                    )
            );
        }

        return ResponseEntity.ok(teacherService.onboardSubject(request));
    }


}
