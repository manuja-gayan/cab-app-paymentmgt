package com.ceyloncab.paymentmgtservice.domain.entity;

import com.ceyloncab.paymentmgtservice.domain.utils.RideType;
import com.ceyloncab.paymentmgtservice.domain.utils.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;


@Data
@Document(collection = "Driver")
public class DriverEntity {
    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String licenceNumber;
    private String nicNumber;
    private String vehicleNumber;
    private Status status =Status.PENDING;
    private String vehicleType;
    private RideType rideType;
    @Indexed(unique = true)
    private String msisdn;
    private String password;
    private String area;
    private Integer averageStars;
    private Integer totalReviews;
    private String  imgProfile;
    private ArrayList<String> img_ref_licence;
    private ArrayList<String> img_ref_nic;
    private ArrayList<String> img_ref_vehicle_status;
    private ArrayList<String> img_ref_otherDocuments;
    @LastModifiedDate
    private Date updatedTime;

}
