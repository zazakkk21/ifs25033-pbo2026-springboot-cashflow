package org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity;

import java.util.Comparator;

public enum SortOption{
  AMOUNT_ASC(Comparator.comparing(Transaction::getAmount)),
  AMOUNT_DESC(Comparator.comparing(Transaction::getAmount).reversed()),
  INCOME_FIRST(Comparator.comparingInt(Transaction -> Transaction.getType()== TransactionType.INCOME ? 0 : 1)),
  EXPENSE_FIRST(Comparator.comparingInt(Transaction -> Transaction.getType() == TransactionType.EXPENSE ? 0 : 1));

  private final Comparator<Transaction> comparator;
  SortOption(Comparator<Transaction> comparator){
    this.comparator = comparator;
  }

  public Comparator<Transaction> comparator(){
    return comparator;
  }
}