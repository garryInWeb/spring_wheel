package org.litespring.transaction.support;

import org.litespring.aop.config.AspectInstanceFactory;
import org.litespring.transaction.handler.TransactionHandler;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public class TransactionInstanceFactory extends AspectInstanceFactory{

    TransactionHandler transactionHandler;

    public TransactionInstanceFactory(TransactionHandler transactionHandler) {
        this.transactionHandler = transactionHandler;
    }

    public Object getAspectInstance(){
        return transactionHandler;
    }

}
