package com.tarena.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.List;

import com.tarena.entity.Video;
import com.tarena.vo.VideoBlockMessage;

/**
 * 此类专门用来合并所有的文件块,并生成一个文件
 * @author Administrator
 *
 */
public class AssembledFileUtil {
	public static void assembledFile(List<Serializable> currentVideoAllBlock){
		String userId="";
		String videoId="";
		int blockNumber=0;
		int blockIndex=0;
		try{
			if(currentVideoAllBlock!=null && currentVideoAllBlock.size()>0){
				Video video=(Video)currentVideoAllBlock.get(0);
				userId=video.getUser().getId();
				videoId=video.getId();
				//构建文件的存储路径c:/upload/userId/videoId/
				String uploadedUrl=GlobalConstant.UPLOAD_ABSOLUTE_PATH
						+File.separator
						+userId
						+File.separator
						+videoId
						+File.separator;
				File realPath=new File(uploadedUrl);
				if(!realPath.exists()){
					realPath.mkdirs();
				}
				String fileName=video.getFileName();
				String extName=fileName.substring(fileName.lastIndexOf(".")+1);
				
				RandomAccessFile  fileWrite=new RandomAccessFile(uploadedUrl+videoId+"."+extName,"rw");
				//接收数据块,合并成一个文件
				for(int i=1;i<currentVideoAllBlock.size();i++){
					VideoBlockMessage vbm=(VideoBlockMessage)currentVideoAllBlock.get(i);
					if(vbm!=null){
						byte[] block=vbm.getData();
						int read=block.length;
						//文件合并
						long offset=(i-1)*GlobalConstant.BLOCK_SIZE;
						fileWrite.seek(offset);
						//写入文件
						fileWrite.write(block,0,read);
					}
				}
				fileWrite.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
