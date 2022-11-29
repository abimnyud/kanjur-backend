package com.enjoy.kanjurbackend.transaction;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enjoy.kanjurbackend.user.User;
import com.enjoy.kanjurbackend.user.UserRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Page<Transaction> find(Integer skip, Integer take) {
        return transactionRepository.findAll(PageRequest.of(skip, take));
    }

    @Override
    public String getBalance() {
        return transactionRepository.getBalance();
    }

    @Override
    public boolean deposit(Integer userId, double amount) {
        /**
         * Get current canteen's balance
         */
        Double currBalance = Double.parseDouble(transactionRepository.getBalance());
        currBalance = (currBalance == null) ? 0 : currBalance;
        
        /**
         * Create new transaction data
         */
        Transaction newTransaction = new Transaction();
        newTransaction.setUserId(userId);
        newTransaction.setDeposit(amount);
        newTransaction.setBalance(currBalance + amount);

        /**
         * Check if user has any debt
         */
        User user = userRepository.getById(userId);
        double userDebt = user.getDebt();
        
        if (userDebt > 0) {
            double additionalAmount = amount - userDebt;

            if (additionalAmount >= 0) {
                user.setDebt(0);
            } else {
                user.setDebt(userDebt - amount);
            }

            /**
             * Money to deposit after pay debt
             */
            amount = (additionalAmount < 0) ? 0 : additionalAmount;
        }

        /**
         * Update user deposit amount
         */
        user.setDeposit(user.getDeposit() + amount);

        userRepository.save(user);
        transactionRepository.save(newTransaction);
        
        return true;
    }
    
    @Override
    public boolean withdraw(Integer userId, double amount) {
        /**
         * Get current canteen's balance
         */
        Double currBalance = Double.parseDouble(transactionRepository.getBalance());
        currBalance = (currBalance == null) ? 0 : currBalance;

        /**
         * Check if withdraw amount is is exceed the canteen's balance
         */
        if (amount > currBalance) {
            /**
             * If yes, then throw error
             */
            Locale locale = new Locale("id", "id");      
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            throw new Error("Error withdraw, current canteen's balance is only " + currencyFormatter.format(currBalance));
        }
        
        /**
         * Create new transaction data
         */
        Transaction newTransaction = new Transaction();
        newTransaction.setUserId(userId);
        newTransaction.setWithdraw(amount);
        newTransaction.setBalance(currBalance - amount);
        
        /**
         * Check user data
         */
        User user = userRepository.getById(userId);
        double userDebt = user.getDebt();
        double userRevenue = user.getRevenue();
        double userDeposit = user.getDeposit();
        double additionalAmount = amount;

        /**
         * If withdraw amount is more than userDeposit + userRevenue, 
         * take the money from user's deposit first, then take from user's revenue
         */
        if (userDeposit > 0) {
            /**
             * Check if user has deposit
             */
            additionalAmount -= userDeposit;

            if (additionalAmount > 0) {
                /**
                 * If withdraw is exceed user's deposit, then update user's deposit and flag the transaction
                 */
                user.setDeposit(0);
                newTransaction.setFlag(true);
            } else {
                user.setDeposit(Math.abs(additionalAmount));
                additionalAmount = 0;
            }
        } else if (additionalAmount > 0 && userRevenue > 0) {
            /**
             * Check if user has revenue
             */
            additionalAmount -= userRevenue;

            if (additionalAmount > 0) {
                /**
                 * If withdraw is exceed user's revenue, then add to debt and flag the transaction
                 */
                user.setRevenue(0);
                
                user.setDebt(userDebt + additionalAmount);
                newTransaction.setFlag(true);
            } else if (additionalAmount < 0) {
                user.setRevenue(Math.abs(additionalAmount));
                additionalAmount = 0;
            }
        } else {
            /**
             * If user has no deposit or revenue, then add to debt and flag the transaction
             */
            user.setDebt(user.getDebt() + additionalAmount);
            newTransaction.setFlag(true);
        }

        userRepository.save(user);
        transactionRepository.save(newTransaction);

        return true;
    }
}
