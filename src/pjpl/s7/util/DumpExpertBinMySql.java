package pjpl.s7.util;

import Moka7.S7;
import com.mysql.jdbc.Connection;
import java.io.ByteArrayInputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import pjpl.s7.run.SimaticServer;

/**
 * Zapisuje dane w formacie binarnym do bazy danych
 */
public class DumpExpertBinMySql extends DumpExpert{

	public DumpExpertBinMySql(String url, String userName, String password, String processName){
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.processName = processName;

		try {
			mySqlConnection = (Connection) DriverManager.getConnection(url, userName, password);
			System.out.println("Database connected!");
		} catch (SQLException e) {
				throw new IllegalStateException("Cannot connect the database!", e);
		}

	}
	private DumpExpertBinMySql(){}


	@Override
	public void dump(MemByteClip memClip) {

		summaryRun = "------------------------------------------------------------------------------\n";
		summaryRun += format_date.format(System.currentTimeMillis()) + " MySqlStore.run() Start czyli czakanie na kolejkę\n";

		try{
			timeStart = System.currentTimeMillis();

			sql = "INSERT INTO " + processName + " VALUES (default,?,?,?,?) ";
			stmtPrepare = mySqlConnection.prepareStatement(sql);
				stmtPrepare.setLong(1, memClip.timeStamp);
				timePrepareQueryStart = System.currentTimeMillis();
				stmtPrepare.setBinaryStream(2, new ByteArrayInputStream(memClip.buffD));
				stmtPrepare.setBinaryStream(3, new ByteArrayInputStream(memClip.buffI));
				stmtPrepare.setBinaryStream(4, new ByteArrayInputStream(memClip.buffQ));
				timePrepareQueryStop = System.currentTimeMillis();
				stmtPrepare.executeUpdate();
				timeQueryExecute = System.currentTimeMillis();

				summaryRun += format_date.format(timePrepareQueryStart)
						+ " MySqlStore.run() utworzono zapytanie"
						+ String.format("%4d", timePrepareQueryStop - timePrepareQueryStart ) + " [ms]"
						+ "\n";
				summaryRun += format_date.format(timeQueryExecute)
						+ " MySqlStore.run() wykonano zapytanie"
						+ String.format("%4d", ( timeQueryExecute - timePrepareQueryStop )) + " [ms]"
						+ "\n";
				summaryRun += format_date.format( timeStop = System.currentTimeMillis() )
						+ " MySqlStore.run() Stop praca = "
						+ (timeStop-timeStart)
						+ " [ms]"
						+ "\n";

			} catch (SQLException e) {
				summaryRun += "-- SQLException\n";
				summaryRun += "---- "+e.getMessage()+"\n";
			} finally {
			}
			System.out.println(summaryRun);
	}

	//------------------------------------------------------------------------------
	// atrybuty prywatne

	private String url;
	private String userName;
	private String password;
	private String sql;
	private String processName = "";
	private Connection mySqlConnection;
	private PreparedStatement stmtPrepare;


	//------------------------------------------------------------------------------
	// Nie związane z głónym zadaniem
	private final DateFormat format_date = new SimpleDateFormat(SimaticServer.config.getProperty("format_dateMS"));
	private final DateFormat dateFileNameFormat = new SimpleDateFormat(SimaticServer.config.getProperty("format_datePackedMS"));
	private String summaryRun = new String();
	private volatile long timeStart;
	private volatile long timePrepareQueryStart;
	private volatile long timePrepareQueryStop;
	private volatile long timeQueryExecute;
	private volatile long timeStop;

}
