import java.lang.*;
import java.util.*;

public class Turn {

  private final static int JOBLESS = 0;
  private final static int OFFCARD = 1;
  private final static int ONCARD = 2;

  private final static int MOVE = 0;
  private final static int TAKEROLE = 1;
  private final static int ACT = 2;
  private final static int REHEARSE = 3;
  private final static int UPGRADE = 4;
  private final static int DO_NOTHING = 5;

  private static Player curPlayer = null;

  private static Scanner playerInput = new Scanner(System.in);
  private static int pindex;
  public static int choice = -1;
  public static boolean done = false;
  private static Thread controller = new Thread();
  /*
  startTurn
  -------------------------------------------------
  -
  */
  public static void startTurn(int index) {
    pindex = index;
    curPlayer = Deadwood.getPlayer(pindex);
    View.setToDefault();


      // options for player NOT in a role
      if (curPlayer.getStatus() == JOBLESS) {
        View.setMoveButton(true);
        if (curPlayer.getLocation() == 10) { // if player starts in trailer and chooses to move/do nothing
          View.setMoveButton(true);
          View.setNothingButton(true);
        }else if (curPlayer.getLocation() == 11) { // if player starts in office and chooses to move/upgrade/do nothing
          View.setMoveButton(true);
          View.setNothingButton(true);
          View.setUpgradeButton(true);
          View.setRoleButton(false);
        }else if ((curPlayer.getLocation() != 11 && curPlayer.getLocation() != 10) && ((Set)Deadwood.getRoom(curPlayer.getLocation())).isActive()){ // if player is in a set and chooses to move, take role, or do nothing
          View.setMoveButton(true);
          View.setNothingButton(true);
          View.setRoleButton(true);
        }

      }

      // options for player in a role
      else if( curPlayer.getStatus() != JOBLESS) {
        View.setActButton(true);
        View.setRehearseButton(true);
      }else{
        choice = -1;
      }
      if(done){
        choice = -1;
        done = false;
      }
  }

/*
displayRoles
-------------------------------------------------
- Gets all of the available roles
-
*/
public static LinkedList<Integer> displayRoles() {
  Role[] roles = ((Set)Deadwood.getRoom(curPlayer.getLocation())).getRoles();
  int len = roles.length;
  Role curRole = null;
  int i = 0;

  LinkedList<Integer> temp = new LinkedList<Integer>();

  int validRoles = 0;
  if (len == 0){
    View.updateMessageText("There are no extra roles available.");
  }else{
    for(; i < len; i++){
      curRole = roles[i];
      // prints roles that are not taken and less or equal to their rank
      if(!curRole.isTaken() && (curPlayer.getRank() >= curRole.getRank())) {
        temp.add(i);
      }
    }
  }



  roles = null;
  roles = Deadwood.getScene(curPlayer.getLocation()).getRoles();
  len = roles.length;

  if (len == 0){
  }else{

    for(int j = 0; j < len; j++){
      curRole = roles[j];
      // prints roles that are not taken and less or equal to their rank
      if(!curRole.isTaken() && (curPlayer.getRank() >= curRole.getRank())){
        temp.add(i);

      }
      i++;
    }
  }
  if(temp.isEmpty()){
    View.updateMessageText("No Avalible Roles");
  }
  return temp;
}

/*
upgrade
-------------------------------------------------
-Displays Rank options
-Gets player input
-Checks if player has sufficient funds
-Upgrades player rank
*/
public static void upgrade(String type, int number) {

  String payChoice = type;
  int rank = number;
  int costInDollars = 0;
  int costInFame = 0;
    // switch cases for which rank the player chooses
    switch (rank) {
      case 2:
      costInDollars = 4;
      costInFame = 5;
      break;
      case 3:
      costInDollars = 10;
      costInFame = 10;
      break;
      case 4:
      costInDollars = 18;
      costInFame = 15;
      break;
      case 5:
      costInDollars = 28;
      costInFame = 20;
      break;
      case 6:
      costInDollars = 40;
      costInFame = 25;
      break;
      default:
      return;
    }
    if (rank <= curPlayer.getRank()){
      View.updateMessageText("Downgrading a rank is not possible");
    }
    // Gets input from player until they have chosen valid payment option
    else if (payChoice.equals("dollar")) {
      if (curPlayer.getMoney() >= costInDollars) {
        View.removeMouseListener();
        Deadwood.getPlayer(pindex).changeMoney(-costInDollars);
        curPlayer.setRank(rank);
        View.updateMessageText("You upgraded to rank " + rank);
        View.upgradePlayerIcon(pindex, rank);
        Deadwood.runGame();
        View.setUpgradeButton(false);
      }else {
        View.updateMessageText("You do not have sufficient funds!");
      }
    }else if (payChoice.equals("credit")) {
      if (curPlayer.getFame() >= costInFame) {
        View.removeMouseListener();
        Deadwood.getPlayer(pindex).changeFame(-costInFame);
        curPlayer.setRank(rank);
        View.updateMessageText("You upgraded to rank " + rank);
        View.upgradePlayerIcon(pindex, rank);
        Deadwood.runGame();
        View.setUpgradeButton(false);
      }else {
        View.updateMessageText("You do not have sufficient funds!");
      }
    }
  }

