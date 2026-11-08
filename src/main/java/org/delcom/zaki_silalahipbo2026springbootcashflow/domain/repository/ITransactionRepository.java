package org.delcom.zaki_silalahipbo2026springbootcashflow.domain.repository;

import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.Transaction;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.TransactionType;

public interface ITransactionRepository extends IRepository<Transaction, Integer>{
  Transaction save(String description, double amount, TransactionType type);
}