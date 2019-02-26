package com.charter.codeTest.rewardsCalculator.domain;

public class RewardsInfo {

    private Long accountId;

    private Long rewardPointsForPastThirtyDays;

    private Long rewardPointsBetweenPastThirtyToSixtyDays;

    private Long rewardPointsBetweenPastSixtyToNinetyDays;

    private Long totalRewardsInPastNinetyDays;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRewardPointsForPastThirtyDays() {
        return rewardPointsForPastThirtyDays;
    }

    public void setRewardPointsForPastThirtyDays(Long rewardPointsForPastThirtyDays) {
        this.rewardPointsForPastThirtyDays = rewardPointsForPastThirtyDays;
    }

    public Long getRewardPointsBetweenPastThirtyToSixtyDays() {
        return rewardPointsBetweenPastThirtyToSixtyDays;
    }

    public void setRewardPointsBetweenPastThirtyToSixtyDays(Long rewardPointsBetweenPastThirtyToSixtyDays) {
        this.rewardPointsBetweenPastThirtyToSixtyDays = rewardPointsBetweenPastThirtyToSixtyDays;
    }

    public Long getRewardPointsBetweenPastSixtyToNinetyDays() {
        return rewardPointsBetweenPastSixtyToNinetyDays;
    }

    public void setRewardPointsBetweenPastSixtyToNinetyDays(Long rewardPointsBetweenPastSixtyToNinetyDays) {
        this.rewardPointsBetweenPastSixtyToNinetyDays = rewardPointsBetweenPastSixtyToNinetyDays;
    }

    public Long getTotalRewardsInPastNinetyDays() {
        return totalRewardsInPastNinetyDays;
    }

    public void setTotalRewardsInPastNinetyDays(Long totalRewardsInPastNinetyDays) {
        this.totalRewardsInPastNinetyDays = totalRewardsInPastNinetyDays;
    }
}
