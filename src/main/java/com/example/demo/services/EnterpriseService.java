package com.example.demo.services;

import com.example.demo.entities.Enterprise;
import com.example.demo.repositories.EnterpriseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    public List<Enterprise> findAll() {
        return enterpriseRepository.findAll();
    }

//    public Enterprise findById(Long id) {
//        return
//    }
}
