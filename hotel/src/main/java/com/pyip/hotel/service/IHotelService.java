package com.pyip.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyip.hotel.pojo.Hotel;
import com.pyip.hotel.pojo.PageResult;
import com.pyip.hotel.pojo.RequestParams;

import java.util.List;
import java.util.Map;

public interface IHotelService extends IService<Hotel> {
    PageResult search(RequestParams params);

    Map<String, List<String>> getFilters(RequestParams params);

    List<String> getSuggestions(String prefix);

    void saveById(Long hotelId);

    void deleteById(Long hotelId);
}
