import java.util.*;

public class Rank {

  private int number = 0;
  private int x = 0;
  private int y = 0;
  private int h = 0;
  private int w = 0;
  private String type = "";
  private int amount = 0;

  // constructor
  public Rank(int number, int x, int y, int h, int w, String type, int amount) {
    this.number = number;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
    this.type = type;
    this.amount = amount;
  }

  public int getNumber() {
    return this.number;
  }
  public String getType() {
    return this.type;
  }

  public int[] getArea() {
    int[] area = {this.x, this.y,this.h, this.w};
    return area;

}

}
