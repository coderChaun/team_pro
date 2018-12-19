package com.tarena.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.http.entity.mime.content.AbstractContentBody;

public class BlockStreamBody extends AbstractContentBody {
	//块的大小
	private long blockSize=0;
	//上传文件的文件名
	private String fileName;
	//writeTo方法需要的三个必要的数据
	private int blockNumber=0;
	private int blockIndex=0;
	private File targetFile;//这个就是真个的视频的File对象
	@Override
	public String getFilename() {
		// TODO Auto-generated method stub
		return fileName;
	}
	public BlockStreamBody(int blockNumber,int blockIndex,File targetFile){
		super("application/octet-stream");//告知httpClient操作的是什么类型的文件
		//super("image/png");
		this.blockNumber=blockNumber;
		this.blockIndex=blockIndex;
		this.targetFile=targetFile;
		this.fileName=this.targetFile.getName();
		if(blockIndex<blockNumber){
			//除了最后一个块之前的块
			this.blockSize=GlobalConstant.BLOCK_SIZE;
		}else{
			//最后一块
			this.blockSize=targetFile.length()-GlobalConstant.BLOCK_SIZE*(blockNumber-1);
		}
	}
	/**
	 * 此方法每调用一次,只输出文件中的某一块的数据
	 */
	@Override
	public void writeTo(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		byte[] b=new byte[1024];
		/*
		 * 1.此类输入和输出都可以用RandomAccessFile的对象
		 * 2.读取的字节流
		 * 3.可以通过文件的指针定位读写的位置
		 */
		RandomAccessFile raf=new RandomAccessFile(this.targetFile,"r");
		if(this.blockIndex==1){
			//处理第一块(有可能是就一块)
			int n=0;
			long readLength=0;//存储的是读取的字节数
			//一定是从0开始到倒数第二个块
			while(readLength<=this.blockSize-1024){
				n=raf.read(b,0,1024);
				readLength+=1024;
				out.write(b,0,n);
			}
			//是大的文件块中的1024缓存块的最后一块
			if(readLength<=this.blockSize){
				//余下的不足1024个字节的在此读取
				n=raf.read(b,0,(int)(blockSize-readLength));
				out.write(b,0,n);
			}
		}else if(blockIndex<blockNumber){
			//处理不是第一块也不是最后一块
			//指针的定位,定位开始读取的字节数
			raf.seek(GlobalConstant.BLOCK_SIZE*(blockIndex-1));
			int n=0;
			long readLength=0;//存储的是读取的字节数
			while(readLength<=this.blockSize-1024){
				n=raf.read(b,0,1024);
				readLength+=1024;
				out.write(b,0,n);
			}
			if(readLength<=this.blockSize){
				//余下的不足1024个字节的在此读取
				n=raf.read(b,0,(int)(blockSize-readLength));
				out.write(b,0,n);
			}
		}else{
			//处理最后一块
			raf.seek(GlobalConstant.BLOCK_SIZE*(blockIndex-1));
			int n=0;
			while((n=raf.read(b,0,1024))!=-1){
				out.write(b,0,n);
			}
		}
		raf.close();
	}

	@Override
	public long getContentLength() {
		// TODO Auto-generated method stub
		return this.blockSize;
	}

	@Override
	public String getTransferEncoding() {
		// TODO Auto-generated method stub
		return "binary";
	}

}
