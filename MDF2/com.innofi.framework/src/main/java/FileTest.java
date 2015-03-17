import java.io.IOException;

import com.innofi.framework.utils.file.FileUtil;


public class FileTest {
	public static void main(String[] args) throws IOException{
		System.out.println(FileUtil.read("E:/tran_sync/server/log/2014-12-12.log"));
	}
}
