package com.crecescu.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNotFoundException extends RuntimeException
    implements ExceptionMapper<AccountNotFoundException> {

  private static final long serialVersionUID = 1L;

  public AccountNotFoundException() {
  }

  public AccountNotFoundException(String msg) {
    super(msg);
  }

  @Override
  public Response toResponse(AccountNotFoundException exception) {
    return Response.status(404).entity(exception.getMessage())
        .type(MediaType.TEXT_PLAIN_TYPE).build();
  }

}
