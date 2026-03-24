package com.example.demo.services;

import com.example.demo.controllers.VacancyController;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.ExperienceRange;
import com.example.demo.enums.WorkModality;
import com.example.demo.repositories.VacancyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class VacancyRecommendationService {

    private final VacancyRepository vacancyRepository;

//    private static final int SCORE_EXPERIENCE    = 40;
    private static final int SCORE_SECTOR        = 25;
    private static final int SCORE_MODALITY      = 20;
    private static final int SCORE_LOCALITY      = 10;
    private static final int SCORE_RECENT        = 5;

    public VacancyRecommendationService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    public List<VacancyResponse> recommend(Candidate candidate, int limit) {
        List<Vacancy> activeVacancies = vacancyRepository.findAllActive();

        List<Map.Entry<Vacancy, Integer>> scored = activeVacancies.stream()
                .map(vacancy -> Map.entry(vacancy, score(vacancy, candidate)))
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<Vacancy, Integer>comparingByValue().reversed())
                .limit(limit)
                .toList();

        printRecommendations(scored);

        return scored.stream()
                .map(Map.Entry::getKey)
                .map(VacancyResponse::fromEntity)
                .toList();
    }

    private void printRecommendations(List<Map.Entry<Vacancy, Integer>> scored) {
        int maxScore = 60;
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║        VAGAS RECOMENDADAS — DEBUG        ║");
        System.out.println("╠══════════════════════════════════════════╣");

        for (int i = 0; i < scored.size(); i++) {
            Vacancy v = scored.get(i).getKey();
            int score = scored.get(i).getValue();
            int barFilled = (int) ((score / (double) maxScore) * 20);
            String bar = "█".repeat(barFilled) + "░".repeat(20 - barFilled);

            System.out.printf("║  #%-2d  ID: %-6d  %-20s  ║%n", i + 1, v.getId(), "");
            System.out.printf("║        Score: %-3d  [%s]  ║%n", score, bar);
            System.out.println("╠══════════════════════════════════════════╣");
        }

        System.out.println("╚══════════════════════════════════════════╝\n");
    }

    private int score(Vacancy vacancy, Candidate candidate) {
        int score = 0;

//        if(isExperienceCompatible(vacancy, candidate)) {
//            score += SCORE_EXPERIENCE;
//        }

        if(vacancy.getSector() != null
                && vacancy.getSector().equals(candidate.getSector())) {
            score += SCORE_SECTOR;
        }

        if(vacancy.getModalityType() != null
                && vacancy.getModalityType() == WorkModality.ON_SITE) {
            score += SCORE_MODALITY;
        }

        if(vacancy.getModalityType() != WorkModality.REMOTE
                && vacancy.getLocality() != null
                && vacancy.getLocality().equals(candidate.getProfile().getLocality())) {

            score += SCORE_LOCALITY;
        }

        if(vacancy.getPublicationDate() != null
                 && vacancy.getPublicationDate().isAfter(LocalDate.now().minusDays(7))) {
            score += SCORE_RECENT;
        }

        return score;
    }

//    private boolean isExperienceCompatible(Vacancy vacancy, Candidate candidate) {
//        Long candidateMonths = candidate.getExperienceMonths(); // renomear o campo
//        if (candidateMonths == null) return false;
//
//        // Range numérico tem prioridade (mais preciso)
//        if (vacancy.getMinExperienceRange() != null && vacancy.getMaxExperienceRange() != null) {
//            return candidateMonths >= vacancy.getMinExperienceRange()
//                    && candidateMonths <= vacancy.getMaxExperienceRange();
//        }
//
//        // Fallback para o enum
//        if (vacancy.getExperienceRange() != null) {
//            return isCompatibleWithEnum(vacancy.getExperienceRange(), candidateMonths);
//        }
//
//        return false;
//    }
//
//    private boolean isCompatibleWithEnum(ExperienceRange range, Long candidateMonths) {
//        return candidateMonths >= range.getMinMonths()
//                && candidateMonths <= range.getMaxMonths();
//    }
}
