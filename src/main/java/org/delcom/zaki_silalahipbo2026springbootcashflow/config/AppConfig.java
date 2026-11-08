
package org.delcom.zaki_silalahipbo2026springbootcashflow.config;

import org.delcom.zaki_silalahipbo2026springbootcashflow.adapter.presenter.FinancePresenter;
import org.delcom.zaki_silalahipbo2026springbootcashflow.adapter.repository.TransactionRepository;
import org.delcom.zaki_silalahipbo2026springbootcashflow.domain.repository.ITransactionRepository;
import org.delcom.zaki_silalahipbo2026springbootcashflow.usecase.FinanceUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root Spring:
 * merakit dependency antar-layer.
 */
@Configuration
public class AppConfig {

    @Bean
    public ITransactionRepository transactionRepository() {
        return new TransactionRepository();
    }

    @Bean
    public FinanceUseCase financeUseCase(
            ITransactionRepository transactionRepository
    ) {
        return new FinanceUseCase(transactionRepository);
    }

    @Bean
    public FinancePresenter financePresenter(
            FinanceUseCase financeUseCase
    ) {
        return new FinancePresenter(financeUseCase);
    }
}
