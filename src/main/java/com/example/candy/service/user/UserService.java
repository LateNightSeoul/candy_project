package com.example.candy.service.user;

import com.example.candy.controller.user.dto.UserInfoResponseDto;
import com.example.candy.domain.user.User;
import com.example.candy.repository.user.UserRepository;
import com.example.candy.service.candyHistory.CandyHistoryService;
import com.example.candy.service.email.MailService;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CandyHistoryService candyHistoryService;
    @Autowired private MailService mailService;

    @Transactional
    public User join(String email, boolean emailCheck, String password, String parentPassword, String name, String phone, String birth) {
        checkArgument(isNotEmpty(password), "password must be provided.");
        checkArgument(
                password.length() >= 4 && password.length() <= 15,
                "password length must be between 4 and 15 characters."
        );
        checkArgument(emailCheck, "have to verify email");

        if (findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already exists.");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .parentPassword(passwordEncoder.encode(parentPassword))
                .name(name)
                .phone(phone)
                .birth(birth)
                .enabled(true)
                .createDate(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .loginCount(1)
                .build();

        User savedUser = save(user);
        candyHistoryService.initCandy(savedUser);
        return savedUser;
    }

    @Transactional
    public User login(String email, String password) throws NotFoundException {
        checkArgument(password != null, "password must be provided.");

        User user = findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        user.verifyPassword(passwordEncoder, password);
        user.afterLoginSuccess();
        return user;
    }

    @Transactional
    public User parentLogin(Long userId, String parentPassword) throws NotFoundException {
        checkArgument(parentPassword != null, "parentPassword must be provided.");
        User user = findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        user.verifyParentPassword(passwordEncoder, parentPassword);
        return user;
    }
    
    @Transactional
    public String find_email(String name) throws NotFoundException {
    	checkArgument(name != null, "name must be provided.");
    	
    	User user = findByName(name)
    			.orElseThrow(() -> new NotFoundException("User Not Found"));
    	
    	return new StringBuilder(user.getEmail()).toString();
    }
    
    @Transactional
    public Boolean email(String email) throws NotFoundException {
 
    	User user = findByEmail(email)
    			.orElseThrow(() -> new NotFoundException("User Not Found"));
    	
    	
    	mailService.mailSend(user);
    	
    	return true;
    }
    
    @Transactional
    public Boolean validate(String email, String auth) throws NotFoundException {
 
    	User user = findByEmail(email)
    			.orElseThrow(() -> new NotFoundException("User Not Found"));
    	
    	
    	if(user.getAuthCode().equals(auth)) {
    		user.setAuth(true);
    		return true;
    	} else {
    		user.setAuth(false);
    		return false;
    	}
    }
    
    @Transactional
    public Boolean new_pw(String email, String password) throws NotFoundException {
 
    	User user = findByEmail(email)
    			.orElseThrow(() -> new NotFoundException("User Not Found"));
    	
    	
    	if(user.isAuth() == true) {
    		user.setPassword(new BCryptPasswordEncoder().encode(password).toString());
    		return true;
    	} else {
    		return false;
    	}
    }

    public UserInfoResponseDto getUserInfo(Long userId) throws NotFoundException {
        User user = findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        return new UserInfoResponseDto(user.getEmail(), user.getName(), user.getPhone(), user.getBirth());
    }
    @Transactional
    public UserInfoResponseDto changeUserInfo(Long userId, String name, String phone, String birth) throws NotFoundException {
        checkArgument(name != null && phone != null && birth != null, "nothing has to be null");
        checkArgument(name.length() >= 2, "name length must be over 1");
        User user = findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        user.setName(name);
        user.setPhone(phone);
        user.setBirth(birth);
        User savedUser = save(user);
        return new UserInfoResponseDto(savedUser.getEmail(), savedUser.getName(), savedUser.getPhone(), savedUser.getBirth());
    }
    @Transactional
    public User changePassword(Long userId, String newPassword, String originPassword) throws NotFoundException {
        checkArgument(
                newPassword.length() >= 4 && newPassword.length() <= 15,
                "password length must be between 4 and 15 characters."
        );
        User user = findById(userId)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
        user.verifyPassword(passwordEncoder, originPassword);
        user.compareNewPassword(passwordEncoder, newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        save(user);
        return user;
    }

//    
//    @Transactional
//    public Boolean password_matches(String email, String password) throws NotFoundException {
// 
//    	User user = findByEmail(email)
//    			.orElseThrow(() -> new NotFoundException("User Not Found"));
//    	
//    	
//    	if(new BCryptPasswordEncoder().matches(password, user.getPassword())) {
////    		user.setPassword(new BCryptPasswordEncoder().encode(password).toString());
//    		return true;
//    	} else {
//    		return false;
//    	}
//    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        checkArgument(email != null, "email must be provided.");

        return userRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByName(String name) {
    	checkArgument(name != null, "name must be provided.");
    	
    	return userRepository.findByName(name);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    private User save(User user) { return userRepository.save(user); }
}
