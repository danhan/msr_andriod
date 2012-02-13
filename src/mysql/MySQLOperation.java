package mysql;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySQLOperation {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private String database = null;
	private String tableName = null;

	public void preConnect(String database,String tableName) throws Exception{
		try{
			this.database = database;
			this.tableName = tableName;
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/"+database+"?"
							+ "user=root&password=passw0rd");	
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
		} catch (Exception e) {
			throw e;
		} 
	}
	
	public void insert(String[] keys,List<String> values,int[] types) throws Exception{
		try{
			if(connect.isClosed()){
				connect = DriverManager.getConnection("jdbc:mysql://localhost/"+database+"?"+ "user=root&password=passw0rd");	
			}
			if(!connect.isClosed()){
				String insertSQL = "";
				String columns = "";
				for(int i=0;i<keys.length;i++) {
					insertSQL+="?,";
					columns += keys[i]+",";					
				}	
				insertSQL = insertSQL.substring(0,insertSQL.lastIndexOf(','));
				columns = columns.substring(0,columns.lastIndexOf(','));
				insertSQL = "insert into "+this.tableName+"("+columns+") values("+insertSQL+")";

				this.preparedStatement = this.connect.prepareStatement(insertSQL);
				
				for(int i=0;i<keys.length;i++){
					int type = types[i];
					String value = values.get(i);					
					if (null == value)
						value = "NULL";
					if(type == 0){ // int
						preparedStatement.setInt(i+1,new Integer(value));
				
					}else if(type ==1){ // timestamp
						//preparedStatement.setTimestamp(i, new Timestamp(value));
						
						
					}else if(type ==2){ // String						
						preparedStatement.setString(i+1, value);
					}
					//preparedStatement.set
				}
				
				preparedStatement.executeUpdate();
				preparedStatement.close();

			}else{
				System.out.println("connection to mysql is closed");
			}
			
		}catch (Exception e) {			
			close();
			throw e;
		}
	}	
	
	public void getDescription(ResultSet resultSet) throws Exception{
		BufferedWriter out = null;
		
		try{
			out = new BufferedWriter(new FileWriter("/home/dan/desc.dat"));
			int num = 0;
			while (resultSet.next()) {
				num++;
				String id = resultSet.getString("description").trim();
				id.replaceAll("\\p{C}", " ");				
				String desc = "";// StringBuffer desc = new StringBuffer();
				StringTokenizer tokens = new StringTokenizer(id," ");
				while(tokens.hasMoreTokens()){
					String t = tokens.nextToken();
					Pattern p = Pattern.compile("[a-zA-Z]"); //[a-zA-Z_0-9]
					Matcher m = p.matcher(t);
					if(m.find()){						
						//desc.append(t+" ");
						t = t.replaceAll("[^a-zA-Z]", "");
						desc += t + " ";
						
					}
				}
				
				out.write(desc+"\n");
						
			}	
			System.out.println("record is "+num);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	public void readDescription(String selectSQL) throws Exception{
		try{
			if(null != connect){
				preparedStatement = connect.prepareStatement(selectSQL);
				resultSet = preparedStatement.executeQuery();
				getDescription(resultSet);
			}
	
		}catch (Exception e) {
			close();
			throw e;
		} 
	}	
	
	public void readDatabase(String selectSQL) throws Exception{
		try{
			if(null != connect){
				preparedStatement = connect.prepareStatement(selectSQL);
				resultSet = preparedStatement.executeQuery();
				writeMetaData(resultSet);
				writeResultSet(resultSet);
			}
	
		}catch (Exception e) {
			close();
			throw e;
		} 
	}
	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// 	Now get some metadata from the database
		// Result set get the result of the SQL query
		
		System.out.println("The columns in the table are: ");
		
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}


	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}
	
	
	
	public void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		try{
			int num = 0;
			while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number
				// which starts at 1
				// e.g. resultSet.getSTring(2);
				String id = resultSet.getString("changeid");
				
				//String commitDate = resultSet.getString("committer_date");

				SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ");  				
				
				System.out.println("id=>" + id );//+ "; commit=>"+commitDate +"; GMTString=>"+df.parse(commitDate).toGMTString() +" ;Time => "+df.parse(commitDate).getTime());
				num++;	
						
			}	
			System.out.println("record is "+num);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	

}
