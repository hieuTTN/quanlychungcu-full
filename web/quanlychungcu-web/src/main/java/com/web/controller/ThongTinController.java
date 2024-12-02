package com.web.controller;

import com.web.entity.Resident;
import com.web.entity.Vehicle;
import com.web.repository.ResidentRepository;
import com.web.repository.VehicleRepository;
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
        for(Vehicle v : vehicleList){
            if(v.getVehicleType() == 2){
                phiXe += 2000000;
                ++ xeOto;
            }
            if(v.getVehicleType() == 1){
                phiXe += 150000;
                ++ xeMay;
            }
            if(v.getVehicleType() == 0){
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
