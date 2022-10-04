import java.util.*;
import javax.swing.JLabel;

public class Shot {

  private int number = 0;
  private int x = 0;
  private int y = 0;
  private int h = 0;
  private int w = 0;
  private boolean visible = true;
  private int markerIndex;

  // constructor
  public Shot(int number, int x, int y, int h, int w){
    this.number = number;
    this.x = x;
    this.y = y;
    this.h = h;
    this.w = w;
  }

  public int getNumber() {
    return this.number;
  }

  public int[] getArea() {
    int[] area = {this.x, this.y,this.h, this.w};
    return area;

}
public void setLabel( int marker){
  this.markerIndex = marker;
}
public int getLabel(){
  return this.markerIndex;
}

}
