package com.modulr.finance.ATM;

import com.modulr.finance.account.AccountService;

import java.math.BigDecimal;
import java.util.*;

public class ATMServiceImpl {

    private final AccountService accountService;

    private final Map<Integer, Integer> bankNotes;

    private static final Integer MIN_AMOUNT = 20;
    private static final Integer MAX_AMOUNT = 250;

    public ATMServiceImpl(AccountService accountService, Map<Integer, Integer> bankNotes) {
        this.accountService = accountService;
        this.bankNotes = bankNotes;
    }

    void replenish() {
        bankNotes.put(5, 20);
        bankNotes.put(10, 20);
        bankNotes.put(20, 20);
        bankNotes.put(50, 20);
    }

    public String checkBalance(int accountId) {
        return accountService.checkBalance(accountId);
    }

    List<Integer> withdrawAmount(int accountId, Integer amount) {
        checkAmountBounds(amount);
        List<Integer> bankNotes = getBankNotes(amount);
        accountService.withdrawAmount(accountId, BigDecimal.valueOf(amount));
        return bankNotes;
    }

    // visible for testing
    Map<Integer, Integer> getBankNotes() {
        return bankNotes;
    }

    private List<Integer> getBankNotes(Integer amount) {
        List<Integer> solutionWith5 = tryWith5(amount, new HashMap<>(bankNotes));
        if (!solutionWith5.isEmpty()) {
            applySolution(solutionWith5);
            return solutionWith5;
        }

        List<Integer> solutionWithout5 = bankNotesRecursion(amount, new ArrayList<>(), new HashMap<>(bankNotes), 0);
        if (!solutionWithout5.isEmpty()) {
            applySolution(solutionWithout5);
            return solutionWithout5;
        } else {
            throw new IllegalStateException("Not enough bank notes");
        }
    }

    private void applySolution(List<Integer> notes) {
        for (int note : notes) {
            bankNotes.put(note, bankNotes.get(note) - 1);
        }
    }

    private List<Integer> tryWith5(Integer amount, Map<Integer, Integer> bankNotesCopy) {
        List<Integer> notes = new ArrayList<>();
        deductNote(bankNotesCopy, notes, 5);
        return bankNotesRecursion(amount - 5, notes, bankNotesCopy, 0);
    }

    private List<Integer> bankNotesRecursion(Integer amount, List<Integer> notes, Map<Integer, Integer> bankNotesCopy, int iteration) {
        for (int note : getIncrementallySortedNotes()) {
            if (amount >= note && bankNotesCopy.get(note) > 0) {
                deductNote(bankNotesCopy, notes, note);
                return bankNotesRecursion(amount - note, notes, bankNotesCopy, iteration + 1);
            }
        }
        return amount == 0 ? notes : new ArrayList<>();
    }

    private void deductNote(Map<Integer, Integer> bankNotesCopy, List<Integer> notes, int note) {
        bankNotesCopy.put(note, bankNotesCopy.get(note) - 1);
        notes.add(note);
    }

    private List<Integer> getIncrementallySortedNotes() {
        List<Integer> keys = new ArrayList<>(bankNotes.keySet());
        keys.sort(Comparator.reverseOrder());
        return keys;
    }

    private void checkAmountBounds(Integer amount) {
        if (amount < MIN_AMOUNT) {
            throw new IllegalArgumentException("Cannot withdraw less than 20");
        }
        if (amount > MAX_AMOUNT) {
            throw new IllegalArgumentException("Cannot withdraw more than 250");
        }
    }

}
