package com.ezpaytest.ezpaytest.webService;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateSubscriptionResponse {
    private float amount;
    private String subscriptionType;
    private List listOfInvoiceDates;
}
