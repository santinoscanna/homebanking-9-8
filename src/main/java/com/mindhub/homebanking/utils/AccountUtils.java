package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.AccountRepository;

public final class AccountUtils {

    public AccountUtils(){}
    public static String generateAccountNumber(int min,int max, AccountRepository accountRepository){
        String accountNumber;
        int flag=0;

        do {
            // Genero un numero random y lo concateno con "VIN"
            int num = (int) ((Math.random() * (max - min)) + min);
            accountNumber = "VIN" + num;
            // findByNumber (Compara los Strings de accountNumber con los numeros de cuenta ya creados y revisa si hay otro igual)
            if (accountRepository.findByNumber(accountNumber) == null) {
                // Si no hay otro igual devuelve null y sale del do while
                flag = 1;
            }
        // En el caso de que haya otro igual vuelve a ingresar al do y crea otro numero random
        }while (flag != 1) ;
        return accountNumber;
    }
}