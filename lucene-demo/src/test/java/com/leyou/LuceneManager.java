package com.leyou;

import com.leyou.entity.JobInfo;
import com.leyou.service.JobInfoServiceImpl;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuceneManager  {
    @Autowired
    private JobInfoServiceImpl jobInfoService;
    @Test
    public  void  testCreateIndex() throws Exception{
        //1指定索引文件的储存位置D:\Demo\index
        Directory directory = FSDirectory.open(new File("D:\\Demo\\index"));
        //2配置版本和分词器
        //标准分词器
        StandardAnalyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,analyzer);
        //3创建一个用来创建索引的对象IndexWriter
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //先删除索引
        indexWriter.deleteAll();
        //4.获取原始数据
        List<JobInfo> jobInfoList = jobInfoService.findAll();
        //根据数据创建lucene的文档对象document
        for (JobInfo jobInfo : jobInfoList) {
            Document document = new Document();
            //域名   值  源数据是否存储
            document.add(new LongField("id",jobInfo.getId(), Field.Store.YES));
            document.add(new TextField("companyName",jobInfo.getCompanyName(), Field.Store.YES));
            document.add(new TextField("companyAddr",jobInfo.getCompanyAddr(), Field.Store.YES));
            document.add(new TextField("jobName",jobInfo.getJobName(), Field.Store.YES));
            document.add(new TextField("jobAddr",jobInfo.getJobAddr(), Field.Store.YES));
            document.add(new IntField("salary",jobInfo.getSalary(), Field.Store.YES));
            document.add(new StringField("url",jobInfo.getUrl(), Field.Store.YES));
          //  StringField 不需要分词时使用  举例：电话号码、身份证号
            indexWriter.addDocument(document);
        }
         //关闭资源
        indexWriter.close();
    }
}
