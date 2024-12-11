package com.web.service;

import com.web.entity.VehicleServiceFee;
import com.web.repository.VehicleServiceFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private VehicleServiceFeeRepository vehicleServiceFeeRepository;


    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        VehicleServiceFee phiOTo = vehicleServiceFeeRepository.findById(1L).get();
        VehicleServiceFee phiXeMay = vehicleServiceFeeRepository.findById(2L).get();
        VehicleServiceFee phiXeDap = vehicleServiceFeeRepository.findById(3L).get();
        model.addAttribute("phiOTo", phiOTo.getFee());
        model.addAttribute("phiXeMay", phiXeMay.getFee());
        model.addAttribute("phiXeDap", phiXeDap.getFee());
    }
}
