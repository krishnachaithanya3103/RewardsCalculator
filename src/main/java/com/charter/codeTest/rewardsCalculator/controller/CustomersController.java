package com.charter.codeTest.rewardsCalculator.controller;

import com.charter.codeTest.rewardsCalculator.domain.CustomerRepository;
import com.charter.codeTest.rewardsCalculator.domain.CustomerTrxn;
import com.charter.codeTest.rewardsCalculator.domain.RewardsInfo;
import com.charter.codeTest.rewardsCalculator.domain.Transaction;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
    public List<RewardsInfo> getAllCustomerRewards(){

        List<CustomerTrxn> allCustomers = customerRepository.findAll();

        return allCustomers.stream().map(customerTrxn -> calculateRewardsForCustomer(customerTrxn)).collect(Collectors.toList());
    }

    @GetMapping(value="/{id}")
    public RewardsInfo getCustomerRewardsById(@PathVariable("id") Long accountId){


        return calculateRewardsForCustomer(customerRepository.findByAccountId(accountId));
    }

    /**
     * Generally POST method is used to post the data to db, here i am using it to create mock test data for test purpose
     * Please do not consider the first name and last name, they are just random strings generated for test purpose.
     */

    @PostMapping
    public List<CustomerTrxn> createCustomer(){

        for(int i=0; i<10; i++){
            CustomerTrxn customer = new CustomerTrxn();
            customer.setAccountId(Long.valueOf(i));
            customer.setFirstName(RandomStringUtils.random(10,true,false));
            customer.setLastName(RandomStringUtils.random(8,true,false));
            customer.setCustomerSpending(new ArrayList<>());
            for (int k=0;k<10;k++){
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                calendar.add(Calendar.DATE,-90);
                Date ninetyDaysEarlier = calendar.getTime();
                Transaction transaction = new Transaction();
                Date randomDate = new Date(ThreadLocalRandom.current()
                        .nextLong(ninetyDaysEarlier.getTime(),currentDate.getTime()));
                LocalDate transactionDate = randomDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                transaction.setDate(transactionDate);
                transaction.setAmountSpent(new BigDecimal(ThreadLocalRandom.current().nextDouble(10,1000)).setScale(2, RoundingMode.HALF_DOWN).doubleValue());
                customer.getCustomerSpending().add(transaction);
            }

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


    public RewardsInfo calculateRewardsForCustomer(CustomerTrxn customer){

        RewardsInfo rewardInfoOfCustomer = new RewardsInfo();
        rewardInfoOfCustomer.setAccountId(customer.getAccountId());

        List<Transaction> customerSpending = customer.getCustomerSpending();

        if(customerSpending.isEmpty()){
            return rewardInfoOfCustomer ;
        }
        LocalDate currentTime =  LocalDate.now();

        LocalDate thirtyDaysEarlier = currentTime.minusDays(THIRTY_DAY_OFFSET);

        LocalDate sixtyDaysEarlier = currentTime.minusDays(SIXTY_DAY_OFFSET);

        LocalDate ninetyDaysEarlier = currentTime.minusDays(NINTEY_DAY_OFFSET);


        List<Transaction> thirtyDayTransactions = customerSpending.stream().filter(transaction -> transaction.getDate().isAfter(thirtyDaysEarlier)).collect(Collectors.toList());
        List<Transaction> ninetyDayTransactions = customerSpending.stream().filter(transaction -> transaction.getDate().isAfter(ninetyDaysEarlier)&&transaction.getDate().isBefore(sixtyDaysEarlier)).collect(Collectors.toList());
        List<Transaction> sixtyDayTransactions = customerSpending.stream().filter(transaction -> transaction.getDate().isAfter(sixtyDaysEarlier)&&transaction.getDate().isBefore(thirtyDaysEarlier)).collect(Collectors.toList());

        Long thirtyDayRewards = thirtyDayTransactions.stream().map(transaction -> calcualteAndReturnPoints(transaction)).collect(Collectors.summingLong(i -> i));
        Long ninetyDayRewards = ninetyDayTransactions.stream().map(transaction -> calcualteAndReturnPoints(transaction)).collect(Collectors.summingLong(i -> i));
        Long sixtyDayRewards = sixtyDayTransactions.stream().map(transaction -> calcualteAndReturnPoints(transaction)).collect(Collectors.summingLong(i -> i));

        rewardInfoOfCustomer.setRewardPointsForPastThirtyDays(thirtyDayRewards);
        rewardInfoOfCustomer.setRewardPointsBetweenPastThirtyToSixtyDays(sixtyDayRewards);
        rewardInfoOfCustomer.setRewardPointsBetweenPastSixtyToNinetyDays(ninetyDayRewards);

        rewardInfoOfCustomer.setTotalRewardsInPastNinetyDays(thirtyDayRewards+sixtyDayRewards+ninetyDayRewards);

        return rewardInfoOfCustomer;
    }

    private Long calcualteAndReturnPoints(Transaction transaction) {

        long rewardPoints = 0;
        if (null != transaction) {


            if (HUNDRED_DOLLARS < transaction.getAmountSpent()) {
                rewardPoints = Math.round(DOUBLE_POINT * (transaction.getAmountSpent() - HUNDRED_DOLLARS));
            }

            if (FIFTY_DOLLARS < transaction.getAmountSpent()) {
                rewardPoints += Math.round(SINGLE_POINT * (transaction.getAmountSpent() - FIFTY_DOLLARS));
            }
        }

        return rewardPoints;
    }




}
