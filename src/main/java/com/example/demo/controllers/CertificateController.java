package com.example.demo.controllers;

import com.example.demo.dto.CertificateCreateDTO;
import com.example.demo.dto.CertificateResponse;
import com.example.demo.entities.Certificates;
import com.example.demo.services.CertificatesService;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.cert.Certificate;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificatesService certificatesService;

    public CertificateController(CertificatesService certificatesService) {
        this.certificatesService = certificatesService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateResponse>> findAll() {
        List<CertificateResponse> certificates = certificatesService.findAll();
        return ResponseEntity.ok().body(certificates);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateResponse> findById(@PathVariable Long id) {
        CertificateResponse certificate = certificatesService.findById(id);
        return ResponseEntity.ok().body(certificate);
    }

    @PostMapping(value = "/{candidateId}/add-certificate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CERTIFICATE_CREATE')")
    public ResponseEntity<CertificateResponse> insert(@PathVariable Long candidateId,
                                                      @RequestPart("data") CertificateCreateDTO objDto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) {
        objDto.setFile(file);
        CertificateResponse certificate = certificatesService.insert(candidateId, objDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(certificate.id()).toUri();
        return ResponseEntity.created(uri).body(certificate);
    }
}
