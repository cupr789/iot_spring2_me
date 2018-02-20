package com.iot.spring.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

public interface SqlDAO {
	List<Object> selectSQL(HttpSession hs, String sql);
	int insertSQL(HttpSession hs, String sql);
	int updateSQL(HttpSession hs, String sql);
	int deleteSQL(HttpSession hs, String sql);
}
