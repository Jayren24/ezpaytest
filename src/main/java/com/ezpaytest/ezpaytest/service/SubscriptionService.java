package com.ezpaytest.ezpaytest.service;

import com.ezpaytest.ezpaytest.webService.CreateSubscriptionRequest;
import com.ezpaytest.ezpaytest.webService.CreateSubscriptionResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SubscriptionService {
    private static List<String> thirtyDaysMonths = Arrays.asList("APRIL","JUNE","SEPTEMBER","NOVEMBER");

    public CreateSubscriptionResponse createSubscription (CreateSubscriptionRequest createSubscriptionRequest){

        CreateSubscriptionResponse createSubscriptionResponse = new CreateSubscriptionResponse();
        createSubscriptionResponse.setAmount(createSubscriptionRequest.getAmount());
        createSubscriptionResponse.setSubscriptionType(createSubscriptionRequest.getSubscriptionType());

        if (createSubscriptionRequest.getSubscriptionType().equals("DAILY")) {
            createSubscriptionResponse.setListOfInvoiceDates(getDailyInvoiceDates(createSubscriptionRequest));
        }
        else if (createSubscriptionRequest.getSubscriptionType().equals("WEEKLY")) {
            createSubscriptionResponse.setListOfInvoiceDates(getWeeklyInvoiceDates(createSubscriptionRequest));
        }
        else if (createSubscriptionRequest.getSubscriptionType().equals("MONTHLY")) {
            createSubscriptionResponse.setListOfInvoiceDates(getMonthlyInvoiceDates(createSubscriptionRequest));
        }

        return createSubscriptionResponse;
    }

    private List getDailyInvoiceDates(CreateSubscriptionRequest createSubscriptionRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(createSubscriptionRequest.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createSubscriptionRequest.getEndDate(), formatter);
        List<String> invoiceDates = new ArrayList<>();

        while (!startDate.isAfter(endDate)) {
            invoiceDates.add(startDate.format(formatter));
            startDate = startDate.plusDays(1);
        }

        return invoiceDates;
    }

    private List getWeeklyInvoiceDates(CreateSubscriptionRequest createSubscriptionRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(createSubscriptionRequest.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createSubscriptionRequest.getEndDate(), formatter);
        List<String> invoiceDates = new ArrayList<>();

        while (!startDate.getDayOfWeek().toString().equals(createSubscriptionRequest.getDayOfWeekOrMonth())) {
            startDate = startDate.plusDays(1);
        }

        while (!startDate.isAfter(endDate)) {
            invoiceDates.add(startDate.format(formatter));
            startDate = startDate.plusWeeks(1);
        }

        return invoiceDates;
    }

    private List getMonthlyInvoiceDates(CreateSubscriptionRequest createSubscriptionRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(createSubscriptionRequest.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(createSubscriptionRequest.getEndDate(), formatter);
        List<String> invoiceDates = new ArrayList<>();

        while (startDate.getDayOfMonth() != Integer.parseInt(createSubscriptionRequest.getDayOfWeekOrMonth())) {
            startDate = startDate.plusDays(1);
        }

        while (!startDate.isAfter(endDate)) {
            //Below if clause is done assuming there must be an invoice every month and will use the last day of the month if the day of month input is larger than the last day of month.
            if (!((startDate.getMonth().toString().equals("FEBRUARY") && Integer.parseInt(createSubscriptionRequest.getDayOfWeekOrMonth()) > 28)
                    || (thirtyDaysMonths.contains(startDate.getMonth().toString()) && Integer.parseInt(createSubscriptionRequest.getDayOfWeekOrMonth()) > 30))) {
                while (startDate.getDayOfMonth() < Integer.parseInt(createSubscriptionRequest.getDayOfWeekOrMonth())) {
                    startDate = startDate.plusDays(1);
                }
            }

            invoiceDates.add(startDate.format(formatter));
            startDate = startDate.plusMonths(1);
        }

        return invoiceDates;
    }
}
