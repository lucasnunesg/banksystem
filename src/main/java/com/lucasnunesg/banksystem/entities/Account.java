package com.lucasnunesg.banksystem.entities;

import com.lucasnunesg.banksystem.entities.enums.AccountType;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_accounts")
public abstract class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String fullName;
    private String document;
    private String email;
    private String password;
    private BigDecimal balance = BigDecimal.ZERO;
    private Integer accountType;

    protected Account(String fullName, String document, String email, String password, AccountType accountType) {
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.password = password;
        setAccountType(accountType);
    }

    protected Account(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() { return AccountType.valueOf(accountType);
    }

    public void setAccountType(AccountType accountType) {
        if (accountType != null){
            this.accountType = accountType.getCode();
        }
    }
}
