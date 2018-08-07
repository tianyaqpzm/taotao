package com.taotao.rest.jedis;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTest {

    @Test
    public void addDocument()throws Exception{
        // 创建连接
        SolrServer solrServer = new HttpSolrServer("http://127.0.0.1:8080/solr");
        // 创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test001");
        document.addField("item_title","测试商品1");
        // 把文档对象写入库
        solrServer.add(document);
        solrServer.commit();

    }
    // update  只要添加一个同样id 的对象即可

    @Test
    public void deleteDecoument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://127.0.0.1:8080/solr");

//        solrServer.deleteById("test001");
        solrServer.deleteByQuery("*:*"); // 全部删除
        solrServer.commit();
    }

}
