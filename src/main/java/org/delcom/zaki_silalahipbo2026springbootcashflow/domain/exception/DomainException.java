package org.delcom.zaki_silalahipbo2026springbootcashflow.domain.exception;

public class DomainException extends Exception{
  public DomainException(String message){
    super(message);
  }

  public DomainException(String message, Throwable cause){
    super(message, cause);
  }
}