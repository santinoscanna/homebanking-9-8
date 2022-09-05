package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;

public final class CardUtils {
    private CardUtils(){}
    public static String generateCardNumber(int min, int max, CardRepository cardRepository){
        String cardNumber;
        int flag=0;

        do {      //Genero 4 numeros random y los concateno para crear el numero de la Card
            int num1 = (int) ((Math.random() * (max - min)) + min);
            int num2 = (int) ((Math.random() * (max - min)) + min);
            int num3 = (int) ((Math.random() * (max - min)) + min);
            int num4 = (int) ((Math.random() * (max - min)) + min);
            cardNumber = num1 + "-" + num2 + "-" + num3 + "-" + num4;
            // findByNumber (Compara los Strings de los numeros de tarjeta ya creados y revisa si hay otro igual)
            if (cardRepository.findByNumber(cardNumber) == null) {
                // Si no hay otro igual devuelve null y sale del do while
                flag = 1;
            }
        // En el caso de que haya otro igual vuelve a ingresar al do y crea otro numero random
        }while (flag != 1) ;
        return cardNumber;
    }

    public static int generateCvv(int min, int max) {
        int cvv = (int) ((Math.random() * (max - min)) + min);
        return cvv;
    }
}