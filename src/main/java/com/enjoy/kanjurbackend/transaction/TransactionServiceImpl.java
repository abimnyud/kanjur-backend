package com.enjoy.kanjurbackend.transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enjoy.kanjurbackend.cart.Cart;
import com.enjoy.kanjurbackend.cart.CartRepository;
import com.enjoy.kanjurbackend.product.Product;
import com.enjoy.kanjurbackend.product.ProductRepository;
import com.enjoy.kanjurbackend.product_transaction.ProductTransaction;
import com.enjoy.kanjurbackend.product_transaction.ProductTransactionRepository;
import com.enjoy.kanjurbackend.user.User;
import com.enjoy.kanjurbackend.user.UserRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductTransactionRepository productTransactionRepository;

    @Override
    public Page<Transaction> find(Integer skip, Integer take) {
        return this.transactionRepository.findAll(PageRequest.of(skip, take));
    }

    @Override
    public Double getBalance() {
        String balance = this.transactionRepository.getBalance();

        return (balance == null) ? 0 : Double.parseDouble(balance);
    }

    @Override
    public boolean deposit(Integer userId, Double amount) {
        /**
         * Get current canteen's balance
         */
        Double currBalance = this.getBalance();
        
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
        User user = this.userRepository.getById(userId);
        double userDebt = user.getDebt();
        
        if (userDebt > 0) {
            double additionalAmount = amount - userDebt;

            if (additionalAmount >= 0) {
                user.setDebt(0.00);
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

        this.userRepository.save(user);
        this.transactionRepository.save(newTransaction);
        
        return true;
    }

    @Override
    public boolean withdraw(Integer userId, Double amount) {
        /**
         * Get current canteen's balance
         */
        Double currBalance = this.getBalance();

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
        User user = this.userRepository.getById(userId);
        double userDebt = user.getDebt();
        double userRevenue = user.getRevenue();
        double userDeposit = user.getDeposit();
        double additionalAmount = amount;
        user.setWithdraw(user.getWithdraw() + additionalAmount);

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
                user.setDeposit(0.00);
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

            if (additionalAmount >= 0) {
                /**
                 * If withdraw is exceed user's revenue, then add to debt and flag the transaction
                 */
                user.setRevenue(0.00);
                
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

        this.userRepository.save(user);
        this.transactionRepository.save(newTransaction);

        return true;
    }

    @Override
    public boolean addUserRevenue(Integer userId, Double amount) {
        User user = this.userRepository.getById(userId);

        /**
         * Check if user has any debt
         */
        Double userDebt = user.getDebt();
        if (userDebt > 0) {
            /**
             * If has debt, then pay user's debt
             */
            Double additionalAmount = amount - userDebt;

            if (additionalAmount > 0) {
                user.setDebt(0.00);
                user.setRevenue(additionalAmount);
            } else {
                user.setDebt(userDebt - amount);
            }
        } else {
            /**
             * Else, add to user's revenue
             */
            user.setRevenue(user.getRevenue() + amount);
        }

        this.userRepository.save(user);

        return true;
    }

    @Override
    public boolean checkout(Integer userId, Double deposit) {
        /**
         * Get total price
         */
        Double totalPrice = 0.0;
        List<Cart> cartList = this.cartRepository.findAllByUserId(userId);

        for (Cart cart : cartList) {
            totalPrice += cart.getPrice();
        }

        /**
         * Get current canteen's balance
         */
        Double currBalance = this.getBalance();
        
        /**
         * Create new transaction data
         */
        Transaction newTransaction = new Transaction();
        newTransaction.setUserId(userId);
        newTransaction.setDeposit(deposit);
        newTransaction.setTotalPrice(totalPrice);
        newTransaction.setBalance(currBalance + deposit);

        /**
         * Check if user has any debt
         */
        User user = this.userRepository.getById(userId);
        double userDebt = user.getDebt();

        /**
         * Check if deposit more than the total price,
         * then add to user's balance
         */
        if (deposit >= totalPrice) {
            Double additionalAmount = deposit - totalPrice;
            
            if (userDebt > 0) {
                additionalAmount = additionalAmount - userDebt;

                if (additionalAmount >= 0) {
                    user.setDebt(0.00);
                } else {
                    user.setDebt(userDebt - additionalAmount);
                }

                /**
                 * Money to deposit after pay debt
                 */
                additionalAmount = (additionalAmount < 0) ? 0 : additionalAmount;
            }

            /**
             * Update user deposit amount
             */
            user.setDeposit(user.getDeposit() + additionalAmount);
        } else {
            user.setDebt(user.getDebt() + totalPrice - deposit);
            newTransaction.setFlag(true);
        }

        this.userRepository.save(user);
        this.transactionRepository.save(newTransaction);

        for (Cart cart : cartList) {
            Product product = cart.product;
            /**
             * Add revenue to the user
             */
            User seller = this.userRepository.getById(product.getCreatedBy());
            this.addUserRevenue(seller.getId(), cart.getPrice());

            /**
             * Add items to transaction
             */
            ProductTransaction productTransaction = new ProductTransaction();
            productTransaction.setTransactionId(newTransaction.getId());
            productTransaction.setProductId(cart.getProductId());
            productTransaction.setQty(cart.getQty());
            productTransaction.setPrice(cart.getPrice());

            this.productTransactionRepository.save(productTransaction);

            /**
             * Decrement product stock
             */
            product.setStock(product.getStock() - cart.getQty());
            this.productRepository.save(product);
            this.cartRepository.delete(cart);
        }

        return true;
    }
}