package com.rpdevelopment.user_service_api.exception.exceptions;

public class DuplicateResourceException extends RuntimeException {
  public DuplicateResourceException(String message) {
    super(message);
  }
}
