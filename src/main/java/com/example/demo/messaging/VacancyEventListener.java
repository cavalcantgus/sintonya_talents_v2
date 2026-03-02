package com.example.demo.messaging;

import com.example.demo.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VacancyEventListener {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handleEvent(Map<String, Object> event) {
        System.out.println("Evento recebido: " + event);
    }
}