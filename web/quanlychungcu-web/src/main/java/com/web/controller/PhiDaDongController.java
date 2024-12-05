package com.web.controller;

import com.web.entity.Resident;
import com.web.entity.ServiceFee;
import com.web.entity.UtilityBill;
import com.web.entity.VehicleFee;
import com.web.repository.ServiceFeeRepository;
import com.web.repository.UtilityBillRepository;
import com.web.repository.VehicleFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PhiDaDongController {

    @Autowired
    private ServiceFeeRepository serviceFeeRepository;

    @Autowired
    private VehicleFeeRepository vehicleFeeRepository;

    @Autowired
    private UtilityBillRepository utilityBillRepository;

    @GetMapping(value = {"/phidadong"})
    public String view(HttpSession session, Model model, @RequestParam(required = false) Integer month,
                       @RequestParam(required = false) Integer year){
        Resident resident = (Resident) session.getAttribute("resident");
        List<VehicleFee> vehicleFees = vehicleFeeRepository.dichVuDaDong(resident.getApartment().getId());
        List<ServiceFee> serviceFees = serviceFeeRepository.dichVuDaDong(resident.getApartment().getId());
        List<UtilityBill> utilityBills = utilityBillRepository.dichVuDaDong(resident.getApartment().getId());
        if(month != null && year != null){
            vehicleFees = vehicleFeeRepository.dichVuDaDong(resident.getApartment().getId(), month, year);
            serviceFees = serviceFeeRepository.dichVuDaDong(resident.getApartment().getId(), month, year);
            utilityBills = utilityBillRepository.dichVuDaDong(resident.getApartment().getId(), month, year);
        }

        model.addAttribute("vehicleFees",vehicleFees);
        model.addAttribute("serviceFees",serviceFees);
        model.addAttribute("utilityBills",utilityBills);
        return "phidadong";
    }
}
