package com.pyip.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyip.hotel.pojo.Hotel;
import com.pyip.hotel.pojo.PageResult;
import com.pyip.hotel.pojo.RequestParams;

public interface IHotelService extends IService<Hotel> {
    PageResult search(RequestParams params);
}
