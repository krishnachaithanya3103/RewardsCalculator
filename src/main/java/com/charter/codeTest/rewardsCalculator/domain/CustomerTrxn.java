package com.charter.codeTest.rewardsCalculator.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="CustomerTrxn")
public class CustomerTrxn {


    @Id
    private Long accountId;

    private String firstName;

    private String lastName;

    private List<Transaction> customerSpending;

    public List<Transaction> getCustomerSpending() {
        return customerSpending;
    }

    public void setCustomerSpending(List<Transaction> customerSpending) {
        this.customerSpending = customerSpending;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
       this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
