package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.dataIntegrity.file.FileOperation;
import com.taotao.dataIntegrity.publicVerification.DataOwner;
import com.taotao.dataIntegrity.publicVerification.PublicInfor;
import com.taotao.dataIntegrity.tool.PropertiesUtil;
import com.taotao.dataIntegrity.tool.Stopwatch;
import com.taotao.service.IFileTagService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

class Fileurl{
    public String fileurl;

    public Fileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}

/**
 * Created by pei on 2017/6/21.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Controller
@RequestMapping("/backend")
public class Chain_PretreatmentController {
    @Autowired
    private IFileTagService iFileTagService;

    @RequestMapping(value = "/pretreatment")
    public String pretreatment(){
        return "backend/pretreatment/index";
    }

    @RequestMapping(value = "/pretreatment/file")
    @ResponseBody
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {

        System.out.println("开始");
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
//        String fileName = new Date().getTime()+".jpg";
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        model.addAttribute("fileUrl", targetFile);
        //为了保证功能的兼容性，需要把Result转化为json格式的字符串
        Fileurl fileurl1 = new Fileurl(path+fileName);
        String result= JsonUtils.objectToJson(fileurl1);
        return result;
    }

//        Map result1= iPretreatmentService.uploadPicture(uploadFile);



    @RequestMapping(value = "/pretreatment/cutfile")
    public String cutfile( ModelMap model )throws IOException {

// 加载双线性映射对
        PairingFactory.getInstance().setUsePBCWhenPossible(false);
        Pairing p = PairingFactory.getPairing(PublicInfor.CURVEPATH);
        DataOwner dataOwner = new DataOwner(p);

        // 读取配置文件
        String filePath1 = "/Users/pei/program/java/IdeaProjects/taotao/taotao-common/src/main/resources/fileconfig.properties";
        Properties pro = PropertiesUtil.loadProperties(filePath1);
//        String filePath = pro.getProperty("filePath"); // 获取待处理文件位置
        String filePath = "/Users/pei/Documents/testDataIntegrity/1G.txt";
        // 分块数目
        int sectors = Integer.valueOf(pro.getProperty("sectors"));
        int sectorSize = Integer.valueOf(pro.getProperty("sectorSize"));
        int blockSize1 = Integer.valueOf(pro.getProperty("blockSize"));

        int blockSize = sectors * sectorSize / 1000; // 单位KB？？？

        System.out.println(blockSize+' '+sectors+" "+sectorSize);
        Stopwatch genTagTime = new Stopwatch(); // 计数器
//		System.out.println(" "+genTagTime);
        double DoTime = 0.00;

//        对文件进行预处理 生成块对数字签名
        Element[][] nSectors = FileOperation.preProcessFile(filePath, sectors, sectorSize, p.getZr());
        int n = FileOperation.blockNumbers(filePath, blockSize); // 块总数
        System.out.println("n:"+n);

        dataOwner.setup(sectors);// 初始化校验参数
        dataOwner.keyGen(); // 生成公钥和私钥

//        在此处应用Merkle tree 未完成？？？？
        Element[] tags = dataOwner.metaGen(nSectors, n);// 计算块标签
        iFileTagService.insertTag(tags);


        model.addAttribute("tags", tags);
//        DoTime += genTagTime.elapsedTime();
////		DoTime = DataFilter.roundDouble(DoTime / 1000, 3);
//        System.out.println("时间 "+DoTime);
//        dataOwner.publicInfor();
//        for(Element i: tags){
//            System.out.println(i);
//        }
//        dataOwner.storeTag(tags);

        return "/backend/pretreatment/index";

    }
}
