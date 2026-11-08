
package org.delcom.zaki_silalahipbo2026springbootcashflow.adapter.presenter;

import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.Transaction;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.TransactionType;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.SortOption;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.exception.ValidationException;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.exception.EntityNotFoundException;
import org.delcom.zaki_silalahipbo2026springbootcashflow.usecase.FinanceUseCase;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FinancePresenter {

    private final FinanceUseCase financeUseCase;

    public FinancePresenter(FinanceUseCase financeUseCase) {
        this.financeUseCase = financeUseCase;
    }

    // ==========================================
    // FORMAT DATA
    // ==========================================

    /**
     * Memformat nominal menjadi Rupiah.
     * Contoh: Rp 1,000,000.00
     */
    public String formatAmount(double amount) {
        return String.format(Locale.US, "Rp %,.2f", amount);
    }

    /**
     * Memformat satu transaksi menjadi teks.
     */
    public String format(Transaction transaction) {
        String type = transaction.getType() == TransactionType.INCOME
                ? "Pemasukan"
                : "Pengeluaran";

        return String.format(
                "[%d] %s | %s | %s",
                transaction.getId(),
                transaction.getDescription(),
                type,
                formatAmount(transaction.getAmount())
        );
    }

    // ==========================================
    // DATA TRANSAKSI
    // ==========================================

    /**
     * Mengambil seluruh transaksi.
     */
    public List<Transaction> getAllTransactions() {
        return financeUseCase.getAllTransactions();
    }

    /**
     * Menambahkan transaksi.
     */
    public Transaction addTransaction(
            String description,
            double amount,
            TransactionType type
    ) throws ValidationException {
        return financeUseCase.addTransaction(
                description,
                amount,
                type
        );
    }

    /**
     * Menghapus transaksi berdasarkan ID.
     */
    public void deleteTransaction(int id)
            throws EntityNotFoundException {
        financeUseCase.deleteTransaction(id);
    }

    /**
     * Memperbarui transaksi.
     */
    public void updateTransaction(
            int id,
            String description,
            Double amount,
            TransactionType type
    ) throws ValidationException, EntityNotFoundException {
        financeUseCase.updateTransaction(
                id,
                description,
                amount,
                type
        );
    }

    // ==========================================
    // RINGKASAN KEUANGAN
    // ==========================================

    /**
     * Menghitung total seluruh transaksi.
     */
    public int getTotalTransactions() {
        return getAllTransactions().size();
    }

    /**
     * Menghitung total pemasukan.
     */
    public double getTotalIncome() {
        double total = 0;

        for (Transaction transaction : getAllTransactions()) {
            if (transaction.getType() == TransactionType.INCOME) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    /**
     * Menghitung total pengeluaran.
     */
    public double getTotalExpense() {
        double total = 0;

        for (Transaction transaction : getAllTransactions()) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    /**
     * Mengambil saldo dari UseCase.
     */
    public double getBalance() {
        return financeUseCase.getBalance();
    }

    // ==========================================
    // PENCARIAN DAN PENGURUTAN
    // ==========================================

    /**
     * Mencari transaksi berdasarkan deskripsi,
     * kemudian mengurutkannya jika opsi tersedia.
     */
    public List<Transaction> getVisibleTransactions(
            String keyword,
            SortOption option
    ) {
        List<Transaction> transactions;

        if (keyword == null || keyword.isBlank()) {
            transactions = getAllTransactions();
        } else {
            transactions = financeUseCase.searchTransactions(keyword);
        }

        if (option != null) {
            Comparator<Transaction> comparator = option.comparator();

            return transactions.stream()
                    .sorted(comparator)
                    .toList();
        }

        return transactions;
    }

    /**
     * Mengambil hasil pencarian tanpa pengurutan tambahan.
     */
    public List<Transaction> searchTransactions(String keyword) {
        return financeUseCase.searchTransactions(keyword);
    }

    /**
     * Mengurutkan seluruh transaksi.
     */
    public List<Transaction> sortTransactions(SortOption option) {
        return financeUseCase.sortTransactions(option);
    }

    // ==========================================
    // PESAN UNTUK VIEW
    // ==========================================

    public String showAddSuccess(Transaction transaction) {
        return "Transaksi berhasil ditambahkan: "
                + transaction.getDescription();
    }

    public String showDeleteSuccess() {
        return "Transaksi berhasil dihapus.";
    }

    public String showUpdateSuccess() {
        return "Transaksi berhasil diperbarui.";
    }

    public String showValidationError(String message) {
        return "Gagal: " + message;
    }

    public String showNotFoundError(int id) {
        return "Transaksi dengan ID " + id + " tidak ditemukan.";
    }
}
