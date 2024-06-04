package com.pyip.hotel;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.jdbc.result.UpdatableResultSet;
import com.pyip.hotel.pojo.Hotel;
import com.pyip.hotel.pojo.HotelDoc;
import com.pyip.hotel.service.IHotelService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;
import java.io.IOException;
import java.util.List;

import static com.pyip.hotel.contents.HotelContents.MODEL_TEMPLATE;

@SpringBootTest
public class HotelIndexTest {
    @Autowired
    private IHotelService hotelService;

    private RestHighLevelClient client;

    @Test
    public void name(){
        System.out.println(client);
    }
    @BeforeEach
    public void before(){
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.233.34:9200")
        ));
    }
    @AfterEach
    public void after() throws IOException {
        this.client.close();
    }

//    创建资源库
    @Test
    public void testCreateHotelIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("hotel");
        request.source(MODEL_TEMPLATE, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }
//    删除资源库
    @Test
    public void testDeleteHotelIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        client.indices().delete(request,RequestOptions.DEFAULT);
    }
//    查询资源库
    @Test
    public void testExistHotelIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("hotel");
        boolean bExist = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(bExist ? "存在hotel资源库" : "不存在hotel资源库");
    }

    //添加文档
    @Test
    public void addDoc() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        HotelDoc hotelDoc = new HotelDoc(hotel);
        String json = JSON.toJSONString(hotelDoc);
        IndexRequest request = new IndexRequest("hotel").id(hotelDoc.getId().toString());
        request.source(json,XContentType.JSON);
        client.index(request,RequestOptions.DEFAULT);
    }
//    查询文档
    @Test
    public void getDoc() throws IOException {
        GetRequest request = new GetRequest("hotel", "61083");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        HotelDoc hotelDoc = JSON.parseObject(json, HotelDoc.class);
        System.out.println(hotelDoc);
    }
    // 删除文档
    @Test
    public void delDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel", "61083");
        client.delete(request,RequestOptions.DEFAULT);
    }
    // 修改文档
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel", "61083");
        request.doc(
                "price", "952",
                "starName", "四钻"
        );
        client.update(request,RequestOptions.DEFAULT);
    }
    // 批量导入文档
    @Test
    public void testBulk() throws IOException {
        // 批量查询酒店数据
        List<Hotel> hotels = hotelService.list();
        BulkRequest request = new BulkRequest();
        for (Hotel hotel : hotels) {
            // 2.1.转换为文档类型HotelDoc
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel").id(hotelDoc.getId().toString()).source(JSON.toJSONString(hotelDoc),XContentType.JSON));
        }
        client.bulk(request,RequestOptions.DEFAULT);
    }
}
