package com.aj.clgportal.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aj.clgportal.dto.ChangePasswordRequest;
import com.aj.clgportal.entity.Admin;
import com.aj.clgportal.entity.Student;
import com.aj.clgportal.entity.Teacher;
import com.aj.clgportal.repository.AdminRepository;
import com.aj.clgportal.repository.StudentRepository;
import com.aj.clgportal.repository.TeacherRepository;
import com.aj.clgportal.service.UserPasswordRequestService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserPasswordRequestImpl implements UserPasswordRequestService{
	@Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private AdminRepository adminRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @Override
	public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
		String username = request.getEmail();
        // 🔍 Find user from all tables
        Object user = teacherRepo.findByUsernameOrEmail(username,username);

        if (user == null) {
            user = studentRepo.findByUsernameOrEmail(username,username);
        }

        if (user == null) {
            user = adminRepo.findByUsernameOrEmail(username,username);
        }

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String storedPassword = null;

        if (user instanceof Teacher) {
            storedPassword = ((Teacher) user).getPassword();
        } else if (user instanceof Student) {
            storedPassword = ((Student) user).getPassword();
        } else if (user instanceof Admin) {
            storedPassword = ((Admin) user).getPassword();
        }

        // ✅ Check current password
        if (!encoder.matches(request.getCurrentPassword(), storedPassword)) {
            return ResponseEntity.badRequest().body("Current password incorrect");
        }

        // ✅ Validate new password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        String newHashed = encoder.encode(request.getNewPassword());

        // ✅ Save based on type
        if (user instanceof Teacher) {
            ((Teacher) user).setPassword(newHashed);
            teacherRepo.save((Teacher) user);
        } else if (user instanceof Student) {
            ((Student) user).setPassword(newHashed);
            studentRepo.save((Student) user);
        } else if (user instanceof Admin) {
            ((Admin) user).setPassword(newHashed);
            adminRepo.save((Admin) user);
        }

        return ResponseEntity.ok("Password updated successfully");
	}

	

}
