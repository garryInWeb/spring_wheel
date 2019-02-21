package org.litespring.service.v7;


import org.litespring.service.v6.IPetStoreService;
import org.litespring.stereotype.Component;
import org.litespring.transaction.annotation.Transactional;

@Component(value="petStore")
public class PetStoreService implements IPetStoreService {
	
	public PetStoreService() {		
		
	}
	@Transactional
	public void placeOrder(){
		System.out.println("place order");
	}
	
	
	
}
