package com.casa.app.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUploadDTO {
    private long deviceId;
    private MultipartFile file;
}
