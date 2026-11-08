
package org.delcom.zaki_silalahipbo2026springbootcashflow.framework.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.delcom.zaki_silalahipbo2026springbootcashflow.adapter.presenter.FinancePresenter;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.SortOption;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.Transaction;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.entity.TransactionType;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.exception.EntityNotFoundException;

@Route("")
@PageTitle("Cashflow")
public class FinanceView extends VerticalLayout {

    private final FinancePresenter financePresenter;

    // ==========================================
    // KOMPONEN RINGKASAN
    // ==========================================

    private final Span totalTransactionsLabel = new Span("0");
    private final Span totalIncomeLabel = new Span("Rp 0.00");
    private final Span totalExpenseLabel = new Span("Rp 0.00");
    private final Span balanceLabel = new Span("Rp 0.00");

    // ==========================================
    // KOMPONEN TOOLBAR
    // ==========================================

    private final TextField searchField =
            new TextField();

    private final ComboBox<SortOption> sortField =
            new ComboBox<>();

    // ==========================================
    // KOMPONEN GRID
    // ==========================================

    private final Grid<Transaction> transactionGrid =
            new Grid<>(Transaction.class, false);

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public FinanceView(FinancePresenter financePresenter) {
        this.financePresenter = financePresenter;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        createHeader();
        createSummaryCards();
        createToolbar();
        createTransactionGrid();

        refreshData();
    }

    // ==========================================
    // HEADER
    // ==========================================

    private void createHeader() {
        H2 title = new H2("Personal Cashflow");

        Span subtitle = new Span(
                "Kelola pemasukan dan pengeluaran keuanganmu."
        );

        VerticalLayout header = new VerticalLayout(
                title,
                subtitle
        );

        header.setPadding(false);
        header.setSpacing(false);

        add(header);
    }

    // ==========================================
    // SUMMARY CARDS
    // ==========================================

    private void createSummaryCards() {

        Div totalTransactionsCard = createSummaryCard(
                "Total Transaksi",
                totalTransactionsLabel
        );

        Div totalIncomeCard = createSummaryCard(
                "Total Pemasukan",
                totalIncomeLabel
        );

        Div totalExpenseCard = createSummaryCard(
                "Total Pengeluaran",
                totalExpenseLabel
        );

        Div balanceCard = createSummaryCard(
                "Saldo Saat Ini",
                balanceLabel
        );

        HorizontalLayout summaryLayout = new HorizontalLayout(
                totalTransactionsCard,
                totalIncomeCard,
                totalExpenseCard,
                balanceCard
        );

        summaryLayout.setWidthFull();
        summaryLayout.setSpacing(true);

        summaryLayout.setFlexGrow(1,
                totalTransactionsCard,
                totalIncomeCard,
                totalExpenseCard,
                balanceCard
        );

        add(summaryLayout);
    }

    /**
     * Membuat satu kartu ringkasan.
     */
    private Div createSummaryCard(String title, Span value) {

        H3 cardTitle = new H3(title);

        value.getStyle()
                .set("font-size", "22px")
                .set("font-weight", "bold");

        Div card = new Div(
                cardTitle,
                value
        );

        card.getStyle()
                .set("border", "1px solid #dddddd")
                .set("border-radius", "10px")
                .set("padding", "16px")
                .set("box-sizing", "border-box")
                .set("min-width", "0");

        return card;
    }

    // ==========================================
    // TOOLBAR
    // ==========================================

    private void createToolbar() {

        searchField.setPlaceholder("Cari keterangan transaksi...");
        searchField.setClearButtonVisible(true);
        searchField.setWidth("300px");

        searchField.addValueChangeListener(event -> refreshGrid());

        sortField.setItems(SortOption.values());

        sortField.setItemLabelGenerator(option -> switch (option) {
            case AMOUNT_ASC -> "Nominal Terendah";
            case AMOUNT_DESC -> "Nominal Tertinggi";
            case INCOME_FIRST -> "Pemasukan Terlebih Dahulu";
            case EXPENSE_FIRST -> "Pengeluaran Terlebih Dahulu";
        });

        sortField.setPlaceholder("Urutkan transaksi");
        sortField.setWidth("230px");

        sortField.addValueChangeListener(event -> refreshGrid());

        Button reloadButton = new Button(
                "Muat Ulang",
                event -> refreshData()
        );

        HorizontalLayout searchToolbar = new HorizontalLayout(
                searchField,
                sortField,
                reloadButton
        );

        searchToolbar.setAlignItems(Alignment.END);
        searchToolbar.setWidthFull();

        Button addIncomeButton = new Button(
                "+ Tambah Pemasukan",
                event -> openAddDialog(TransactionType.INCOME)
        );

        Button addExpenseButton = new Button(
                "+ Tambah Pengeluaran",
                event -> openAddDialog(TransactionType.EXPENSE)
        );

        HorizontalLayout actionToolbar = new HorizontalLayout(
                addIncomeButton,
                addExpenseButton
        );

        actionToolbar.setWidthFull();

        add(
                new H3("Daftar Transaksi"),
                searchToolbar,
                actionToolbar
        );
    }

    // ==========================================
    // TRANSACTION GRID
    // ==========================================

