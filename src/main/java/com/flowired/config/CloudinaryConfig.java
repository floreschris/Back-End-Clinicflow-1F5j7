package com.flowired.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnwvrt1tw",
                "api_key", "289851318916745",
                "api_secret", "Nrk-2iSqPV5pu-wS2s0wHPvvDT8"
        ));
    }
}
