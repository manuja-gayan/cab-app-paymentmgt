/**
 * Copyrights 2020 Axiata Digital Labs Pvt Ltd.
 * All Rights Reserved.
 * <p>
 * These material are unpublished, proprietary, confidential source
 * code of Axiata Digital Labs Pvt Ltd (ADL) and constitute a TRADE
 * SECRET of ADL.
 * <p>
 * ADL retains all title to and intellectual property rights in these
 * materials.
 */
package com.ceyloncab.paymentmgtservice.application.controller;

import com.ceyloncab.paymentmgtservice.application.transport.request.CompleteRequest;
import com.ceyloncab.paymentmgtservice.application.transport.request.InitRequest;
import com.ceyloncab.paymentmgtservice.application.transport.request.PaymentRequest;
import com.ceyloncab.paymentmgtservice.domain.service.PaymentManagementService;
import com.ceyloncab.paymentmgtservice.domain.utils.CompleteRequestData;
import com.ceyloncab.paymentmgtservice.domain.utils.Redirect;
import com.ceyloncab.paymentmgtservice.domain.utils.RequestData;
import com.ceyloncab.paymentmgtservice.domain.utils.TransactionAmount;
import com.ceyloncab.paymentmgtservice.external.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * * This is the controller class for Payment management.
 */
@Controller
@RequestMapping("${base-url.context}/payment")
@Slf4j
public class PaymentController extends BaseController {

    @Autowired
    PaymentManagementService paymentManagementService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${auth.token}")
    private String authToken;
    @Value("${confirm.baseurl}")
    private String baseUrl;
    @Value("${clientId}")
    private String clientId;
    @Value("${returnUrl}")
    private String returnUrl;


    @PostMapping(value = "/initialize")
    public ResponseEntity<String> initializePayment(@Validated @RequestBody(required = true) PaymentRequest paymentRequest, HttpServletRequest request) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {

        paymentManagementService.initializePayment(paymentRequest);

        InitRequest initRequest = new InitRequest();
        initRequest.setVersion("1.5");
        initRequest.setMsgId("AD32B8FC-0D72-41D3-8F6B-51FB2107835E");
        initRequest.setOperation("PAYMENT_INIT");
        initRequest.setRequestDate(setRequestDate());
        initRequest.setValidateOnly(false);
        initRequest.setRequestData(new RequestData(clientId, "", "PURCHASE", new TransactionAmount(0l, 200l, 0l, paymentRequest.getCurrency()),
                new Redirect(returnUrl, "", "GET"), "2303299742", "", false, "", "", true,"{1,2,3}"));

        String result = hmacWithJava(hmacSHA256Algorithm, objectMapper.writeValueAsString(initRequest), key).toLowerCase();
        System.out.println(objectMapper.writeValueAsString(initRequest));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("HMAC", result);
        httpHeaders.add("AUTHTOKEN", "ed50a4b4-32a9-4a2e-9bde-ee36856a5712");
        httpHeaders.setAccept(Arrays.asList(MediaType.ALL));


        System.out.println(initRequest);

        HttpEntity<InitRequest> httpEntity = new HttpEntity<>(initRequest, httpHeaders);

        System.out.println(objectMapper.writeValueAsString(httpEntity));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://sampath.paycorp.lk/rest/service/proxy", HttpMethod.POST,httpEntity, String.class);
        System.out.println("test");
        System.out.println("test123 " + responseEntity.getStatusCode());

        System.out.println(responseEntity.getBody());

        return responseEntity;

    }

    @GetMapping(value = "/callback")
    public String paymentCallBack(@RequestParam("reqid") String reqId, @RequestParam("clientRef") String clientRef, Model model, HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException {
        log.info("payment callback received.ReqId:{}|ClientRef:{}", reqId, clientRef);

        try {

            CompleteRequest completeRequest = new CompleteRequest();
            completeRequest.setVersion("1.5");
            completeRequest.setOperation("PAYMENT_COMPLETE");
            completeRequest.setMsgId("AD32B8FC-0D72-41D3-8F6B-51FB2107835E");
            completeRequest.setRequestDate(setRequestDate());
            completeRequest.setValidateOnly(false);
            completeRequest.setRequestData(new CompleteRequestData(clientId, reqId));

            String result = hmacWithJava(hmacSHA256Algorithm, objectMapper.writeValueAsString(completeRequest), key).toLowerCase();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("HMAC", result);
            httpHeaders.add("AUTHTOKEN", "ed50a4b4-32a9-4a2e-9bde-ee36856a5712");
            httpHeaders.setAccept(Arrays.asList(MediaType.ALL));

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity httpEntity = new HttpEntity<>(completeRequest, httpHeaders);
            System.out.println("Request2: "+objectMapper.writeValueAsString(httpEntity));
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://sampath.paycorp.lk/rest/service/proxy", HttpMethod.POST, httpEntity, String.class);
            System.out.println("test");
            System.out.println("test123 " + responseEntity.getStatusCode());

            System.out.println("Body:"+responseEntity.getBody());


            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            String s = jsonNode.get("responseData").get("responseCode").asText();
            String e = jsonNode.get("responseData").get("txnReference").asText();

            if (!"00".equals(s)) {
                paymentManagementService.paymentCallback(reqId, clientRef, false);
                model.addAttribute("txnReference", e);
                return "payment-failed ";
            }

            paymentManagementService.paymentCallback(reqId, clientRef, true);
            model.addAttribute("txnReference", e);
            return "payment-success";


        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }


   public String setRequestDate() {
       Date dat = new Date();
        final  SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFmt.format(dat);
        return dateFmt.format(dat);
   }

    String hmacSHA256Algorithm = "HmacSHA256";
    String key = "K9CeM1J7Q1O9OqR7";
    public static String hmacWithJava(String algorithm, String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);


}


