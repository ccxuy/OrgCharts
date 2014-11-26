package utilities;

import java.io.*;
import java.sql.*;

import javax.sql.rowset.serial.SerialClob;

import org.apache.log4j.Logger;

public class ClobConverter {
	private static final Logger log = Logger.getLogger(ClobConverter.class);

	public static Clob stringToClob(String source) {
		if (source == null) {
			return null;
		}else{
			try {
				return new SerialClob(((String) source).toCharArray());
			} catch (Exception e) {
				log.error("Could not convert string to a CLOB", e);
				return null;
			}
		}
	}

	public static String clobToString(Clob data) {
		if(null==data){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		try {
			Reader reader = data.getCharacterStream();
			BufferedReader br = new BufferedReader(reader);

			String line;
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
			br.close();
		} catch (SQLException e) {
			log.error("Could not convert CLOB to string", e);
			return "";
		} catch (IOException e) {
			log.error("Could not convert CLOB to string", e);
			return "";
		}
		return sb.toString();
	}
}
