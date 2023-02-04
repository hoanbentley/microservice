package com.tonycode.customer;

import com.tonycode.amqp.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);

        FraudCheckHistoryResponse fraudCheckHistoryResponse = restTemplate.getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckHistoryResponse.class,
                customer.getId()
        );

        if (fraudCheckHistoryResponse.isFraudulentCustomer()) {
            throw new IllegalStateException("fraud");
        }

        NewCustomerRequest newCustomerRequest = new NewCustomerRequest(
                "hoantk",
                "hoanbentley@gmail.com",
                37
        );
        rabbitMQMessageProducer.publish(
                newCustomerRequest, "internal.exchange", "internal.notification.routing-key"
        );
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ) {
    }

}
