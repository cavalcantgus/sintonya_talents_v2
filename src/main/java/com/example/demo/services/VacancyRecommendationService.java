package com.example.demo.services;

import com.example.demo.controllers.VacancyController;
import com.example.demo.dto.VacancyResponse;
import com.example.demo.entities.Candidate;
import com.example.demo.entities.Vacancy;
import com.example.demo.enums.ExperienceRange;
import com.example.demo.enums.WorkModality;
import com.example.demo.repositories.VacancyRepository;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class VacancyRecommendationService {

    private final VacancyRepository vacancyRepository;

    private static final int SCORE_EXPERIENCE    = 40;
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


        return scored.stream()
                .map(Map.Entry::getKey)
                .map(VacancyResponse::fromEntity)
                .toList();
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

    private int scoreModality(Vacancy vacancy, Candidate candidate) {
        WorkModality vacancyModality = vacancy.getModalityType();
        WorkModality candidatePreference = candidate.getPreferences().getModality();

        if(vacancyModality == null) return 0;

        if(candidatePreference == null) return 0;

        if(vacancyModality.equals(candidatePreference)) {
            return SCORE_MODALITY;
        }

        if(vacancyModality.equals(WorkModality.HYBRID) || candidatePreference.equals(WorkModality.HYBRID)) {
            return SCORE_MODALITY / 2;
        }

        return 0;
    }

    private double isExperienceCompatible(Vacancy vacancy, Candidate candidate) {
        Long minExp = vacancy.getMinExperienceRange();
        Long maxExp = vacancy.getMaxExperienceRange();
        Long candidateExp = candidate.getPreferences().getTotalExperience();
        double kUnder = 0.4;
        double kOver = 0.2;
        double k = 0.2;

        if(candidateExp < minExp) {
            double decay = Math.exp(-kUnder * (minExp - candidateExp));
            return k + (0.5 - k) * decay;

        } else if (candidateExp <= maxExp) {
            double ratio = (double) (candidateExp - minExp) / (maxExp - minExp);
            return 0.5 + 0.5 * ratio;
        }

        else {
            double decay = Math.exp(-kOver * (candidateExp - maxExp));
            return 0.5 + 0.5 * decay;
        }
    }
//
//    private boolean isCompatibleWithEnum(ExperienceRange range, Long candidateMonths) {
//        return candidateMonths >= range.getMinMonths()
//                && candidateMonths <= range.getMaxMonths();
//    }
}
