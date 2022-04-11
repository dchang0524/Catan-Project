import javax.swing.*;
public class GameFrame extends JFrame{
    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1000;
    public GameFrame(String framename) {
        super(framename);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        CatanPanel cp = new CatanPanel();
        add(cp);
        setVisible(true);
    }

}
