package com.ceyloncab.paymentmgtservice.domain.utils;

import lombok.Getter;

public class Constants {
    public static final String UNHANDLED_ERROR_CODE = "PMS3000";
    @Getter
    public enum ResponseData {
        CREATE_SUCCESS("PMS1000", "Success","200"),
        QUERY_SUCCESS("PMS1001", "Verified","200"),
        COMMON_FAIL("PMS2000", "Failed","400"),
        CUSTOMER_NOT_FOUND("APMS2003", "Customer not found","400"),
        DRIVER_NOT_FOUND("APMS2003", "Driver not found","400"),
        INTERNAL_SERVER_ERROR("PMS3001", "Internal Server Error","500");


        private String code;
        private String message;
        private String responseCode;

        ResponseData(String code, String message, String responseCode) {
            this.code = code;
            this.message = message;
            this.responseCode= responseCode;
        }
    }
}