  /*
  postMove
  -------------------------------------------------
  -is called after player has moved
  -if valid - displays upgrade or take a role option
  */
  public static void postMove() {
    choice = -1;
    done = false;
    View.setToDefault();
    View.setMoveButton(false);

    //display room name, scene name, shot markers, scene budget
    View.updateMessageText("You have moved to " + Deadwood.getRoom(curPlayer.getLocation()).getName());

    //checks if the room that the player is in is a set and is active
    if ( (curPlayer.getLocation() < 10) && ( ((Set)Deadwood.getRoom(curPlayer.getLocation())).isActive() ) ) {

      View.setRoleButton(true);
      //place a scene card
      Set curSet = (Set)Deadwood.getRoom(curPlayer.getLocation());
      int sceneNum = curSet.getScene().getNumber();
      if (View.sceneArray[sceneNum] == null){
        View.placeSceneCards(curPlayer.getLocation());
      }

    }else if(curPlayer.getLocation() == 11){
      View.setUpgradeButton(true);
    }

  }

/*
act
-------------------------------------------------
-is called after player has moved
-if valid - displays upgrade or take a role option
*/
public static void act() {

  //player rolls die
  Random r = new Random();
  int dice = r.nextInt(6) + 1;
  View.resetDicePanel();
  View.updateDicePanel(dice);
  int budget = ((Set)Deadwood.getRoom(curPlayer.getLocation())).getScene().getBudget();
  int rehearsalValue = curPlayer.getRehearsal();

  if (rehearsalValue > 0){
    View.updateMessageText("Your rehersal value is " + curPlayer.getRehearsal());
  }
  // successful roll
  if ((dice + rehearsalValue) >= budget) {
    View.removeShot(curPlayer.getLocation());
    ((Set)Deadwood.getRoom(curPlayer.getLocation())).decShots();

    // dole out off- and on-card rewards
    if (curPlayer.getStatus() == OFFCARD) {
      Deadwood.getPlayer(pindex).changeMoney(1);
      Deadwood.getPlayer(pindex).changeFame(1);
      View.updateMessageText("You recieve 1 dollar & 1 fame!");
    }else {
      Deadwood.getPlayer(pindex).changeFame(2);
      View.updateMessageText("You recieve 2 fame!");
    }

  }

  // unsuccessful roll
  else {
    if (curPlayer.getStatus() == OFFCARD) {
      Deadwood.getPlayer(pindex).changeMoney(1);
      View.updateMessageText("You recieve 1 dollar!");
    }else {
      View.updateMessageText("You were unsuccessful!");
    }
  }
  if ( (((Set)Deadwood.getRoom(curPlayer.getLocation())).getShots() == 0) && (((Set)Deadwood.getRoom(curPlayer.getLocation())).isActive() == true) ) {
    Deadwood.closeSet(curPlayer.getLocation());
  }
  Deadwood.runGame();
}

/*
rehearse
-------------------------------------------------
- increase player rehearsal value
*/
public static void rehearse() {
  if (curPlayer.getRehearsal() < 5){
    curPlayer.incRehearsal();
    int thing = curPlayer.getRehearsal();
    View.updateMessageText("Your rehearsal value is now " + thing + "!");

  }
  else{
    View.updateMessageText("You can no longer rehearse!");
  }
  Deadwood.runGame();
}

/*
moveToRoom
-------------------------------------------------
- increase player rehearsal value
*/
public static void moveToRoom(int c) {
  choice = -1;
  //updates player location
  curPlayer.setLocation(c);
  postMove();

}

/*
takeRole
-------------------------------------------------
- displays available roles
- check to see if players choice of role is valid
- updates players role and role status
*/
public static void takeSceneRole(int i){
  int choice = i;

  // get scene roles
  Role[] sceneRoles = Deadwood.getScene(curPlayer.getLocation()).getRoles();
  int sceneRolesLength = sceneRoles.length;

    if (curPlayer.getRank() >= sceneRoles[choice].getRank()){
      //role is available
      if(sceneRoles[choice].isTaken() == false){

        Set curSet = (Set)Deadwood.getRoom(Deadwood.getPlayer(Deadwood.getPindex()).getLocation());
        int setX = curSet.getArea()[0];
        int setY = curSet.getArea()[1];

        int xCoord = (sceneRoles[choice]).getArea()[0];
        int yCoord = (sceneRoles[choice]).getArea()[1];
        View.placePlayerIcon(setX+xCoord-3, setY+yCoord-3, Deadwood.getPindex());

        View.removeMouseListener();
        curPlayer.setStatus(ONCARD);
        curPlayer.setRoleIndex(choice);
        curPlayer.setRoleName(sceneRoles[choice].getName());
        View.updateMessageText("You are the " + sceneRoles[choice].getName());
        // change data for Role taken
        sceneRoles[choice].setRoleStatus(true);
        // change data for Room
        ((Set)Deadwood.getRoom(curPlayer.getLocation())).addPlayer(pindex);
        Deadwood.runGame();
      }
      else{
        View.updateMessageText("Role is already Taken");
      }
    }
    else{
      View.updateMessageText("Rank was not high enough");
    }


}

public static void takeSetRole(int i){
  int choice = i;

  // get set roles
  Role[] setRoles = ((Set)Deadwood.getRoom(curPlayer.getLocation())).getRoles();
  int setRolesLength = setRoles.length;



  if (curPlayer.getRank() >= setRoles[choice].getRank()){
    //role is available
    if(setRoles[choice].isTaken() == false){

      int xCoord = (setRoles[choice]).getArea()[0];
      int yCoord = (setRoles[choice]).getArea()[1];
      View.placePlayerIcon(xCoord-2, yCoord-2, Deadwood.getPindex());

      View.removeMouseListener();
      curPlayer.setStatus(OFFCARD);
      curPlayer.setRoleIndex(choice);
      curPlayer.setRoleName(setRoles[choice].getName());
      View.updateMessageText("You are the " + setRoles[choice].getName());
      // change data for Role taken
      setRoles[choice].setRoleStatus(true);
      // change data for Room
      ((Set)Deadwood.getRoom(curPlayer.getLocation())).addPlayer(pindex);
      Deadwood.runGame();
    }
    else{
      View.updateMessageText("Role " + setRoles[choice].getName() +" is already Taken");
    }
  }
    else{
      View.updateMessageText("Your rank is not high enough");
    }
}


public static boolean arrayContains(int[] array, int i){

  for(int j = 0; j < array.length; j++){
    if(array[j] == i){
      return true;
    }
  }

  return false;
}






}
