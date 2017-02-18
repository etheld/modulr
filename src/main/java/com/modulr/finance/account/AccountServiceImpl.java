package com.modulr.finance.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountServiceImpl implements AccountService {

    private final Map<Integer, BigDecimal> accountBalance = new HashMap<>();

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

    public boolean withdrawAmount(int accountId, BigDecimal amount) {

        if (amount.signum() == -1) {
            throw new IllegalArgumentException("The amount cannot be negative");
        }

        if (!accountBalance.containsKey(accountId)) {
            throw new IllegalArgumentException("Cannot found the accountId: " + accountId);
        }

        BigDecimal updatedBalance = accountBalance.get(accountId).subtract(amount);
        if (updatedBalance.signum() == -1) {
            return false;
        }
        accountBalance.put(accountId, updatedBalance);
        return true;
    }
}
