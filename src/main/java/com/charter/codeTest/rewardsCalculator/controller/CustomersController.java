package com.charter.codeTest.rewardsCalculator.controller;

import com.charter.codeTest.rewardsCalculator.domain.CustomerRepository;
import com.charter.codeTest.rewardsCalculator.domain.CustomerTrxn;
import com.charter.codeTest.rewardsCalculator.domain.RewardsInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customerrewards")
public class CustomersController {

    private static final int CURRENT_OFFSET=0;
    private static final int THIRTY_DAY_OFFSET=30;
    private static final int SIXTY_DAY_OFFSET=60;
    private static final int NINTEY_DAY_OFFSET=90;
    private static final int HUNDRED_DOLLARS=100;
    private static final int FIFTY_DOLLARS=50;
    private static final int DOUBLE_POINT=2;
    private static final int SINGLE_POINT=1;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<CustomerTrxn> getAllCustomerTransactions(){
        return customerRepository.findAll();
    }

    @GetMapping(value="/{id}")
    public RewardsInfo getCustomerRewardsById(@PathVariable("id") Long accountId){

        Date currentDate = getCalendarPriorToDays(CURRENT_OFFSET);
       Date thirtyDaysPriorDate= getCalendarPriorToDays(THIRTY_DAY_OFFSET);
       Date sixtyDaysPriorDate = getCalendarPriorToDays(SIXTY_DAY_OFFSET);
       Date ninetyDaysPriorDate = getCalendarPriorToDays(NINTEY_DAY_OFFSET);

        List<CustomerTrxn> thirtyDayTranscactions=customerRepository.findCustomerTrxnByTransactionDateBetweenAndAccountId(thirtyDaysPriorDate,currentDate,accountId);
        List<CustomerTrxn> sixtyDayTransactions=customerRepository.findCustomerTrxnByTransactionDateBetweenAndAccountId(sixtyDaysPriorDate,thirtyDaysPriorDate,accountId);
        List<CustomerTrxn> ninetyDayTransactions=customerRepository.findCustomerTrxnByTransactionDateBetweenAndAccountId(ninetyDaysPriorDate,sixtyDaysPriorDate,accountId);
         return getRewardsInfoOfCustomer(thirtyDayTranscactions,sixtyDayTransactions,ninetyDayTransactions,accountId);
    }

    /**
     * Generally POST method is used to post the data to db, here i am using it to create mock test data for test purpose
     * Please do not consider the first name and last name, they are just random strings generated for test purpose.
     */

    @PostMapping
    public List<CustomerTrxn> createCustomer(){

        for(int i=0; i<100; i++){
            CustomerTrxn customer = new CustomerTrxn();
            customer.setAccountId(ThreadLocalRandom.current().nextLong(1,10));
            customer.setFirstName(RandomStringUtils.random(10,true,false));
            customer.setLastName(RandomStringUtils.random(8,true,false));
            customer.setTransactionId(System.currentTimeMillis());
            customer.setTransactionAmount(new BigDecimal(ThreadLocalRandom.current().nextDouble(10,1000)).setScale(2, RoundingMode.HALF_DOWN).doubleValue());
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            calendar.add(Calendar.DATE,-90);
            Date ninetyDaysEarlier = calendar.getTime();
            Date randomDate = new Date(ThreadLocalRandom.current()
                    .nextLong(ninetyDaysEarlier.getTime(),currentDate.getTime()));

            customer.setTransactionDate(randomDate);
            customerRepository.save(customer);
        }



        return customerRepository.findAll();
    }

    @DeleteMapping(value="/{id}")
    public void deleteCustomerById(@PathVariable("id") Long accountId){
        customerRepository.deleteById(accountId);
    }

    @DeleteMapping
    public void deleteAll(){
        customerRepository.deleteAll();
    }

    private Date getCalendarPriorToDays(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-days);
        return calendar.getTime();
    }

    public RewardsInfo  getRewardsInfoOfCustomer(List<CustomerTrxn> thirtyDayTransactions,List<CustomerTrxn> sixtyDayTransactions,List<CustomerTrxn> ninetyDayTransactions, Long accountId){

        RewardsInfo rewardsInfoOfCustomer = new RewardsInfo();
        rewardsInfoOfCustomer.setAccountId(accountId);
        rewardsInfoOfCustomer.setRewardPointsForPastThirtyDays(calculateRewardPointsForTransactions(thirtyDayTransactions));
        rewardsInfoOfCustomer.setRewardPointsBetweenPastThirtyToSixtyDays(calculateRewardPointsForTransactions(sixtyDayTransactions));
        rewardsInfoOfCustomer.setRewardPointsBetweenPastSixtyToNinetyDays(calculateRewardPointsForTransactions(ninetyDayTransactions));

        rewardsInfoOfCustomer.setTotalRewardsInPastNinetyDays(
                rewardsInfoOfCustomer.getRewardPointsForPastThirtyDays()+
                        rewardsInfoOfCustomer.getRewardPointsBetweenPastThirtyToSixtyDays()+
                        rewardsInfoOfCustomer.getRewardPointsBetweenPastSixtyToNinetyDays()
        );

        return rewardsInfoOfCustomer;

    }


    private Long calculateRewardPointsForTransactions(List<CustomerTrxn> customerTransactions){
        return customerTransactions.stream().map(customerTrxn -> calculateAndReturnPoints(customerTrxn)).collect(Collectors.summingLong(i -> i));

    }

    private Long calculateAndReturnPoints(CustomerTrxn customerTrxn){

        long rewardPoints=0;
        if(null!=customerTrxn&& HUNDRED_DOLLARS<customerTrxn.getTransactionAmount()){
            rewardPoints=Math.round(DOUBLE_POINT*(customerTrxn.getTransactionAmount()-HUNDRED_DOLLARS));
        }

        if(null!=customerTrxn&&FIFTY_DOLLARS<customerTrxn.getTransactionAmount()){
            rewardPoints+=Math.round(SINGLE_POINT*(customerTrxn.getTransactionAmount()-FIFTY_DOLLARS));
        }

        return rewardPoints;
    }


}
