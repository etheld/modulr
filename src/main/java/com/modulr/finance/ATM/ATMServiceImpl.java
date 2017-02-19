package com.modulr.finance.ATM;

import com.modulr.finance.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

public class ATMServiceImpl {

    private final AccountService accountService;

    private final Map<Integer, Integer> bankNotes = new HashMap<>();

    private static final Integer MIN_AMOUNT = 20;
    private static final Integer MAX_AMOUNT = 250;

    private static final Logger LOG = LoggerFactory.getLogger(ATMServiceImpl.class);

    public ATMServiceImpl(AccountService accountService) {
        this.accountService = accountService;
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

    /**
     * @param accountId
     * @param amount
     * @return @{code List}of {@code Integer}(bank notes)
     * @throws IllegalArgumentException when the amount is less than 20
     * @throws IllegalArgumentException when the amount is more than 250
     * @throws IllegalArgumentException when the amount is not dividable by 5
     */
    public List<Integer> withdrawAmount(int accountId, Integer amount) {
        checkAmountBounds(amount);
        List<Integer> bankNotes = getBankNotes(amount);
        accountService.withdrawAmount(accountId, BigDecimal.valueOf(amount));
        LOG.info("Handing out: amount={} accountId={}", amount, accountId);
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

        LOG.warn("Could not handout money with 5 bank note in it");
        List<Integer> solutionWithout5 = bankNotesRecursion(amount, new ArrayList<>(), new HashMap<>(bankNotes));
        if (!solutionWithout5.isEmpty()) {
            applySolution(solutionWithout5);
            return solutionWithout5;
        } else {
            LOG.error("Could not hand out amount={}, because there are not enough bank notes", amount);
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
        return bankNotesRecursion(amount - 5, notes, bankNotesCopy);
    }

    private List<Integer> bankNotesRecursion(Integer amount, List<Integer> notes, Map<Integer, Integer> bankNotesCopy) {
        for (int note : descSort(bankNotes.keySet())) {
            if (amount >= note && bankNotesCopy.get(note) > 0) {
                deductNote(bankNotesCopy, notes, note);
                return bankNotesRecursion(amount - note, notes, bankNotesCopy);
            }
        }
        return amount == 0 ? notes : new ArrayList<>();
    }

    private void deductNote(Map<Integer, Integer> bankNotesCopy, List<Integer> notes, int note) {
        bankNotesCopy.put(note, bankNotesCopy.get(note) - 1);
        notes.add(note);
    }

    private List<Integer> descSort(Set<Integer> keysSet) {
        List<Integer> keys = new ArrayList<>(keysSet);
        keys.sort(Comparator.reverseOrder());
        return keys;
    }

    private void checkAmountBounds(Integer amount) {
        if (amount < MIN_AMOUNT) {
            LOG.warn("User tried to withdraw less than 20");
            throw new IllegalArgumentException("Cannot withdraw less than 20");
        }
        if (amount > MAX_AMOUNT) {
            LOG.warn("User tried to withdraw more than 250");
            throw new IllegalArgumentException("Cannot withdraw more than 250");
        }
        if (amount % 5 != 0) {
            LOG.warn("User tried to get amount which was not dividable by 5");
            throw new IllegalArgumentException("Sorry, the ATM cannot hand out " + amount + " as it is not dividable by 5");
        }
    }

}
