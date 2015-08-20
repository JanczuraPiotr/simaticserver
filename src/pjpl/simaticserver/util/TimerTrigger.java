
package pjpl.simaticserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class TimerTrigger {
	public static long timeInterval = 1000;
	private TimerTask timerTask;
	private Timer timer;
	private long time;
	private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:S");

	public TimerTrigger(){
		long currentTime = System.currentTimeMillis();
		long ms = currentTime % timeInterval;
		long s = currentTime / timeInterval;

		timer = new Timer();
		timerTask = new TimerTask() {
			private long currentTime = System.currentTimeMillis();
			private long prevTime = currentTime;
			private long ms = currentTime % timeInterval;
			private long s = currentTime / timeInterval;

			@Override
			public void run() {

				// Odpalanie wątków procesów

				currentTime = System.currentTimeMillis();
				ms = currentTime % timeInterval;
				s = currentTime / timeInterval;

				System.out.println(prevTime+" --- "+dateFormat.format(prevTime));
				System.out.println(currentTime+" --- "+dateFormat.format(currentTime));
				System.out.println("s = "+ ( s ) );
				System.out.println("ms = "+ ( ms ) );

				if( ( currentTime - timeInterval ) != prevTime){
					System.out.println("Przesunięcie = "+ ( currentTime - prevTime) );
				}
				prevTime = currentTime;

			}

		};

		currentTime = System.currentTimeMillis();
		s = currentTime / timeInterval;
		ms = currentTime % timeInterval;

		timer.scheduleAtFixedRate(timerTask, new Date( ( s * 1000 ) + ( timeInterval * 2 ) ) , timeInterval);

	}
}
