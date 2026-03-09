package com.example.demo.services;

import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.UserCreateDTO;
import com.example.demo.dto.UserCreateEnterpriseDTO;
import com.example.demo.dto.UserResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.RoleName;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.repositories.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EnterpriseRepository enterpriseRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, EnterpriseRepository enterpriseRepository,
                       CandidateRepository candidateRepository,
                       PasswordEncoder passwordEncoder,
                       ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.enterpriseRepository = enterpriseRepository;
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findyById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public UserResponse insertCandidate(UserCreateDTO objDto) {
        if(userRepository.existsByEmail(objDto.getEmail())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }
        User user = new User();

        Role role = roleService.insert("CANDIDATO");


        user.setEmail(objDto.getEmail());
        user.setPassword(passwordEncoder.encode(objDto.getPassword()));

        userRepository.save(user);
        createCandidate(user, objDto.getCandidateName(), objDto.getCpf(), objDto.getContact());

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse insertEnterprise(UserCreateEnterpriseDTO objDto) {
        User user = new User();

        Role role = roleService.insert("EMPRESA");

        user.setEmail(objDto.getEmail());
        user.getRoles().add(role);
        userRepository.save(user);

        createEnterprise(user, objDto.getEnterpriseName(), objDto.getCnpj(), objDto.getContact());

        return UserResponse.fromEntity(user);
    }

    private void createCandidate(User user, String candidateName, String cpf, String contact) {
        Candidate candidate = new Candidate();
        candidate.setUser(user);
        candidate.setFullName(candidateName);
        candidate.setContact(contact);
        candidate.setCpf(cpf);

        Profile profile = createProfile(candidate);

        candidate.setProfile(profile);
        candidateRepository.save(candidate);

    }

    private Profile createProfile(Candidate candidate) {
        Profile profile = new Profile();
        return profileRepository.save(profile);
    }

    private void createEnterprise(User user, String enterpriseName, String cnpj, String contact) {
        Enterprise enterprise = new Enterprise();
        enterprise.setUser(user);
        enterprise.setEnterpriseName(enterpriseName);
        enterprise.setCnpj(cnpj);
        enterprise.setContact(contact);
        enterpriseRepository.save(enterprise);
    }
}


