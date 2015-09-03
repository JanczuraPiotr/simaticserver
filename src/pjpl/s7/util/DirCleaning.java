package pjpl.s7.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import pjpl.s7.run.SimaticServer;

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
		runStart  = System.currentTimeMillis();
		summaryRun = "------------------------------------------------------------------------------\n";
		summaryRun += dateFormat.format(runStart) + " DirCleaning.run() Start";
		summaryRun += "\n";

		long now_ms = System.currentTimeMillis();
		long limes_ms = ( ( now_ms - fileAge_ms ) / fileAge_ms ) * fileAge_ms ;
		String limes_str = format_fileName.format(limes_ms);
		deleteObsolete(listObsolete(limes_str));
		runStop = System.currentTimeMillis();
		summaryRun += dateFormat.format(runStop) + " DirCleaning.run() Stop praca = "+ (runStop - runStart) + "[ms]" ;
		summaryRun += "\n";
		if(summaryShow){
//			System.out.println(summaryRun);
			summaryShow = false;
		}
	}

	private void deleteObsolete(ArrayList<String> listObsolete){
		File fileToDelete;
		for(String file : listObsolete){
			fileToDelete = new File(dir+"/"+file);
			summaryRun += "delete file : " + fileToDelete.toString() + "\n";
			fileToDelete.delete();
		}
	}
	private ArrayList<String> listObsolete(String limesDeleteFile){
		String[] allFiles;
		ArrayList<String> toDeleteFiles = new ArrayList<>();

		allFiles = dir.list();
		for( String file : allFiles){
			if( limesDeleteFile.compareTo(file) > 0 ){
				toDeleteFiles.add(file);
			}

		}
		if( ! toDeleteFiles.isEmpty()){
			summaryShow = true;
		}
		return toDeleteFiles;
	}

	//------------------------------------------------------------------------------
	private long runStart;
	private long runStop;
	private String summaryRun;
	private boolean summaryShow = false;
	// Format czasu przy pomocy którego utworzono nazwę pliku.
	private DateFormat fileNameFormat;
	// Katalog z którego należy usunąć pliki.
	private File dir;
	// Wiek plików w ms. Starsze pliki należy usunąć.
	private long fileAge_ms;

	private final DateFormat format_fileName = new SimpleDateFormat(SimaticServer.config.getProperty("format_fileName"));
	private final DateFormat dateFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
}
