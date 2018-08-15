package org.litespring.service.v3;

import org.litespring.dao.v3.AccountDao;
import org.litespring.dao.v3.ItemDao;

/**
 * Created by zhengtengfei on 2018/6/30.
 */
public class PetStoreService {
    private AccountDao accountDao;
    private ItemDao itemDao;
    private String owner;
    private int version;

    public PetStoreService(AccountDao accountDao, ItemDao itemDao, String owner, int version) {
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.owner = owner;
        this.version = version;
    }

    public PetStoreService(AccountDao accountDao, ItemDao itemDao, String owner) {
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.owner = owner;
        this.version = -1;
    }

    public PetStoreService(AccountDao accountDao, ItemDao itemDao, int version) {
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.owner = "";
        this.version = version;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public String getOwner() {
        return owner;
    }

    public int getVersion() {
        return version;
    }

}
