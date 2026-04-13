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
    private final SupabaseStorageService storageService;

    @Value("${app.upload.dir:/var/uploads}")
    private String uploadDir;

    public CandidateService(CandidateRepository candidateRepository,
                            ProfileRepository profileRepository,
                            SkillBaseRepository skillBaseRepository,
                            SkillCandidateRepository skillCandidateRepository,
                            SectorRepository sectorRepository,
                            SupabaseStorageService storageService) {
        this.candidateRepository = candidateRepository;
        this.profileRepository = profileRepository;
        this.skillBaseRepository = skillBaseRepository;
        this.skillCandidateRepository = skillCandidateRepository;
        this.sectorRepository = sectorRepository;
        this.storageService = storageService;
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
        validateFile(file);

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Profile profile = candidate.getProfile();

        // Remove foto antiga se existir
        if (profile.getPhoto() != null) {
            String oldPath = extractPath(profile.getPhoto());
            storageService.delete(oldPath);
        }

        String path = "users/candidate/" + id + "/photo_" + System.currentTimeMillis() + ".jpg";
        String publicUrl = storageService.upload(file, path);

        profile.setPhoto(publicUrl);
        profileRepository.save(profile);
    }

    public void updateProfileBanner(MultipartFile file, Long id) throws IOException {
        validateFile(file);

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Profile profile = candidate.getProfile();

        // Remove banner antigo se existir
        if (profile.getBanner() != null) {
            String oldPath = extractPath(profile.getBanner());
            storageService.delete(oldPath);
        }

        String path = "users/candidate/" + id + "/banner_" + System.currentTimeMillis() + ".jpg";
        String publicUrl = storageService.upload(file, path);

        profile.setBanner(publicUrl);
        profileRepository.save(profile);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }
    }

    /**
     * Extrai o path relativo a partir da URL pública.
     * Ex: "https://xxx.supabase.co/storage/v1/object/public/profiles/candidate/1/photo.jpg"
     *  -> "candidate/1/photo.jpg"
     */
    private String extractPath(String publicUrl) {
        return publicUrl.replaceAll(".*/public/[^/]+/", "");
    }
}
