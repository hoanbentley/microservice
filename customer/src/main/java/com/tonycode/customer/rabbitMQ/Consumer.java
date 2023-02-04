package com.tonycode.customer.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = "internal.exchange")
    public void consumer(NewCustomerRequest newCustomerRequest){
        log.info("Consumed {} from queue", newCustomerRequest);
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ) {
    }
}
