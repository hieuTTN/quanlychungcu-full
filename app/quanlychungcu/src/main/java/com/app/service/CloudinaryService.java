package com.app.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "dxqh3xpza");
        config.put("api_key", "159365789567286");
        config.put("api_secret", "jVbDdyDleglBDRE2UIvjKebvWSM");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    public String uploadFile(File uploadedFile){
        try {
            Map uploadResult = cloudinaryConfig().uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            uploadedFile.delete();
            return  uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
