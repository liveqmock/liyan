

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.string.StringUtil;

public class SyncUserInfo {

	/**
	 * 手动同步用户数据
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) {
		Connection conn= null;
		Connection con = null;
		Statement stat= null;
		
		int loop = 0;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			
			conn = DriverManager.getConnection("jdbc:db2://10.3.20.203:50001/amp","amp","wmpay.777");
			stat = conn.createStatement();
			for(int i = 18 ; i <= 19 ; i ++){
				String date = null;
				if(i<10){
					date = "'2015010"+i+"'";
				}else{
					date = "'201501"+i+"'";
				}

				for(int j = 0 ; j < 24 ; j++){
					
					
					String startTime = null;
					String endTime = null;
					
					if(j<10){
						startTime = "'0"+j+"0000'";
						if((j+1)<10){
							endTime = "'0"+(j+1)+"0000'";
						}else{
							endTime = "'"+(j+1)+"0000'";
						}
					}else{
						startTime = "'"+j+"0000'";
						endTime = "'"+(j+1)+"0000'";
					}
					
					String sql = "delete from sd_tran_data where tran_date="+date+" and tran_time >= "+startTime+"  and tran_time < "+endTime;
					 
					System.out.println(sql);
					int updateCount = stat.executeUpdate(sql);
					System.out.println(updateCount);
					conn.commit();
					//if(updateCount==0)break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				stat.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getDatas(String filePath) throws Exception{
		List<String> lines = FileUtil.loadlingFileContentsForList(filePath);
		return lines;
	}
}
