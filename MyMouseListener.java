import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Class to implement the mouse listener
//used for when an area in the board.jpg is clicked
//needs to be compiled separately

public class MyMouseListener implements MouseListener{


  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    //figure out which button was clicked
    if (View.getMoveButton()){
      if(Deadwood.findLocation(x,y)){
        View.placePlayerIcon(x,y, Deadwood.getPindex());
      }
    }
    else if(View.getRoleButton()){

      Boolean found = Deadwood.findRole(x,y);
      if (found == false){
        Deadwood.findSceneRole(x,y);
      }
    }
    else if(View.getUpgradeButton()){
       Deadwood.findRank(x,y);
    }
  }
  public void mouseEntered(MouseEvent e) {
  }
  public void mouseExited(MouseEvent e) {
  }
  public void mousePressed(MouseEvent e) {
  }
  public void mouseReleased(MouseEvent e) {
  }
}
