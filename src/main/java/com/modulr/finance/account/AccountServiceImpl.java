package com.modulr.finance.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountServiceImpl implements AccountService {

    private final Map<Integer, BigDecimal> accountBalance = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl() {
        accountBalance.put(1001, BigDecimal.valueOf(2738.59));
        accountBalance.put(1002, BigDecimal.valueOf(23.00));
        accountBalance.put(1003, BigDecimal.valueOf(0.00));
    }

    public String checkBalance(int accountId) {
        if (!accountBalance.containsKey(accountId)) {
            throw new IllegalArgumentException("Could not find accountId: " + accountId);
        }
        return accountBalance.get(accountId).toPlainString();
    }

    /**
     *
     * @param accountId
     * @param amount
     * @return {@code boolean} whether the transaction was successful or not
     * @throws IllegalArgumentException when the amount is negative
     * @throws IllegalArgumentException when it could not find accountId
     *
     */
    public boolean withdrawAmount(int accountId, BigDecimal amount) {

        if (amount.signum() == -1) {
            LOG.warn("The amount is negative: {}", amount);
            throw new IllegalArgumentException("The amount cannot be negative");
        }

        if (!accountBalance.containsKey(accountId)) {
            LOG.error("Could not find accountId={}", accountId);
            throw new IllegalArgumentException("Could not find accountId: " + accountId);
        }

        BigDecimal updatedBalance = accountBalance.get(accountId).subtract(amount);
        if (updatedBalance.signum() == -1) {
            LOG.info("Insufficient funds, accountId={}, amount={}, funds={}", accountId, amount, accountBalance.get(accountId));
            return false;
        }
        accountBalance.put(accountId, updatedBalance);
        LOG.info("Handing out: amount={} to accountId={}", amount, accountId);
        return true;
    }
}
