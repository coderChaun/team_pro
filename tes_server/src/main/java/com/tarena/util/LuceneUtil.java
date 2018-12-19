package com.tarena.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.tarena.entity.Course;
import com.tarena.entity.User;
import com.tarena.entity.Video;
import com.tarena.service.VideoService;

@Component("luceneUtil")
public class LuceneUtil {
	@Resource(name="indexWriter")
	private IndexWriter indexWriter;
	@Resource(name="analyzer")
	private IKAnalyzer analyzer;
	
	@Resource(name="videoService")
	private VideoService videoService;
	@PostConstruct
	public  void init(){
		//在当前类实例化完后,会自动调用此方法,做初始化工作
		System.out.println("init lucene index ");
		List<Video> videos=this.videoService.findAllVideosByLucene();
		System.out.println(videos);
		this.createIndex(videos);
	}
	
	/**
	 * 基于查询数据库的视频结果,做lucene索引
	 * @param videos  ,数据库的视频数据
	 */
	public void createIndex(List<Video> videos){
		Document document=null;
		try{
			//创建新索引前,必须先删除原有的索引,否则索引会叠加
			this.indexWriter.deleteAll();
			
			//创建索引
			for(Video video : videos){
				document=new Document();
				if(video.getId()!=null){
					Field id=new Field("id",video.getId(),Field.Store.YES,Field.Index.NO);
					document.add(id);
				}
				if(video.getTitle()!=null){
					Field title=new Field("title",video.getTitle(),Field.Store.YES,Field.Index.ANALYZED);
					document.add(title);
				}
				if(video.getCourse()!=null && video.getCourse().getName()!=null){
					Field courseName=new Field("courseName",video.getCourse().getName(),Field.Store.YES,Field.Index.ANALYZED);
					document.add(courseName);
				}
				if(video.getUser()!=null && video.getUser().getNickName()!=null){
					Field nickName=new Field("nickName",video.getUser().getNickName(),Field.Store.YES,Field.Index.ANALYZED);
					document.add(nickName);
				}
				if(video.getCount()>=0){
					Field clickCount=new Field("clickCount",video.getCount().toString(),Field.Store.YES,Field.Index.NO);
					document.add(clickCount);
				}
				if(video.getPicture()!=null){
					Field picture=new Field("picture",video.getPicture(),Field.Store.YES,Field.Index.NO);
					document.add(picture);
				}
				if(video.getFileName()!=null){
					Field fileName=new Field("fileName",video.getFileName(),Field.Store.YES,Field.Index.NO);
					document.add(fileName);
				}
				if(video.getPicturecc()!=null){
					Field picturecc=new Field("picturecc",video.getPicturecc(),Field.Store.YES,Field.Index.NO);
					document.add(picturecc);
				}
				if(video.getVideoUrlcc()!=null){
					Field videoUrlcc=new Field("videoUrlcc",video.getVideoUrlcc(),Field.Store.YES,Field.Index.NO);
					document.add(videoUrlcc);
				}
				if(video.getIntroduction()!=null){
					Field introduction=new Field("introduction",video.getIntroduction(),Field.Store.YES,Field.Index.NO);
					document.add(introduction);
				}
				this.indexWriter.addDocument(document);
			}
			this.indexWriter.commit();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	//搜索索引
	public List<Video> search(String queryString){
		List<Video> videos=new ArrayList<Video>();
		try{
			//获取索引查询器
			IndexSearcher searcher=this.getSearcher();
			//创建查询转换器
			QueryParser parser=new MultiFieldQueryParser(Version.LUCENE_48,
					new String[]{
							"title",
							"courseName",
							"nickName"
					},analyzer);
			//创建查询,queryString,要基于索引查询的字符串
			Query query=parser.parse(queryString);
			TopDocs td=searcher.search(query, 100);
			ScoreDoc[] sd=td.scoreDocs;
			
			Video video=null;
			Document doc=null;
			for(int i=0;i<sd.length;i++){
				video=new Video();
				int docId=sd[i].doc;
				doc=searcher.doc(docId);
				
				Course course=new Course();
				course.setName(doc.get("courseName"));
				
				User user=new User();
				user.setNickName(doc.get("nickName"));
				
				video.setId(doc.get("id"));
				video.setTitle(doc.get("title"));
				video.setCourse(course);
				video.setUser(user);
				video.setCount(Long.parseLong(doc.get("clickCount")));
				video.setPicture(doc.get("picture"));
				//如果从doucment中读取的数据为null,则报异常
				if(doc.get("picturecc")!=null){
					video.setPicturecc(doc.get("picturecc"));
				}
				if(doc.get("fileName")!=null){
					video.setFileName(doc.get("fileName"));
				}
				if(doc.get("videoUrlcc")!=null){
					video.setVideoUrlcc(doc.get("videoUrlcc"));
				}
				video.setIntroduction(doc.get("introduction"));
				videos.add(video);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return videos;
	}
	private IndexSearcher getSearcher()throws Exception{
		return new IndexSearcher(IndexReader.open(this.indexWriter.getDirectory()));
	}
}