    private void createTransactionGrid() {

        transactionGrid.addColumn(Transaction::getId)
                .setHeader("ID")
                .setAutoWidth(true);

        transactionGrid.addColumn(Transaction::getDescription)
                .setHeader("Keterangan")
                .setFlexGrow(1);

        transactionGrid.addColumn(transaction ->
                financePresenter.formatAmount(transaction.getAmount())
        ).setHeader("Jumlah")
                .setAutoWidth(true);

        transactionGrid.addColumn(transaction ->
                transaction.getType() == TransactionType.INCOME
                        ? "Pemasukan"
                        : "Pengeluaran"
        ).setHeader("Tipe")
                .setAutoWidth(true);

        transactionGrid.addComponentColumn(transaction -> {

            Button deleteButton = new Button(
                    "Hapus",
                    event -> confirmDelete(transaction)
            );

            return deleteButton;

        }).setHeader("Tindakan")
                .setAutoWidth(true);

        transactionGrid.setWidthFull();
        transactionGrid.setHeight("400px");

        add(transactionGrid);
    }

    // ==========================================
    // ADD TRANSACTION DIALOG
    // ==========================================

    private void openAddDialog(TransactionType type) {

        String typeLabel = type == TransactionType.INCOME
                ? "Pemasukan"
                : "Pengeluaran";

        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Tambah " + typeLabel);
        dialog.setWidth("400px");

        TextField descriptionField = new TextField(
                "Keterangan"
        );

        descriptionField.setPlaceholder(
                "Contoh: Gaji bulanan"
        );

        descriptionField.setWidthFull();
        descriptionField.setRequiredIndicatorVisible(true);

        NumberField amountField = new NumberField(
                "Jumlah"
        );

        amountField.setPlaceholder(
                "Masukkan nominal"
        );

        amountField.setMin(0.01);
        amountField.setStep(1000);
        amountField.setWidthFull();
        amountField.setRequiredIndicatorVisible(true);

        VerticalLayout formLayout = new VerticalLayout(
                descriptionField,
                amountField
        );

        formLayout.setPadding(false);
        formLayout.setSpacing(true);

        Button cancelButton = new Button(
                "Batal",
                event -> dialog.close()
        );

        Button saveButton = new Button(
                "Simpan",
                event -> {

                    String description = descriptionField.getValue();
                    Double amount = amountField.getValue();

                    if (amount == null) {
                        Notification.show(
                                "Jumlah transaksi harus diisi."
                        );
                        return;
                    }

                    try {
                        Transaction transaction =
                                financePresenter.addTransaction(
                                        description,
                                        amount,
                                        type
                                );

                        Notification.show(
                                financePresenter.showAddSuccess(transaction)
                        );

                        dialog.close();
                        refreshData();

                    } catch (Exception e) {
                        Notification.show(
                                financePresenter.showValidationError(
                                        e.getMessage()
                                )
                        );
                    }
                }
        );

        HorizontalLayout buttons = new HorizontalLayout(
                cancelButton,
                saveButton
        );

        buttons.setWidthFull();
        buttons.setJustifyContentMode(
                JustifyContentMode.END
        );

        dialog.add(
                formLayout,
                buttons
        );

        dialog.open();
    }

    // ==========================================
    // DELETE CONFIRMATION
    // ==========================================

    private void confirmDelete(Transaction transaction) {

        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setHeader("Konfirmasi Hapus");

        dialog.setText(
                "Apakah kamu yakin ingin menghapus transaksi \""
                        + transaction.getDescription()
                        + "\"?"
        );

        dialog.setCancelable(true);
        dialog.setCancelText("Batal");
        dialog.setConfirmText("Hapus");

        dialog.addConfirmListener(event ->
                deleteTransaction(transaction.getId())
        );

        dialog.open();
    }

    /**
     * Menghapus transaksi berdasarkan ID.
     */
    private void deleteTransaction(int id) {

        try {
            financePresenter.deleteTransaction(id);

            Notification.show(
                    financePresenter.showDeleteSuccess()
            );

            refreshData();

        } catch (EntityNotFoundException e) {

            Notification.show(
                    financePresenter.showNotFoundError(id)
            );
        }
    }

    // ==========================================
    // REFRESH DATA
    // ==========================================

    /**
     * Memperbarui kartu ringkasan dan Grid.
     */
    private void refreshData() {

        totalTransactionsLabel.setText(
                String.valueOf(
                        financePresenter.getTotalTransactions()
                )
        );

        totalIncomeLabel.setText(
                financePresenter.formatAmount(
                        financePresenter.getTotalIncome()
                )
        );

        totalExpenseLabel.setText(
                financePresenter.formatAmount(
                        financePresenter.getTotalExpense()
                )
        );

        balanceLabel.setText(
                financePresenter.formatAmount(
                        financePresenter.getBalance()
                )
        );

        refreshGrid();
    }

    /**
     * Memperbarui isi Grid berdasarkan pencarian
     * dan pengurutan yang sedang dipilih.
     */
    private void refreshGrid() {

        String keyword = searchField.getValue();
        SortOption option = sortField.getValue();

        transactionGrid.setItems(
                financePresenter.getVisibleTransactions(
                        keyword,
                        option
                )
        );
    }
}
