import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.*;
import java.lang.*;
import java.lang.Math;

public class Deadwood {

  private static Room[] rooms;
  private static Player[] players;
  private static Scene[] scenes;
  private static int activeRooms = 10;
  private static int currDay = 0;
  private static int maxDay = 5;
  private static int pIndex;
  private static Thread controller = new Thread();

  public static void main(String[] args) {

    View.createGUI();
    View.getPlayers();
    rooms = Board.buildBoard();
    scenes = Board.buildScenes();
    pIndex = -1;
    View.placeShots();
  }

  public static void runGame(){
    Turn.choice = -1;
    Turn.done = false;
    View.setPressedToDefault();
    if(currDay < maxDay){
      pIndex = (pIndex + 1) % players.length;
      if(activeRooms ==1){
        View.updateMessageText("Day " + currDay + " is over!");
        startNewDay();

      }
      if(activeRooms > 1) {
        View.DisplayPlayer(players[pIndex].getName(), players[pIndex].getRank(), players[pIndex].getMoney(), players[pIndex].getFame());
        Turn.startTurn(pIndex);
        // System.out.printf("Status=%d\n", players[pIndex].getStatus());
        //if(players[pIndex].getStatus() > 0)
        if(players[pIndex].getLocation() < 10){
          Set currentSet = (Set)rooms[players[pIndex].getLocation()];
          // System.out.printf("Shots=%d\n", currentSet.getShots());
          if ((currentSet.getShots() == 0) && (currentSet.isActive() == true)) {
            // System.out.println("in int");
            closeSet(players[pIndex].getLocation());
          }
        }

      }
    }else{
    endGame();
    }
 }

  /*
  initPlayers
  -------------------------------------------------
  -Gets user input for number of players
  -Gets player names
  -Initializes player array
  */
  public static void initPlayers(int numPlayers) {

    //gets user input for the number of players in game
    // System.out.println("numPlayers: "+ numPlayers);

    /*int numPlayers = 0;

    while (numPlayers > 4 || numPlayers <= 1){
      System.out.println("Please enter the number of players between 2 and 4 inclusive:");
      try {
        numPlayers = playerInput.nextInt();
      }catch (InputMismatchException ex) {
        playerInput.nextLine();
        numPlayers = 0;
      }
    }*/

    players = new Player[numPlayers];

    String name = "";

    for (int i = 0; i < numPlayers; i++){
      // System.out.printf("Enter player %d name:\n", i+1);
      //debugging
      //This displays the message in the GUI

      name = "" + (i+1);
      players[i] = new Player(name);
    }
    View.placePlayers(numPlayers);
    startNewDay();
  }

  public static void closeSet(int roomNo) {
    Room curRoom = rooms[roomNo];
    View.updateMessageText("\nYour Scene " + curRoom.getName() + " has wrapped!");
    ((Set)curRoom).setActive(false);
    View.removeAScene(roomNo);
    activeRooms--;
    LinkedList<Player> playersInRoom = curRoom.getAllPlayers();
    for (Player player : playersInRoom){

      if(player.getStatus() == 2){
        payOut(roomNo);
      }
    }

    for (Player player : playersInRoom){
      player.setStatus(0);
      player.resetRehearsal();
    }
    curRoom.removePlayers();

  }

