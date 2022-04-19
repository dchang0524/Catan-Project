import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{
    private static int WIDTH = 1900;
    private static int HEIGHT = 1000;
    public GameFrame(String framename) {
        super(framename);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        CatanPanel cp = new CatanPanel();
        add(cp);
        setVisible(true);


    }

}
