package pjpl.simaticserver.util;

import Moka7.S7;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import pjpl.simaticserver.device.BramaDump;
import pjpl.simaticserver.run.SimaticServer;

/**
 * Zapisywanie stanu procesora do pliku binarnego.
 * Nazwa pliku składa się z pieczęci czasu a rozszerzeniem jest symbol bloku i nazwa urzadzenia:
 * 20150824180056113.pa.brama
 * 20150824180056113.pe.brama
 * lub plik będzie przechowywał całą zawrtość procesora
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class FileBinLogger implements Runnable{

	public FileBinLogger(LinkedBlockingQueue<BramaDump> queue, pjpl.simaticserver.process.Brama parent){
		this.queue = queue;
		this.parent = parent;
	}
	@Override
	public void run() {
		while(true){
			summaryRun = "------------------------------------------------------------------------------\n";
			summaryRun += format_date.format(System.currentTimeMillis()) + " FileBinLogger.run() Start czyli czakanie na kolejkę\n";
			try {

				bramaDump = queue.take();
				outputDB = bramaDump.bin(S7.S7AreaDB);
				outputPA = bramaDump.bin(S7.S7AreaPA);
				outputPE = bramaDump.bin(S7.S7AreaPE);
				fileName =  dir_dump + "/" + dateFileNameFormat.format(bramaDump.getTimeStamp());

				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileBinLogger.run() Po queue.take()\n";

				fos = new FileOutputStream( new File( fileName + ".db."+bramaDump.getDivaceName() ));
				outputDB.writeTo(fos);
				fos.close();
				fos = new FileOutputStream( new File( fileName + ".pa."+bramaDump.getDivaceName() ));
				outputPA.writeTo(fos);
				fos.close();
				fos = new FileOutputStream( new File( fileName + ".pe."+bramaDump.getDivaceName() ));
				outputPE.writeTo(fos);
				fos.close();


				summaryRun += format_date.format(System.currentTimeMillis())+" FileBinLogger.run() utworzono plik : "+dateFileNameFormat.format(bramaDump.getTimeStamp())+".pdu\n";
				summaryRun += format_date.format( timeStop = System.currentTimeMillis() )+" FileBinLogger.run() Stop praca = "+ (timeStop-timeStart)+"[ms]\n";

			} catch (InterruptedException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
			}
			System.out.println(summaryRun);
		}
	}

	//------------------------------------------------------------------------------
	private BramaDump bramaDump;
	private String fileName;
	private FileOutputStream fos;
	private final String dir_dump = SimaticServer.config.getProperty("dir_dump");
	private final DateFormat format_date = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
	private final DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_datePackedMS"));
	private final LinkedBlockingQueue<BramaDump> queue;
	private volatile long timeStart;
	private volatile long timeStop;
	private String summaryRun = new String();
	private ByteArrayOutputStream outputDB;
	private ByteArrayOutputStream outputPA;
	private ByteArrayOutputStream outputPE;
	private final pjpl.simaticserver.process.Brama parent;

}