    private static void payOut(int roomNum){
      Room curRoom = rooms[roomNum];
      LinkedList<Player> playersInRoom = curRoom.getAllPlayers();
      int onCardPlayerCount = 0;
      LinkedList<Player> playersOnCard = new LinkedList<Player>();
      for (Player player : playersInRoom){
        if(player.getStatus() == 2){
          //pay the on card player
          onCardPlayerCount++;

          playersOnCard.add(player);
        }else if (player.getStatus() == 1){
          //pay the off card player

          int roleRank = ((Set)curRoom).getRoles()[player.getRoleIndex()].getRank();

          player.changeMoney(roleRank);
        }
      }
      int budget = ((Set)curRoom).getScene().getBudget();
      int[] moneyToPay = new int[budget];
      Random rand = new Random();
      for(int i = 0; i < budget; i++){
        moneyToPay[i] = rand.nextInt(6) + 1;
        // View.updateDicePanel(moneyToPay[i]);
      }
      View.resetDicePanel();
      Arrays.sort(moneyToPay);
      int[]rMoneyToPay  = reverseArray(moneyToPay);
      for(int die : rMoneyToPay){
        View.updateDicePanel(die);
      }




      Role[] roles = ((Set)curRoom).getScene().getRoles();
      int len = roles.length;
      for(int i = 0; i < budget; i++){
        for(int j = 0; j < onCardPlayerCount; j++){

          if(playersOnCard.get(j).getRoleIndex() == i % len){

            playersOnCard.get(j).changeMoney(moneyToPay[i]);
          }
        }
      }
    }

    public static int findIndex(int len, int i){
      if(len - i >= 0){
        return len - i;
      }else if(len - i == -1){
        return len;

      }else if(len - i == -2){
        return len - 1;

      }else if(len - i == -3){
        return len - 2;
      }
      return 0;
    }

    public static int[] reverseArray(int[] e){
      int[] temp = new int[e.length];
      int len = e.length;
      for(int i = 0; i < len; i++){
        temp[i] = e[len -1 - i];
      }

      return temp;
    }

    public static void startNewDay(){

      View.resetShots();
      View.resetPlayerIcons();
      currDay++;
      View.updateMessageText("\n\nYou are on day " + currDay);

      activeRooms = 5;
      Role[] roles = null;
      int len = players.length;
      //reset payers
      for(int i = 0; i < len; i++){
        players[i].setLocation(10);
        players[i].resetRehearsal();
        players[i].setStatus(0);
        players[i].setRoleName("");
        players[i].setRoleIndex(-1);
      }
      //reset rooms
      for(int i = 0; i < 10; i++){
        ((Set)rooms[i]).setActive(true);
        ((Set)rooms[i]).reSetShots();
        ((Set)rooms[i]).removePlayers();

        len = ((Set)rooms[i]).getRoles().length;
        roles = ((Set)rooms[i]).getRoles();

        for(int j = 0; j < len; j++){
          roles[j].setRoleStatus(false);
        }
      }
      //reset scenes
      for(int i = 0; i < 40; i++){
        roles = scenes[i].getRoles();

        len = roles.length;

        for(int j = 0; j < len; j++){

          roles[j].setRoleStatus(false);
        }

      }
      if(currDay > 1){
        for(int i = 0; i < 10; i++){
          if(((Set)rooms[i]).isActive()){
            View.removeAScene(i);
          }
        }
      }
      Collections.shuffle(Arrays.asList(scenes));

      for(int i = 0; i < 10; i++){
        ((Set)rooms[i]).setScene(i);
      }
      runGame();
    }

    public static int calcScore(int index){
      return players[index].getMoney() + players[index].getFame() + 5*players[index].getRank();
    }

    public static void endGame(){
      Player winner = players[0];
      int winNum = calcScore(0);
      int len = players.length;
      Player challenger = null;
      int challengerScore = 0;

      for(int i = 1; i < len; i++){
        challenger = players[i];
        challengerScore = calcScore(i);

        if(challengerScore >= winNum){
          winner = challenger;
          winNum = challengerScore;
        }
      }

      View.updateMessageText("Player " + winner.getName() + " won!");

    }

    public static void findSceneRole(int x, int y){
      Set curSet = (Set)Deadwood.getRoom(Deadwood.getPlayer(Deadwood.getPindex()).getLocation());
      Scene curScene = curSet.getScene();
      Role[] roles = curScene.getRoles();

      int[] setArea = curSet.getArea();
      int setX = setArea[0];
      int setY = setArea[1];
      int setH = setArea[2];
      int setW = setArea[3];


      for (int j = 0; j < roles.length; j++) {
        //get room
        Role curRole = roles[j];

        //get room area Info
        int[] roleArea = curRole.getArea();
        int roleX = roleArea[0];
        int roleY = roleArea[1];
        int roleH = roleArea[2];
        int roleW = roleArea[3];

        //check if within this rooms area
        if (((x >= roleX + setX) && (x <= roleX + roleW + setX)) && ((y >= roleY + setY) && (y <= roleY + roleH+ setY))){
          View.removeMouseListener();
          View.setRoleButtonPressed(false);
          Turn.takeSceneRole(j);
        }
      }
    }

