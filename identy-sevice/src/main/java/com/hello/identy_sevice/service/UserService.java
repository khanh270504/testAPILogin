package com.hello.identy_sevice.service;

import com.hello.identy_sevice.dto.request.UserCreationRequest;
import com.hello.identy_sevice.dto.request.UserUpdateRequest;
import com.hello.identy_sevice.dto.response.UserResponse;
import com.hello.identy_sevice.entity.User;
import com.hello.identy_sevice.enums.Role;
import com.hello.identy_sevice.exception.AppException;
import com.hello.identy_sevice.exception.ErrorCode;
import com.hello.identy_sevice.mapper.UserMapper;
import com.hello.identy_sevice.repository.RoleRepository;
import com.hello.identy_sevice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

     UserRepository UserRepository;
     RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
     UserMapper userMapper;

    public UserResponse createRequest(UserCreationRequest request) {
        if(UserRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);
        User user   = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
      //  user.setRoles(roles);

        return userMapper.toUserResponse(UserRepository.save(user));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
      String name =  context.getAuthentication().getName();
        User user = UserRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED) );
        return userMapper.toUserResponse(user);

    }

    public UserResponse UpdateRequest(String id, UserUpdateRequest request) {
        User user = UserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(UserRepository.save(user));
    }

   // @PostAuthorize("hasRole('ADMIN')")
  //  @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<UserResponse> getAllUsers() {
        log.info("In method get Users");
        return UserRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @PostAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        log.info("In method get user by id");
      return  userMapper.toUserResponse(UserRepository.findById(id)
                .orElseThrow(() ->
                    new AppException(ErrorCode.USER_NOT_EXISTED)
                ));

    }

    public void deleteUser(String id) {
        UserRepository.deleteById(id);
    }
}
