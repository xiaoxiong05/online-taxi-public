package com.mashibing.serviceverificationcode.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.responese.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class numberCodeController {

    @GetMapping("/numberCode/{size}")
    public ResponseResult getNumberCode(@PathVariable Integer size) {

        double mathRandom = (Math.random() * 9) * (Math.pow(10, size - 1));
        int resultInt = (int) mathRandom;
        System.out.println(resultInt);


        NumberCodeResponse numberCode = new NumberCodeResponse();
        numberCode.setNumberCode(resultInt);

        return ResponseResult.success(numberCode);

    }
}
