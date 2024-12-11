package com.web.controller;

import com.web.entity.Resident;
import com.web.entity.Vehicle;
import com.web.entity.VehicleServiceFee;
import com.web.repository.ResidentRepository;
import com.web.repository.VehicleRepository;
import com.web.repository.VehicleServiceFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ThongTinController {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleServiceFeeRepository vehicleServiceFeeRepository;


    @GetMapping(value = {"/thongtin"})
    public String loginView(HttpSession session, Model model){
        Resident resident = (Resident) session.getAttribute("resident");
        Resident res = residentRepository.findById(resident.getId()).get();
        model.addAttribute("canHo", res.getApartment());
        List<Resident> residentList = residentRepository.findByApartment(res.getApartment().getId());
        model.addAttribute("listRes", residentList);
        List<Vehicle> vehicleList = vehicleRepository.findByApartment(res.getApartment().getId());
        model.addAttribute("listVeh", vehicleList);
        Integer phiXe = 0;
        Integer xeOto = 0;
        Integer xeMay = 0;
        Integer xeDap = 0;
        VehicleServiceFee phiOTo = vehicleServiceFeeRepository.findById(1L).get();
        VehicleServiceFee phiXeMay = vehicleServiceFeeRepository.findById(2L).get();
        VehicleServiceFee phiXeDap = vehicleServiceFeeRepository.findById(3L).get();
        for(Vehicle v : vehicleList){
            if(v.getVehicleType() == 2){
                phiXe += phiOTo.getFee().intValue();
                ++ xeOto;
            }
            if(v.getVehicleType() == 1){
                phiXe += phiXeMay.getFee().intValue();
                ++ xeMay;
            }
            if(v.getVehicleType() == 0){
                phiXe += phiXeDap.getFee().intValue();
                ++ xeDap;
            }
        }
        model.addAttribute("tongPhiXe", phiXe);
        model.addAttribute("xeOto", xeOto);
        model.addAttribute("xeMay", xeMay);
        model.addAttribute("xeDap", xeDap);
        return "thongtin";
    }

}
