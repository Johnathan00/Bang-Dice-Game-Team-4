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
    public int currentPlayer, numOfPlayers;
    
    public GameFunctions (Character [] players, int totalPlayers){
        this.playerOrder = players;
        this.currentPlayer = 0;
        this.numOfPlayers = totalPlayers;
    }
    
    public Character next_turn (){
        this.currentPlayer = (this.currentPlayer + 1)%(this.numOfPlayers);
        return this.playerOrder[this.currentPlayer];
    }
    
    public Character get_current_player (){
        return this.playerOrder[this.currentPlayer];
    }
    
    public Character get_next_player (){
        int temp;
        temp = (this.currentPlayer + 1)%this.numOfPlayers;
        
        if (temp < 0){
            temp = this.numOfPlayers + temp;
        }
        
        return this.playerOrder[temp];
    }
    
    public Character get_previous_player (){
        int temp;
        temp = (this.currentPlayer - 1)%this.numOfPlayers;
        
        if (temp < 0){
            temp = this.numOfPlayers + temp;
        }
        
        return this.playerOrder[temp];
    }
    
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
    
    public void eliminate_player (Character player, ArrowPile arrowPile){
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
        
        if (numOfPlayers == 1){
            System.out.println(this.playerOrder[1].name + " is the last character remaining");
        } 
    }
    
    public boolean game_over (){
        if (this.numOfPlayers > 1){
            return false;
        }
        else {
            return true;
        }
    }
}
