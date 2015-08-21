package pjpl.simaticserver.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import pjpl.simaticserver.run.SimaticServer;
import static pjpl.simaticserver.run.SimaticServer.config;

/**
 * Kasuje pliki, których nazwa składa się z daty w formacie YYYYMMddHHmmssSSS i "jakiegoś" rozszerzenia.
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class DirCleaning implements Runnable{
	public DirCleaning(File dir, DateFormat fileNameFormat, long fileAge_ms){
		this.dir = dir;
		this.fileNameFormat = fileNameFormat;
		this.fileAge_ms = fileAge_ms;
	}
	public DirCleaning(String dir, String fileNameFormat, String fileAge_ms){
		this.dir = new File(dir);
		this.fileNameFormat = new SimpleDateFormat(fileNameFormat);
		this.fileAge_ms = Long.parseLong(fileAge_ms);
	}

	@Override
	public void run() {
		System.out.println( dateFormat.format(System.currentTimeMillis())+" DirCleaning.run() Start czyszczenia katalogu");

		long now_ms = System.currentTimeMillis();
		long limes_ms = ( ( now_ms - fileAge_ms ) / fileAge_ms ) * fileAge_ms ;
				// Pliki które powstały przed limes_ms należy usuwać
		String limes_str = fileNameDateFormat.format(limes_ms);
				// string do porównywania z nazwami plików
		String plik_str = "1440177900000";

		System.out.println("--- now_ms     : " + now_ms );
		System.out.println("--- fileAge_ms : " + fileAge_ms + " = "+ ( fileAge_ms / 1000 ) + " [sek]");
		System.out.println("--- Limes      : " + limes_ms );
		System.out.println("--- now_ms                           : " + dateFormat.format(now_ms));
		System.out.println("--- Należy usunąć pliki starsze niż  : " + dateFormat.format(limes_ms));
		System.out.println("--- Data pliku                       : " + dateFormat.format(Long.parseLong(plik_str)));

		if( limes_str.compareTo(plik_str) > 0 ){
			// Ten warunek wykona się gdy plik_str wskazuje na starszą datę niż data wyliczona jako limes_str
		}

	}

	private void listDir(){

	}

	// Format czasu przy pomocy którego utworzono nazwę pliku.
	private DateFormat fileNameFormat;
	// Katalog z którego należy usunąć pliki.
	private File dir;
	// Wiek plików w ms
	// Starsze pliki należy usunąć.
	private long fileAge_ms;

	/** @prace */ private final DateFormat dateFormat = new SimpleDateFormat(SimaticServer.config.getProperty("dateMSFormat"));
	/** @prace */ private final DateFormat fileNameDateFormat = new SimpleDateFormat(SimaticServer.config.getProperty("fileNameDateFormat"));
}
