import java.util.ArrayList;
import java.io.*;

public class ViewController{
    
    private ArrayList<Integral> integrals;
    private int activeIntegral;
    private Integral defaultIntegral;
    private boolean isDisplayingAnswer;
    
    private final int TIME_LIMIT = 180;
    
    private DisplayFrame displayFrame;
    private ControllerFrame controllerFrame;
    
    private String getFileName(int number){
        String strNum = "" + number;
        while(strNum.length() < 3){
            strNum = "0" + strNum;
        }
        return Launcher.PDF_FILENAME + "-page-" + strNum + ".png";
    }
    
    public ViewController(String filename){
        activeIntegral = -1;
        defaultIntegral = new Integral("", 0, "white.jpg", "white.jpg");
        integrals = new ArrayList<Integral>();
        isDisplayingAnswer = false;
        
        File infoFile = new File(filename);
        int i=0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader( infoFile ));
            while( reader.ready() ){
                integrals.add(new Integral(reader.readLine(),
                    Integer.parseInt(reader.readLine()), getFileName(i+1), getFileName(i+2)));
                i += 2;
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
    
    public Integral getActiveIntegral(){
        return (activeIntegral == -1) ? defaultIntegral : integrals.get(activeIntegral);
    }
    
    public ArrayList<Integral> getAllIntegrals(){
        return integrals;
    }
    
    public void setActiveIntegral(int val){
        activeIntegral = val;
        isDisplayingAnswer = false;
        displayFrame.displayIntegral();
    }
    public void toggleAnswer(){
        isDisplayingAnswer = !isDisplayingAnswer;
        displayFrame.repaint();
    }
    public boolean getDisplayingAnswer(){
        return isDisplayingAnswer;
    }
    public void updateScoreBoard(String player1, String score1, String player2, String score2, String player3, String score3, String player4, String score4){
        displayFrame.updateScoreBoard(player1, score1, player2, score2, player3, score3, player4, score4);
    }
    
    public void setDisplayFrame(DisplayFrame frame){
        displayFrame = frame;
    }
    public void setControllerFrame(ControllerFrame frame){
        controllerFrame = frame;
    }
}