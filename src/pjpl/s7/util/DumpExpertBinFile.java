package pjpl.s7.util;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import pjpl.s7.run.SimaticServer;

/**
 * Zapisuje dane w formacie binarnym do pliku.
 */
public class DumpExpertBinFile extends DumpExpert{
	
	public DumpExpertBinFile(String dirDump, String stringFileFormat){
		this.dirDump = dirDump;
		this.fileNameFormat =  new SimpleDateFormat(stringFileFormat);
	}

	private DumpExpertBinFile(){
		this.dirDump = "";
		this.fileNameFormat = new SimpleDateFormat();
	}

	@Override
	public void dump(MemByteClip data) {

//		FileWriter writerD = new FileWriter( dirDump + "/" + dateFileNameFormat.format(parent.getMsStartTime())+".pdu");
//		writerD.write(json);
//		writerD.close();
	}

	//------------------------------------------------------------------------------
	private final String dirDump;
	private final DateFormat fileNameFormat;

}
