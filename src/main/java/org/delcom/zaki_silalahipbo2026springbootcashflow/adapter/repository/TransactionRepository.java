package org.delcom.zaki_silalahipbo2026springbootcashflow.adapter.repository;

import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.Transaction;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.TransactionType;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.repository.ITransactionRepository;

public class TransactionRepository extends InMemoryRepository<Transaction, Integer> implements ITransactionRepository{
  private int idCounter = 0;
  
  public TransactionRepository(){
    super(Transaction::getId);
  }

  @Override 
  public Transaction save(String description, double amount, TransactionType type){
    Transaction transaction = new Transaction(nextId(), description, amount, type);
    return save(transaction);
  }

  private int nextId(){
    return ++idCounter;
  }
}