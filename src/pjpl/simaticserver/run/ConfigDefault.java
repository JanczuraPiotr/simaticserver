package pjpl.simaticserver.run;

import java.util.Properties;

/**
 *
 * @author piotr
 */
public class ConfigDefault extends Properties{

	public ConfigDefault(){
		// katalogi
		setProperty("dirWork"              , "d:\\tmp\\simaticserver\\");
		setProperty("dirLog"               , "d:\\tmp\\simaticserver\\log\\");
		setProperty("dirDump"              , "d:\\tmp\\simaticserver\\dump\\");

		// format czasu
		setProperty("dateMSFormat"         , "YYYY-MM-dd HH:mm:ss:SSS");
		setProperty("dateFormat"           , "YYYY-MM-dd HH:mm:ss");
		setProperty("datePackedFormat"     , "YYYYMMddHHmmss");
		setProperty("dateMSPackedFormat"   , "YYYYMMddHHmmssSSS");
		setProperty("fileNameDateFormat"   , "YYYYMMddHHmmssSSS");

		// czasy
		setProperty("timeInterval"         , "1000"); // Czas w ms pomiędzy wywołaniami pętli "sterowania"
		setProperty("timeStorage"          , "3000"); // Czas w ms !! przechowywania plików zanim zostaną usunięte
	}
}
