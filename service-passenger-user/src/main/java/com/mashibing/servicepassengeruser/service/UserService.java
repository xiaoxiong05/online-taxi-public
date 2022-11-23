package com.mashibing.servicepassengeruser.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicepassengeruser.dto.PassengerUser;
import com.mashibing.servicepassengeruser.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private PassengerUserMapper passengerUserMapper;

    public ResponseResult LoginOrRegister(String passengerPhone) {
        System.out.println("user service 被调用");
        HashMap<String, Object> map = new HashMap<>();
        map.put("paggenger_phone", passengerPhone);
        List<PassengerUser> users = passengerUserMapper.selectByMap(map);
        System.out.println(users.size() == 0 ? "无用户" : users.get(0).getPassengerName());

        if (users.size() == 0) {
            PassengerUser user = new PassengerUser();
            user.setPaggengerPhone(passengerPhone);
            user.setPassengerName("张三");
            user.setPassengerGender((byte) 0);
            user.setState((byte) 0);
            LocalDateTime now = LocalDateTime.now();
            user.setGmtCreate(now);
            user.setGmtModified(now);
            passengerUserMapper.insert(user);
        }
        return ResponseResult.success();
    }
}
