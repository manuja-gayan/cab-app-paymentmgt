package com.ceyloncab.paymentmgtservice.domain.service;

import com.ceyloncab.paymentmgtservice.application.aop.AopConstants;
import com.ceyloncab.paymentmgtservice.application.transport.request.PaymentRequest;
import com.ceyloncab.paymentmgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.TransactionEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.TripEntity;
import com.ceyloncab.paymentmgtservice.domain.entity.dto.response.TransactionDTO;
import com.ceyloncab.paymentmgtservice.domain.exception.DomainException;
import com.ceyloncab.paymentmgtservice.domain.utils.Constants;
import com.ceyloncab.paymentmgtservice.domain.utils.TripInfo;
import com.ceyloncab.paymentmgtservice.domain.utils.UserType;
import com.ceyloncab.paymentmgtservice.external.repository.CustomerRepository;
import com.ceyloncab.paymentmgtservice.external.repository.TransactionRepository;
import com.ceyloncab.paymentmgtservice.external.repository.TripRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class PaymentManagementService {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TripRepository tripRepository;


    @Value("${auth.token}")
    private String authToken;
    @Value("${confirm.baseurl}")
    private String baseUrl;


    public void initializePayment(PaymentRequest paymentRequest) {
        System.out.println(MDC.get(AopConstants.MDC_USERID));

        CustomerEntity customerEntity = customerRepository.findById(MDC.get(AopConstants.MDC_USERID))
       /* CustomerEntity customerEntity = customerRepository.findById("63d20b41afcbe23a5c3c4717")*/
                .orElseThrow(() -> {
                    log.error("Customer does not exist for given userId:{}", AopConstants.MDC_USERID);
                    return new DomainException(Constants.ResponseData.CUSTOMER_NOT_FOUND);
                });
        try {
            System.out.println(paymentRequest.toString());
            Optional<TripEntity> byTripId = tripRepository.findByTripId(paymentRequest.getTripId());
            if (byTripId.isPresent()) {
                TransactionEntity transactionEntity = new TransactionEntity();
                transactionEntity.setUserId(customerEntity.getUserId());
//                transactionEntity.setUserType(UserType.valueOf(AopConstants.CHANNEL));
                transactionEntity.setUserType(UserType.CUSTOMER);
                transactionEntity.setTransactionType(paymentRequest.getTransactionType());
                transactionEntity.setTripInfo(new TripInfo(byTripId.get().getTripId(), byTripId.get().getDriverId(), null));
                transactionEntity.setTransactionStatus("PENDING");
                transactionEntity.setAmount(paymentRequest.getPaymentAmount());
                transactionEntity.setClientRef(getClientRefId());
                transactionEntity.setCurrency(paymentRequest.getCurrency());
                transactionRepository.save(transactionEntity);
            }else {
                log.error("Trip does not found for given tripId:{}", paymentRequest.getTripId());
            }

        } catch (Exception ex) {
            log.error("Error occurred in update Driver Profile.Error:{}", ex.getMessage());
        }


    }


    public void paymentCallback(String reqId, String clientRef, boolean status) {

        Optional<TransactionEntity> byClientRef = transactionRepository.findByClientRefAndTransactionStatus(clientRef, "PENDING");

        if (!byClientRef.isPresent()) {
            log.error("Trip does not found for given tripId:{}");
        }

        if (status) {
            byClientRef.get().setRequestId(reqId);
            byClientRef.get().setTransactionStatus("COMPLETED");
        } else {
            byClientRef.get().setRequestId(reqId);
            byClientRef.get().setTransactionStatus("FAILED");
        }

    }


    public TransactionDTO processRequest(String reqId, String clientRef) {


        String confirmUrl = baseUrl + "?csrfToken=" + reqId + "&authToken=" + authToken;

        TransactionDTO transactionDTO = new TransactionDTO();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Request entity is created with request headers
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
          /*  ResponseEntity<String> response = restTemplate.exchange(confirmUrl, HttpMethod.GET,requestEntity,String.class);*/

            URL url = new URL(confirmUrl);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");

            BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));

            String inputLine = in.readLine();// Get the response after POSTing


            // The response data will be in x-www-form-urlencode format
            String[] responseArray = inputLine.split("&");
            Map<String, String> respMap = new HashMap<>();
            for (String keyValuePair : responseArray) {
                String[] split = keyValuePair.split("=");
                respMap.put(split[0], split[1]);
            }


            transactionDTO.setResponseCode(respMap.get("responseCode"));
            transactionDTO.setResponseText(respMap.get("responseText"));
            transactionDTO.setTxnReference(respMap.get("txnReference"));
            transactionDTO.setStatus(true);

            return transactionDTO;

        } catch (Exception ex) {
            log.error("Error occurred in transaction.Error:{}", ex.getMessage());
            return transactionDTO;
        }

    }


    public String getClientRefId() {
        final Long LIMIT = 10000L;
        final Long deltaId = Long.parseLong(Long.toString(Long.parseLong((java.time.LocalDate.now()
                .format(DateTimeFormatter
                        .ofPattern("yyMMdd")))))
                .concat(Long.toString(System.currentTimeMillis() % LIMIT)));
        System.out.println("deltaId" + deltaId);
        return String.valueOf(deltaId);

    }

}
