package com.taotao.service.imp;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;
import org.aspectj.util.FileUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by pei on 20/03/2018.
 */

@Service
public class PictureServiceImpl implements PictureService {

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USENAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_BASE_PATH}")
    private String FTP_BASE_PATH;

    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public Map uploadPicture(MultipartFile uploadFile) {
        Map resultMap = new HashMap<>();
        // 生成一个新的文件名
        String oldName = uploadFile.getOriginalFilename();
//        UUID.randomUUID();
        String newName = IDUtils.genImageName();
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        String imagePath = new DateTime().toString("/yyyy/MM/dd");
        try {
            boolean result= FtpUtil.uploadFile(FTP_ADDRESS,FTP_PORT,FTP_USENAME,FTP_PASSWORD,
                    FTP_BASE_PATH,imagePath,newName,uploadFile.getInputStream());
            if(!result){
                resultMap.put("error",1);
                resultMap.put("message","文件上传失败");
                return resultMap;
            }
            resultMap.put("error",0);
            resultMap.put("url",IMAGE_BASE_URL + imagePath+"/"+newName);
            return resultMap;
        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("error",1);
            resultMap.put("message","文件发生异常");
            return resultMap;
        }

    }
}