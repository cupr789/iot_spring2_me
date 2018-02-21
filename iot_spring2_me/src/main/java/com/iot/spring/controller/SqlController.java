package com.iot.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.tools.StandardLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iot.spring.service.SqlService;

@Controller
@RequestMapping("/sql")
public class SqlController {
	
	
	private static final Logger log = LoggerFactory.getLogger(UrlController.class);
	
	@Autowired
	SqlService sqlService;
	
	@RequestMapping(value="/sendSql", method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> getSqlResult(HttpSession hs, @RequestBody String sendSQL, Map<String,Object> map) {
		long startTime = System.currentTimeMillis();
		
			log.info("받아온 질의문이 뭐야?? =>{}",sendSQL);
		
			String[] sqlArr = sendSQL.trim().split(";");
			System.out.println("첫번째 자른것 :"+sqlArr[0]);
			List<Object> excuteResult = new ArrayList<Object>();
	/*		Map<String, Object> inMap = new HashMap<String, Object>(); */
			List<Object> effectCountList = new ArrayList<Object>();
			List<Object> selectCountList = new ArrayList<Object>();
			for(int x=0; x<sqlArr.length;x++) {
				
				if(sqlArr[x].indexOf("select")!=-1) {
					selectCountList.add(sqlService.getSelectSQL(hs, sqlArr[x]).size());
					excuteResult.add(sqlService.getSelectSQL(hs, sqlArr[x]));
					effectCountList.add(0);
				}else if(sqlArr[x].indexOf("update")!=-1) {
					
					effectCountList.add(sqlService.getUpdateSQL(hs, sqlArr[x]));
					selectCountList.add(0);
				}else if(sqlArr[x].indexOf("delete")!=-1) {
					
					effectCountList.add(sqlService.getDeleteSQL(hs, sqlArr[x]));
					selectCountList.add(0);
				}else if(sqlArr[x].indexOf("insert")!=-1) {
					
					effectCountList.add(sqlService.getInsertSQL(hs, sqlArr[x]));
					selectCountList.add(0);
				}
				
				
			}
			
			
			
			map.put("strSql", sqlArr);
			map.put("list", excuteResult);
			map.put("effectCnt", effectCountList);
			map.put("listCnt", selectCountList);
			long setTime =System.currentTimeMillis()-startTime;
			map.put("excuteTime", setTime);
		
		return map;
	}
}
