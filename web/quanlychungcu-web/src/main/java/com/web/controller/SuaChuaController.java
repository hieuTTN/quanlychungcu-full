package com.web.controller;

import com.web.entity.RepairRequest;
import com.web.entity.Resident;
import com.web.entity.Vehicle;
import com.web.repository.RepairRequestRepository;
import com.web.repository.ResidentRepository;
import com.web.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SuaChuaController {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private RepairRequestRepository repairRequestRepository;

    @GetMapping(value = {"/sua-chua"})
    public String view(HttpSession session, Model model){
        Resident resident = (Resident) session.getAttribute("resident");
        List<RepairRequest> residentList = repairRequestRepository.findByApartment(resident.getApartment().getId());
        model.addAttribute("listRes", residentList);
        return "suachua";
    }

    @GetMapping(value = {"/add-sua-chua"})
    public String viewAdd(HttpSession session, Model model, @RequestParam(required = false) Long id){
        model.addAttribute("repair", new RepairRequest());
        if(id != null){
            model.addAttribute("repair", repairRequestRepository.findById(id).get());
        }
        return "addsuachua";
    }

    @PostMapping(value = {"/add-sua-chua"})
    public String postAdd(HttpSession session, @ModelAttribute RepairRequest repair, RedirectAttributes redirectAttributes){
        Resident resident = (Resident) session.getAttribute("resident");
        repair.setRequestDate(LocalDateTime.now());
        repair.setApartment(resident.getApartment());
        repair.setPaid(false);
        repair.setCanceled(false);
        repair.setFee(0D);
        if(repair.getId() != null){
            RepairRequest repairRequest = repairRequestRepository.findById(repair.getId()).get();
            repair.setFee(repairRequest.getFee());
            repair.setRequestDate(repairRequest.getRequestDate());
            repair.setFixed(repairRequest.getFixed());
        }
        repairRequestRepository.save(repair);
        return "redirect:/sua-chua";
    }

    @GetMapping(value = {"/delete-sua-chua"})
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        RepairRequest repairRequest = repairRequestRepository.findById(id).get();
        repairRequest.setCanceled(true);
        repairRequest.setCancelDate(LocalDateTime.now());
        repairRequestRepository.save(repairRequest);
        redirectAttributes.addFlashAttribute("message", "Hủy thành công!");
        return "redirect:/sua-chua";
    }
}
