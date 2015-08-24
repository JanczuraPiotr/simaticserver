package pjpl.simaticserver.run;

import java.util.Properties;

/**
 * @author piotr
 */
public class ConfigDefault extends Properties{

	public ConfigDefault(){

		// Sterowniki
		setProperty("plc_brama_ip"         , "192.168.1.150");
		setProperty("plc_brama_rack"       , "0");
		setProperty("plc_brama_slot"       , "0");

		// katalogi
		setProperty("dir_work"             , "d:\\tmp\\simaticserver");
		setProperty("dir_log"              , "d:\\tmp\\simaticserver\\log");
		setProperty("dir_dump"             , "d:\\tmp\\simaticserver\\dump");

		// format daty
		setProperty("format_date"          , "YYYY-MM-dd HH:mm:ss");
		setProperty("format_dateMS"        , "YYYY-MM-dd HH:mm:ss:SSS");
		setProperty("format_datePacked"    , "YYYYMMddHHmmss");
		setProperty("format_datePackedMS"  , "YYYYMMddHHmmssSSS");
		setProperty("format_fileName"      , "YYYYMMddHHmmssSSS");

		// czasy.
		// Jeżeli string opisujący czas nie ma przyrostka to wyraża milisekundy
		// Przyrostki określające jednostkę czasu to : d - dzień, h - godzina, m - minuta, s - sekunda
		// Jeden string może zawierać jedną jednostkę czasu czyli 1d albo 2s.
		// Nie można łączyć np : 5d12h3m
		setProperty("time_interval"        , "1000"); // Czas pomiędzy wywołaniami głównej pętli ( pętli "sterowania" )
		setProperty("time_storage"         , "5s");   // Czas przechowywania plików zanim zostaną usunięte
		setProperty("time_storageClean"    , "2s");   // Czas wuruchamiania procedury czyszczenia bufora przechowującego zrzuty z PLC
	}
}
