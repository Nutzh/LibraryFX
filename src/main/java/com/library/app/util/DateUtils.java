package com.library.app.util;

import java.time.*;

import java.time.temporal.ChronoUnit;

public class DateUtils {
        
        public static boolean enRetard (LocalDate DeadLine){
            LocalDate today = LocalDate.now();
            return today.isAfter(DeadLine); // ici on verifie si la date actuelle est apres la date d'echeance.
        }

        public static long DureeEntreDeuxDate(LocalDate date1, LocalDate date2) {
            return ChronoUnit.DAYS.between(date1, date2); } // ici on verifie si la date2 est apres la date1.

        public static double CalculatePenalty (LocalDate DeadLine, int penalite) {
            
            if (!enRetard(DeadLine)) {
                return 0.0;} // Pas de penalite 
            else {
                long daysLate = DureeEntreDeuxDate(DeadLine, LocalDate.now());
                return daysLate * penalite; // Calcul de la penalite en chaque jour de retard 
            }
        }
}