import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.innofi.framework.utils.file.FileUtil;
import com.innofi.framework.utils.string.StringUtil;


public class Test {
	public static void main(String args[]) throws IOException{
		File f = new File("F:/02_workspaces/rrxworkspace/develop-platform");
		
		List<File> fileArray  = new ArrayList<File>();
		
		recursionViewDir(f,fileArray);
		

		for(File file : fileArray){
			String absoulutePath = file.getAbsolutePath();
			String sourcePath = StringUtil.replace(absoulutePath,"\\", "/");
			String targetPath = sourcePath.substring(sourcePath.indexOf("main/java/")+10,sourcePath.length());
			targetPath = "F:/02_workspaces/rrxworkspace/develop-platform/com.innofi.d7.view/src/main/java/"+targetPath;
			System.out.println(targetPath);
			FileUtil.copyDirectory(new File(sourcePath), new File(targetPath));
		}
		System.out.println(fileArray.size());
	}
	public static void recursionViewDir(final File f , final List viewDirList){
		File[] fileArray = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(java.io.File pathname) {
				if(pathname.isDirectory()&&
						pathname.getName().equals("view")&&
						pathname.getAbsolutePath().indexOf("classes")==-1
						&&pathname.getAbsolutePath().indexOf(".settings")==-1
						&&pathname.getAbsolutePath().indexOf("com\\innofi\\module")==-1){
					return true;
				}else{
					recursionViewDir(pathname,viewDirList);
				}
				return false;
			}
		});
		if(fileArray!=null){
			for(File file : fileArray){
				viewDirList.add(file);
			}
		}
	}
	
}
