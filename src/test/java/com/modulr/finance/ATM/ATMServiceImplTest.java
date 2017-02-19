package com.modulr.finance.ATM;

import com.modulr.finance.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;


class ATMServiceImplTest {

    private ATMServiceImpl atmService;

    private AccountService accountServiceMock;

    @BeforeEach
    void setup() {
        accountServiceMock = Mockito.mock(AccountService.class);
        atmService = new ATMServiceImpl(accountServiceMock);
        atmService.replenish();
    }

    @Test
    void replenish() {
        // given
        atmService.getBankNotes().put(20, 0);
        atmService.getBankNotes().put(10, 0);
        atmService.getBankNotes().put(5, 0);

        // when
        Throwable throwable = assertThrows(IllegalStateException.class,
                () -> atmService.withdrawAmount(1001, 20));

        // then
        assertThat(throwable.getMessage()).isEqualTo("Not enough bank notes");
    }

    @Test
    @DisplayName("checkBalance invokes accountService's checkBalance")
    void checkBalance() {
        // given
        int accountId = 1001;

        // when
        atmService.checkBalance(accountId);

        // then
        Mockito.verify(accountServiceMock, times(1)).checkBalance(accountId);

    }

    @Test
    @DisplayName("withdrawAmount throws an exception if the amount is less than 20")
    void withDrawAmount_withLessThan20() {
        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class,
                () -> atmService.withdrawAmount(1001, 1));

        // then
        assertThat(throwable.getMessage()).isEqualTo("Cannot withdraw less than 20");
    }

    @Test
    @DisplayName("withdrawAmount throws an exception if the amount is more than 250")
    void withDrawAmount_withMoreThan250() {

        //when
        Throwable throwable = assertThrows(IllegalArgumentException.class,
                () -> atmService.withdrawAmount(1001, 251));

        //then
        assertThat(throwable.getMessage()).isEqualTo("Cannot withdraw more than 250");
    }

    @Test
    @DisplayName("withDrawAmount throws exception if accountService throws one")
    void withdrawAmount_ThrowsException_ifAccountService_throwsAnException() {
        // given
        int amount = 30;
        int accountId = 1001;
        doThrow(new IllegalArgumentException("Cannot withdraw")).when(accountServiceMock).withdrawAmount(accountId, BigDecimal.valueOf(amount));

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class,
                () -> atmService.withdrawAmount(accountId, amount));

        // then
        assertThat(throwable.getMessage()).isEqualTo("Cannot withdraw");

    }

    @Test
    @DisplayName("withdrawAmount returns 2x50 for 100")
    void withDrawAmount_returns_right_notes_for_100() {
        atmService.getBankNotes().put(5, 1);
        atmService.getBankNotes().put(50, 2);
        // given
        Integer amount = 100;
        int accountId = 1001;
        given(accountServiceMock.withdrawAmount(accountId, BigDecimal.valueOf(amount))).willReturn(true);

        // when
        List<Integer> notes = atmService.withdrawAmount(accountId, amount);

        // then
        assertThat(notes).containsExactlyInAnyOrder(50, 50);
    }

    @Test
    @DisplayName("withdrawAmount returns 2x5, 2x20 for 50")
    void withDrawAmount_returns_right_notes_for_50() {
        // given
        Integer amount = 50;
        int accountId = 1001;
        given(accountServiceMock.withdrawAmount(accountId, BigDecimal.valueOf(amount))).willReturn(true);

        // when
        List<Integer> notes = atmService.withdrawAmount(accountId, amount);

        // then
        assertThat(notes).containsExactlyInAnyOrder(5, 5, 20, 20);
    }

    @Test
    @DisplayName("withdrawAmount returns 2x5, 2x20 for 25")
    void withDrawAmount_returns_right_notes_for_25() {
        // given
        Integer amount = 25;
        int accountId = 1001;
        given(accountServiceMock.withdrawAmount(accountId, BigDecimal.valueOf(amount))).willReturn(true);

        // when
        List<Integer> notes = atmService.withdrawAmount(accountId, amount);

        // then
        assertThat(notes).containsExactlyInAnyOrder(20, 5);
    }

    @Test
    @DisplayName("withdrawAmount returns 1x20, 1x10 for 30")
    void withDrawAmount_returns_right_notes_for_30() {
        atmService.getBankNotes().put(5, 1);
        // given
        Integer amount = 30;
        int accountId = 1001;
        given(accountServiceMock.withdrawAmount(accountId, BigDecimal.valueOf(amount))).willReturn(true);

        // when
        List<Integer> notes = atmService.withdrawAmount(accountId, amount);

        // then
        assertThat(notes).containsExactlyInAnyOrder(20, 10);
    }

    @Test
    @DisplayName("withdrawAmount throws an exception when it is not dividable by 5, for example 23")
    void withDrawAmount_returns_exception_for_23() {
        atmService.getBankNotes().put(5, 1);
        // given
        Integer amount = 23;
        int accountId = 1001;
        given(accountServiceMock.withdrawAmount(accountId, BigDecimal.valueOf(amount))).willReturn(true);

        // when
        Throwable throwable = assertThrows(IllegalArgumentException.class,
                () -> atmService.withdrawAmount(accountId, amount));

        // then
        assertThat(throwable.getMessage()).isEqualTo("Sorry, the ATM cannot hand out 23 as it is not dividable by 5");
    }

}