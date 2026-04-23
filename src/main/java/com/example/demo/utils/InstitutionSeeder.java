//    package com.example.demo.utils;
//
//    import com.example.demo.dto.InstitutionDTO;
//    import com.example.demo.entities.Institution;
//    import com.example.demo.repositories.InstitutionRepository;
//    import com.fasterxml.jackson.databind.ObjectMapper;
//    import org.springframework.beans.factory.annotation.Autowired;
//    import org.springframework.boot.CommandLineRunner;
//    import org.springframework.stereotype.Component;
//
//    import java.io.InputStream;
//    import java.util.List;
//
//    @Component
//    public class InstitutionSeeder implements CommandLineRunner {
//
//        @Autowired
//        private InstitutionRepository repository;
//
//        @Autowired
//        private ObjectMapper objectMapper;
//
//        @Override
//        public void run(String... args) throws Exception {
//            if (repository.count() > 0) return; // já populado, não roda de novo
//
//            InputStream is = getClass().getResourceAsStream("/data/institutions.json");
//            List<InstitutionDTO> list = objectMapper.readValue(is,
//                    objectMapper.getTypeFactory().constructCollectionType(List.class, InstitutionDTO.class));
//
//            List<Institution> institutions = list.stream().map(dto -> {
//                Institution i = new Institution();
//                i.setName(dto.name());
//                i.setCountry(dto.country());
//                i.setDomain(DomainExtractor.extract(dto.website()));
//                return i;
//            }).toList();
//
//            repository.saveAll(institutions);
//        }
//    }