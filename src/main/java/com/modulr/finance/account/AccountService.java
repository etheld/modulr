package com.modulr.finance.account;

import java.math.BigDecimal;

public interface AccountService {

    String checkBalance(int accountId);

    boolean withdrawAmount(int accountId, BigDecimal amount);

}
