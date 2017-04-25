package com.dodoca.datamagic.common.controller.springmvc;

import com.dodoca.datamagic.common.model.BaseResponse;
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
 * Created by lifei on 2016/12/6.
 */
@Controller
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private DataMagicService dataMagicService;

    private Logger logger = Logger.getLogger(EventsController.class);

    /**
     * 查询书签或者概览
     *
     * @param params
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public void segmentation(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            dataMagicService.service(params, request, response, new DataMagicHandle() {
                public com.dodoca.datamagic.utils.model.BaseResponse handle(String bookmarkID, String data, String token) {
                    if(null == token){
                        return DataMagicUtil.reportSegmentation(bookmarkID, data);
                    }
                    return DataMagicUtil.reportSegmentation(bookmarkID,data,token);
                }
            });
        }catch (Exception e){
            logger.debug(e);
            BaseResponse.sendMessage(response,BaseResponse.getMessage(response,e.getMessage()));
        }

    }

}
