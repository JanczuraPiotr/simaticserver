package pjpl.s7.util;

/**
 * Klasa Zosta�a rozbudowana by umo�liwi� podawanie jesdnostki zmiennych opisuj�cych czas.
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class Properties extends java.util.Properties{
	public Properties(){
		super();
	}
	public Properties(pjpl.s7.run.ConfigDefault config){
		super(config);
	}
	public String getProperty(String property){
		String valueStr = super.getProperty(property);
		long valueLong = 0;

		if( property.startsWith("time")){
			if(valueStr.endsWith("d")){
				valueStr = valueStr.replace("d", "");
				valueLong = Long.parseLong(valueStr);
				valueLong *= 24 * 60 * 60 * 1000;
				valueStr = Long.toString(valueLong);
			}else if(valueStr.endsWith("h")){
				valueStr = valueStr.replace("h", "");
				valueLong = Long.parseLong(valueStr);
				valueLong *=  60 * 60 * 1000;
				valueStr = Long.toString(valueLong);
			}else if(valueStr.endsWith("m")){
				valueStr = valueStr.replace("m", "");
				valueLong = Long.parseLong(valueStr);
				valueLong *= 60 * 1000;
				valueStr = Long.toString(valueLong);
			}else if(valueStr.endsWith("s")){
				valueStr = valueStr.replace("s", "");
				valueLong = Long.parseLong(valueStr);
				valueLong *= 1000;
				valueStr = Long.toString(valueLong);
			}
		}

		return valueStr;
	}
}
