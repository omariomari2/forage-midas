package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_record")
public class TransactionRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    
    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;
    
    @Column(nullable = false)
    private Float amount;
    
    @Column(nullable = false)
    private Float incentive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private UserRecord sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", insertable = false, updatable = false)
    private UserRecord recipient;
    
    protected TransactionRecord() {
    }
    
    public TransactionRecord(Long senderId, Long recipientId, Float amount, Float incentive) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    
    public Long getRecipientId() {
        return recipientId;
    }
    
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
    
    public Float getAmount() {
        return amount;
    }
    
    public void setAmount(Float amount) {
        this.amount = amount;
    }
    
    public UserRecord getSender() {
        return sender;
    }
    
    public void setSender(UserRecord sender) {
        this.sender = sender;
    }
    
    public UserRecord getRecipient() {
        return recipient;
    }
    
    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }
    
    public Float getIncentive() {
        return incentive;
    }
    
    public void setIncentive(Float incentive) {
        this.incentive = incentive;
    }
    
    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                ", amount=" + amount +
                ", incentive=" + incentive +
                '}';
    }
}
