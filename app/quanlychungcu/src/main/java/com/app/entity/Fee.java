package com.app.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fee {

    private Apartment apartment;

    private ServiceFee serviceFee;

    private VehicleFee vehicleFee;

    private UtilityBill utilityBill;
}
