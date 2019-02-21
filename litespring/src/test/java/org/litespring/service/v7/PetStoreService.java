package org.litespring.service.v7;


import org.litespring.stereotype.Component;
import org.litespring.transaction.annotation.Transactional;
import org.litespring.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Component(value="petStore")
public class PetStoreService {

	Connection conn;

	public PetStoreService() {		
		conn = JDBCUtils.getConnection();
	}
	@Transactional
	public void placeOrder() throws Exception {
		String sql = "UPDATE score SET age=age+1 WHERE id=2";
		PreparedStatement ps = conn.prepareStatement(sql);
		int users = ps.executeUpdate();
		System.out.println(users);
		throw new Exception("gg");
	}

}
