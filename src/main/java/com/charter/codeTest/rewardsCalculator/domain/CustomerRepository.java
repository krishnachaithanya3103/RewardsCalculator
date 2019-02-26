package com.charter.codeTest.rewardsCalculator.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerTrxn, Long> {
     CustomerTrxn findByAccountId(Long accountId);

}
