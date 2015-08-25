package pjpl.simaticserver.util;

import Moka7.S7;
import com.mysql.jdbc.Connection;
import java.io.ByteArrayOutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
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

		System.out.println("url do bazy danych : "+url);
		try {
			Connection connection = (Connection) DriverManager.getConnection(url, username, password);
			System.out.println("Database connected!");
		} catch (SQLException e) {
				throw new IllegalStateException("Cannot connect the database!", e);
		}
//		Context context = new InitialContext();
//		DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/myDB");

//		MysqlDataSource dataSource = new MysqlDataSource();
//		dataSource.setUser();
//		dataSource.setPassword("simaticserver");
//		dataSource.setServerName("myDBHost.example.org");
	}

	@Override
	public void run() {
		while(true){

			try{
				bramaDump = queue.take(); // <- wątek czeka na wstawienie danych do kolejki
				outputDB = bramaDump.bin(S7.S7AreaDB);
				outputPA = bramaDump.bin(S7.S7AreaPA);
				outputPE = bramaDump.bin(S7.S7AreaPE);

			} catch (InterruptedException e) {

//			} catch (IOException ex) {
//				Logger.getLogger(FileJsonLogger.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
			}
		}
	}

	//------------------------------------------------------------------------------
	private String url = "jdbc:mysql://"+SimaticServer.config.getProperty("mysql_server")+":3306/"+SimaticServer.config.getProperty("mysql_db_name");
	private String username = SimaticServer.config.getProperty("mysql_user_name");
	private String password = SimaticServer.config.getProperty("mysql_user_password");
	private BramaDump bramaDump;
	private pjpl.simaticserver.process.Brama parent;
	private LinkedBlockingQueue<BramaDump> queue;
	private ByteArrayOutputStream outputDB;
	private ByteArrayOutputStream outputPA;
	private ByteArrayOutputStream outputPE;
}
