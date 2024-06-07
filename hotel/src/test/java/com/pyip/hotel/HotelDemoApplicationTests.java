package com.pyip.hotel;

import com.pyip.hotel.service.IHotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HotelDemoApplicationTests {
    @Autowired
    private IHotelService hotelService;

    @Test
    public void testFilter(){

    }
}
