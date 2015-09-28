package pjpl.s7.util;

import java.net.URLConnection;
import pjpl.s7.run.SimaticServer;

/**
 * Wysyła do servera web zrzuty pamięci procesora.
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class SimaticWeb implements Runnable{

	@Override
	public void run() {

	}

	/**
	 * Inicjuje połączenie z serwerem Web.
	 */
	private void initConnection(){

	}
	/**
	 * Zapisywanie informacji o pracy wątku.
	 */
	private void log(){

	}

	private URLConnection urlConection;
	private String url = SimaticServer.config.getProperty("url_simaticweb");
	private String user = SimaticServer.config.getProperty("user");
	private String password = SimaticServer.config.getProperty("password");
}
