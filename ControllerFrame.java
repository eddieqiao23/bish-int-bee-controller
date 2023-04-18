import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class ControllerFrame extends JFrame implements ListSelectionListener, DocumentListener{
    private JPanel mainPanel;
    private ViewController controller;
    
    private JList<String> integralList;
    private ImagePanel integralPanel;
    private AnswerPanel answerPanel;
    private JLabel activeIntegralLabel;

    private BufferedImage blackImage;
    
    private JTextField player1Text, player2Text, player3Text, player4Text, score1Text, score2Text, score3Text, score4Text;
    
    private boolean isAnswerHidden = true;
    
    public ControllerFrame(ViewController controller){
        super("2023 Bishop's Integration Bee Controller");
                
        //set shorthand
        final String NORTH = SpringLayout.NORTH;
        final String SOUTH = SpringLayout.SOUTH;
        final String EAST = SpringLayout.EAST;
        final String WEST = SpringLayout.WEST;
        
        //panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        mainPanel.setLayout(layout);
        setContentPane(mainPanel);


        //toggle answer button
        JButton toggleAnsButton = new JButton("Toggle Answer");
        toggleAnsButton.addActionListener(new ToggleAnsButtonListener());
        mainPanel.add(toggleAnsButton);
        layout.putConstraint(WEST, toggleAnsButton, 25, WEST, mainPanel);
        layout.putConstraint(EAST, toggleAnsButton, 225, WEST, mainPanel);
        layout.putConstraint(NORTH, toggleAnsButton, -50, SOUTH, mainPanel);
        layout.putConstraint(SOUTH, toggleAnsButton, -10, SOUTH, mainPanel);
        
        //clear button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonListener());
        mainPanel.add(clearButton);
        layout.putConstraint(WEST, clearButton, 0, WEST, toggleAnsButton);
        layout.putConstraint(EAST, clearButton, 0, EAST, toggleAnsButton);
        layout.putConstraint(NORTH, clearButton, -50, NORTH, toggleAnsButton);
        layout.putConstraint(SOUTH, clearButton, -10, NORTH, toggleAnsButton);
        
        //display button
        JButton displayButton = new JButton("Display");
        displayButton.addActionListener(new DisplayButtonListener());
        mainPanel.add(displayButton);
        layout.putConstraint(WEST, displayButton, 0, WEST, clearButton);
        layout.putConstraint(EAST, displayButton, 0, EAST, clearButton);
        layout.putConstraint(NORTH, displayButton, -50, NORTH, clearButton);
        layout.putConstraint(SOUTH, displayButton, -10, NORTH, clearButton);
        
        //integral list
        DefaultListModel<String> integralNames = new DefaultListModel<>();
        for(Integral integral : controller.getAllIntegrals()){
            integralNames.addElement(integral.getName());
        }
        integralList = new JList(integralNames);
        integralList.setFont(new Font("System", Font.PLAIN, 16));
        integralList.addListSelectionListener(this);
        JScrollPane integralScrollPane = new JScrollPane(integralList);
        mainPanel.add(integralScrollPane);
        layout.putConstraint(WEST, integralScrollPane, 10, WEST, mainPanel);
        layout.putConstraint(EAST, integralScrollPane, 10, EAST, displayButton);
        layout.putConstraint(NORTH, integralScrollPane, 10, NORTH, mainPanel);
        layout.putConstraint(SOUTH, integralScrollPane, -20, NORTH, displayButton);
        
        //scoreboard
        JLabel player1Label = new JLabel("Player 1 Name");
        JLabel player2Label = new JLabel("Player 2 Name");
        JLabel player3Label = new JLabel("Player 3 Name");
        JLabel player4Label = new JLabel("Player 4 Name");
        JLabel score1Label = new JLabel("Score 1");
        JLabel score2Label = new JLabel("Score 2");
        JLabel score3Label = new JLabel("Score 3");
        JLabel score4Label = new JLabel("Score 4");
        player1Text = new JTextField("");
        player2Text = new JTextField("");
        player3Text = new JTextField("");
        player4Text = new JTextField("");
        score1Text = new JTextField("0");
        score2Text = new JTextField("0");
        score3Text = new JTextField("0");
        score4Text = new JTextField("0");
        
        player1Text.getDocument().addDocumentListener(this);
        player2Text.getDocument().addDocumentListener(this);
        player3Text.getDocument().addDocumentListener(this);
        player4Text.getDocument().addDocumentListener(this);
        score1Text.getDocument().addDocumentListener(this);
        score2Text.getDocument().addDocumentListener(this);
        score3Text.getDocument().addDocumentListener(this);
        score4Text.getDocument().addDocumentListener(this);
        
        JComponent[] scoreboardComps = 
            {player1Label, player1Text,
             score1Label, score1Text,
             player2Label, player2Text,
             score2Label, score2Text,
             player3Label, player3Text,
             score3Label, score3Text,
             player4Label, player4Text,
             score4Label, score4Text};
        for(int i=0; i<scoreboardComps.length; ++i){
            mainPanel.add(scoreboardComps[i]);
            layout.putConstraint(WEST, scoreboardComps[i], -150, EAST, mainPanel);
            layout.putConstraint(EAST, scoreboardComps[i], -10, EAST, mainPanel);
            if(i != 0){
                layout.putConstraint(NORTH, scoreboardComps[i], 10*((i+1)%2), SOUTH, scoreboardComps[i-1]);
                //layout.putConstraint(SOUTH, scoreboardComps[i], 40, NORTH, scoreboardComps[i-1]);
            }
            else{
                layout.putConstraint(NORTH, scoreboardComps[i], 10, NORTH, mainPanel);
            }
        }
        
        
        //integral panel
        integralPanel = new ImagePanel();
        mainPanel.add(integralPanel);
        layout.putConstraint(WEST, integralPanel, 10, EAST, integralScrollPane);
        layout.putConstraint(EAST, integralPanel, -10, WEST, player1Label);
        layout.putConstraint(NORTH, integralPanel, 10, NORTH, mainPanel);
        layout.putConstraint(SOUTH, integralPanel, 250, NORTH, integralPanel);

        
        //answer panel
        answerPanel = new AnswerPanel();
        mainPanel.add(answerPanel);
        answerPanel.addMouseListener(new ShowHideAnswerListener(answerPanel));
        layout.putConstraint(WEST, answerPanel, 10, EAST, integralScrollPane);
        layout.putConstraint(EAST, answerPanel, -10, WEST, player1Label);
        layout.putConstraint(NORTH, answerPanel, 10, SOUTH, integralPanel);
        layout.putConstraint(SOUTH, answerPanel, 250, NORTH, answerPanel);
        //get black image ready
        try{
            blackImage = ImageIO.read(new File("images/black.jpg"));
        }
        catch (IOException e){
            System.out.println(e);
        }

        //active integral label
        activeIntegralLabel = new JLabel("Currently Displaying: None");
        activeIntegralLabel.setFont(new Font("System", Font.PLAIN, 16));
        mainPanel.add(activeIntegralLabel);
        layout.putConstraint(WEST, activeIntegralLabel, 10, EAST, integralScrollPane);
        layout.putConstraint(EAST, activeIntegralLabel, -10, EAST, mainPanel);
        layout.putConstraint(NORTH, activeIntegralLabel, 10, SOUTH, answerPanel);        
        
        
        //final setup
        setSize(new Dimension(1000,600));
        setVisible(true);
        this.controller = controller;
        controller.setControllerFrame(this);
    }
    
    public void valueChanged(ListSelectionEvent e){
        Integral currentIntegral = controller.getAllIntegrals()
            .get(integralList.getSelectedIndex());
        integralPanel.setImage(currentIntegral.getIntegralImage());
        answerPanel.setImage(currentIntegral.getAnswerImage());
    }
    
    // CHANGE
    public void updateScoreBoard(){
        controller.updateScoreBoard(
            player1Text.getText(),
            score1Text.getText(),
            player2Text.getText(),
            score2Text.getText(),
            player3Text.getText(),
            score3Text.getText(),
            player4Text.getText(),
            score4Text.getText()
        );
    }
    public void insertUpdate(DocumentEvent e) {
        updateScoreBoard();
    }
    public void removeUpdate(DocumentEvent e) {
        updateScoreBoard();
    }
    public void changedUpdate(DocumentEvent e) {
        //Plain text components do not fire these events
    }
    
    private class DisplayButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            controller.setActiveIntegral(integralList.getSelectedIndex());
            activeIntegralLabel.setText("Currently Displaying: " 
                + controller.getActiveIntegral().getName());
        }
    }
    private class ClearButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            controller.setActiveIntegral(-1);
            activeIntegralLabel.setText("Currently Displaying: " 
                + controller.getActiveIntegral().getName());
        }
    }
    private class ToggleAnsButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            controller.toggleAnswer();
        }
    }
    
    private class ImagePanel extends JPanel{
        protected BufferedImage img;
        public void paintComponent(Graphics g){
            //setSize(new Dimension(getWidth(), (int)(getWidth()*Launcher.IMG_RATIO)));
            g.drawImage(img, 0, (getHeight()-getPreferredHeight())/2,
                getWidth(), getPreferredHeight(), null);
        }
        public void setImage(BufferedImage img){
            this.img = img;
            repaint();
        }
        public int getPreferredHeight(){
            return (int)(getWidth() * Launcher.IMG_RATIO);
        }
    }
    
    private class AnswerPanel extends ImagePanel{
        public boolean isAnswerHidden = true;
        public void paintComponent(Graphics g){
            //setSize(new Dimension(getWidth(), (int)(getWidth()*Launcher.IMG_RATIO)));
            g.drawImage(isAnswerHidden ? blackImage : img,
                0, (getHeight()-getPreferredHeight())/2,
                getWidth(), getPreferredHeight(), null);
        }
    }
    
    private class ShowHideAnswerListener implements MouseListener{
        private AnswerPanel parentPanel;
        public ShowHideAnswerListener(AnswerPanel panel){
            parentPanel = panel;
        }
        public void mousePressed(MouseEvent e){
            parentPanel.isAnswerHidden = !parentPanel.isAnswerHidden;
            parentPanel.repaint();
        }
        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){} 
        public void mouseExited(MouseEvent e){}
        public void mouseReleased(MouseEvent e){} 
    }
}
