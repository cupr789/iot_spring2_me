package com.iot.spring.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public interface SqlService {
	List<Object> getSelectSQL(HttpSession hs, String sql);
	int getInsertSQL(HttpSession hs, String sql);
	int getUpdateSQL(HttpSession hs, String sql);
	int getDeleteSQL(HttpSession hs, String sql);
}
