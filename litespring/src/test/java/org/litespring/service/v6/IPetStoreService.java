package org.litespring.service.v6;

import org.springframework.jdbc.core.JdbcTemplate;

public interface IPetStoreService {
	public void placeOrder() throws Exception;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate);
}
