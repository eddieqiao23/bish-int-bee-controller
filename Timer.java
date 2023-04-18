

public class Timer implements Runnable{
    private DisplayFrame frame;
    private long timeStamp;
    
    private final int SECOND = 1000000000;
    
    public Timer(DisplayFrame frame){
        this.frame = frame;
        timeStamp = System.nanoTime()-1;
        Thread t = new Thread(this);
        t.start();
    }
    
    public void startTimer(int seconds){
        timeStamp = 1 + System.nanoTime() + SECOND * ((long)seconds);
    }
    
    public void run(){
        while(true){
            if(System.nanoTime() < timeStamp + SECOND){
                long currentTimeLeft = (timeStamp - System.nanoTime() + SECOND-1) / SECOND;
                if(currentTimeLeft < frame.getTimeLeft()){
                    frame.decreaseTimer();
                }
            }
            try{
                Thread.sleep(15);
            }
            catch(Exception e){
                return;
            }
        }
    }
}