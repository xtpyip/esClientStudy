package com.pyip.hotel.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyip.hotel.mapper.HotelMapper;
import com.pyip.hotel.pojo.Hotel;
import com.pyip.hotel.service.IHotelService;
import org.springframework.stereotype.Service;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {
}
