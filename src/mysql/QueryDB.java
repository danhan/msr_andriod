package mysql;

public class QueryDB {
	public static void main(String[] args) throws Exception {
		//MySQLAccess dao = new MySQLAccess();
		//dao.rea
		MySQLOperation dao = new MySQLOperation();
		dao.preConnect("msr","bug_report");
		dao.readDescription("select description from bug_report");
	}
}
