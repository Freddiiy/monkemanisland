package com.groupc.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button extends JFrame implements ActionListener
{
 JButton button;
public Button()
{
 JButton button = new JButton();
 button.setBounds(200,100,100,50);
 button.addActionListener(this);
 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 this.setLayout(null);
 this.setSize(500,500);
 this.setVisible(true);
 this.add(button);
}

public  void startGameB()
{
 JButton button = new JButton();
 button.setBounds(300,200,100,50);
 button.addActionListener(this);

 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 this.setLayout(null);
 this.setSize(500,500);
 this.setVisible(true);
 this.add(button);

}


 @Override
 public void actionPerformed(ActionEvent e) {
  if(e.getSource() == button)
   System.out.println("i made a button");
  {
 }
}
}
