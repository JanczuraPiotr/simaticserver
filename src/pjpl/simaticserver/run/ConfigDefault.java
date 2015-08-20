package pjpl.simaticserver.run;

import java.util.Properties;

/**
 *
 * @author piotr
 */
public class ConfigDefault extends Properties{

	public ConfigDefault(){
		setProperty("dateMSFormat","YYYY-MM-dd HH:mm:ss:SSS");
		setProperty("dateFormat","YYYY-MM-dd HH:mm:ss");
		setProperty("datePackedFormat","YYYYMMddHHmmss");
		setProperty("dateMSPackedFormat","YYYYMMddHHmmssSSS");
		setProperty("timeInterval", "1000");
	}
}
