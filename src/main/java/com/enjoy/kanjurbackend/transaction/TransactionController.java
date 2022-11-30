package com.enjoy.kanjurbackend.transaction;

import com.enjoy.kanjurbackend.shared.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "transaction")
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("")
    @Operation(summary = "Get list of transactions")
    public ResponseEntity<BaseResponse<Page<Transaction>>> findAll(
        @RequestParam(value = "skip", defaultValue = "0") Integer skip, 
        @RequestParam(value = "take", defaultValue = "10") Integer take
    ) {
        if (skip == null) skip = 0;
        if (take == null) take = 10;

        Page<Transaction> transactionData = transactionService.find(skip, take);

        return new ResponseEntity<BaseResponse<Page<Transaction>>>(
            new BaseResponse<Page<Transaction>>(true, HttpStatus.OK, transactionData), 
            HttpStatus.OK
        );
    }

    @GetMapping("/balance")
    @Operation(summary = "Get canteen's balance")
    public ResponseEntity<BaseResponse<Double>> getBalance() {
        return new ResponseEntity<BaseResponse<Double>>(
            new BaseResponse<Double>(true, HttpStatus.OK, transactionService.getBalance()), 
            HttpStatus.OK
        );
    }
   
    @PostMapping("/deposit")
    @Operation(summary = "Deposit money to canteen's balance")
    public ResponseEntity<?> deposit(
        @RequestParam(value = "userId") Integer userId, 
        @RequestParam(value = "amount") double amount
    ) {
        transactionService.deposit(userId, amount);
        
        return new ResponseEntity<BaseResponse<String>>(
            new BaseResponse<String>(
                true, HttpStatus.OK, 
                "Successfully deposit Rp" + amount + ",- to canteen's balance."
            ), 
            HttpStatus.OK
        );
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw money from canteen's balance")
    public ResponseEntity<?> withdraw(
        @RequestParam(value = "userId") Integer userId, 
        @RequestParam(value = "amount") double amount
    ) {
        try {
            transactionService.withdraw(userId, amount);
        } catch(Throwable t) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(
                    false, HttpStatus.FORBIDDEN, 
                    t.getMessage()
                ), 
                HttpStatus.FORBIDDEN
            );
        }
        
        return new ResponseEntity<BaseResponse<String>>(
            new BaseResponse<String>(
                true, HttpStatus.OK, 
                "Successfully withdraw Rp" + amount + ",- from canteen's balance."
            ), 
            HttpStatus.OK
        );
    }

    @PostMapping("/checkout")
    @Operation(summary = "Withdraw money from canteen's balance")
    public ResponseEntity<?> checkout(
        @RequestParam(value = "userId") Integer userId,
        @RequestParam(value = "deposit") Double deposit
    ) {
        try {
            transactionService.checkout(userId, deposit);

            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(
                    false, HttpStatus.OK, 
                    "Checkout success."
                ), 
                HttpStatus.OK
            );
        } catch(Throwable t) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(
                    false, HttpStatus.FORBIDDEN, 
                    t.getMessage()
                ), 
                HttpStatus.FORBIDDEN
            );
        }
    }
}
