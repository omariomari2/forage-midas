package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    
    @Value("${general.kafka-topic}")
    private String topic;
    
    @Autowired
    private TransactionService transactionService;
    
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleTransaction(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);
        logger.info("Transaction amount: {}", transaction.getAmount());
        
        // Process the transaction through the service
        boolean success = transactionService.processTransaction(transaction);
        
        if (success) {
            logger.info("Transaction processed successfully: {}", transaction);
        } else {
            logger.warn("Transaction processing failed: {}", transaction);
        }
    }
}
