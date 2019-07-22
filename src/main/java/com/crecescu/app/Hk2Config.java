package com.crecescu.app;

import com.crecescu.dao.AccountDao;
import com.crecescu.dao.TransactionDao;
import com.crecescu.dao.impl.AccountDaoImpl;
import com.crecescu.dao.impl.TransactionDaoImpl;
import com.crecescu.exception.AccountException;
import com.crecescu.exception.AccountNotFoundException;
import com.crecescu.service.AccountService;
import com.crecescu.service.TransactionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class Hk2Config extends ResourceConfig {

  public Hk2Config() {
    register(TransactionService.class);
    register(AccountService.class);
    register(AccountNotFoundException.class);
    register(AccountException.class);

    register(new AbstractBinder() {

      @Override
      protected void configure() {
        bind(AccountDaoImpl.class).to(AccountDao.class);
        bind(TransactionDaoImpl.class).to(TransactionDao.class);
      }

    });
  }

}
