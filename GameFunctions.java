/*
 * Cierra Ditmore
 * CS 2365
 */
package bangdicegame;

/**
 *
 * @author cmdma
 */
public class GameFunctions {
    public Character [] playerOrder;
    public int currentPlayer, numOfPlayers, originalNumOfPlayers;
    public Boolean game_over;
    
    public GameFunctions (Character [] players, int totalPlayers){
        this.playerOrder = players;
        this.currentPlayer = 0;
        this.numOfPlayers = totalPlayers;
        this.originalNumOfPlayers = totalPlayers;
        this.game_over = false;
    }
    
    public Character next_turn (){
        this.currentPlayer = (this.currentPlayer + 1)%(this.numOfPlayers);
        return this.playerOrder[this.currentPlayer];
    }
    
    public Character get_current_player (){
        return this.playerOrder[this.currentPlayer];
    }
    
    //gets player 1 to the right
    public Character get_next_player (){
        int temp;
        temp = (this.currentPlayer + 1)%this.numOfPlayers;
        
        if (temp < 0){
            temp = this.numOfPlayers + temp;
        }
        
        return this.playerOrder[temp];
    }
    
    //gets player 1 to the left
    public Character get_previous_player (){
        int temp;
        temp = (this.currentPlayer - 1)%this.numOfPlayers;
        
        if (temp < 0){
            temp = this.numOfPlayers + temp;
        }
        
        return this.playerOrder[temp];
    }
    
    //gets player 2 to the right
    public Character get_two_away_player (Character currentPlayer){
        int temp;
        temp = (this.currentPlayer + 2)%this.numOfPlayers;
        
        if (temp < 0){
            temp = this.numOfPlayers + temp;
        }
        
        Character nextCharacter = this.playerOrder[temp];
        
        if (nextCharacter != currentPlayer){
            return nextCharacter;
        }
        else {
            return this.get_next_player();
        }
    }
    
    //gets player 2 to the left
    public Character get_two_before_player (Character currentPlayer){
        int temp;
        temp = (this.currentPlayer - 2)%this.numOfPlayers;
        
        if (temp < 0){
            temp = this.numOfPlayers + temp;
        }
        
        Character previousCharacter = this.playerOrder[temp];
        
        if (previousCharacter != currentPlayer){
            return previousCharacter;
        }
        else {
            return this.get_previous_player();
        }
    }
    
    public void eliminate_player (Character player, ArrowPile arrowPile, Boolean killedByPlayer){
        int i;
        
        while(player.arrows > 0){
                arrowPile.add_arrow(player);
            }
        
        for (i = 0; i < this.numOfPlayers; i++){
            if (this.playerOrder[i] == player){
                for (i = i; i < this.numOfPlayers - 1; i++){
                    this.playerOrder[i] = this.get_next_player();
                }
            }
        }
        
        this.playerOrder[this.numOfPlayers] = null;
        
        this.numOfPlayers -= 1;
        
        this.game_over = this.determine_game_over(this, player, killedByPlayer);
        
        if (!this.game_over){
            for (i = 0; i < this.numOfPlayers; i++){
                if ("Vulture Sam".equals(this.playerOrder[i].name)){
                    this.playerOrder[i].gain_life();
                    this.playerOrder[i].gain_life();
                }
            }
        }
    }
    
    public boolean determine_game_over (GameFunctions playerOrder, Character deadPlayer, Boolean killedByPlayer){
        if (playerOrder.originalNumOfPlayers == 3){
            if (killedByPlayer){
                if (playerOrder.numOfPlayers == 2){
                    if ("Deputy".equals(playerOrder.get_current_player().role) && "Renegade".equals(deadPlayer.role)){
                        System.out.println("The sheriff has killed the renegade and has won the game.");
                        return true;
                    }
                    else if ("Renegade".equals(playerOrder.get_current_player().role) && "Outlaw".equals(deadPlayer.role)){
                        System.out.println("The renegade has killed the outlaw and has won the game.");
                        return true;
                    }
                    else if ("Outlaw".equals(playerOrder.get_current_player().role) && "Deputy".equals(deadPlayer.role)){
                        System.out.println("The outlaw has killed the deputy and has won the game.");
                        return true;
                    }
                    else {
                        System.out.println(deadPlayer.name + " has been killed. The winner will be decided by who is the last alive.");
                        return false;
                    }
                }
                else {
                    System.out.println(deadPlayer.name + " has been killed. The winner of the game is " + playerOrder.get_current_player().name);
                    return true;
                }
            }
            else {
                if (playerOrder.numOfPlayers == 2){
                    System.out.println(deadPlayer.name + " has died. The winner will be decided by who is the last alive.");
                    return false;
                }
                else {
                    System.out.println(deadPlayer.name + " has died. The winner of the game is " + playerOrder.get_current_player().name);
                    return true;
                }
            }
        }
        
        else {
            int sheriffAlive = 0;
            int outlawAlive = 0;
            int renegadeAlive = 0;
            int i;
        
            for (i = 0; i < playerOrder.numOfPlayers; i++){
                if ("Sheriff".equals(playerOrder.playerOrder[i].role)){
                    sheriffAlive += 1;
               }
                else if ("Outlaw".equals(playerOrder.playerOrder[i].role)){
                    outlawAlive += 1;
                }
                else if ("Renegade".equals(playerOrder.playerOrder[i].role)){
                    renegadeAlive += 1;
                }
            }
        
            if ((sheriffAlive == 1) && (outlawAlive == 0) && (renegadeAlive == 0)){
                System.out.println("All renegades and outlaws are dead, so the sheirff and deputies win.");
                return true;
            }
            else if (sheriffAlive == 0){
                if ((playerOrder.numOfPlayers == 1) && (renegadeAlive == 1)){
                    System.out.println("The renegade is the last one alive, so they win.");
                    return true;
                }
                else {
                    System.out.println("The sheriff is dead, so the outlaws win.");
                    return true;
                }
            }
            else if (playerOrder.numOfPlayers == 0){
                System.out.println("All players are dead, so the outlaws win.");
                    return true;
            }
            else {
                return false;
            }
        }
    }
}
