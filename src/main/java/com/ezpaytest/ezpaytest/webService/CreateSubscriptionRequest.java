package com.ezpaytest.ezpaytest.webService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateSubscriptionRequest {
    private float amount;
    private String subscriptionType;
    private String dayOfWeekOrMonth;
    private String startDate;
    private String endDate;

    @JsonCreator
    public CreateSubscriptionRequest(@JsonProperty("amount")float amount) {
        this.amount = amount;
    }
}
