package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.services.EnterpriseService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping
    public ResponseEntity<List<EnterpriseResponse>> findAll() {
        List<EnterpriseResponse> enterprises = enterpriseService.findAll();
        return ResponseEntity.ok().body(enterprises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseResponse> findById(@PathVariable Long id) {
        EnterpriseResponse enterpriseResponse = enterpriseService.findById(id);
        return ResponseEntity.ok().body(enterpriseResponse);
    }

    @GetMapping("/find-enterprise-by-usar/{userId}")
    @PreAuthorize("hasRole('ENTERPRISE') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<EnterpriseResponse> findByUserId(@PathVariable Long userId) {
        EnterpriseResponse enterprise = enterpriseService.findByUserId(userId);
        return ResponseEntity.ok().body(enterprise);
    }

    @PutMapping("/update-personal-infos/enterprise/{id}")
    @PreAuthorize("hasAuthority('PROFILE_UPDATE') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<EnterpriseResponse> updatePersonalInfos(@PathVariable Long id, @RequestBody EnterpriseUpdateDTO objDto) {
        EnterpriseResponse enterprise = enterpriseService.update(id, objDto);
        return ResponseEntity.ok().body(enterprise);
    }

//    @PutMapping("/update-personal-summary/enterprise/{id}")
//    @PreAuthorize("hasAuthority('PROFILE_UPDATE') or hasRole('ADMINISTRATOR')")
//    public ResponseEntity<EnterpriseResponse> updatePersonalSummary(@PathVariable Long id, @RequestBody String personalSummary) {
//        EnterpriseResponse enterprise = enterpriseService.update(id, personalSummary);
//        return ResponseEntity.ok().body(enterprise);
//    }


    @PostMapping("/{id}/profile/upload-photo")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("photo") MultipartFile photo,
            @PathVariable Long id
    ) throws IOException {

        enterpriseService.updateProfilePhoto(photo, id);
        return ResponseEntity.ok().build();
    }
}
