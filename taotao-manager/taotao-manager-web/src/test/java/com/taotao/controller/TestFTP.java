package com.taotao.controller;

import com.taotao.common.utils.FtpUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by pei on 20/03/2018.
 */
public class TestFTP {

    @Test
    public void testFtpClient() throws IOException {
        // 创建FtpClient 对象
        FTPClient ftpClient = new FTPClient();
        // 创建ftp连接 默认端口21
        ftpClient.connect("127.0.0.1", 21);
//    登录ftp服务器，使用用户名和密码
        ftpClient.login("pei", "062525");
//上传文件， 参数1：服务器端文档名，
// 第二个参数： 上传的inputStream
        String file = "/Users/pei/program/java/IdeaProjects/taotao/taotao-manager/taotao-manager-web/src/main/webapp/upload/2.jpg";
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        ftpClient.changeWorkingDirectory("/Users/pei/program/java/IdeaProjects/taotao/taotao-manager/taotao-manager-web/src/main/webapp/upload");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.storeFile("testFtp.jpg", fileInputStream);
        ftpClient.logout();
    }

    @Test
    public void testFTP2() throws FileNotFoundException {
        String file = "/Users/pei/program/java/IdeaProjects/taotao/taotao-manager/taotao-manager-web/src/main/webapp/upload/2.jpg";
        FileInputStream fileInputStream = new FileInputStream(new File(file));
        String res = "/Users/pei/program/java/IdeaProjects/taotao/taotao-manager/taotao-manager-web/src/main/webapp/upload";
        FtpUtil.uploadFile("127.0.0.1",21,"pei","062525",res,"/2018/03/20","testFTP2.jpg",fileInputStream);
    }
}
