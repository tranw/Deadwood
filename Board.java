import java.util.Scanner;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.io.File;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.Collections;

public class Board {

  private static final int TRAIN_STATION = 0;
  private static final int SECRET_HIDEOUT = 1;
  private static final int CHURCH = 2;
  private static final int HOTEL = 3;
  private static final int MAIN_STREET = 4;
  private static final int JAIL = 5;
  private static final int GENERAL_STORE = 6;
  private static final int RANCH = 7;
  private static final int BANK = 8;
  private static final int SALOON = 9;
  private static final int TRAILER = 10;
  private static final int OFFICE = 11;

  private static Room[] rooms = new Room[12];
  private static Scene[] scenes = new Scene[40];
  private static Rank[] ranks = null;

  //construct board object
  private static Board instance = new Board();
  private Board() {}
      //defeat instantiation
  public static Board getInstance( ) {
      return instance;
  }

  /*
  buildBoard
  -------------------------------------------------
  -Gets called by main
  -Opens board XML file (hardcoded file name)
  -Initializes room array, and set array
  */
  public static Room[] buildBoard() {
    File boardFile = new File("board.xml");
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = null;
    Document document = null;

    int board_load_index = 0;

    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(boardFile);
      NodeList setList  = document.getElementsByTagName("set");
      int[] neighbors = null;
      Role[] roles = null;
      Shot[] shots = null;
      int x = 0;
      int y = 0;
      int h = 0;
      int w = 0;

      //loops for each set in the xml file
      for (int temp = 0; temp < setList.getLength(); temp++) {
        //Get informtion of Set attribute
        Node setNode = setList.item(temp).getAttributes().item(0);
        String setName = setNode.getNodeValue();
        //System.out.printf("\n%d setlist: %s %s\n", temp, setNode.getNodeName(), setNode.getNodeValue());
        int numMarkers = 0;

        NodeList childList = setList.item(temp).getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
          Node childNode = childList.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE) {
            //System.out.println("child name: " + childNode.getNodeName());
            if (childNode.getNodeName().equals("neighbors")) {
              // get children of <neighbors>
              NodeList neighborList = childList.item(i).getChildNodes();
              neighbors = new int[neighborList.getLength()/2];
              int counter = 0;
              for (int j = 0; j < neighborList.getLength(); j++) {
                Node nNode = neighborList.item(j);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                  //System.out.println("node name: " + nNode.getAttributes().item(0).getNodeValue());
                  int cur_neighbor_index = findNeighbor(nNode);
                  neighbors[counter] = cur_neighbor_index;
                  counter++;
                }
              }

            }else if (childNode.getNodeName().equals("parts")) {
              // get children of <parts>
              NodeList roleList = childList.item(i).getChildNodes();
              roles = new Role[roleList.getLength()/2];
              int counter = 0;
              for (int j = 0; j < roleList.getLength(); j++) {
                Node rNode = roleList.item(j);
                if (rNode.getNodeType() == Node.ELEMENT_NODE) {
                  String rank = rNode.getAttributes().item(0).getNodeValue();
                  String name = rNode.getAttributes().item(1).getNodeValue();
                  String description = rNode.getTextContent().trim();
                  //System.out.print("Role Name: " + name + " Rank: " + rank +  " description: " + description);

                  Node mNodeList = rNode.getChildNodes().item(1);
                  if ((mNodeList.getNodeType() == Node.ELEMENT_NODE) && (mNodeList.getNodeName().equals("area"))) {
                    int partsY = Integer.parseInt(mNodeList.getAttributes().item(3).getNodeValue());
                    int partsX = Integer.parseInt(mNodeList.getAttributes().item(2).getNodeValue());
                    int partsW = Integer.parseInt(mNodeList.getAttributes().item(1).getNodeValue());
                    int partsH = Integer.parseInt(mNodeList.getAttributes().item(0).getNodeValue());
                    //System.out.println("parts area: " + partsX +" "+ partsY +" "+ partsH +" "+ partsW +" ");
                    roles[counter] = new Role(Integer.parseInt(rank), name, description, false, partsX, partsY, partsH, partsW);
                    counter++;
                  }
                }
              }
            }else if (childNode.getNodeName().equals("takes")) {
              // get children of <neighbors>
              NodeList markersList = childList.item(i).getChildNodes();
              //System.out.println("length of ml: " + markersList.getLength());
              shots = new Shot[markersList.getLength()/2];
              for (int j = 0; j < markersList.getLength(); j++) {
                Node mNode = markersList.item(j);
                if (mNode.getNodeType() == Node.ELEMENT_NODE) {
                  Node mNodeList = mNode.getChildNodes().item(0);
                  int takeY = Integer.parseInt(mNodeList.getAttributes().item(3).getNodeValue());
                  int takeX = Integer.parseInt(mNodeList.getAttributes().item(2).getNodeValue());
                  int takeW = Integer.parseInt(mNodeList.getAttributes().item(1).getNodeValue());
                  int takeH = Integer.parseInt(mNodeList.getAttributes().item(0).getNodeValue());
                  int shotNum = Integer.parseInt(mNode.getAttributes().item(0).getNodeValue());
                  //System.out.println("take area: "+ shotNum +" " + takeX +" "+ takeY +" "+ takeW +" "+ takeH +" ");
                  shots[numMarkers] = new Shot(shotNum, takeX, takeY, takeH, takeW);
                  numMarkers ++;
                }
              }
            }
            else if (childNode.getNodeName().equals("area")){
              //System.out.Println("area found");
              y = Integer.parseInt(childNode.getAttributes().item(3).getNodeValue());
              x = Integer.parseInt(childNode.getAttributes().item(2).getNodeValue());
              w = Integer.parseInt(childNode.getAttributes().item(1).getNodeValue());
              h = Integer.parseInt(childNode.getAttributes().item(0).getNodeValue());
              //System.out.println("set area: "+ x +" "+ y +" "+ h +" "+ w +" ");
            }
          }
        }
        Set s = new Set(setName, neighbors, shots, numMarkers, roles, x, y, h, w);
        //System.out.println("new Set Created- name: " + setName+ " neighbors: " + Arrays.toString(x.getAllNeighbors()) + " markers: " +numMarkers+ " roles: " + Arrays.toString(x.getRoles()));
        rooms[board_load_index] = s;
        board_load_index++;
      }

      // create trailer ---------------------------------------------------------------
      NodeList ntrailer= document.getElementsByTagName("trailer");
      int[] t_neighbors = null;

      NodeList t_childList = ntrailer.item(0).getChildNodes();
      // get neighbors
      for (int i = 0; i < t_childList.getLength(); i++) {
        Node t_childnode = t_childList.item(i);
        if (t_childnode.getNodeType() == Node.ELEMENT_NODE) {
          //System.out.println("child name: " + childNode.getNodeName());
          if (t_childnode.getNodeName().equals("neighbors")) {
            // get children of <neighbors>
            NodeList t_neighborList = t_childList.item(i).getChildNodes();
            t_neighbors = new int[t_neighborList.getLength()/2];
            int counter = 0;
            // neighbors
            for (int j = 0; j < t_neighborList.getLength(); j++) {
              Node t_nNode = t_neighborList.item(j);
              if (t_nNode.getNodeType() == Node.ELEMENT_NODE) {
                //findNeighbor gets one of the set neighbors and returns the index
                int cur_neighbor_index = findNeighbor(t_nNode);
                t_neighbors[counter] = cur_neighbor_index;
                counter++;
              }
            }
          }
          else if (t_childnode.getNodeName().equals("area")){
            //System.out.Println("area found");
            y = Integer.parseInt(t_childnode.getAttributes().item(3).getNodeValue());
            x = Integer.parseInt(t_childnode.getAttributes().item(2).getNodeValue());
            w = Integer.parseInt(t_childnode.getAttributes().item(1).getNodeValue());
            h = Integer.parseInt(t_childnode.getAttributes().item(0).getNodeValue());
            //System.out.println("trailer area: "+ x +" "+ y +" "+ h +" "+ w +" ");
          }
        }
      }
      Room trailer = new Room("Trailer", t_neighbors, x, y, h, w);
      //System.out.println("new Set Created- name: " + trailer.getName() + " neighbors: " + Arrays.toString(trailer.getAllNeighbors()));
      rooms[board_load_index] = trailer;
      board_load_index++;

      // create office -------------------------------------------------------------------
      NodeList noffice= document.getElementsByTagName("office");
      int[] o_neighbors = null;


      NodeList o_childList = noffice.item(0).getChildNodes();
      //System.out.println(o_childList.getLength());
      // get neighbors
      for (int i = 0; i < o_childList.getLength(); i++) {
        Node o_childnode = o_childList.item(i);
        if (o_childnode.getNodeType() == Node.ELEMENT_NODE) {
          //System.out.println("child name: " + childNode.getNodeName());
          if (o_childnode.getNodeName().equals("neighbors")) {
            // get children of <neighbors>
            NodeList o_neighborList = o_childList.item(i).getChildNodes();
            o_neighbors = new int[o_neighborList.getLength()/2];
            int counter = 0;
            // neighbors
            for (int j = 0; j < o_neighborList.getLength(); j++) {
              Node o_nNode = o_neighborList.item(j);
              if (o_nNode.getNodeType() == Node.ELEMENT_NODE) {
                // System.out.println("Neighbor name: " + o_nNode.getAttributes().item(0).getNodeValue());

                int cur_neighbor_index = findNeighbor(o_nNode);
                o_neighbors[counter] = cur_neighbor_index;

                counter++;
              }
            }
          }
          //gets the upgrade info but doesn't store it
          //information will necessary to get the area for assignment 3
          else if (o_childnode.getNodeName().equals("upgrades")){

            NodeList upgradeList = o_childList.item(i).getChildNodes();
            ranks = new Rank[upgradeList.getLength()/2];
            int rankCount = 0;
            // neighbors
            for (int j = 0; j < upgradeList.getLength(); j++) {
              Node upgradeNode = upgradeList.item(j);
              if (upgradeNode.getNodeType() == Node.ELEMENT_NODE) {
                String amount = upgradeNode.getAttributes().item(0).getNodeValue();
                String currencyType = upgradeNode.getAttributes().item(1).getNodeValue();
                String thisRank = upgradeNode.getAttributes().item(2).getNodeValue();
                 //System.out.println("Upgrade rank: " + thisRank + " currency type: " + currencyType +  " amount: " + amount);
                Node mNodeList = upgradeNode.getChildNodes().item(1);
                if ((mNodeList.getNodeType() == Node.ELEMENT_NODE) && (mNodeList.getNodeName().equals("area"))) {
                  int upY = Integer.parseInt(mNodeList.getAttributes().item(3).getNodeValue());
                  int upX = Integer.parseInt(mNodeList.getAttributes().item(2).getNodeValue());
                  int upW = Integer.parseInt(mNodeList.getAttributes().item(1).getNodeValue());
                  int upH = Integer.parseInt(mNodeList.getAttributes().item(0).getNodeValue());
                  //System.out.println("upgrade area: " + upX +" "+ upY +" "+ upH +" "+ upW +" ");
                  ranks[rankCount] = new Rank(Integer.parseInt(thisRank), upX, upY, upH, upW, currencyType, Integer.parseInt(amount));
                  rankCount ++;
                }
              }
            }
          }
          else if (o_childnode.getNodeName().equals("area")){

            y = Integer.parseInt(o_childnode.getAttributes().item(3).getNodeValue());
            x = Integer.parseInt(o_childnode.getAttributes().item(2).getNodeValue());
            w = Integer.parseInt(o_childnode.getAttributes().item(1).getNodeValue());
            h = Integer.parseInt(o_childnode.getAttributes().item(0).getNodeValue());
            //System.out.println("office area: "+ x +" "+ y +" "+ h +" "+ w +" ");
          }
        }
      }
      Room office = new Room("Office", o_neighbors, x, y, h, w, ranks);
      rooms[board_load_index] = office;
      board_load_index++;
      //System.out.println("new Set Createrivaterivate
    }catch (Exception e){
      System.out.printf("Error in parsing file %s\n", boardFile);
      System.out.println("Error"+ e);
      System.exit(0);
    }
    return rooms;
  }


  /*
  findNeighbor
  -------------------------------------------------
  - Gets called by buildBoard
  - Eliminates code repitition when searching thru a XML file
  - Find all of the tags listed as <neighbor>
  */
  private static int findNeighbor(Node node){
    int cur_neighbor_index = -1;
    String cur_neighbor = node.getAttributes().item(0).getNodeValue();

    switch(cur_neighbor) {
      case "Train Station":
      cur_neighbor_index = TRAIN_STATION;
      break;
      case "Secret Hideout":
      cur_neighbor_index = SECRET_HIDEOUT;
      break;
      case "Church":
      cur_neighbor_index = CHURCH;
      break;
      case "Hotel":
      cur_neighbor_index = HOTEL;
      break;
      case "Main Street":
      cur_neighbor_index = MAIN_STREET;
      break;
      case "Jail":
      cur_neighbor_index = JAIL;
      break;
      case "General Store":
      cur_neighbor_index = GENERAL_STORE;
      break;
      case "Ranch":
      cur_neighbor_index = RANCH;
      break;
      case "Bank":
      cur_neighbor_index = BANK;
      break;
      case "Saloon":
      cur_neighbor_index = SALOON;
      break;
      case "trailer":
      cur_neighbor_index = TRAILER;
      break;
      case "office":
      cur_neighbor_index = OFFICE;
      break;
    }
    return cur_neighbor_index;
  }

  /*
  buildScenes
  -------------------------------------------------
  - Gets called by main
  - Opens XML file (hardcoded file name)
  - Initializes the scene cards and all roles assocaiated
  */
  public static Scene[] buildScenes() {
    File boardFile = new File("cards.xml");
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = null;
    Document document = null;
    int board_load_index = 0;

    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(boardFile);
      NodeList cardList  = document.getElementsByTagName("card");
      Role[] roles = null;

      int k = 1;
      for (int temp = 0; temp < cardList.getLength(); temp++){

        String sceneName = cardList.item(temp).getAttributes().item(2).getNodeValue();
        String cardIMG = cardList.item(temp).getAttributes().item(1).getNodeValue();
        String sceneBudget = cardList.item(temp).getAttributes().item(0).getNodeValue();

        NodeList childList = cardList.item(temp).getChildNodes();
        int roleCounter = 0;
        String sceneDescription = "";
        String sceneNumber = "";
        roles = new Role[(childList.getLength()-2)/2];
        for (int i = 0; i < childList.getLength(); i++) {
          Node childNode = childList.item(i);
          if (childNode.getNodeType() == Node.ELEMENT_NODE) {

            if (childNode.getNodeName().equals("scene")) {
              sceneDescription = childNode.getTextContent().trim().replaceAll("[\n\r]", "").replaceAll("     ", "");
              sceneNumber = childNode.getAttributes().item(0).getNodeValue();


            }else if (childNode.getNodeName().equals("part")) {
              String roleName = childNode.getAttributes().item(1).getNodeValue();
              String roleRank = childNode.getAttributes().item(0).getNodeValue();
              String roleDescription = childNode.getTextContent().trim().replaceAll("[\n\r]", "");

              Node mNodeList = childNode.getChildNodes().item(1);
              if ((mNodeList.getNodeType() == Node.ELEMENT_NODE) && (mNodeList.getNodeName().equals("area"))) {
                int partsY = Integer.parseInt(mNodeList.getAttributes().item(3).getNodeValue());
                int partsX = Integer.parseInt(mNodeList.getAttributes().item(2).getNodeValue());
                int partsW = Integer.parseInt(mNodeList.getAttributes().item(1).getNodeValue());
                int partsH = Integer.parseInt(mNodeList.getAttributes().item(0).getNodeValue());
                //System.out.println("scene parts area: " + partsX +" "+ partsY +" "+ partsH +" "+ partsW +" ");
                roles[roleCounter] = new Role(Integer.parseInt(roleRank), roleName, roleDescription, false, partsX, partsY, partsH, partsW);
                roleCounter++;
              }
            }
          }
        }
        //System.out.println(Arrays.toString(roles));
        scenes[temp] = new Scene(sceneName, Integer.parseInt(sceneBudget), roles, sceneDescription, k);
        //System.out.println(Arrays.toString(scenes[temp].getRoles()));
        k++;

      }
    }catch(Exception e){
      System.out.println("Error in reading in the scene");

    }
    return scenes;
  }

  public static Rank[] getRank(){
    return ranks;
  }

}
