package com.example.demo.services;

import com.example.demo.dto.CandidateResponse;
import com.example.demo.dto.CertificateCreateDTO;
import com.example.demo.dto.CertificateResponse;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.*;
import com.example.demo.enums.AttachmentType;
import com.example.demo.enums.SkillLevel;
import com.example.demo.enums.SkillSource;
import com.example.demo.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.engine.spi.EntityUniqueKey;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificatesService {

    private final CertificatesRepository certificatesRepository;
    private final CandidateRepository candidateRepository;
    private final SkillBaseRepository skillBaseRepository;
    private final SkillCandidateRepository skillCandidateRepository;
    private final AttachmentService attachmentService;
    private final FileRepository fileRepository;
    private final SectorRepository sectorRepository;
    private final SupabaseStorageService storageService;
    private final InstitutionRepository institutionRepository;

    public CertificatesService(CertificatesRepository certificatesRepository,
                               CandidateRepository candidateRepository,
                               SkillBaseRepository skillBaseRepository,
                               SkillCandidateRepository skillCandidateRepository,
                               AttachmentService attachmentService,
                               FileRepository fileRepository,
                               SectorRepository sectorRepository,
                               SupabaseStorageService storageService,
                               InstitutionRepository institutionRepository) {
        this.certificatesRepository = certificatesRepository;
        this.candidateRepository = candidateRepository;
        this.skillBaseRepository = skillBaseRepository;
        this.skillCandidateRepository = skillCandidateRepository;
        this.attachmentService = attachmentService;
        this.fileRepository = fileRepository;
        this.sectorRepository = sectorRepository;
        this.storageService = storageService;
        this.institutionRepository = institutionRepository;
    }

    public List<CertificateResponse> findAll() {
        return certificatesRepository.findAll()
                .stream()
                .map(CertificateResponse::fromEntity)
                .toList();
    }

    public CertificateResponse findById(Long id) {
        Certificates certificate = certificatesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Certificado não encontrado"));

        return CertificateResponse.fromEntity(certificate);
    }

    @Transactional
    public CertificateResponse insert(Long candidateId, CertificateCreateDTO objDto) throws IOException {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado"));

        Institution institution = institutionRepository.findById(objDto.getInstitution())
                .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada"));

        Certificates certificate = new Certificates();
        certificate.setTitle(objDto.getTitle());
        certificate.setInstitution(institution);
        certificate.setUrl(objDto.getUrl());
        certificate.setHours(objDto.getHours());
        certificate.setCandidate(candidate);
        certificate.setIssueDate(buildDate(objDto.getIssueMonth(), objDto.getIssueYear()));
        certificate.setExpirationDate(buildDate(objDto.getExpirationMonth(), objDto.getExpirationYear()));

        Set<Sector> sectors = new HashSet<>();
        if (objDto.getSectorIds() != null && !objDto.getSectorIds().isEmpty()) {
            sectors.addAll(sectorRepository.findAllById(objDto.getSectorIds()));
        }
        certificate.setSectors(sectors);
        candidate.getCertificates().add(certificate);

        Set<SkillBase> skills = new HashSet<>();
        if (objDto.getSkillsId() != null && !objDto.getSkillsId().isEmpty()) {
            skills.addAll(skillBaseRepository.findAllById(objDto.getSkillsId()));
        }

        for (SkillBase skill : skills) {
            SkillCandidate skillCandidate = new SkillCandidate();
            skillCandidate.setCandidate(candidate);
            skillCandidate.setSkillBase(skill);
            skillCandidate.setSource(SkillSource.CERTIFICATE);
            skillCandidate.setCertificate(certificate);
            skillCandidate.setSkillLevel(SkillLevel.NON_SPECIFIED);

            candidate.getCandidateSkills().add(skillCandidate);
            certificate.getCertificateSkills().add(skillCandidate);
        }

        if (objDto.getFile() != null && !objDto.getFile().isEmpty()) {
            attachCertificateFile(objDto.getFile(), candidate, certificate);
        }

        candidateRepository.save(candidate);

        return CertificateResponse.fromEntity(certificate);
    }

    private void attachCertificateFile(MultipartFile multipartFile, Candidate candidate, Certificates certificate) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String path = "users/candidate/" + candidate.getId() + "/documentos/" + UUID.randomUUID() + extension;
        String publicUrl = storageService.upload(multipartFile, path);

        File file = new File();
        file.setFileName(originalFilename);
        file.setContentType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        file.setPath(publicUrl);

        fileRepository.save(file);

        Attachment attachment = attachmentService.insert(candidate, file, AttachmentType.CERTIFICATE);
        candidate.getAttachments().add(attachment);
    }

    private LocalDate buildDate(String month, String year) {
        if (month == null || year == null || month.isBlank() || year.isBlank()) {
            return null;
        }
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
    }
}
