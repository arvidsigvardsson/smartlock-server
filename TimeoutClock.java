/**
 * Håller koll på när en notis ska sändas ut till app:en om att dörren varit
 * öppen för länge. Servern tar emot ett meddelande från Arduinot i klassen
 * LockHandler där servern får info om dörren är öppen eller stängd, och där
 * finns en tillståndsmaskin som hanterar dörrstatus, som använder denna klass
 * för timeoutfunktion
 * 
 * @author Arvid Sigvardsson och Sebastian Sologuren
 *
 */
public class TimeoutClock {
	private long startTime;
	private int timeLimit;

	/**
	 * Konstruerar en TimeoutClock med angiven timeLimit i sekunder
	 * 
	 * @param tiden
	 *            i sekunder dörren ska ha stått öppen
	 */
	public TimeoutClock(int timeLimit) {
		this.timeLimit = timeLimit;
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * Returnerar inställda tiden för när en timeout push notis ska göras
	 * 
	 * @return timeLimit tiden i sekunder dörren ska ha stått öppen
	 */
	public int getTimeLimit() {
		return this.timeLimit;
	}
	/**
	 * Sätter timeout tiden för hur länge dörren ska ha varit öppen innan en
	 * push notis skickas ut.
	 * 
	 * @param limit tiden i sekunder dörren ska ha stått öppen
	 */
	public void setTimeLimit(int limit) {
		timeLimit = limit;
	}

	/**
	 * Nollställer räknaren. Används då dörren är stängd.
	 */
	public void reset() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * Tar reda på om dörren varit öppen i för länge.
	 * 
	 * @return boolean Om timeLimiti tiden uppnåtts eller passerats
	 */
	public boolean isTimeUp() {
		return (System.currentTimeMillis() - startTime >= timeLimit * 1000);
	}

	/**
	 * Returnerar den nuvarande tiden på räknaren sedan startTime där startTime
	 * är tiden från det att en pushNotis om att dörren varit öppen länge, sänts
	 * ut.
	 * 
	 * @return den nuvarande tiden på räknaren sedan pushNotisen sändes
	 */
	public int timeElapsed() {
		return ((int) (System.currentTimeMillis() - startTime)) / 1000;
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