import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
public class MyFrame extends JFrame {
    public MyFrame(){
        this.setTitle("flight window");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(420,450);
        this.setVisible(true);
        ImageIcon imageIcon = new ImageIcon("./images/add.png");
        this.setIconImage(imageIcon.getImage());
        this.getContentPane().setBackground(Color.DARK_GRAY);
    }
}
