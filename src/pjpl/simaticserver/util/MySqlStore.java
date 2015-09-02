package pjpl.simaticserver.util;

import Moka7.S7;
import com.mysql.jdbc.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;
import javax.naming.NamingException;
import pjpl.simaticserver.device.BramaDump;
import pjpl.simaticserver.run.SimaticServer;

/**
 *
 * @author Piotr Janczura <piotr@janczura.pl>
 */
public class MySqlStore implements Runnable {

	public MySqlStore(LinkedBlockingQueue<BramaDump> queue, pjpl.simaticserver.process.Brama parent)
			throws NamingException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		this.queue = queue;
		this.parent = parent;
		//------------------------------------------------------------------------------
		// inicjacja bazy danych

		try {
			mySqlConnection = (Connection) DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
		} catch (SQLException e) {
				throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	@Override
	public void run() {
		while(true){
			summaryRun = "------------------------------------------------------------------------------\n";
			summaryRun += format_date.format(System.currentTimeMillis()) + " MySqlStore.run() Start czyli czakanie na kolejkę\n";
			try{
				bramaDump = queue.take(); // <- wątek czeka na wstawienie danych do kolejki
				timeStart = System.currentTimeMillis();

				sql = "INSERT INTO " + bramaDump.getDeviceName() + " VALUES (default,?,?,?,?) ";
				stmtPrepare = mySqlConnection.prepareStatement(sql);
				stmtPrepare.setLong(1, bramaDump.getTimeStamp());
				stmtPrepare.setBinaryStream(2, bramaDump.byteInput(S7.S7AreaDB));
				stmtPrepare.setBinaryStream(3, bramaDump.byteInput(S7.S7AreaPA));
				stmtPrepare.setBinaryStream(4, bramaDump.byteInput(S7.S7AreaPE));
				stmtPrepare.executeUpdate();

				summaryRun += format_date.format(System.currentTimeMillis())+" MySqlStore.run() utworzono plik : "+dateFileNameFormat.format(bramaDump.getTimeStamp())+"\n";
				summaryRun += format_date.format( timeStop = System.currentTimeMillis() )+" MySqlStore.run() Stop praca = "+ (timeStop-timeStart)+"[ms]\n";

			} catch (InterruptedException e) {
				summaryRun += "-- InterruptedException\n";
				summaryRun += "---- "+e.getMessage()+"\n";
			} catch (SQLException e) {
				summaryRun += "-- SQLException\n";
				summaryRun += "---- "+e.getMessage()+"\n";
			} finally {
			}
			System.out.println(summaryRun);
		}
	}

	//------------------------------------------------------------------------------
	private String url = "jdbc:mysql://"+SimaticServer.config.getProperty("mysql_server")+":"+SimaticServer.config.getProperty("mysql_port")+"/"+SimaticServer.config.getProperty("mysql_db_name");
	private String username = SimaticServer.config.getProperty("mysql_user_name");
	private String password = SimaticServer.config.getProperty("mysql_user_password");
	private String sql = "";
	private Connection mySqlConnection;
	private PreparedStatement stmtPrepare;

	private pjpl.simaticserver.process.Brama parent;
	private LinkedBlockingQueue<BramaDump> queue;
	private BramaDump bramaDump;

	//------------------------------------------------------------------------------
	// Nie związane z głónym zadaniem
	private final DateFormat format_date = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
	private final DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_datePackedMS"));
	private String summaryRun = new String();
	private volatile long timeStart;
	private volatile long timeStop;
	private volatile Timestamp timestamp;
	private volatile Date data;

}
