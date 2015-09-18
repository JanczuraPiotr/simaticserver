package pjpl.s7.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	public void dump(MemByteClip memClip) {
		try {

			outputD.reset();
			outputI.reset();
			outputQ.reset();

			outputD.write(memClip.buffD, 0, memClip.buffD.length);
			outputI.write(memClip.buffI, 0, memClip.buffI.length);
			outputQ.write(memClip.buffQ, 0, memClip.buffQ.length);
			
			fileName =  dirDump + "/" + fileNameFormat.format(memClip.timeStamp);

			fos = new FileOutputStream( new File( fileName + ".D" ));
			outputD.writeTo(fos);
			fos.close();
			fos = new FileOutputStream( new File( fileName + ".I" ));
			outputI.writeTo(fos);
			fos.close();
			fos = new FileOutputStream( new File( fileName + ".Q" ));
			outputQ.writeTo(fos);
			fos.close();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(DumpExpertBinFile.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(DumpExpertBinFile.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//------------------------------------------------------------------------------
	// atrybuty prywatne

	private String fileName;
	private FileOutputStream fos;
	private final String dirDump;
	private final DateFormat fileNameFormat;
	private final ByteArrayOutputStream outputD = new ByteArrayOutputStream();
	private final ByteArrayOutputStream outputI = new ByteArrayOutputStream();
	private final ByteArrayOutputStream outputQ = new ByteArrayOutputStream();

}
