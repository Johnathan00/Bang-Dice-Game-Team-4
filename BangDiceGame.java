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
        
        //Gets how many AI players there are
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
        
        //Creates all of the players
        while (aiPlayers >= 0){
            players[i] = new Character(randomSelection[aiPlayers]);
            players[i].set_role(roles[i]);
            
            //Increases life points if they are the sheriff
            if ("Sheriff".equals(players[i].role)){
                players[i].lifePoints += 2;
                players[i].maxLife += 2;
            }
            
            //Temporary output, just showing players and order
            System.out.println("Player " + i + " name is: " + players[i].name);
            System.out.println("Player " + i + " role is: " + players[i].role);
            aiPlayers -= 1;
            i += 1;
        }
        
        
        GameFunctions playerOrder = new GameFunctions (players, totalPlayers);
        
        i = 0;
        
        //Creating Dice
        while (i < 5){
            allDice[i] = new Dice();
            i++;
        }
        
        
        //
        //START OF GAME
        //
        
        i = 0;
        
        //Making the sheriff the first player to go
        while (!"Sheriff".equals(players[i].role)){
            playerOrder.next_turn();
            i++;
        }
        
        System.out.println();
        
        Boolean dynamiteExecuted = false;
        Boolean gatlingExecuted = false;
        Boolean doubleDamage = false;
        char tempDoubleDamage;
        int rollsRemaining, numBullsEye1, numBullsEye2, numBeer, numGatling;
        
        
        
        while (!playerOrder.game_over()){
            i = 0;
            dynamiteExecuted = false;
            gatlingExecuted = false;
            rollsRemaining = 3;
            numBullsEye1 = numBullsEye2 = numBeer = numGatling = 0;
            
            //Lucky Duke Special ability: Extra Reroll
            if ("Lucky Duke".equals(playerOrder.get_current_player().name)){
                rollsRemaining = 4;
            }
            
            System.out.println();
            
            //Prints players current turn
            System.out.println("It is currently " + playerOrder.get_current_player().name + "'s turn\n");
            
            if ("Sid Ketchum".equals(playerOrder.get_current_player().name)){
                System.out.println("Since it is Sid Ketchum's turn, he may serve 1 person a beer.");
                Dice.beer_roll(playerOrder);
            }
            
            i = 0;
            
            //Rolls all 5 dice
            while (i < 5){
                allDice[i].roll_dice();
                i++;
            }
            
            //Determines if the first roll contains any arrows
            for (i = 0; i < 5; i ++){
                if (!playerOrder.game_over()){
                    if ("Arrow".equals(allDice[i].roll)){
                        System.out.println("You rolled an arrow. You must pick up an arrow before continuing.");
                        Dice.arrow_roll(playerOrder.get_current_player(), arrowPile, playerOrder);
                    }
                }
            }
            
            
            //Allows for rerolls, if they roll 3 dynamite, takes care of that
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
            
            //Displays final roll
            System.out.println("\nYour final roll: ");
                for (i = 0; i < 5; i++){
                    System.out.println("Dice " + (i+1) + ": " + allDice[i].roll);
                }
            
            
            System.out.println();
            
            //Completes all of the dice rolls
            for (i = 0; i < 5; i ++){
                if (!playerOrder.game_over()){
                    if ("Dynamite".equals(allDice[i].roll)){
                        if (!dynamiteExecuted){
                            Dice.dynamite_roll(allDice, playerOrder.get_current_player(), playerOrder, arrowPile);
                            dynamiteExecuted = true;
                        }
                    }
                    else if ("Bull's Eye 1".equals(allDice[i].roll)){
                        numBullsEye1 += 1;   
                    }
                    else if ("Bull's Eye 2".equals(allDice[i].roll)){
                        numBullsEye2 += 1;
                    }
                    else if ("Beer".equals(allDice[i].roll)){
                        numBeer += 1;
                    }
                    else if("Gatling".equals(allDice[i].roll)){
                        numGatling += 1;
                    }
                }
            }
            
            if ("Suzy Lafayette".equals(playerOrder.get_current_player().name)){
                if (numBullsEye1 == 0 && numBullsEye2 == 0){
                    playerOrder.get_current_player().gain_life();
                    playerOrder.get_current_player().gain_arrow();
                }
            }
            
            while (numBullsEye1 > 0){
                if ("Slab the Killer".equals(playerOrder.get_current_player().name)){
                    if (numBeer > 0){
                        System.out.print("Would you like to double the damage of your Bull's Eye 1 for 1 beer? (Y/N): ");
                        tempDoubleDamage = input.nextLine().charAt(0);
                        while (tempDoubleDamage != 'y' && tempDoubleDamage != 'Y' && tempDoubleDamage != 'n' && tempDoubleDamage != 'N'){
                            System.out.print("Invalid input. Please try again (Y/N): ");
                            tempDoubleDamage = input.nextLine().charAt(0);
                        }
                        if (tempDoubleDamage == 'y' || tempDoubleDamage == 'Y'){
                            doubleDamage = true;
                            numBeer -= 1;
                        }
                    }
                }
                
                Dice.bullsEye1_roll(playerOrder, arrowPile, doubleDamage);
                numBullsEye1 -= 1;
                
                doubleDamage = false;
            }
            while (numBullsEye2 > 0){
                if ("Slab the Killer".equals(playerOrder.get_current_player().name)){
                    if (numBeer > 0){
                        System.out.print("Would you like to double the damage of your Bull's Eye 2 for 1 beer? (Y/N): ");
                        tempDoubleDamage = input.nextLine().charAt(0);
                        while (tempDoubleDamage != 'y' && tempDoubleDamage != 'Y' && tempDoubleDamage != 'n' && tempDoubleDamage != 'N'){
                            System.out.print("Invalid input. Please try again (Y/N): ");
                            tempDoubleDamage = input.nextLine().charAt(0);
                        }
                        if (tempDoubleDamage == 'y' || tempDoubleDamage == 'Y'){
                            doubleDamage = true;
                            numBeer -= 1;
                        }
                    }
                }
                
                Dice.bullsEye2_roll(playerOrder, arrowPile, doubleDamage);
                numBullsEye2 -= 1;
                
                doubleDamage = false;
            }
            while (numBeer > 0){
                Dice.beer_roll(playerOrder);
                numBeer -= 1;
            }
            while (numGatling > 0){
                if (!gatlingExecuted){
                    Dice.gatling_roll(allDice, playerOrder.get_current_player(), playerOrder, arrowPile);
                    gatlingExecuted = true;
                }
                numGatling -= 1;
            }
            
            System.out.println("\nThe current standings are: ");
            
            //Shows standing of life points and arrows at end of round
            for (i = 0; i < playerOrder.numOfPlayers; i++){
                System.out.println(players[i].name + " has " + players[i].lifePoints + " life and " + players[i].arrows + " arrow(s)");
            }
            
            //Goes to next player
            playerOrder.next_turn();
            
            System.out.println("\n*** Press enter to progress to the next turn. ***");
            input.nextLine();
            input.nextLine();
            
            System.out.println("\n--------------------------------------------------\n");
        }
    }
    
}
