package src.chat.application;

import java.util.*;
import java.text.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.net.*;

public class Client implements ActionListener {
    
    JTextField text;
    static JPanel p2;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    
    Client()
    {
        f.setLayout(null);
        f.setIconImage(new ImageIcon("Icons/Chat Application Icon.png").getImage());
        
        JPanel p1 = new JPanel();
        p1.setBackground(new Color(91, 73, 248));
        p1.setBounds(new Rectangle(0, 0, 450, 60));
        p1.setLayout(null);
        
        JLabel userPic = new JLabel(new ImageIcon(new ImageIcon("Icons/User 2.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        userPic.setBounds(40, 10, 40, 40);
        p1.add(userPic);
        
        JLabel back = new JLabel(new ImageIcon(new ImageIcon("Icons/Back Arrow.png").getImage().getScaledInstance(20, 30, Image.SCALE_DEFAULT)));
        back.setBounds(10, 15, 20, 20);
        p1.add(back);
        
        back.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e)
            {
                System.exit(0);
            }
        });
        
        JLabel userName = new JLabel("USER 2");
        userName.setBounds(110, 10, 100, 20);
        p1.add(userName);
        
        JLabel status = new JLabel("Active now");
        status.setBounds(110, 10, 100, 55);
        p1.add(status);
        
        f.add(p1);
        
        p2 = new JPanel();
        p2.setBounds(5, 65, 440, 575);
        f.add(p2);
        
        text = new JTextField();
        text.setBounds(5, 650, 340, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);
        
        JButton send = new JButton("Send");
        send.setBounds(355, 649, 90, 40);
        send.setBackground(Color.blue);
        send.setForeground(Color.WHITE);
        send.addActionListener(this);
        f.add(send);
        
        f.setSize(450, 700);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(new Color(179, 194, 206));
        
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String out = text.getText();

            JPanel p3 = formatLabel(out);

            p2.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p3, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            p2.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);
            
            text.setText("");
            f.repaint();
            f.invalidate();
            f.validate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static JPanel formatLabel(String out)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        
        panel.add(output);
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        panel.add(time);
        
        return panel;
    }
    
    public static void main(String[] args)
    {
        new Client();
        
        try
        {
            Socket s = new Socket("127.0.0.1", 6001);
            DataInputStream din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            while(true)
            {
                p2.setLayout(new BorderLayout());
                String msg = din.readUTF();
                JPanel panel = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                
                vertical.add(Box.createVerticalStrut(15));
                p2.add(vertical, BorderLayout.PAGE_START);
                
                f.validate();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
