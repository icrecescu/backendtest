package com.crecescu.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountException extends RuntimeException implements
    ExceptionMapper<AccountException> {

  private static final long serialVersionUID = 1L;

  public AccountException() {
  }

  public AccountException(String msg) {
    super(msg);
  }

  public AccountException(String msg, Throwable cause) {
    super(msg, cause);
  }

  @Override
  public Response toResponse(AccountException exception) {
    return Response.status(Status.BAD_REQUEST).entity(exception.getMessage())
        .type(MediaType.TEXT_PLAIN_TYPE).build();
  }

}
