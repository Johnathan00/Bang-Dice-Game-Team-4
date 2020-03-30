/*
 * Cierra Ditmore
 * CS 2365
 */
package bangdicegame;

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
        Dice [] allDice = new Dice [5];
        ArrowPile arrowPile = new ArrowPile();
        

        
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
            
            if ("Sheriff".equals(players[i].role)){
                players[i].lifePoints += 2;
                players[i].maxLife += 2;
            }
            
            System.out.println("Player " + i + " name is: " + players[i].name);
            System.out.println("Player " + i + " role is: " + players[i].role);
            aiPlayers -= 1;
            i += 1;
        }
        
        GameFunctions playerOrder = new GameFunctions (players, totalPlayers);
        
        i = 0;
        while (i < totalPlayers){
            System.out.println(playerOrder.playerOrder[i].name);
            i++;
        }
        
        i = 0;
        
        while (i < 5){
            allDice[i] = new Dice();
            i++;
        }
        
        
        //
        //START OF GAME
        //
        
        i = 0;
        
        while (!"Sheriff".equals(players[i].role)){
            playerOrder.next_turn();
            i++;
        }
        
        System.out.println();
        
        Boolean dynamiteExecuted = false;
        Boolean gatlingExecuted = false;
        int rollsRemaining;
        
        
        
        while (!playerOrder.game_over()){
            i = 0;
            dynamiteExecuted = false;
            gatlingExecuted = false;
            rollsRemaining = 3;
            
            for (i = 0; i < playerOrder.numOfPlayers; i++){
                System.out.println(players[i].name + " has " + players[i].lifePoints + " life and " + players[i].arrows + " arrow(s)");
            }
            
            System.out.println();
            
            System.out.println("It is currently " + playerOrder.get_current_player().name + "'s turn\n");
            
            i = 0;
            
            while (i < 5){
                allDice[i].roll_dice();
                i++;
            }
            
            for (i = 0; i < 5; i ++){
                if (!playerOrder.game_over()){
                    if ("Arrow".equals(allDice[i].roll)){
                        System.out.println("You rolled an arrow. You must pick up an arrow before continuing.");
                        Dice.arrow_roll(playerOrder.get_current_player(), arrowPile, playerOrder);
                    }
                }
            }
            
            
            
            while (rollsRemaining > 0 && !dynamiteExecuted){
                System.out.println("\nYour roll: ");
                for (i = 0; i < 5; i++){
                    System.out.println("Dice " + (i+1) + ": " + allDice[i].roll);
                }
                rollsRemaining = Dice.reroll_dice(allDice, rollsRemaining, arrowPile, playerOrder);
                
                for (i = 0; i < 5; i++){
                    if ("Dynamite".equals(allDice[i].roll)){
                        if (!dynamiteExecuted){
                            dynamiteExecuted = Dice.dynamite_roll(allDice, playerOrder.get_current_player(), playerOrder, arrowPile);
                            
                        }
                    }
                }
            }
            
            System.out.println("\nYour final roll: ");
                for (i = 0; i < 5; i++){
                    System.out.println("Dice " + (i+1) + ": " + allDice[i].roll);
                }
            
            
            System.out.println();
            
            for (i = 0; i < 5; i ++){
                if (!playerOrder.game_over()){
                    //if ("Arrow".equals(allDice[i].roll)){
                    //    Dice.arrow_roll(playerOrder.get_current_player(), arrowPile, playerOrder);
                    //}
                    if ("Dynamite".equals(allDice[i].roll)){
                        if (!dynamiteExecuted){
                            Dice.dynamite_roll(allDice, playerOrder.get_current_player(), playerOrder, arrowPile);
                            dynamiteExecuted = true;
                        }
                    }
                    else if ("Bull's Eye 1".equals(allDice[i].roll)){
                        Dice.bullsEye1_roll(playerOrder, arrowPile);
                    }
                    else if ("Bull's Eye 2".equals(allDice[i].roll)){
                        Dice.bullsEye2_roll(playerOrder, arrowPile);
                    }
                    else if ("Beer".equals(allDice[i].roll)){
                        Dice.beer_roll(playerOrder);
                    }
                    else if("Gatling".equals(allDice[i].roll)){
                        if (!gatlingExecuted){
                            Dice.gatling_roll(allDice, playerOrder.get_current_player(), playerOrder, arrowPile);
                            gatlingExecuted = true;
                        }
                    }
                }
            }
            
            playerOrder.next_turn();
            
            System.out.println("\n--------------------------------------------------\n");
        }
    }
    
}
