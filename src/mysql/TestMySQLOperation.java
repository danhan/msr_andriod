package mysql;

public class TestMySQLOperation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		
		MySQLOperation dao = new MySQLOperation();
		String database = "msr";
		String tablename = "change_message";
		try{
			dao.preConnect(database,tablename);
			//"select id,committer_date from change_report where id IN(select changeid from change_message where target regexp '\\.cpp|\\.h|\\.java|\\.pl|\\.cc|\\.pde|\\.py|\\.scala|\\.js|\\.css|\\.ko|\\.m4|\\.proto|\\.fstab|\\.yaml|\\.sql|\\.dbf|\\.properties');";
			String sql = "select changeid from change_message where target regexp '\\.cpp|\\.h|\\.java|\\.pl|\\.cc|\\.pde|\\.py|\\.scala|\\.js|\\.css|\\.ko|\\.m4|\\.proto|\\.fstab|\\.yaml|\\.sql|\\.dbf|\\.properties'";
			dao.readDatabase(sql);
			
			dao.close();
		}catch(Exception e){
			if (dao != null) dao.close();
			e.printStackTrace();
		}
	}

}
