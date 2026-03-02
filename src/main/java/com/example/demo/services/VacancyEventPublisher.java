package com.example.demo.services;

import com.example.demo.config.RabbitConfig;
import com.example.demo.entities.Vacancy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VacancyEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public VacancyEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishVacancyCreated(Vacancy vacancy) {
        Map<String, Object> event = Map.of(
                "vacancyId", vacancy.getId(),
                "title", vacancy.getTitle(),
                "description", vacancy.getDescription(),
                "postion", vacancy.getPosition(),
                "vacancyType", vacancy.getVacancyType().toString(),
                "locality", vacancy.getLocality(),
                "modalityType", vacancy.getModalityType().toString(),
                "enterpriseId", vacancy.getEnterprise().getId(),
                "enterpriseIsApproved", vacancy.getEnterprise().isApproved()
        );

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }

}
