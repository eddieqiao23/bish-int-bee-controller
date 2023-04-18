import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.awt.image.*;

public class Integral{
    private String name;
    private BufferedImage integralImage;
    private BufferedImage answerImage;
    private int timeLimit;
    
    public Integral(String name, int timeLimit, String integralFileName, String answerFileName){
        this.name = name;
        this.timeLimit = timeLimit;
        
        try{
            integralImage = ImageIO.read(new File("images/" + integralFileName));
            answerImage = ImageIO.read(new File("images/" + answerFileName));
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
    
    public String getName(){
        return name;
    }
    public BufferedImage getIntegralImage(){
        return integralImage;
    }
    public BufferedImage getAnswerImage(){
        return answerImage;
    }
    public int getTimeLimit(){
        return timeLimit;
    }
}