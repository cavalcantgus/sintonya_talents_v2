package com.example.demo.controllers;

import com.example.demo.dto.PopupCreateDTO;
import com.example.demo.dto.PopupResponse;
import com.example.demo.dto.PopupUpdateDTO;
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

    @GetMapping("/find-random-popups")
    public ResponseEntity<List<PopupResponse>> findRandomPopups() {
        List<PopupResponse> popups = popupService.findRandomPopups();
        return ResponseEntity.ok().body(popups);
    }

    @PostMapping
    public ResponseEntity<PopupResponse> insert(@RequestPart("data") PopupCreateDTO objDto, @RequestPart(value = "file", required = true) MultipartFile file) throws IOException {
        PopupResponse popupResponse = popupService.insert(objDto, file);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(popupResponse.id()).toUri();
        return ResponseEntity.created(uri).body(popupResponse);
    }

    @PutMapping("/popup/{id}")
    public ResponseEntity<PopupResponse> update(@RequestPart("data") PopupUpdateDTO objDto,
                                                @RequestPart(value = "file", required = false) MultipartFile file,
                                                @PathVariable Long id) throws IOException {
        System.out.println("POPUP VINDO DO FRONT: " + objDto.getTitle());
        PopupResponse popupResponse = popupService.update(objDto, file, id);
        return ResponseEntity.ok().body(popupResponse);
    }

    @PutMapping("/popup/disable/{id}")
    public ResponseEntity<PopupResponse> disablePopup(@PathVariable Long id) {
        PopupResponse popupResponse = popupService.disablePopup(id);
        return ResponseEntity.ok().body(popupResponse);
    }

    @PutMapping("/popup/enable/{id}")
    public ResponseEntity<PopupResponse> enablePopup(@PathVariable Long id) {
        PopupResponse popupResponse = popupService.enablePopup(id);
        return ResponseEntity.ok().body(popupResponse);
    }

    @DeleteMapping("/popup/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        popupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
