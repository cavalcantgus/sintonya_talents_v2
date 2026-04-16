package com.example.demo.services;

import com.example.demo.dto.PopupCreateDTO;
import com.example.demo.dto.PopupResponse;
import com.example.demo.dto.PopupUpdateDTO;
import com.example.demo.entities.Enterprise;
import com.example.demo.entities.Popup;
import com.example.demo.entities.Profile;
import com.example.demo.repositories.PopupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PopupService {

    private final PopupRepository popupRepository;
    private final SupabaseStorageService storageService;

    public PopupService(PopupRepository popupRepository, SupabaseStorageService storageService) {
        this.popupRepository = popupRepository;
        this.storageService = storageService;
    }

    public List<PopupResponse> findAll() {
        return popupRepository.findAll()
                .stream()
                .map(PopupResponse::fromEntity)
                .toList();
    }

    public List<PopupResponse> findRandomPopups() {
        return popupRepository.findRandomPopups(PageRequest.of(0, 10))
                .stream()
                .map(PopupResponse::fromEntity)
                .toList();
    }

    public PopupResponse findById(Long id) {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pop-up não encontrado"));
        return PopupResponse.fromEntity(popup);
    }

    public PopupResponse insert(PopupCreateDTO objDto, MultipartFile file) throws IOException {
        validateFile(file);

        Popup popup = new Popup();
        popup.setTitle(objDto.getTitle());
        popup.setCallToActionUrl(objDto.getCallToActionUrl());
        popup.setActive(false);
        popupRepository.save(popup);

        savePopupFileInStorage(file, popup);
        return PopupResponse.fromEntity(popup);
    }

    private String extractPath(String publicUrl) {
        return publicUrl.replaceAll(".*/public/[^/]+/", "");
    }

    public void savePopupFileInStorage(MultipartFile file, Popup popup) throws IOException {
        if (popup.getUrl() != null) {
            String oldPath = extractPath(popup.getUrl());
            storageService.delete(oldPath);
        }

        String path = "popups/" + popup.getId() + "/photo_" + System.currentTimeMillis() + ".jpg";
        String publicUrl = storageService.upload(file, path);

        popup.setUrl(publicUrl);
        popupRepository.save(popup);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }
    }

    public PopupResponse update(PopupUpdateDTO objDto, MultipartFile file, Long id) throws IOException {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pop-up não encontrado"));

        popup.setTitle(objDto.getTitle());
        popup.setCallToActionUrl(objDto.getCallToActionUrl());

        if (file != null && !file.isEmpty()) {
            savePopupFileInStorage(file, popup);
        }

        return PopupResponse.fromEntity(popup);
    }

    public PopupResponse disablePopup(Long id) {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pop-up não encontrado"));

        popup.setActive(false);

        popupRepository.save(popup);
        return PopupResponse.fromEntity(popup);
    }

    public PopupResponse enablePopup(Long id) {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pop-up não encontrado"));

        popup.setActive(true);

        popupRepository.save(popup);
        return PopupResponse.fromEntity(popup);
    }

    public void delete(Long id) {
        Popup popup = popupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pop-up não encontrado"));

        storageService.delete(extractPath(popup.getUrl()));
        popupRepository.delete(popup);
    }
}
