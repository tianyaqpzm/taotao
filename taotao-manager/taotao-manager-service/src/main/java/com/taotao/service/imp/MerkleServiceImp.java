package com.taotao.service.imp;

import com.taotao.mapper.FiletagMapper;
import com.taotao.pojo.Filetag;
import com.taotao.service.MerkleService;
import org.springframework.stereotype.Service;
import com.taotao.dataIntegrity.db.DBOperation;
import it.unisa.dia.gas.jpbc.Element;
import com.taotao.dataIntegrity.publicVerification.MerkleTree;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pei on 2017/8/21.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")

@Service
public class MerkleServiceImp implements MerkleService{

    @Resource
    private FiletagMapper filetagMapper;

    @Override
    public String computeRoot() {
        MerkleTree merkleTree = null;
        merkleTree = new MerkleTree(readTags());

        merkleTree.merkle_tree();
        String root = merkleTree.getRoot();
        System.out.println("Mrekle root:"+root);
        return root;
    }

    public  Map<String,byte[]> readTags(){

//        Filetag filetag = filetagMapper.selectAll();

        DBOperation dbo=new DBOperation();
        Map<String,byte[]> results=dbo.selectBatch("filetag");
        return results;
    }



}
