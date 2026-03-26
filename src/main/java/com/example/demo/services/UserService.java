package com.example.demo.services;

import com.example.demo.config.SecurityConfig;
import com.example.demo.dto.*;
import com.example.demo.entities.*;
import com.example.demo.enums.CertificateError;
import com.example.demo.enums.RoleName;
import com.example.demo.exception.CertificateValidationException;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.repositories.*;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EnterpriseRepository enterpriseRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final CandidatePreferencesRepository candidatePreferencesRepository;
    private final CnpjService cnpjService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, EnterpriseRepository enterpriseRepository,
                       CandidateRepository candidateRepository,
                       PasswordEncoder passwordEncoder,
                       ProfileRepository profileRepository,
                       CandidatePreferencesRepository candidatePreferencesRepository,
                       CnpjService cnpjService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.enterpriseRepository = enterpriseRepository;
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.candidatePreferencesRepository = candidatePreferencesRepository;
        this.cnpjService = cnpjService;
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

    public UserResponse insertAdmin(UserCreateDTO objDto) {
        if(userRepository.existsByEmail(objDto.getEmail())) {
            throw new EmailAlreadyExistsException("E-mal já cadastrado");
        }

        User user = new User();

        Role role = roleService.insert("ADMINISTRATOR");

        user.setEmail(objDto.getEmail());
        user.setPassword(passwordEncoder.encode(objDto.getPassword()));
        user.getRoles().add(role);

        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse insertCandidate(UserCreateDTO objDto) {
        if(userRepository.existsByEmail(objDto.getEmail())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }
        User user = new User();

        Role role = roleService.insert("CANDIDATE");


        user.setEmail(objDto.getEmail());
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(objDto.getPassword()));

        userRepository.save(user);
        createCandidate(user, objDto.getCandidateName(), objDto.getCpf(), objDto.getContact());

        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse insertEnterprise(
            UserCreateEnterpriseDTO objDto,
            MultipartFile file
    ) {
        Role role = roleService.insert("ENTERPRISE");

        validateCertificate(file, objDto);

        User user = new User();
        user.setEmail(objDto.getEmail());
        user.getRoles().add(role);

        createEnterprise(user, objDto);
        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    private void validateEnterprise(UserCreateEnterpriseDTO objDto) {
        try {
            String cnpj = objDto.getCnpj().replaceAll("\\D", "");

            CnpjApiResponse api = cnpjService.buscarCnpj(cnpj);

            if (!"ATIVA".equalsIgnoreCase(api.getDescricao_situacao_cadastral())) {
                throw new CertificateValidationException(CertificateError.INACTIVE_CNJP);
            }

            if (!objDto.getSocialReason().equalsIgnoreCase(api.getRazao_social())) {
                throw new CertificateValidationException(CertificateError.INVALID_SOCIAL_REASON);
            }

        } catch (CertificateValidationException e) {
            throw e; // mantém suas exceções de negócio

        }
    }

    private void validateCertificate(
            MultipartFile file,
            UserCreateEnterpriseDTO objDto
    ) {

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

//            try (InputStream is = file.getInputStream()) {
//                keyStore.load(is, objDto.getCertificatePassword().toCharArray());
//            }

            Enumeration<String> aliases = keyStore.aliases();

            if (!aliases.hasMoreElements()) {
                throw new CertificateValidationException(CertificateError.INVALID_FILE);
            }

            String alias = aliases.nextElement();

            X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

            cert.checkValidity();

            String subject = cert.getSubjectDN().getName();

            String cnpj = extractCnpj(subject);

            if (cnpj == null || !cnpj.equals(objDto.getCnpj())) {
                throw new CertificateValidationException(CertificateError.INVALID_CNPJ);
            }

//        } catch (IOException e) {
//            throw new CertificateValidationException(CertificateError.INVALID_FILE);

        } catch (CertificateExpiredException e) {
            throw new CertificateValidationException(CertificateError.EXPIRED);

        } catch (CertificateNotYetValidException e) {
            throw new CertificateValidationException(CertificateError.NOT_YET_VALID);

        } catch (CertificateValidationException e) {
            throw e;

        } catch (Exception e) {
            throw new CertificateValidationException(CertificateError.INVALID_PASSWORD);
        }
    }

    private String extractCnpj(String subject) {
        Pattern pattern = Pattern.compile("(\\d{14})");
        Matcher matcher = pattern.matcher(subject);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private void createCandidate(User user, String candidateName, String cpf, String contact) {
        Candidate candidate = new Candidate();
        candidate.setUser(user);
        candidate.setFullName(candidateName);
        candidate.setContact(contact);
        candidate.setCpf(cpf);

        Profile profile = createProfile(candidate);
        createCandidatePreferences(candidate);
        candidate.setProfile(profile);
        candidateRepository.save(candidate);

    }

    private void createCandidatePreferences(Candidate candidate) {
        CandidatePreferences candidatePreferences = new CandidatePreferences();
        candidatePreferences.setCandidate(candidate);
        candidatePreferences.setTotalExperience(0L);
        candidatePreferencesRepository.save(candidatePreferences);
    }

    private Profile createProfile(Object object) {
        Profile profile = new Profile();
        return profileRepository.save(profile);
    }

    private void createEnterprise(User user, UserCreateEnterpriseDTO objDto) {
        Enterprise enterprise = new Enterprise();
        enterprise.setUser(user);
        enterprise.setEnterpriseName(objDto.getEnterpriseName());
        enterprise.setCnpj(objDto.getCnpj());
        enterprise.setContact(objDto.getContact());

        Profile profile = createProfile(enterprise);

        enterprise.setProfile(profile);
        enterpriseRepository.save(enterprise);
    }
}


