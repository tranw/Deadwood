
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ButtonListeners {

  public ButtonListeners(){

  }
  public class MoveButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      View.addMouseListener();
      View.setMoveButtonPressed(true);
      View.setUpgradeButton(false);
    }
  }

  public class ActButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      Turn.act();
    }
  }

  public class RehearseButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      Turn.rehearse();
    }
  }

  public class UpgradeButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      View.setUpgradeButtonPressed(true);
      View.addMouseListener();
    }
  }

  public class TakeRoleButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      //Turn.takeRole();
      View.setRoleButtonPressed(true);
      View.addMouseListener();
    }
  }

  public class DoNothingButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      Turn.done = true;
      Turn.choice = 6;
      Deadwood.runGame();
    }
  }

  public class ExitButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      System.exit(0);
    }
  }

  public class TwoButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      Deadwood.initPlayers(2);
      View.mainFrame.dispose();
    }
  }

  public class ThreeButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      Deadwood.initPlayers(3);
      View.mainFrame.dispose();
    }
  }

  public class FourButtonActionListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e){
      Deadwood.initPlayers(4);
      View.mainFrame.dispose();
    }
  }
}
