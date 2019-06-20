package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @ApiOperation(value = "Return all students", response = Student.class, responseContainer = "List")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(@PageableDefault(page = 0, size = 5) Pageable pageable)
    {
        List<Student> myStudents = studentService.findAll(pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @ApiOperation(value = "Retrieves a student associated with the student ID", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student Found", response = Student.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetail.class)})
    @GetMapping(value = "/student/{studentid}", produces = {"application/json"})
    public ResponseEntity<?> getStudentById(@ApiParam(value = "Student id", required = true, example = "1") @PathVariable Long studentid)
    {
        Student r = studentService.findStudentById(studentid);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @ApiOperation(value = "Retrieves a student associated with the studentid", response = Student.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). " + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Students Found", response = Student.class),
            @ApiResponse(code = 404, message = "Students Not Found", response = ErrorDetail.class)})
    @GetMapping(value = "/student/namelike/{name}", produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(@ApiParam(value = "Partial name", required = true, example = "john") @PathVariable String name, @PageableDefault(page = 0, size = 5) Pageable pageable)
    {
        List<Student> myStudents = studentService.findStudentByNameLike(name, pageable);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @ApiOperation(value = "Creates a new student", notes = "The newly created student id will be sent in the location header via URL", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student created successfully", response = void.class),
            @ApiResponse(code = 500, message = "Error creating student", response = ErrorDetail.class)})
    @PostMapping(value = "/student", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody Student newStudent) throws URISyntaxException
    {
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @ApiOperation(value = "Updates an existing student", notes = "The updated student id will be stored for future retrieval", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student updated successfully", response = void.class),
            @ApiResponse(code = 500, message = "Error updating student", response = ErrorDetail.class)})
    @PutMapping(value = "/student/{studentid}")
    public ResponseEntity<?> updateStudent(@RequestBody Student updateStudent, @ApiParam(value = "Student ID", required = true, example = "1") @PathVariable long studentid)
    {
        studentService.update(updateStudent, studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USERS')")
    @ApiOperation(value = "Deletes an existing student", notes = "The deleted student will be removed permanently", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Student deleted successfully", response = void.class),
            @ApiResponse(code = 500, message = "Error deleting student", response = ErrorDetail.class)})
    @DeleteMapping("/student/{studentid}")
    public ResponseEntity<?> deleteStudentById(@ApiParam(value = "Student ID", required = true, example = "1") @PathVariable long studentid)
    {
        studentService.delete(studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
