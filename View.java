
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class View extends JFrame{
  private static JButton moveButton;
  private static JButton roleButton;
  private static JButton actButton;
  private static JButton rehearseButton;
  private static JButton upgradeButton;
  private static JButton nothingButton;
  private static JButton exitButton;

  private static Boolean moveButtonPressed = false;
  private static Boolean roleButtonPressed = false;
  private static Boolean actButtonPressed = false;
  private static Boolean rehearseButtonPressed = false;
  private static Boolean upgradeButtonPressed = false;
  private static Boolean nothingButtonPressed = false;

  private static JLabel picLabel;
  private static JLabel message;
  private static JLabel rank;
  private static JLabel dollars;
  private static JLabel fame;
  private static JLabel title;

  private static MyMouseListener listener;
  private static JLayeredPane boardLayers;
  private static JLabel[] markers = new JLabel[22];
  private static JLabel[] scenesArray = new JLabel[10];
  public static JFrame mainFrame = new JFrame();
  public static JLabel[] sceneArray = new JLabel[40];

  private static JPanel dicePanel;
  private static JPanel gamePanel;
  private static JLabel diceLabel;

  private static ArrayList<JPanel> players = new ArrayList<JPanel>();

  public static void createGUI() {


    try{
      JFrame window = new JFrame();
      boardLayers = new JLayeredPane();

      BufferedImage myPicture = ImageIO.read(new File("board.jpg"));
      picLabel = new JLabel(new ImageIcon(myPicture));
      picLabel.setBounds(0,0, myPicture.getWidth(), myPicture.getHeight());
      boardLayers.add(picLabel, new Integer(1));

      JPanel buttonPanel = createButtonPanel();
      buttonPanel.setBounds(200, myPicture.getHeight()+100, buttonPanel.getWidth(), buttonPanel.getHeight());

      JPanel combinedPanel = combinedPanel();
      combinedPanel.setBounds(myPicture.getWidth()+80, 20, combinedPanel.getWidth(), myPicture.getHeight());

      gamePanel = new JPanel();
      gamePanel.setPreferredSize(new Dimension(1800,1800));

      gamePanel.setLayout(null);
      boardLayers.setBounds(20, 20, myPicture.getWidth(), myPicture.getHeight());
      dicePanel =  createDicePanel();
      dicePanel.setBounds(myPicture.getWidth()+145, myPicture.getHeight()-215, dicePanel.getWidth(),dicePanel.getHeight());

      gamePanel.add(boardLayers);
      gamePanel.add(buttonPanel);
      gamePanel.add(dicePanel);
      gamePanel.add(combinedPanel);



      window.add(gamePanel);

      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.pack();
      window.setLocationRelativeTo(null);
      window.setVisible(true);

    }catch(Exception e){
      System.out.println("no");
    }
  }

  public static void upgradePlayerIcon(int playerIndex, int rank){
    String color = "b";
    if (playerIndex == 1){ color = "c";
    }else if (playerIndex == 2){ color = "v";
    }else if (playerIndex == 3){ color = "y"; }
    String fileName =  "dice/" + color + rank + ".png";
    JPanel player = players.get(playerIndex);
    try{
    BufferedImage die = ImageIO.read(new File(fileName));
    player.remove((JLabel)player.getComponent(0));
    JLabel playerIcon = new JLabel(new ImageIcon(die));
    player.add(playerIcon);

    }catch(Exception e){
    System.out.println("Unable to find image dice/" +  color + rank + ".png");
    }

  }

  public static JPanel createPlayerIcon(String color){
    JPanel playerIconPanel = null;

    String fileName =  "dice/" + color +  "1.png";
    try{
      BufferedImage die = ImageIO.read(new File(fileName));
      JLabel playerIcon =  new JLabel(new ImageIcon(die));
      playerIcon.setPreferredSize(new Dimension(die.getHeight(), die.getWidth()));
      playerIconPanel =  new JPanel(new FlowLayout(FlowLayout.CENTER));
      playerIconPanel.setSize(die.getHeight(), die.getWidth());
      playerIconPanel.setPreferredSize(new Dimension(die.getHeight(), die.getWidth()));
      playerIconPanel.setOpaque(false);
      playerIconPanel.add(playerIcon);
    }catch(Exception e){
      System.out.println("Unable to find image dice/" +  color + "1.png");
    }
    return playerIconPanel;
  }

  public static void placePlayers(int numPlayers){

    for (int i = 0; i < numPlayers; i++){
      // System.out.println("i : "+ i );
      String color = "b";
      int[] trailerArea = Deadwood.getRoom(10).getArea();
      // System.out.println("Trailer: "+ Deadwood.getRoom(10).getName() + Arrays.toString(trailerArea));
      int startX = trailerArea[0];
      int startY = trailerArea[1];
      if (i == 1){ color = "c"; startX += trailerArea[2]-40;
      }else if (i == 2){ color = "v"; startX = trailerArea[0]; startY = trailerArea[3] + trailerArea[1]-40;
      }else if (i == 3){ color = "y"; startX = trailerArea[2] + trailerArea[0]-40; startY = trailerArea[1] + trailerArea[3]-40; }
      JPanel playerIcon = createPlayerIcon(color);

      playerIcon.setBounds(startX, startY, 50, 50);

      players.add(playerIcon);
      boardLayers.add(playerIcon, new Integer(5));
    }
  }

  public static void resetPlayerIcons(){
    for (int i = 0; i < players.size(); i++){

      int[] trailerArea = Deadwood.getRoom(10).getArea();
      int startX = trailerArea[0];
      int startY = trailerArea[1];
      if (i == 1){ startX += trailerArea[2]-40;
      }else if (i == 2){ startX = trailerArea[0]; startY = trailerArea[3] + trailerArea[1]-40;
      }else if (i == 3){ startX = trailerArea[2] + trailerArea[0]-40; startY = trailerArea[1] + trailerArea[3]-40; }
      placePlayerIcon(startX, startY, i);
    }
  }

  public static void placePlayerIcon(int x, int y, int playerIndex){
    JPanel player = players.get(playerIndex);
    //gets the image within the players panel
    JLabel playerImage = (JLabel)player.getComponent(0);
    // System.out.println("playerImage: "+ playerImage.getHeight() + " " + playerImage.getWidth());
    player.setBounds(x,y, playerImage.getWidth()+10, playerImage.getHeight()+10);
  }

    public static JPanel createDicePanel(){
      dicePanel =  new JPanel(new FlowLayout(FlowLayout.CENTER));
      dicePanel.setBackground(new Color(172,104,58));
      dicePanel.setPreferredSize(new Dimension(200,200));
      dicePanel.setSize(170, 170);
      JLabel diceMessage = new JLabel();
      diceMessage.setText("Your dice roll was:");
      diceMessage.setSize(150, 20);
      diceMessage.setPreferredSize(new Dimension(130, 60));
      dicePanel.add(diceMessage);
      //updateDicePanel(1);
      return dicePanel;
    }

    public static void resetDicePanel(){
      dicePanel.removeAll();
      JLabel diceMessage = new JLabel();
      diceMessage.setText("Your dice roll is:");
      diceMessage.setSize(150, 20);
      diceMessage.setPreferredSize(new Dimension(130, 60));
      dicePanel.add(diceMessage);
    }

    public static void updateDicePanel(int roll){
      String value = roll + "";
      String die =  "dice/p" + value +  ".png";
      try{
        BufferedImage dice = ImageIO.read(new File(die));
        diceLabel =  new JLabel(new ImageIcon(dice));
        diceLabel.setSize(40,40);
        dicePanel.add(diceLabel);
      }catch(Exception e){
        System.out.println("Unable to find image dice/p" +  value + ".png");
      }
    }


  public static void getPlayers(){
    mainFrame =  new JFrame("player Number");
    JPanel panel = new JPanel();
    JLabel temp =  new JLabel("How many Players");
    mainFrame.setVisible(true);
    mainFrame.setSize(400, 400);

    JButton two =  new JButton("2");
    JButton three =  new JButton("3");
    JButton  four =  new JButton("4");

    ButtonListeners t =  new ButtonListeners();
    ButtonListeners.TwoButtonActionListener c  =  t.new TwoButtonActionListener();
    two.addActionListener(c);

    ButtonListeners th   =  new ButtonListeners();
    ButtonListeners.ThreeButtonActionListener d  =  th.new ThreeButtonActionListener();
    three.addActionListener(d);

    ButtonListeners f =  new ButtonListeners();
    ButtonListeners.FourButtonActionListener e  =  f.new FourButtonActionListener();
    four.addActionListener(e);

    mainFrame.add(panel);
    panel.add(two);
    panel.add(three);
    panel.add(four);
  }

  public static void resetShots(){
    for(int i =0; i< markers.length; i++){
      markers[i].setVisible(true);
    }
  }

  public static void removeAScene(int roomNum){
    Set curSet = (Set)Deadwood.getRoom(roomNum);
    int sceneNum = curSet.getScene().getNumber();
    if (sceneArray[sceneNum] != null){
      sceneArray[sceneNum].setVisible(false);
      boardLayers.remove(sceneArray[sceneNum]);
    }
  }


  public static void removeScenes(){
    for(int i  =  0;   i  <  40; i++){
      boardLayers.remove(sceneArray[i]);
      sceneArray[i] = null;
    }
  }




  public static void placeShots(){

    int k = 0;
    for (int i = 0;i < 10; i++ ){
      //get set shots
      Set curSet = (Set)Deadwood.getRoom(i);
      Shot[] shots = curSet.getAllShots();
      //loop through shots

      for(int j = 0; j < shots.length; j++){

        Shot curShot = shots[j];
        int[] area = curShot.getArea();
        JLabel markerLabel = createShot();
        markerLabel.setBounds(area[0],area[1], area[2], area[3]);
        //curShot.setVisible(true);
        curShot.setLabel(k);
        boardLayers.add(markerLabel, new Integer(3));
        markers[k] = markerLabel;
        k++;
      }
    }
  }
  public static void placeSceneCards(int roomNum){
    //loop through rooms
    Set curSet = (Set)Deadwood.getRoom(roomNum);
    int[] sceneArea = curSet.getArea();
    int sceneNum = curSet.getScene().getNumber();
    JLabel sceneLabel = createScene(sceneNum);
    sceneArray[sceneNum] = sceneLabel;
    sceneLabel.setBounds(sceneArea[0],sceneArea[1], sceneArea[3], sceneArea[2]);
    boardLayers.add(sceneLabel, new Integer(4));
  }

  public static void removeShot(int roomNumber){
    Set curSet = (Set)Deadwood.getRoom(roomNumber);
    Shot[] curShots = curSet.getAllShots();
    Shot curShot = curShots[curSet.getShots() - 1];
    int index = curShot.getLabel();
    markers[index].setVisible(false);
  }

  private static JLabel createScene(int sceneNum){

    JLabel label = null;
    try{
      //boardPanel.setBorder(BorderFactory.createTitledBorder("This is the board"));
      //gets the board image
      String filename = "";
      if(sceneNum < 10){
        filename = "cards/0" + sceneNum + ".png";
      }
      else{
        filename = "cards/" + sceneNum + ".png";
      }
      BufferedImage sceneImg = ImageIO.read(new File(filename));
      // ImageIcon board = new ImageIcon(myPicture);
      //label is an image (could be a button/text too)
      label = new JLabel(new ImageIcon(sceneImg));
      label.setSize(sceneImg.getWidth(),sceneImg.getHeight());

    }catch(Exception e){
      System.out.println("Error in finding image file: " + "shot.png");
    }

    return label;

  }

  private static JLabel createShot(){

    JLabel markerLabel = null;
    try{
      BufferedImage marker = ImageIO.read(new File("shot.png"));
      // ImageIcon board = new ImageIcon(myPicture);
      //label is an image
      markerLabel = new JLabel(new ImageIcon(marker));
      markerLabel.setSize(50,50);

    }catch(Exception e){
      System.out.println("Error in finding image file: " + "shot.png");
    }
    return markerLabel;
  }

  private static JPanel createButtonPanel(){
    //boardPanel is an area inside the window (called a panel)
    //not sure what GridLayout does
    // JLayeredPane boardPane = new JLayeredPane();
    // boardPane.setPreferredSize(new Dimension(300, 310));
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setPreferredSize(new Dimension(800,100));
    buttonPanel.setSize(800,100);
    //buttonPanel.setBorder(BorderFactory.createTitledBorder("buttonPanel border"));
    //buttonPanel.setMaximumSize(buttonPanel.getPreferredSize());

    moveButton = new JButton("Move");
    moveButton.setPreferredSize(new Dimension(100,50));
    moveButton.setBackground(new Color(225,177,145));
    ButtonListeners move =  new ButtonListeners();
    ButtonListeners.MoveButtonActionListener c  =  move.new MoveButtonActionListener();
    moveButton.addActionListener(c);
    buttonPanel.add(moveButton);

    roleButton = new JButton("Take A Role");
    roleButton.setPreferredSize(new Dimension(120,50));
    roleButton.setBackground(new Color(225,177,145));
    ButtonListeners role =  new ButtonListeners();
    ButtonListeners.TakeRoleButtonActionListener d  =  role.new TakeRoleButtonActionListener();
    roleButton.addActionListener(d);
    buttonPanel.add(roleButton);

    actButton = new JButton("Act");
    actButton.setPreferredSize(new Dimension(100,50));
    actButton.setBackground(new Color(225,177,145));
    ButtonListeners act =  new ButtonListeners();
    ButtonListeners.ActButtonActionListener e  =  act.new ActButtonActionListener();
    actButton.addActionListener(e);
    buttonPanel.add(actButton);

    rehearseButton = new JButton("Rehearse");
    rehearseButton.setPreferredSize(new Dimension(110,50));
    rehearseButton.setBackground(new Color(225,177,145));
    ButtonListeners rehearse =  new ButtonListeners();
    ButtonListeners.RehearseButtonActionListener f  =  rehearse.new RehearseButtonActionListener();
    rehearseButton.addActionListener(f);
    buttonPanel.add(rehearseButton);

    upgradeButton = new JButton("Upgrade");
    upgradeButton.setPreferredSize(new Dimension(100,50));
    upgradeButton.setBackground(new Color(225,177,145));
    ButtonListeners upgrade =  new ButtonListeners();
    ButtonListeners.UpgradeButtonActionListener g  =  upgrade.new UpgradeButtonActionListener();
    upgradeButton.addActionListener(g);
    buttonPanel.add(upgradeButton);

    nothingButton = new JButton("End Turn");
    nothingButton.setPreferredSize(new Dimension(100,50));
    nothingButton.setBackground(new Color(225,177,145));
    ButtonListeners nothing =  new ButtonListeners();
    ButtonListeners.DoNothingButtonActionListener h  =  nothing.new DoNothingButtonActionListener();
    nothingButton.addActionListener(h);
    buttonPanel.add(nothingButton);

    exitButton = new JButton("Exit");
    exitButton.setPreferredSize(new Dimension(100,50));
    exitButton.setBackground(new Color(225,177,145));
    ButtonListeners exit =  new ButtonListeners();
    ButtonListeners.ExitButtonActionListener k  =  exit.new ExitButtonActionListener();
    exitButton.addActionListener(k);
    buttonPanel.add(exitButton);


    setToDefault();

    return buttonPanel;
  }

  private static JPanel createPlayerPanel(){

    JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
    playerPanel.setBackground(new Color(172,104,58));
    playerPanel.setPreferredSize(new Dimension(400,400));
    playerPanel.setSize(400,400);
    //playerPanel.setBorder(BorderFactory.createTitledBorder("playerPanel border"));

    title = new JLabel("Player Info:");
    //title.setBorder(BorderFactory.createTitledBorder("playerTitle border"));
    title.setPreferredSize(new Dimension(300, 80));
    title.setBackground(new Color(255,246,219));
    title.setOpaque(true);

    playerPanel.add(title);

    rank = new JLabel("Rank:");
    playerPanel.add(rank);

    dollars = new JLabel("Dollars:");
    playerPanel.add(dollars);

    fame = new JLabel("Fame:");
    playerPanel.add(fame);


    //playerPanel.add();

    return playerPanel;
  }

  private static JPanel createMessagePanel(){
    JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    messagePanel.setBackground(new Color(172,104,58));
    messagePanel.setPreferredSize(new Dimension(400,400));
    messagePanel.setSize(400,400);
    //messagePanel.setBorder(BorderFactory.createTitledBorder("messagePanel border"));

    JLabel title = new JLabel("Message:");
    //title.setBorder(BorderFactory.createTitledBorder("Message border"));
    title.setPreferredSize(new Dimension(300, 80));
    title.setBackground(new Color(255,246,219));
    title.setOpaque(true);
    messagePanel.add(title);

    message = new JLabel();
    message.setText("Standard Message goes here!");
    messagePanel.add(message);

    return messagePanel;
  }

  private static JPanel combinedPanel(){

    JPanel combinedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    combinedPanel.setBackground(new Color(172,104,58));
    combinedPanel.setPreferredSize(new Dimension(300,1000));
    combinedPanel.setSize(300,1000);
    //combinedPanel.setBorder(BorderFactory.createTitledBorder("Combined Panel border"));

    JPanel messagePanel = createMessagePanel();
    JPanel playerPanel = createPlayerPanel();

    combinedPanel.add(playerPanel);
    combinedPanel.add(messagePanel);
    return combinedPanel;
  }

  //pass in the message you want to be displayed in the message box
  public static void updateMessageText(String newMessage){

    message.setText(newMessage);
  }
  public static void DisplayPlayer(String player, int rankValue, int money, int stardome){

    title.setText("Player: " + player);
    rank.setText("Rank: " + rankValue);
    dollars.setText("Dollars: " + money);
    fame.setText("Fame: " + stardome);
  }
  public static void setMoveButton(boolean t){
    moveButton.setEnabled(t);
  }
  public static void setNothingButton(boolean t){
    nothingButton.setEnabled(t);
  }
  public static void setUpgradeButton(boolean t){
    upgradeButton.setEnabled(t);
  }

  public static void setRoleButton(boolean t){
    roleButton.setEnabled(t);
  }

  public static void setActButton(boolean t){
    actButton.setEnabled(t);
  }

  public static void setRehearseButton(boolean t){
    rehearseButton.setEnabled(t);
  }
  public static void setMoveButtonPressed(boolean v){
    moveButtonPressed = v;
  }

  public static void setNothingButtonPressed(boolean v){
    nothingButtonPressed = v;
  }

  public static void setUpgradeButtonPressed(boolean v){
    upgradeButtonPressed = v;
  }

  public static void setRoleButtonPressed(boolean v){
    roleButtonPressed = v;
  }

  public static void setActButtonPressed(boolean v){
    actButtonPressed = v;
  }

  public static void setRehearseButtonPressed(boolean v){
    rehearseButtonPressed = v;
  }
  public static boolean getRehearseButton(){
    return rehearseButtonPressed;
  }
  public static boolean getMoveButton(){
    return moveButtonPressed;
  }
  public static boolean getRoleButton(){
    return roleButtonPressed;
  }
  public static boolean getUpgradeButton(){
    return upgradeButtonPressed;
  }
  public static boolean getActButton(){
    return actButtonPressed;
  }

  public static void setToDefault(){
    setMoveButton(false);
    setUpgradeButton(false);
    setRoleButton(false);
    setActButton(false);
    setRehearseButton(false);
  }
  public static void setPressedToDefault(){
    setMoveButtonPressed(false);
    setUpgradeButtonPressed(false);
    setRoleButtonPressed(false);
    setActButtonPressed(false);
    setRehearseButtonPressed(false);
  }

  public static void addMouseListener() {
    listener = new MyMouseListener();
    picLabel.addMouseListener(listener);

  }

  public static void removeMouseListener() {
    picLabel.removeMouseListener(listener);
  }
}
