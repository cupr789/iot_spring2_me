package com.iot.spring.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.spring.dao.SqlDAO;
import com.iot.spring.service.SqlService;

@Service
public class SqlServiceImpl implements SqlService{

	@Autowired
	SqlDAO sDao;

	@Override
	public List<Object> getSelectSQL(HttpSession hs, String sql) {
		
		return sDao.selectSQL(hs, sql);
	}

	@Override
	public int getInsertSQL(HttpSession hs, String sql) {
		// TODO Auto-generated method stub
		return sDao.insertSQL(hs, sql);
	}

	@Override
	public int getUpdateSQL(HttpSession hs, String sql) {
		// TODO Auto-generated method stub
		return sDao.updateSQL(hs, sql);
	}

	@Override
	public int getDeleteSQL(HttpSession hs, String sql) {
		// TODO Auto-generated method stub
		return sDao.deleteSQL(hs, sql);
	}
	

}
