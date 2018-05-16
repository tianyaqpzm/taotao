package com.taotao.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by pei on 20/03/2018.
 */
public interface PictureService {
    Map uploadPicture(MultipartFile multipartFile);
}
