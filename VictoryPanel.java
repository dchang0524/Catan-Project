import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VictoryPanel extends JFrame{
    private static int WIDTH = 1900;
    private static int HEIGHT = 1000;
    PlayerManager pManage;
    BufferedImage endBackground;


    public VictoryPanel(String s) {
        Container window = getContentPane();
        window.setLayout(new FlowLayout());
        window.setBackground(Color.GRAY);
        window.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        window.setVisible(true);
        window.setFocusable(true);
        window.requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JLabel label = new JLabel("Congratulations " + s + "!" + " You have won the game!");
        label.setFont(new Font("Arial", Font.BOLD, 75));
        label.setForeground(Color.BLUE);
        window.add(label);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Victory");
    }


}

