package com.iot.spring.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iot.spring.dao.ConnectionInfoDAO;
import com.iot.spring.vo.ColumnVO;
import com.iot.spring.vo.ConnectionInfoVO;
import com.iot.spring.vo.SqlVO;
import com.iot.spring.vo.TableVO;

@Repository
public class ConnectionInfoDAOImpl implements ConnectionInfoDAO {

	@Autowired
	private SqlSessionFactory ssf;
	
	@Override
	public List<ConnectionInfoVO> selectConnectionInfoList(String uiId) {
		SqlSession ss = ssf.openSession();
		List<ConnectionInfoVO> ciList = ss.selectList("connection_info.selectConnectionInfo", uiId);
		ss.close();
		return ciList;
	}
	@Override
	public ConnectionInfoVO selectConnectionInfo(int ciNo) {
		SqlSession ss = ssf.openSession();
		ConnectionInfoVO ci = ss.selectOne("connection_info.selectConnectionInfoWithCiNo", ciNo);
		ss.close();
		return ci; 
	}

	@Override
	public List<ConnectionInfoVO> selectConnectionInfoList(ConnectionInfoVO ci) {
		List<ConnectionInfoVO> result = null;
		final SqlSession ss = ssf.openSession();
		result = ss.selectList("connection_info.selectConnectionInfo",ci);
		ss.close();
		return result;
	}

	@Override
	public int insertConnectionInfo(ConnectionInfoVO ci) {
		int result = 0;
		final SqlSession ss = ssf.openSession();
		result = ss.insert("connection_info.insertConnectionInfo", ci);
		ss.close();
		return result;
	}

	@Override
	public List<Map<String, Object>> selectDatabaseList(SqlSession ss) throws Exception {
		return ss.selectList("connection_info.selectDatabase");
	}
	@Override
	public List<TableVO> selectTableList(SqlSession ss,String dbName) {
		List<TableVO> result = null;
		result = ss.selectList("connection_info.selectTable",dbName);
		return result;
	}

	@Override
	public List<ColumnVO> selectColumnList(SqlSession ss,Map<String,String> map) {
		return ss.selectList("connection_info.selectColumn",map);
	}
	
	
	@Override
	public List<Object> excuteAnySql(SqlSession ss, String sql) {
		System.out.println("질의문을 시작하기전!!!!!! 받아온 sql 제대로 왓니?? "+sql);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		List<Object> list = ss.selectList("connection_info.excuteAnySql", map);
		int result=0;
		if(sql.indexOf("update")!=-1) {
			System.out.println("들어왓어!?!?!?!?!?!?!?");
			result = ss.update("connection_info.excuteUpdate",map);		
			System.out.println("update 된 갯수: "+result);
		}
		
		System.out.println("??????   "+list.size());
		return list;
	}
	
	
	@Override
	public List<Object> selectTableDatas(SqlSession ss, Map<String, String> map) {
		System.out.println(map.get("tableName"));
		List<Object> list = ss.selectList("connection_info.selectTableDatas", map);
		System.out.println(list.get(0));
		return list;
	}

}
