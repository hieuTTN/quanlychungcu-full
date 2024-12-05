package com.web.controller;

import com.web.entity.*;
import com.web.repository.ServiceFeeRepository;
import com.web.repository.UtilityBillRepository;
import com.web.repository.VehicleFeeRepository;
import com.web.vnpay.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class DongChiPhiController {

    @Autowired
    private ServiceFeeRepository serviceFeeRepository;

    @Autowired
    private VehicleFeeRepository vehicleFeeRepository;

    @Autowired
    private UtilityBillRepository utilityBillRepository;

    @Autowired
    private VNPayService vnPayService;

    @GetMapping(value = {"/dong-chi-phi"})
    public String view(HttpSession session, Model model){
        Resident resident = (Resident) session.getAttribute("resident");
        List<VehicleFee> vehicleFees = vehicleFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<ServiceFee> serviceFees = serviceFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<UtilityBill> utilityBills = utilityBillRepository.dichVuChuaDong(resident.getApartment().getId());

        model.addAttribute("vehicleFees",vehicleFees);
        model.addAttribute("serviceFees",serviceFees);
        model.addAttribute("utilityBills",utilityBills);
        return "dongchiphi";
    }

    @PostMapping("/thanhtoan")
    public String createUrl(@RequestParam String loai, @RequestParam Long id, HttpSession session){
        Double fee = 0D;
        session.setAttribute("loai", loai);
        if(loai.equals("GUI_XE")){
            VehicleFee vehicleFee = vehicleFeeRepository.findById(id).get();
            fee = vehicleFee.getFee();
            session.setAttribute("objfee", vehicleFee);
        }
        if(loai.equals("DICH_VU")){
            ServiceFee serviceFee = serviceFeeRepository.findById(id).get();
            fee = serviceFee.getFee();
            session.setAttribute("objfee", serviceFee);
        }
        if(loai.equals("DIEN_NUOC")){
            UtilityBill utilityBill = utilityBillRepository.findById(id).get();
            fee = utilityBill.getFee();
            session.setAttribute("objfee", utilityBill);
        }
        String url = vnPayService.createOrder(fee.intValue(), String.valueOf(System.currentTimeMillis()), "http://localhost:8080/checkthanhtoan");
        return "redirect:"+url;
    }
}
