package com.example.demo.services;

import com.example.demo.entities.Attachment;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.File;
import com.example.demo.enums.AttachmentType;
import com.example.demo.repositories.AttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.type.AbstractType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public List<Attachment> findAll() {
        return attachmentRepository.findAll();
    }

    public Attachment findById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Anexo não encontrado"));
    }

    public Attachment insert(Candidate candidate, File file, AttachmentType type) {
        Attachment attachment = new Attachment();
        attachment.setCandidate(candidate);
        attachment.setFile(file);
        attachment.setType(type);

        return attachmentRepository.save(attachment);
    }
}
