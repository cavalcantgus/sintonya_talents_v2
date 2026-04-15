package com.example.demo.controllers;

import com.example.demo.dto.PopupCreateDTO;
import com.example.demo.dto.PopupResponse;
import com.example.demo.entities.Popup;
import com.example.demo.services.PopupService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/popups")
public class PopupController {

    private final PopupService popupService;

    public PopupController(PopupService popupService) {
        this.popupService = popupService;
    }

    @GetMapping
    public ResponseEntity<List<PopupResponse>> findAll() {
        List<PopupResponse> popups = popupService.findAll();
        return ResponseEntity.ok().body(popups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopupResponse> findById(@PathVariable Long id) {
        PopupResponse popupResponse = popupService.findById(id);
        return ResponseEntity.ok().body(popupResponse);
    }

    @PostMapping
    public ResponseEntity<PopupResponse> insert(@RequestPart("data") PopupCreateDTO objDto,
                                                @RequestPart(value = "file", required = true) MultipartFile file) throws IOException {
        PopupResponse popupResponse = popupService.insert(objDto, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(popupResponse.id()).toUri();
        return ResponseEntity.created(uri).body(popupResponse);
    }

}
