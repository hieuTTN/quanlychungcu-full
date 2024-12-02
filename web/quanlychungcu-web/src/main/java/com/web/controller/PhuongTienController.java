package com.web.controller;

import com.web.entity.Resident;
import com.web.entity.Vehicle;
import com.web.repository.ResidentRepository;
import com.web.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PhuongTienController {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping(value = {"/dangkyphuongtien"})
    public String view(HttpSession session, Model model, @RequestParam(required = false) Long id){
        model.addAttribute("vehicle", new Vehicle());
        if(id != null){
            model.addAttribute("vehicle", vehicleRepository.findById(id).get());
        }
        return "dangkyphuongtien";
    }

    @PostMapping(value = {"/dangkyphuongtien"})
    public String post(HttpSession session, @ModelAttribute Vehicle vehicle, RedirectAttributes redirectAttributes){
        Resident resident = (Resident) session.getAttribute("resident");
        vehicle.setCreatedDate(new Date(System.currentTimeMillis()));
        if(vehicle.getId() != null){
            Vehicle ex = vehicleRepository.findById(vehicle.getId()).get();
            vehicle.setCreatedDate(ex.getCreatedDate());
            vehicle.setUpdateDate(new Date(System.currentTimeMillis()));
            if(vehicle.getVehicleType() == 2){
                Vehicle oto = vehicleRepository.findByIdAndTypeAndHd(vehicle.getId(), resident.getApartment().getId());
                if(oto != null){
                    redirectAttributes.addFlashAttribute("error", "Mỗi hộ dân chỉ được đăng ký 1 xe ô tô!");
                    return "redirect:/dangkyphuongtien";
                }
            }
        }
        else{
            if(vehicle.getVehicleType() == 2){
                Vehicle oto = vehicleRepository.findByTypeAndHd(resident.getApartment().getId());
                if(oto != null){
                    redirectAttributes.addFlashAttribute("error", "Mỗi hộ dân chỉ được đăng ký 1 xe ô tô!");
                    return "redirect:/dangkyphuongtien";
                }
            }
        }
        vehicle.setApartment(resident.getApartment());
        vehicleRepository.save(vehicle);
        redirectAttributes.addFlashAttribute("message", "Đăng ký phương tiện thành công!");
        return "redirect:/thongtin";
    }

    @GetMapping(value = {"/delete-vehicle"})
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        vehicleRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa bài viết thành công!");
        return "redirect:/thongtin";
    }
}
