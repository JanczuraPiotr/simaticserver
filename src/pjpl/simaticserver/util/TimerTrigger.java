
package pjpl.simaticserver.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import pjpl.simaticserver.run.SimaticServer;

/**
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class TimerTrigger {
	public static long time_interval = Long.parseLong(SimaticServer.config.getProperty("time_interval"));
	private TimerTask timerTask;
	private Timer timer;
	private long time;
	private DateFormat format_date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:S");

	public TimerTrigger(){
		long currentTime = System.currentTimeMillis();
		long ms = currentTime % time_interval;
		long s = currentTime / time_interval;

		timer = new Timer();
		timerTask = new TimerTask() {
			private long currentTime = System.currentTimeMillis();
			private long prevTime = currentTime;
			private long ms = currentTime % time_interval;
			private long s = currentTime / time_interval;

			@Override
			public void run() {

				// Odpalanie wątków procesów

				currentTime = System.currentTimeMillis();
				ms = currentTime % time_interval;
				s = currentTime / time_interval;

				System.out.println(prevTime+" --- "+format_date.format(prevTime));
				System.out.println(currentTime+" --- "+format_date.format(currentTime));
				System.out.println("s = "+ ( s ) );
				System.out.println("ms = "+ ( ms ) );

				if( ( currentTime - time_interval ) != prevTime){
					System.out.println("Przesunięcie = "+ ( currentTime - prevTime) );
				}
				prevTime = currentTime;

			}

		};

		currentTime = System.currentTimeMillis();
		s = currentTime / time_interval;
		ms = currentTime % time_interval;

		timer.scheduleAtFixedRate(timerTask, new Date( ( s * 1000 ) + ( time_interval * 2 ) ) , time_interval);

	}
}
