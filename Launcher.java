import javax.swing.*;
import java.awt.*;

public class Launcher{
    
    public static final double IMG_RATIO = 0.35;
    public static final String INFO_FILENAME = "integrals_info.txt";
    public static final String PDF_FILENAME = "integrals";
    public static final String TITLE = "Bishop's Integration Bee";
    public static final String FONTNAME = "Latin Modern Sans";
    public static final Color HMMT_COLOR = new Color(111,41,38);
    
    public static void main(String[] args){
        JFrame loadingFrame = new JFrame();
        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Loading Integrals");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        loadingFrame.add(label);
        loadingFrame.setSize(new Dimension(200,120));
        loadingFrame.setVisible(true);

        ViewController controller = new ViewController(INFO_FILENAME);
        DisplayFrame displayFrame = new DisplayFrame(controller);
        ControllerFrame controllerFrame = new ControllerFrame(controller);
        
        loadingFrame.setVisible(false);
    }
}