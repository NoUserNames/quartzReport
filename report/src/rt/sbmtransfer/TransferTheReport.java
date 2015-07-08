package rt.sbmtransfer;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import rt.util.ReadProperties;
import rt.util.TUtil;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class TransferTheReport {

	private Logger log = Logger.getLogger(TransferTheReport.class);
	
	public static void main(String[] a){
		String destFile ="smb://qiang1_zhang:ri-teng1234@10.131.18.8/資訊部/MIS/MES/Tracebilyty 相关文档/报表&计划任务清单/WIP-E5A-reprot.xls";
		String sourceFile ="D:/apache-tomcat-6.0.41/report/WIP-E5A-reprot.xls";
		new TransferTheReport().writeTOsmb(sourceFile, destFile);
	}
	
	/**
	 * 上传文件到局域网
	 * @param sourceFile 源文件
	 * @param destFile 目标文件
	 * @return true || false
	 */
	public boolean writeTOsmb(String sourceFile,String destFile) {
		try {
			SmbFile smbFileOut = new SmbFile(destFile);
			//"smb://qiang1_zhang:ri-teng1234@10.131.18.8/資訊部/MIS/MES/Tracebilyty 相关文档/报表&计划任务清单/Trace_Alert_Report.zip"
			if (!smbFileOut.exists())
				smbFileOut.createNewFile();
			SmbFileOutputStream out = new SmbFileOutputStream(smbFileOut);
			FileInputStream in = new FileInputStream(sourceFile);//"e:/Trace_Alert_Report.zip"
			byte[] b = new byte[4096];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			in.close();
			out.flush();
			out.close();
			
			// smbFileOut.delete();
		} catch (SmbAuthException e) {
//			System.out.println(e.getNtStatus() == SmbException.NT_STATUS_ACCESS_DENIED);
			log.error("NT_STATUS_ACCESS_DENIED="+(e.getNtStatus() == SmbException.NT_STATUS_ACCESS_DENIED));
			for (int i = 0; i < SmbException.NT_STATUS_CODES.length; i++) {
				log.error("状态："+SmbException.NT_STATUS_CODES[i]);
			}
			log.error("与18.8的认证出现错误："+e.getMessage());
			return false;
		} catch (IOException e) {
			log.error("向18.8传输文件时报错："+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 報表上傳到服務器
	 * @param property 報表屬性配置 鍵
	 */
	public void transferToServer(String property){
		String source = TUtil.getURL()+ReadProperties.ReadProprety(property);
		String fileName = source.substring(source.lastIndexOf("/")+1);
		fileName = fileName.substring(0, fileName.indexOf("."));
		String dest = ReadProperties.ReadProprety("server.upload.report")+fileName+"/"+TUtil.format("yyyy-MM-dd")+".xls";

		writeTOsmb(source, dest);
	}
}
