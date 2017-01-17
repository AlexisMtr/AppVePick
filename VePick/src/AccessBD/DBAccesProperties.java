package AccessBD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DBAccesProperties {

	private Properties propriete;
	private String jdbcDriver;
	private String dbUrl;
	private String username;
	private String password;

	public DBAccesProperties(String configurationFile) throws IOException
	{
		try
		{
			propriete = new Properties();
			propriete.load(new FileInputStream(configurationFile));
		}
		catch(FileNotFoundException file)
		{
			return;
		}
		catch (Exception e)
		{
			return;
		}
		
		jdbcDriver = propriete.getProperty("jdbc.driver");
		dbUrl = propriete.getProperty("database.url");
		username = propriete.getProperty("database.username");
		password = propriete.getProperty("database.password");

	}

	public String getJdbcDriver() { return this.jdbcDriver; }
	public String getDBUrl() { return this.dbUrl; }
	public String getUsername() { return this.username; }
	public String getPassword() { return this.password; }
}