    public static boolean findRole(int x, int y){
      Role[] roles = ((Set)Deadwood.getRoom(Deadwood.getPlayer(Deadwood.getPindex()).getLocation())).getRoles();

      Boolean found = false;
      for (int j = 0; j < roles.length; j++) {
        //get room
        Role curRole = roles[j];

        //get room area Info
        int[] roleArea = curRole.getArea();
        int roleX = roleArea[0];
        int roleY = roleArea[1];
        int roleH = roleArea[2];
        int roleW = roleArea[3];

        //check if within this rooms area
        if (((x >= roleX) && (x <= roleX + roleW)) && ((y >= roleY) && (y <= roleY + roleH))){
          View.removeMouseListener();
          View.setRoleButtonPressed(false);
          Turn.takeSetRole(j);
          found = true;
        }
      }
      return found;
    }

    public static boolean findLocation(int x, int y){
      int[] nieghbors = Deadwood.getRoom(Deadwood.getPlayer(Deadwood.getPindex()).getLocation()).getAllNeighbors();

Deadwood.endGame();
      for (int j = 0; j < 12; j++) {
        //get room
        Room curRoom = Deadwood.getRoom(j);

        //get room area Info
        int[] roomArea = curRoom.getArea();
        int roomX = roomArea[0];
        int roomY = roomArea[1];
        int roomH = roomArea[2];
        int roomW = roomArea[3];

        //check if within this rooms area
        if (((x >= roomX) && (x <= roomX + roomW)) && ((y >= roomY) && (y <= roomY + roomH)) && Turn.arrayContains(nieghbors, j)){
          View.removeMouseListener();
          View.setMoveButtonPressed(false);
          Turn.moveToRoom(j);
          return(true);

        }

      }
      return false;
    }

    public static void findRank(int x, int y){

      Room trailer = Deadwood.getRoom(Deadwood.getPlayer(Deadwood.getPindex()).getLocation());
      int[] trailerArea = trailer.getArea();
      int trailerX = trailerArea[0];
      int trailerY = trailerArea[1];
      int trailerH = trailerArea[2];
      int trailerW = trailerArea[3];


      Rank[] ranks = Board.getRank();
      for (int j = 0; j < ranks.length; j++) {
        //get room
        Rank curRank = ranks[j];

        //get room area Info
        int[] roomArea = curRank.getArea();
        int rankX = roomArea[0];
        int rankY = roomArea[1];
        int rankH = roomArea[2];
        int rankW = roomArea[3];

        // System.out.println("SetArea :" + trailerX + " " + trailerY + " " +trailerW + " " + trailerH);
        // System.out.println("RoleArea : " + rankX + " " + rankY + " " +rankW + " " + rankH);
        // System.out.println("me: " + x + " "+ y);
        // System.out.println("rank: " + curRank.getNumber() + " " + curRank.getType());
        //check if within this rooms area
        if (((x >= rankX) && (x <= rankX + rankW)) && ((y >= rankY) && (y <= rankY + rankH))){
          String type = curRank.getType();
          int rankNum = curRank.getNumber();
          View.removeMouseListener();
          View.setUpgradeButtonPressed(false);
          Turn.upgrade(type, rankNum);

        }

      }
    }

  public static Room getRoom(int i) { return rooms[i]; }

  public static Player getPlayer(int i) { return players[i]; }

  public static Scene getScene(int i) { return scenes[i]; }


  public static int getPindex(){ return pIndex;}
}
