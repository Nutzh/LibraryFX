package com.library.app.util;

public class StringValidator {

    public static boolean isNotEmpty (String str) {
        return str != null && !str.trim().isEmpty(); // ici on verifier si la chaine n'est pas nulle et n'est pas vide  apres supperession des espaces.
    }

    public static boolean isValidEmail (String email){
        String emailchar = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailchar); // ici on verifie si l'email n'est pas nulle et correspond au format d'email.
    }

    public static boolean isValidPhoneNumber (String phoneNumber){
        String phonechar = "^[0-9]{10}$";
        return phoneNumber != null && phoneNumber.matches(phonechar); // ici on verifie si le numero de telephone n'est pas nulle et correspond au format de numero de telephone.
    } 

    public static boolean isValidISBN (String isbn){
        String isbnchar = "^(97(8|9))?\\d{9}(\\d|X)$";
        return isbn != null && isbn.matches(isbnchar); // ici on verifie si l'ISBN n'est pas nulle et correspond au format d'ISBN.
    }

    public static boolean isAlpha (String text){
        String textchar = "^[a-zA-Z]+$";
        return text != null && text.matches(textchar); // ici on verifie si le texte n'est pas nulle et contient uniquement des lettres .
    }

}