package com.lucasnunesg.banksystem.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_transfers")
public class Transfer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_account_id")
    private Account payer;

    @ManyToOne
    @JoinColumn(name = "payee_account_id")
    private Account payee;

    private BigDecimal value;

    public Transfer(){}

    public Transfer(Account payer, Account payee, BigDecimal value) {
        this.payer = payer;
        this.payee = payee;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getPayer() {
        return payer;
    }

    public void setPayer(Account payer) {
        this.payer = payer;
    }

    public Account getPayee() {
        return payee;
    }

    public void setPayee(Account payee) {
        this.payee = payee;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
