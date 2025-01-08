package com.aj.clgportal.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.JwtAuthResponse;
import com.aj.clgportal.dto.StudentDto;
import com.aj.clgportal.entity.Role;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.exception.ResourceNotFoundException;
import com.aj.clgportal.repository.RoleRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	TeacherRepository teacherRepo;

	JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();

	@Override
	public StudentDto newStudent(StudentDto studDto) {
		Student student = new Student();
		student.setFirstName(studDto.getFirstName());
		student.setMiddleName(studDto.getMiddleName());
		student.setLastName(studDto.getLastName());
		student.setUserName(studDto.getUserName());
		student.setEmail(studDto.getEmail());
		student.setPassword(studDto.getPassword());
		student.setGuardianName(studDto.getGuardianName());
		student.setProfilePic(studDto.getProfilePic());
		student.setStatus(studDto.getStatus());

		Teacher teacher = teacherRepo.findByUsernameOrEmail(jwtAuthResponse.getLoginId(), jwtAuthResponse.getLoginId())
				.orElseThrow(() -> new UsernameNotFoundException("Teacher does not exists by username or email."));
		student.setTeacher(teacher);

		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_STUDENT");
		roles.add(userRole);
		student.setRoles(roles);

		Student newStudent = studentRepo.save(student);
		StudentDto stud = StudentToDto(newStudent);
		return stud;
	}

	@Override
	public StudentDto updateStudent(StudentDto studDto, long id) {
		Student student = studentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "student id", id));
		student.setFirstName(studDto.getFirstName());
		student.setMiddleName(studDto.getMiddleName());
		student.setLastName(studDto.getLastName());
		student.setUserName(studDto.getUserName());
		student.setEmail(studDto.getEmail());
		student.setPassword(studDto.getPassword());
		student.setGuardianName(studDto.getGuardianName());
		student.setProfilePic(studDto.getProfilePic());
		student.setStatus(studDto.getStatus());

		Teacher teacher = teacherRepo.findByUsernameOrEmail(jwtAuthResponse.getLoginId(), jwtAuthResponse.getLoginId())
				.orElseThrow(() -> new UsernameNotFoundException("Teacher does not exists by username or email."));
		student.setTeacher(teacher);

		List<Role> roles = new ArrayList<>();
		Role userRole = roleRepo.findByName("ROLE_STUDENT");
		roles.add(userRole);
		student.setRoles(roles);
		Student updatedStudent = studentRepo.save(student);
		StudentDto stud = StudentToDto(updatedStudent);
		return stud;
	}

	@Override
	public void deleteStudent(long id) {
		Student student = studentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "student id", id));
		studentRepo.delete(student);
	}

	@Override
	public StudentDto getStudentById(long id) {
		Student student = studentRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student", "student id", id));
		StudentDto studentToDto = StudentToDto(student);
		return studentToDto;
	}

	@Override
	public List<StudentDto> getAllStudents() {
		List<Student> list = studentRepo.findAll();
		List<StudentDto> students = list.stream().map((lst) -> StudentToDto(lst)).collect(Collectors.toList());
		return students;
	}

	public StudentDto StudentToDto(Student stud) {
		StudentDto studentDto = modelMapper.map(stud, StudentDto.class);
		return studentDto;
	}

	public Student DtoToStudent(StudentDto studDto) {
		Student student = modelMapper.map(studDto, Student.class);
		return student;
	}

}
