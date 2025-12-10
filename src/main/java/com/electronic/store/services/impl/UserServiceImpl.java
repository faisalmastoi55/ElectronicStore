package com.electronic.store.services.impl;

import com.electronic.store.entities.Role;
import com.electronic.store.payloads.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.exceptions.ResourceNoteFoundException;
import com.electronic.store.entities.User;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Value("${normal.role.id}")
    private String roleNormalUser;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //encoding password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        //dto -> entity
        User user = dtoToEntity(userDto);

        //fetch role of normal and set it to user
        Role role = roleRepository.findById(roleNormalUser).get();
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        //entity -> dto
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        //get user id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNoteFoundException("User Not Found with given id"));

        //update user
        user.setName(userDto.getName());
        //update email
        user.setPassword(userDto.getPassword());
        user.setGender(userDto.getGender());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());

        //save data
        User saved = userRepository.save(user);
        UserDto userDto1 = entityToDto(saved);

        return userDto1;
    }

    @Override
    public void deleteUser(String userId) {
        //get user id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNoteFoundException("User Not Found with given id"));

        //delete user profile image
        String fullPath = imagePath + user.getImageName();

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (IOException e) {
            e.printStackTrace();
        }

        //delete user
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        //get user id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNoteFoundException("User Not Found with given id"));

        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNoteFoundException("User not found with given email id"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> userList = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = userList.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    private UserDto entityToDto(User savedUser) {
        /*UserDto userDto = UserDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .password(savedUser.getPassword())
                .gender(savedUser.getGender())
                .about(savedUser.getAbout())
                .imageName(savedUser.getImageName()).build();
*/
        return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
        /*User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .gender(userDto.getGender())
                .about(userDto.getAbout())
                .imageName(userDto.getImageName()).build();*/

        return mapper.map(userDto, User.class);
    }
}
