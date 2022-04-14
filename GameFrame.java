import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{
    private static int WIDTH;
    private static int HEIGHT;
    public GameFrame(String framename) {
        super(framename);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//comment

        WIDTH = (int) screenSize.getWidth();
        HEIGHT = (int) screenSize.getHeight();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        CatanPanel cp = new CatanPanel();
        add(cp);
        setVisible(true);


    }

}
