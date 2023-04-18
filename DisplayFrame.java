import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class DisplayFrame extends JFrame{
    
    private DisplayPanel mainPanel;
    private ViewController controller;
    private Timer timer;
    
    private int timeLeft;
    
    private String player1, player2, player3, player4, score1, score2, score3, score4;
    
    public DisplayFrame(ViewController controller){        
        super("2023 Bishop's Integration Bee");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new DisplayPanel();
        setContentPane(mainPanel);
        setSize(new Dimension(800,600));
        setVisible(true);
        
        this.controller = controller;
        controller.setDisplayFrame(this);
        
        player1 = "";
        player2 = "";
        player3 = "";
        player4 = "";
        score1 = "0";
        score2 = "0";
        score3 = "0";
        score4 = "0";
        
        timer = new Timer(this);
    }
    
    public void decreaseTimer(){
        if(timeLeft > 0) timeLeft--;
        repaint();
    }
    
    public int getTimeLeft(){
        return timeLeft;
    }
    
    public void displayIntegral(){
        repaint();
        timeLeft = controller.getActiveIntegral().getTimeLimit();
        timer.startTimer(controller.getActiveIntegral().getTimeLimit());
    }

    public void updateScoreBoard(String player1, String score1, String player2, String score2, String player3, String score3, String player4, String score4){
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        this.score4 = score4;
        repaint();
    }
    
    public class DisplayPanel extends JPanel{
            
        public DisplayPanel(){}
        
        private String timeToDisplay(int time){
            int min = time / 60;
            int sec = time % 60;
            String strMin = "" + min;
            String strSec = "" + sec;
            if(sec < 10) strSec = "0" + strSec;
            return strMin + ":" + strSec;
        }
        public void paintComponent(Graphics g){
            int rectHeight = Math.max(getHeight()/9, 70);
            int imgHeight = (int)(getWidth()*Launcher.IMG_RATIO);
            int remainHeight = getHeight() - rectHeight - imgHeight;
            
            //draw title bar
            g.setColor(Launcher.HMMT_COLOR);
            g.fillRect(0,0, getWidth(),rectHeight);
            g.setFont(new Font(Launcher.FONTNAME, Font.PLAIN, rectHeight/2));
            g.setColor(new Color(255,255,255));
            g.drawString(Launcher.TITLE,rectHeight/6, 2*rectHeight/3);
            
            //draw image
            Integral current = controller.getActiveIntegral();
            BufferedImage img;
            if(controller.getDisplayingAnswer())
                img = current.getAnswerImage();
            else 
                img = current.getIntegralImage();
            g.drawImage(img, 0, rectHeight, getWidth(),imgHeight, null);
            
            g.setColor(new Color(0,0,0));
            
            //draw timer
            int timerFontSize = Math.max(remainHeight/3, 72);
            Font textFont = new Font(Launcher.FONTNAME, Font.PLAIN, timerFontSize/3);
            g.setFont(textFont);
            FontMetrics fm = g.getFontMetrics(textFont);
            
            g.drawString("Time Left",
                (getWidth() - fm.stringWidth("Time Left"))/2,
                getHeight() - 4*remainHeight/5);
            g.setFont(new Font("Menlo", Font.PLAIN, timerFontSize));
            g.drawString(timeToDisplay(timeLeft), getWidth()/2 - timerFontSize*5/4,
                getHeight() - remainHeight/3);
                
            //draw scoreboard
            int scoreFontSize = timerFontSize/2;
            textFont = new Font(Launcher.FONTNAME, Font.PLAIN, scoreFontSize);
            g.setFont(textFont);
            fm = g.getFontMetrics(textFont);
            
            int scoreHeight = scoreFontSize + 25;
            // int scoreTop = getHeight() - remainHeight/3 + 5 - scoreHeight;
            int scoreTop1 = getHeight() - remainHeight/2 + 5 - scoreHeight;
            int scoreTop2 = getHeight() - remainHeight/6 + 5 - scoreHeight;
            int scoreWidth = Math.min(getWidth()/3, getWidth()/2 - timerFontSize*2);
            int textPos1 = scoreTop1 + scoreHeight - 17;
            int textPos2 = scoreTop2 + scoreHeight - 17;
            
            g.setColor(Launcher.HMMT_COLOR);
            g.fillRect(0, scoreTop1, scoreWidth, scoreHeight);
            g.fillRect(0, scoreTop2, scoreWidth, scoreHeight);
            g.fillRect(getWidth() - scoreWidth, scoreTop1, scoreWidth, scoreHeight);
            g.fillRect(getWidth() - scoreWidth, scoreTop2, scoreWidth, scoreHeight);
            
            g.setColor(new Color(255, 255, 255));
            g.drawString(player1, 15, textPos1);
            g.drawString(player2, getWidth() - 15 - fm.stringWidth(player2), textPos1);
            g.drawString(player3, 15, textPos2);
            g.drawString(player4, getWidth() - 15 - fm.stringWidth(player2), textPos2);
            
            g.drawString(score1, scoreWidth - 15 - fm.stringWidth(score1), textPos1);
            g.drawString(score2, getWidth() - scoreWidth + 15, textPos1);
            g.drawString(score3, scoreWidth - 15 - fm.stringWidth(score1), textPos2);
            g.drawString(score4, getWidth() - scoreWidth + 15, textPos2);
        }
    }
}