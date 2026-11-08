package org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity;

public class Transaction{
  private final int id;
  private String description;
  private double amount;
  private TransactionType type;

  public Transaction(int id, String description, double amount, TransactionType type){
    if(description.trim().isEmpty()){
      throw new IllegalArgumentException("deskripsi tidak boleh kosong");
    }

    if(amount <= 0 || description.trim().isEmpty()){
      throw new IllegalArgumentException("Nominal harus lebih dari 0");
    }

    if(type == null){
      throw new IllegalArgumentException("Jenis transaksi tidka boleh kosong");
    }
    this.id = id;
    this.description = description;
    this.amount = amount;
    this.type =type;
  }

  public int getId(){
    return id;
  }

  public String getDescription(){
    return description;
  }

  public double getAmount(){
    return amount;
  }

  public TransactionType getType(){
    return type;
  }

  public void changeDescription(String description){
    this.description = description;
  }

  public void changeAmount(double amount){
    this.amount = amount;
  }

  public void changeType(TransactionType type){
    this.type = type;
  }
}