package com.ezpaytest.ezpaytest.controller;

import com.ezpaytest.ezpaytest.service.SubscriptionService;
import com.ezpaytest.ezpaytest.webService.CreateSubscriptionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path="/api/v1/test")
public class apiController {
    private static List<String> daysOfWeek = Arrays.asList("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY");
    private static List<String> subscriptionType = Arrays.asList("DAILY", "WEEKLY", "MONTHLY");

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping(path="/createSubscription")
    public ResponseEntity createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest) {
        List<String> errorList = new ArrayList<>();

        if (!subscriptionType.contains(createSubscriptionRequest.getSubscriptionType().toUpperCase(Locale.ROOT))) {
            errorList.add("Please use on of this as subscriptionType DAILY, WEEKLY or MONTHLY");
        }

        Pattern pattern = Pattern.compile("\\d{2}\\/\\d{2}\\/\\d{4}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(createSubscriptionRequest.getEndDate());
        boolean matchFound = matcher.find();
        if (!matchFound) {
            errorList.add("Please use dd/MM/yyyy format for endDate");
        }

        matcher = pattern.matcher(createSubscriptionRequest.getStartDate());
        matchFound = matcher.find();
        if (!matchFound) {
            errorList.add("Please use dd/MM/yyyy format for startDate");
        }

        if (createSubscriptionRequest.getSubscriptionType().equals("WEEKLY")) {
            if (!daysOfWeek.contains(createSubscriptionRequest.getDayOfWeekOrMonth())) {
                errorList.add("Please use MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY dayOfWeekOrMonth for WEEKLY subscriptionType");
            }
        }

        if (createSubscriptionRequest.getSubscriptionType().equals("MONTHLY")) {
            if ( Integer.parseInt(createSubscriptionRequest.getDayOfWeekOrMonth()) < 1 ||
                    Integer.parseInt(createSubscriptionRequest.getDayOfWeekOrMonth()) > 31 ) {
                errorList.add("Please use proper day of month");
            }
        }

        createSubscriptionRequest.getDayOfWeekOrMonth();
        if (errorList.size() > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
        }

        return ResponseEntity.ok().body(subscriptionService.createSubscription(createSubscriptionRequest));
    }
}
