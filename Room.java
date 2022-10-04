import java.util.*;

public class Room {

  private String name = null;
  private LinkedList<Player> playersInRoom = new LinkedList<Player>();
  private int[] neighbors = null;
  private int x = 0;
  private int y = 0;
  private int h = 0;
  private int w = 0;
  private Rank[] ranks = null;

  // constructor
  public Room(String n, int[] neigh, int x, int y, int h, int w){
    this.name = n;
    this.neighbors = neigh;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
  }
  public Room(String n, int[] neigh, int x, int y, int h, int w, Rank[] ranks){
    this.name = n;
    this.neighbors = neigh;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
    this.ranks = ranks;
  }
  public Room(String n, int[] neigh){
    this.name = n;
    this.neighbors = neigh;
  }

  public String getName(){ return this.name; }

  public int[] getAllNeighbors() { return this.neighbors; }

  public int[] getArea() {
    int[] area = {this.x, this.y, this.h, this.w};
    return area;
  }

  public LinkedList<Player> getAllPlayers() { return this.playersInRoom; }

  public void addPlayer(int p){
    this.playersInRoom.add(Deadwood.getPlayer(p));
  }

  public void removePlayers() {
    playersInRoom = new LinkedList<Player>();
  }

}
