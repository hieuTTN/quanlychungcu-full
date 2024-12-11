package com.web.controller;

import com.web.entity.*;
import com.web.repository.ServiceFeeRepository;
import com.web.repository.UtilityBillRepository;
import com.web.repository.VehicleFeeRepository;
import com.web.service.MailService;
import com.web.vnpay.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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

    @Autowired
    private MailService mailService;

    @GetMapping(value = {"/dong-chi-phi"})
    public String view(HttpSession session, Model model){
        Resident resident = (Resident) session.getAttribute("resident");
        List<VehicleFee> vehicleFees = vehicleFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<ServiceFee> serviceFees = serviceFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<UtilityBill> utilityBills = utilityBillRepository.dichVuChuaDong(resident.getApartment().getId());
        Double fee = 0D;
        for(VehicleFee v : vehicleFees){
            fee += v.getFee();
        }
        for(ServiceFee v : serviceFees){
            fee += v.getFee();
        }
        for(UtilityBill v : utilityBills){
            fee += v.getFee();
        }
        model.addAttribute("vehicleFees",vehicleFees);
        model.addAttribute("serviceFees",serviceFees);
        model.addAttribute("utilityBills",utilityBills);
        model.addAttribute("tongPhi",fee);
        return "dongchiphi";
    }

    @PostMapping("/thanhtoanAll")
    public String dongTatCa(HttpSession session){
        Double fee = 0D;
        Resident resident = (Resident) session.getAttribute("resident");
        List<VehicleFee> vehicleFees = vehicleFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<ServiceFee> serviceFees = serviceFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<UtilityBill> utilityBills = utilityBillRepository.dichVuChuaDong(resident.getApartment().getId());
        for(VehicleFee v : vehicleFees){
            fee += v.getFee();
        }
        for(ServiceFee v : serviceFees){
            fee += v.getFee();
        }
        for(UtilityBill v : utilityBills){
            fee += v.getFee();
        }
        String maThanhToan = String.valueOf(System.currentTimeMillis());
        session.setAttribute("maThanhToan", maThanhToan);
        String url = vnPayService.createOrder(fee.intValue(), maThanhToan, "http://localhost:8080/checkthanhtoanAll");
        return "redirect:"+url;
    }

    @GetMapping("/checkthanhtoanAll")
    public String checkthanhtoanAll(HttpSession session, HttpServletRequest request){
        int paymentStatus = vnPayService.orderReturn(request);
        if(paymentStatus != 1){
            return "redirect:/thongtin";
        }
        Double fee = 0D;
        Resident resident = (Resident) session.getAttribute("resident");
        List<VehicleFee> vehicleFees = vehicleFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<ServiceFee> serviceFees = serviceFeeRepository.dichVuChuaDong(resident.getApartment().getId());
        List<UtilityBill> utilityBills = utilityBillRepository.dichVuChuaDong(resident.getApartment().getId());
        for(VehicleFee v : vehicleFees){
            v.setPaidStatus(true);
            vehicleFeeRepository.save(v);
            fee += v.getFee();
        }
        for(ServiceFee v : serviceFees){
            v.setPaidStatus(true);
            serviceFeeRepository.save(v);
            fee += v.getFee();
        }
        for(UtilityBill v : utilityBills){
            v.setPaidStatus(true);
            utilityBillRepository.save(v);
            fee += v.getFee();
        }
        String maThanhToan = (String) session.getAttribute("maThanhToan");
        sendMail(resident.getApartment(), "Thanh toán phí còn lại", fee,resident.getFullName(),maThanhToan);
        return "redirect:/thongtin";
    }

    @PostMapping("/thanhtoan")
    public String createUrl(@RequestParam String loai, @RequestParam Long id, HttpSession session){
        Double fee = 0D;
        String maThanhToan = String.valueOf(System.currentTimeMillis());
        session.setAttribute("loai", loai);
        session.setAttribute("idobj", id);
        session.setAttribute("maThanhToan", maThanhToan);
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
        String url = vnPayService.createOrder(fee.intValue(), maThanhToan, "http://localhost:8080/checkthanhtoan");
        return "redirect:"+url;
    }



    @GetMapping("/checkthanhtoan")
    public String createUrl(HttpSession session, HttpServletRequest request){
        Double fee = 0D;
        String loai = (String) session.getAttribute("loai");
        String maThanhToan = (String) session.getAttribute("maThanhToan");
        Long id = (Long) session.getAttribute("idobj");
        Resident resident = (Resident) session.getAttribute("resident");
        int paymentStatus = vnPayService.orderReturn(request);
        if(paymentStatus != 1){
            return "redirect:/thongtin";
        }
        if(loai.equals("GUI_XE")){
            VehicleFee vehicleFee = vehicleFeeRepository.findById(id).get();
            vehicleFee.setPaidStatus(true);
            vehicleFeeRepository.save(vehicleFee);
            sendMail(resident.getApartment(), "Phí gửi xe", vehicleFee.getFee(),resident.getFullName(),maThanhToan);
        }
        if(loai.equals("DICH_VU")){
            ServiceFee serviceFee = serviceFeeRepository.findById(id).get();
            serviceFee.setPaidStatus(true);
            serviceFeeRepository.save(serviceFee);
            sendMail(resident.getApartment(), "Phí dịch vụ căn hộ", serviceFee.getFee(),resident.getFullName(),maThanhToan);
        }
        if(loai.equals("DIEN_NUOC")){
            UtilityBill utilityBill = utilityBillRepository.findById(id).get();
            utilityBill.setPaidStatus(true);
            utilityBillRepository.save(utilityBill);
            sendMail(resident.getApartment(), "Phí điện nước", utilityBill.getFee(),resident.getFullName(),maThanhToan);
        }
        return "redirect:/thongtin";
    }

    public void sendMail(Apartment apartment, String titleAp, Double fee, String tenNguoiThanhToan, String maThanhToan){
        String content = mailContent(tenNguoiThanhToan, maThanhToan, titleAp, fee);
        for(Resident r : apartment.getResidents()){
            mailService.sendEmail(r.getEmail(), "Thông báo thanh toán "+titleAp, content, false, true);
        }
    }

    public String mailContent(String tenNguoiThanhToan, String maThanhToan, String tenDichVu, Double soTien){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
        String ngayHienTai = now.format(formatter);
        Locale vietnamLocale = new Locale("vi", "VN"); // Định dạng theo Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnamLocale);
        String tien = currencyFormatter.format(soTien);
        String content =
        "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; margin: 20px;\">\n" +
                "        <div style=\"max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 8px; padding: 20px;\">\n" +
                "            <h2 style=\"text-align: center; color: #333; border-bottom: 2px solid #f4f4f4; padding-bottom: 10px;\">\n" +
                "                Thanh Toán Hóa Đơn\n" +
                "            </h2>\n" +
                "            <div style=\"margin-bottom: 20px;\">\n" +
                "                <p><strong>Tên người thanh toán:</strong> "+tenNguoiThanhToan+"</p>\n" +
                "                <p><strong>Ngày thanh toán:</strong> "+ngayHienTai+"</p>\n" +
                "                <p><strong>Mã thanh toán:</strong> #"+maThanhToan+"</p>\n" +
                "            </div>\n" +
                "            <table style=\"width: 100%; border-collapse: collapse; margin-bottom: 20px;\">\n" +
                "                <thead>\n" +
                "                    <tr style=\"background-color: #f4f4f4; text-align: left;\">\n" +
                "                        <th style=\"border: 1px solid #ddd; padding: 8px;\">Tên dịch vụ</th>\n" +
                "                        <th style=\"border: 1px solid #ddd; padding: 8px;\">Số tiền</th>\n" +
                "                    </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                    <tr>\n" +
                "                        <td style=\"border: 1px solid #ddd; padding: 8px;\">"+tenDichVu+"</td>\n" +
                "                        <td style=\"border: 1px solid #ddd; padding: 8px; text-align: right;\">"+tien+"</td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "            <div style=\"text-align: center; margin-top: 20px;\">\n" +
                "                <p>Cảm ơn bạn đã thanh toán!</p>\n" +
                "                <p>Nếu bạn có bất kỳ câu hỏi nào, hãy liên hệ với chúng tôi theo địa chỉ <a href=\"mailto:support@example.com\" style=\"color: #007BFF; text-decoration: none;\">support@example.com</a>.</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>";
        return content;
    }
}
