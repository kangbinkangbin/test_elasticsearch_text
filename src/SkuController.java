package com.jk.controller;

import com.alibaba.fastjson.JSONObject;
import com.jk.bean.Sku;
import com.jk.service.SkuService;
import com.jk.utils.RestClientFactory;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SkuController {
    @Autowired
    private SkuService skuService;

    //=新增1
   /* @ResponseBody
    @RequestMapping("addSku")
    public String addSku(Sku sku) throws IOException {
        //==获取链接,用工具类获取客户端
        RestHighLevelClient highLevelClient = RestClientFactory.getHighLevelClient();
        //首先指定索引的位置，可以在外部创建
        IndexRequest indexRequest = new IndexRequest("kk_01","type_02","");
        //===把对象转换成json
        String str = JSONObject.toJSONString(sku);

        //===存放数据到索引中
        IndexRequest indexRequest1 = indexRequest.source(str, XContentType.JSON);


        //====发送请求

            highLevelClient.index(indexRequest1);


        return "1";
    }*/
//====2
    @ResponseBody
    @RequestMapping("addSku")
    public String addSku(Sku sku) throws IOException {
        //==获取链接,用工具类获取客户端
        RestHighLevelClient highLevelClient = RestClientFactory.getHighLevelClient();
        //首先指定索引的位置，可以在外部创建
        IndexRequest indexRequest = new IndexRequest("kk_01", "type_02", "oseCBWkB9fxRqONsl4bK");
        //===把对象转换成json
        String str = JSONObject.toJSONString(sku);

        //===存放数据到索引中
        IndexRequest indexRequest1 = indexRequest.source(str, XContentType.JSON);

        highLevelClient.indexAsync(indexRequest1, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println(222);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(111);            }
        });

        System.out.println("111111");
        return "1";
    }
   //====3
   @ResponseBody
   @RequestMapping("deleSku")
    public String deleSku(String id){


       DeleteRequest deleteRequest = new DeleteRequest("kk_01","type_02",id);
       //==获取链接,用工具类获取客户端
       RestHighLevelClient highLevelClient = RestClientFactory.getHighLevelClient();

       highLevelClient.deleteAsync(deleteRequest, new ActionListener<DeleteResponse>() {
           @Override
           public void onResponse(DeleteResponse deleteResponse) {
               System.out.println("删除了");
           }

           @Override
           public void onFailure(Exception e) {
               System.out.println("失败了");
           }
       });

       return "1";
   }

   //====4 查询

    @ResponseBody
    @RequestMapping("querySku")
    public String querySku(String queryString) throws IOException {

        SearchRequest searchRequest = new SearchRequest("sku_06");
        System.out.println(searchRequest);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //====query{}
         QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString);
        System.out.println(queryStringQueryBuilder);

        sourceBuilder.query(queryStringQueryBuilder);

        searchRequest.source(sourceBuilder);

        //==获取链接,用工具类获取客户端
        RestHighLevelClient highLevelClient = RestClientFactory.getHighLevelClient();
        SearchResponse search = highLevelClient.search(searchRequest);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit : hits) {
            String string = hit.getSourceAsString();
            System.out.println(string);
            Sku sku = JSONObject.parseObject(string, Sku.class);
        }
        return "1";
    }


}
