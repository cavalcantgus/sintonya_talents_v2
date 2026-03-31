package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.CandidateUpdateDTO;
import com.example.demo.dto.SkillCandidateCreateDto;
import com.example.demo.entities.*;
import com.example.demo.enums.SkillLevel;
import com.example.demo.enums.SkillSource;
import com.example.demo.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ProfileRepository profileRepository;
    private final SkillBaseRepository skillBaseRepository;
    private final SkillCandidateRepository skillCandidateRepository;
    private final SectorRepository sectorRepository;

    @Value("${app.upload.dir:/var/uploads}")
    private String uploadDir;

    public CandidateService(CandidateRepository candidateRepository,
                            ProfileRepository profileRepository,
                            SkillBaseRepository skillBaseRepository,
                            SkillCandidateRepository skillCandidateRepository,
                            SectorRepository sectorRepository) {
        this.candidateRepository = candidateRepository;
        this.profileRepository = profileRepository;
        this.skillBaseRepository = skillBaseRepository;
        this.skillCandidateRepository = skillCandidateRepository;
        this.sectorRepository = sectorRepository;
    }

    public List<CandidateResponse> findAll() {
        return candidateRepository.findAll()
                .stream()
                .map(CandidateResponse::fromEntity)
                .toList();
    }

    public Candidate findByUserEmail(String email) {
        return candidateRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));
    }
    public CandidateResponse findById(Long id) {
       Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

       return CandidateResponse.fromEntity(candidate);
    }

    public CandidateResponse findByUserId(Long id) {
       Candidate candidate = candidateRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        return CandidateResponse.fromEntity(candidate);
    }

    public CandidateResponse update(Long id, CandidateUpdateDTO objDto) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Sector sector = sectorRepository.findById(objDto.getSector())
                        .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado"));
        candidate.setFullName(objDto.getFullName());
        candidate.setGender(objDto.getGender());
        candidate.setRaceEthnicity(objDto.getRaceEthnicity());
        candidate.setContact(objDto.getContact());
        candidate.setSector(sector);
        Profile profile = profileRepository.findById(candidate.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        profile.setLocality(objDto.getLocality());
        profile.setSocialLinks(objDto.getSocialLinks());
        profile.setHeadLine(objDto.getHeadLine());
        candidateRepository.save(candidate);
        profileRepository.save(profile);

        return CandidateResponse.fromEntity(candidate);
    }

    @Transactional
    public CandidateResponse update(Long id, String personalSummary) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Profile profile = profileRepository.findById(candidate.getProfile().getId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        profile.setPersonalSummary(personalSummary);
        profileRepository.save(profile);
        candidate.setProfile(profile);
        candidateRepository.save(candidate);
        return CandidateResponse.fromEntity(candidate);
    }

    @Transactional
    public CandidateResponse addSkills(Long id, List<SkillCandidateCreateDto> objDto) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Map<Long, String> skillLevelMap = objDto.stream()
                .collect(Collectors.toMap(
                        SkillCandidateCreateDto::getSkillId,
                        SkillCandidateCreateDto::getLevel
                ));

        Set<SkillBase> skills = new HashSet<>(
                skillBaseRepository.findAllById(skillLevelMap.keySet())
        );

        Set<Long> existingSkillsIds = skillCandidateRepository.findSkillIdsByCandidateIdAndSource(
                id, SkillSource.MANUAL);

        for (SkillBase skill : skills) {
            if (!existingSkillsIds.contains(skill.getId())) {

                SkillCandidate skillCandidate = new SkillCandidate();

                skillCandidate.setCandidate(candidate);
                skillCandidate.setSkillBase(skill);
                skillCandidate.setSource(SkillSource.MANUAL);

                String levelStr = skillLevelMap.get(skill.getId());
                skillCandidate.setSkillLevel(SkillLevel.valueOf(levelStr));

                candidate.getCandidateSkills().add(skillCandidate);
            }
        }

        candidateRepository.save(candidate);
        return CandidateResponse.fromEntity(candidate);
    }

    public void updateProfilePhoto(MultipartFile file, Long id) throws IOException {
        System.out.println(">>> Iniciando upload para empresa id: " + id);
        System.out.println(">>> Arquivo recebido: " + file.getOriginalFilename() + " | Tamanho: " + file.getSize());

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        System.out.println(">>> Empresa encontrada: " + candidate.getId());

        Path dirPath = Paths.get(uploadDir, "candidate", String.valueOf(candidate.getId()));
        System.out.println(">>> Diretório alvo (absoluto): " + dirPath.toAbsolutePath());
        Files.createDirectories(dirPath);
        System.out.println(">>> Diretório criado/confirmado");

        Profile profile = candidate.getProfile();
        System.out.println(">>> Profile atual: " + profile);

//        if (profile == null) {
//            profile = new Profile();
//            profile.setEnterprise(enterprise);
//            System.out.println(">>> Novo profile criado");
//        } else if (profile.getPhoto() != null) {
//            Files.deleteIfExists(Paths.get(profile.getPhoto()));
//        }

        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        Path photoPath = dirPath.resolve(fileName);
        Files.write(photoPath, file.getBytes());
        System.out.println(">>> Arquivo salvo em: " + photoPath.toAbsolutePath());

        profile.setPhoto(photoPath.toString());
        profileRepository.save(profile);
        System.out.println(">>> Profile salvo no banco com foto: " + profile.getPhoto());
    }

    public void updateProfileBanner(MultipartFile file, Long id) throws IOException {
        System.out.println(">>> Iniciando upload para empresa id: " + id);
        System.out.println(">>> Arquivo recebido: " + file.getOriginalFilename() + " | Tamanho: " + file.getSize());

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));
        System.out.println(">>> Empresa encontrada: " + candidate.getId());

        Path dirPath = Paths.get(uploadDir, "candidate", String.valueOf(candidate.getId()));
        System.out.println(">>> Diretório alvo (absoluto): " + dirPath.toAbsolutePath());
        Files.createDirectories(dirPath);
        System.out.println(">>> Diretório criado/confirmado");

        Profile profile = candidate.getProfile();
        System.out.println(">>> Profile atual: " + profile);

//        if (profile == null) {
//            profile = new Profile();
//            profile.setEnterprise(enterprise);
//            System.out.println(">>> Novo profile criado");
//        } else if (profile.getPhoto() != null) {
//            Files.deleteIfExists(Paths.get(profile.getPhoto()));
//        }

        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        Path photoPath = dirPath.resolve(fileName);
        Files.write(photoPath, file.getBytes());
        System.out.println(">>> Arquivo salvo em: " + photoPath.toAbsolutePath());

        profile.setBanner(photoPath.toString());
        profileRepository.save(profile);
        System.out.println(">>> Profile salvo no banco com foto: " + profile.getPhoto());
    }
}
