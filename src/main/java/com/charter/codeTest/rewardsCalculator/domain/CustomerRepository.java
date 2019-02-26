package com.charter.codeTest.rewardsCalculator.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface CustomerRepository extends MongoRepository<CustomerTrxn, Long> {
     List<CustomerTrxn> findByAccountId(Long accountId);

    List<CustomerTrxn> findCustomerTrxnByTransactionDateBetween(Date from, Date to);

    List<CustomerTrxn> findCustomerTrxnByTransactionDateBetweenAndAccountId(Date from, Date to, Long accountId);

}
