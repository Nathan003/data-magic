package com.dodoca.datamagic.common.controller.springmvc;

import com.dodoca.datamagic.common.service.DataMagicHandle;
import com.dodoca.datamagic.common.service.DataMagicService;
import com.dodoca.datamagic.utils.DataMagicUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by lifei on 2016/12/8.
 */
@Controller
@RequestMapping("/addictions")
public class AddictionsController {

    @Autowired
    private DataMagicService dataMagicService;

    private Logger logger = Logger.getLogger(AddictionsController.class);

    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public void addiction(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws IOException {

        dataMagicService.service(params, request, response, new DataMagicHandle() {
            public com.dodoca.datamagic.utils.model.BaseResponse handle(String bookmarkID, String data, String token) {
                if(null == token){
                    return DataMagicUtil.reportAddiction(bookmarkID, data);
                }
                return DataMagicUtil.reportAddiction(bookmarkID,data,token);
            }
        });

    }
}
