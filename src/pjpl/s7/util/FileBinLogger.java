package pjpl.s7.util;

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
import pjpl.s7.device.PlcDump;
import pjpl.s7.run.SimaticServer;

/**
 * Zapisywanie stanu procesora do pliku binarnego.
 * Nazwa pliku składa się z pieczęci czasu a rozszerzeniem jest symbol bloku i nazwa urzadzenia:
 * 20150824180056113.pa.plc
 * 20150824180056113.pe.plc
 * lub plik będzie przechowywał całą zawrtość procesora
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class FileBinLogger implements Runnable{

	public FileBinLogger(LinkedBlockingQueue<PlcDump> queue, pjpl.s7.process.Process1 parent){
		this.queue = queue;
		this.parent = parent;
		System.out.println("FileBinLogget constructor");
	}
	@Override
	public void run() {
		while(true){
			summaryRun = "------------------------------------------------------------------------------\n";
			summaryRun += format_date.format(System.currentTimeMillis()) + " FileBinLogger.run() Start czyli czakanie na kolejkę\n";
			try {

				bramaDump = queue.take();
				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileBinLogger.run() Po queue.take()\n";

				outputDB = bramaDump.bin(S7.S7AreaDB);
				outputPA = bramaDump.bin(S7.S7AreaPA);
				outputPE = bramaDump.bin(S7.S7AreaPE);
				fileName =  dir_dump + "/" + dateFileNameFormat.format(bramaDump.getTimeStamp());
				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileBinLogger.run() Po zapisie do pliku\n";

				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileBinLogger.run()  bramaDump.hex(S7.S7AreaDB) = " + bramaDump.hex(S7.S7AreaDB)+"\n";
				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileBinLogger.run()  bramaDump.hex(S7.S7AreaPA) = " + bramaDump.hex(S7.S7AreaPA)+"\n";
				summaryRun += format_date.format( timeStart = System.currentTimeMillis() )+" FileBinLogger.run()  bramaDump.hex(S7.S7AreaPE) = " + bramaDump.hex(S7.S7AreaPE)+"\n";

				fos = new FileOutputStream( new File( fileName + ".db."+bramaDump.getDeviceName() ));
				outputDB.writeTo(fos);
				fos.close();
				fos = new FileOutputStream( new File( fileName + ".pa."+bramaDump.getDeviceName() ));
				outputPA.writeTo(fos);
				fos.close();
				fos = new FileOutputStream( new File( fileName + ".pe."+bramaDump.getDeviceName() ));
				outputPE.writeTo(fos);
				fos.close();


				summaryRun += format_date.format(System.currentTimeMillis())+" FileBinLogger.run() utworzono plik : "+dateFileNameFormat.format(bramaDump.getTimeStamp())+"\n";
				summaryRun += format_date.format( timeStop = System.currentTimeMillis() )+" FileBinLogger.run() Stop praca = "+ (timeStop-timeStart)+"[ms]\n";

			} catch (InterruptedException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				System.out.println(summaryRun);
			}
		}
	}

	//------------------------------------------------------------------------------
	private PlcDump bramaDump;
	private String fileName;
	private FileOutputStream fos;
	private final String dir_dump = SimaticServer.config.getProperty("dir_dump");
	private final DateFormat format_date = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
	private final DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_datePackedMS"));
	private final LinkedBlockingQueue<PlcDump> queue;
	private volatile long timeStart;
	private volatile long timeStop;
	private String summaryRun = new String();
	private ByteArrayOutputStream outputDB;
	private ByteArrayOutputStream outputPA;
	private ByteArrayOutputStream outputPE;
	private final pjpl.s7.process.Process1 parent;

}
