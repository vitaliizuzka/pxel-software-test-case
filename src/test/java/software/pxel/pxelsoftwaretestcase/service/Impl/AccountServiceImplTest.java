package software.pxel.pxelsoftwaretestcase.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import software.pxel.pxelsoftwaretestcase.model.entity.Account;
import software.pxel.pxelsoftwaretestcase.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountServiceImpl accountService;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private Account accountFrom;
    private Account accountTo;

    @BeforeEach
    void setUp() {
        accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setBalance(new BigDecimal("100.00"));

        accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setBalance(new BigDecimal("50.00"));
    }



    @Test
    void testTransferSuccess() {
        BigDecimal transferAmount = new BigDecimal("30.00");

        when(accountService.getAccountById(1L)).thenReturn(accountFrom);
        when(accountService.getAccountById(2L)).thenReturn(accountTo);

        accountServiceImpl.transfer(1L, 2L, transferAmount);

        assertEquals(new BigDecimal("70.00"), accountFrom.getBalance());
        assertEquals(new BigDecimal("80.00"), accountTo.getBalance());

        verify(accountRepository).save(accountFrom);
        verify(accountRepository).save(accountTo);
    }

    @Test
    void testTransferSameUserIdThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountServiceImpl.transfer(1L, 1L, new BigDecimal("10")));

        assertEquals("you can't transfer money to yourself", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferNegativeAmountThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountServiceImpl.transfer(1L, 2L, new BigDecimal("-10")));

        assertEquals("the transfer amount must be positive", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferZeroAmountThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountServiceImpl.transfer(1L, 2L, BigDecimal.ZERO));

        assertEquals("the transfer amount must be positive", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferAccountFromNotFoundThrows() {
        when(accountService.getAccountById(1L)).thenThrow(new EntityNotFoundException("account 1 not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountServiceImpl.transfer(1L, 2L, new BigDecimal("10")));

        assertEquals("account 1 not found", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferAccountToNotFoundThrows() {
        when(accountService.getAccountById(1L)).thenReturn(accountFrom);
        when(accountService.getAccountById(2L)).thenThrow(new EntityNotFoundException("account 2 not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountServiceImpl.transfer(1L, 2L, new BigDecimal("10")));

        assertEquals("account 2 not found", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testTransferNotEnoughMoneyThrows() {
        when(accountService.getAccountById(1L)).thenReturn(accountFrom);
        when(accountService.getAccountById(2L)).thenReturn(accountTo);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> accountServiceImpl.transfer(1L, 2L, new BigDecimal("200")));

        assertEquals("Not enough money", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }
}