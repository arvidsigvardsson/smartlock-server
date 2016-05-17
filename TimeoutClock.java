public class TimeoutClock {
    private long startTime;
    private int timeLimit; 
    
    public TimeoutClock(int timeLimit) {
        this.timeLimit = timeLimit;
        this.startTime = System.currentTimeMillis();
    }
    
    public void reset() {
        startTime = System.currentTimeMillis();
    }
    
    public boolean isTimeUp() {
        return (System.currentTimeMillis()-startTime >= timeLimit * 1000);
    }
    
    public int timeElapsed() {
        return ((int)(System.currentTimeMillis() - startTime)) / 1000;
    }
    
    public static void main(String[] args) {
        TimeoutClock clock = new TimeoutClock(2);
        clock.reset();
        
        for (int i = 0; i < 20; i++) {
            System.out.println("Har tiden gått ut? " + clock.isTimeUp() + " tid som gått är " + clock.timeElapsed());
            try {
                Thread.sleep(250);
            } catch (Exception e) {
                
            }
        }
    }
}