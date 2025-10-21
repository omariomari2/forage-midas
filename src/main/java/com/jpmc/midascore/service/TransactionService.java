package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private IncentiveService incentiveService;
    
    @Transactional
    public boolean processTransaction(Transaction transaction) {
        logger.info("Processing transaction: {}", transaction);
        
        // Validate transaction
        if (!isValidTransaction(transaction)) {
            logger.warn("Transaction validation failed: {}", transaction);
            return false;
        }
        
        // Get sender and recipient
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        
        if (sender == null || recipient == null) {
            logger.warn("Sender or recipient not found. Sender: {}, Recipient: {}", 
                       transaction.getSenderId(), transaction.getRecipientId());
            return false;
        }
        
        // Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance. Sender balance: {}, Transaction amount: {}", 
                       sender.getBalance(), transaction.getAmount());
            return false;
        }
        
        // Get incentive from the incentive API
        Incentive incentive = incentiveService.getIncentive(transaction);
        logger.info("Incentive received: {}", incentive.getAmount());
        
        // Update balances
        // Sender pays the transaction amount (no incentive deduction)
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        // Recipient receives transaction amount plus incentive
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive.getAmount());
        
        // Save updated users
        userRepository.save(sender);
        userRepository.save(recipient);
        
        // Create and save transaction record
        TransactionRecord transactionRecord = new TransactionRecord(
            transaction.getSenderId(),
            transaction.getRecipientId(),
            transaction.getAmount(),
            incentive.getAmount()
        );
        transactionRepository.save(transactionRecord);
        
        logger.info("Transaction processed successfully. Sender new balance: {}, Recipient new balance: {}, Incentive: {}", 
                   sender.getBalance(), recipient.getBalance(), incentive.getAmount());
        
        return true;
    }
    
    private boolean isValidTransaction(Transaction transaction) {
        // Check if senderId and recipientId are valid (positive numbers)
        if (transaction.getSenderId() <= 0 || transaction.getRecipientId() <= 0) {
            return false;
        }
        
        // Check if amount is positive
        if (transaction.getAmount() <= 0) {
            return false;
        }
        
        // Check if sender and recipient are different
        if (transaction.getSenderId() == transaction.getRecipientId()) {
            return false;
        }
        
        return true;
    }
}
