/*
 * Cierra Ditmore
 * CS 2365
 */
package bangdicegame;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author cmdma
 */
public class BangDiceGame {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        int [] randomSelection = {1, 2, 3, 4, 5, 6, 7, 8, 9 ,10, 11, 12, 13, 14, 15, 16};
        int aiPlayers;
        int totalPlayers;
        Character [] players = new Character [8];
        String [] roles;
        
        randomSelection = Character.shuffle_character(randomSelection);
        
        Scanner input = new Scanner(System.in);
        
        System.out.print("How many AI players would you like to play with? (2-7): ");
        aiPlayers = input.nextInt();
        while (aiPlayers < 2 || aiPlayers > 7){
            System.out.print("Invalid input. Enter a number 2-7: ");
            aiPlayers = input.nextInt();
        }
        
        totalPlayers = aiPlayers + 1;
        
        roles = Character.select_role(aiPlayers);
        System.out.println(roles[0]);
        
        int i = 0;
        
        while (aiPlayers >= 0){
            players[i] = new Character(randomSelection[aiPlayers]);
            players[i].set_role(roles[i]);
            System.out.println("Player " + i + " name is: " + players[i].name);
            System.out.println("Player " + i + " role is: " + players[i].role);
            aiPlayers -= 1;
            i += 1;
        }
        

    }
    
}
