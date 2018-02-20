package com.iot.spring.dao.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.iot.spring.dao.SqlDAO;

@Repository
public class SqlDAOImpl implements SqlDAO {

	@Override
	public List<Object> selectSQL(HttpSession hs, String sql) {
		SqlSession ss = (SqlSession)hs.getAttribute("sqlSession");
		List<Object> list = ss.selectList("sql_info.selectSql", sql);
		return list;
	}

	@Override
	public int insertSQL(HttpSession hs, String sql) {
		SqlSession ss = (SqlSession)hs.getAttribute("sqlSession");
		int result = ss.insert("sql_info.insertSql", sql);
		return result;
	}

	@Override
	public int updateSQL(HttpSession hs, String sql) {
		SqlSession ss = (SqlSession)hs.getAttribute("sqlSession");
		int result = ss.update("sql_info.updateSql", sql);
		return result;
	}

	@Override
	public int deleteSQL(HttpSession hs, String sql) {
		SqlSession ss = (SqlSession)hs.getAttribute("sqlSession");
		int result = ss.delete("sql_info.deleteSql", sql);
		return result;
	}


}
