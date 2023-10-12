/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package com.pruebachain.movimiento.util;

/**
 * @author cberm3o
 */
public enum ErrorMessages {
  BALANCE_NOT_AVAILABLE("Saldo no disponible"),
  DAILY_QUOTA_EXCEEDED("Cupo diario excedido"),
  CREATED_OK("Creado correctamente");

  private String errorMessage;

  ErrorMessages(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }
  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
