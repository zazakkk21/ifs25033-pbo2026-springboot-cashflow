package org.delcom.zaki_silalahipbo2026springbootcashflow.usecase;

import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.Transaction;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.SortOption;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.TransactionType;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.repository.ITransactionRepository;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.exception.ValidationException;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class FinanceUseCase {
  private final ITransactionRepository transactionRepository;
  public FinanceUseCase(ITransactionRepository transactionRepository){
    this.transactionRepository =transactionRepository;
  }

  public List<Transaction> getAllTransactions(){
    return transactionRepository.findAll();
  }

  public Transaction addTransaction(String description, double amount, TransactionType type) throws ValidationException{
    if(description == null || description.trim().isEmpty()){
      throw new ValidationException("Deskripsi tidak boleh kosong");
    }
    if(amount <= 0){
      throw new ValidationException("Jumlah harus lebih besar dari 0");
    }
    if(type == null){
      throw new ValidationException("Tipe tidak boleh null");
    }

    return transactionRepository.save(description, amount, type);
  }

  public void deleteTransaction(int id) throws EntityNotFoundException{
    boolean deleted = transactionRepository.deleteById(id);
    if(!deleted){
      throw new EntityNotFoundException(id);
    }

  }
  public void updateTransaction(int id, String description, Double amount, TransactionType type) throws ValidationException, EntityNotFoundException{
    Optional<Transaction> found = transactionRepository.findById(id);
    
    if(found.isEmpty()){
      throw new EntityNotFoundException(id);
    }

    Transaction transactions = found.get();
    if(description != null){
      if(description.trim().isEmpty()){
        throw new ValidationException("Deskripsi tidak boleh kosong");
      }
      transactions.changeDescription(description);
    }

    if(amount != null){
      if(amount <= 0){
        throw new ValidationException("Jumlah harus lebih besar 0");
      } else{
        transactions.changeAmount(amount);
      }
    }

    if(type != null){
      transactions.changeType(type);
    }

    transactionRepository.update(transactions);
  }

  public double getBalance(){
    double totalIncome = 0;
    double totalExpense = 0;

    List<Transaction> transactions = getAllTransactions();
    for(Transaction transaction : transactions){
      if(transaction.getType() == TransactionType.INCOME){
        totalIncome += transaction.getAmount();
      } else if(transaction.getType() == TransactionType.EXPENSE){
        totalExpense += transaction.getAmount();
      }
    }

    return totalIncome - totalExpense;
    
  }

  public List<Transaction> searchTransactions(String keyword){
    String lowerKey = keyword.toLowerCase();
    return transactionRepository.findBy(Transaction -> Transaction.getDescription().toLowerCase().contains(lowerKey));
  }

  public List<Transaction> sortTransactions(SortOption option){
    return transactionRepository.findAll().stream().sorted(option.comparator()).toList();
  }
}