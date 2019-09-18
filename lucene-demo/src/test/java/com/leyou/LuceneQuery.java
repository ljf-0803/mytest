package com.leyou;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class LuceneQuery {
    @Test
    public  void  testQueryIndex() throws  Exception{
        //1.指定索引文件的位置D:\Demo\index
        Directory directory = FSDirectory.open(new File("D:\\Demo\\index"));
        //2.创建一个用来读取索引的对象indexReader
        IndexReader indexReader= DirectoryReader.open(directory);
        //3.创建一个用来查询索引的对象 IndexSearcher
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        //使用term查询  指定查询的关键字和域名
       /* Query query=new TermQuery(new Term("companyName","凌宇"));*/
        //查询所有
     /*   Query query=new MatchAllDocsQuery();*/
         //通配符查询
        /*Query query=new WildcardQuery(new Term("companyName","*宇*"));*/
        //容错查询

//        Query query = new FuzzyQuery(new Term("companyName","另宇"));
//        区间查询
//        Query query = NumericRangeQuery.newIntRange("salary",10000,20000,true,true);
//        组合查询
      /*  BooleanQuery query = new BooleanQuery();
//        名称中带“宇”字的 并且工资范围是10000到20000之间
        Query query1 = new WildcardQuery(new Term("companyName", "*宇*"));
        Query query2 = NumericRangeQuery.newIntRange("salary",100000,200000,true,true);
        query.add(query1, BooleanClause.Occur.MUST);  //Occur.MUST 一定要符合
        query.add(query2, BooleanClause.Occur.MUST);*/

//        Query query = new TermQuery(new Term("companyName","北京东进航空科技股份有限公司"));
        //分词查询
        QueryParser parser=new QueryParser("companyName",new IKAnalyzer());
        Query query=parser.parse("北京东进航空科技股份有限公司");
        TopDocs topDocs = indexSearcher.search(query, 100);//第二个参数：最多显示多少条数据
        int totalHits = topDocs.totalHits;  //查询的总数量
        System.out.println("符合条件的总数:"+totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs; //获取命中的文档 存储的是文档的id
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docID = scoreDoc.doc;
              // 根据id查询文档
            Document document = indexSearcher.doc(docID);
            System.out.println( "id:"+ document.get("id"));
            System.out.println( "companyName:"+ document.get("companyName"));
            System.out.println( "companyAddr:"+ document.get("companyAddr"));
            System.out.println( "salary:"+ document.get("salary"));
            System.out.println( "url:"+ document.get("url"));
            System.out.println("=================================");
        }
    }
   @Test
    public  void  testAnalyzer() throws  Exception{
      Analyzer analyzer = new StandardAnalyzer();
      String text="The Spring Framework provides a comprehensive programming and configuration model.";

      TokenStream tokenStream= analyzer.tokenStream("text",text);
       //设置指针引用
       CharTermAttribute charTermAttribute=tokenStream.addAttribute(CharTermAttribute.class);
       //指针复位
       tokenStream.reset();
       while (tokenStream.incrementToken()){
           System.out.println(charTermAttribute);
       }
   }
}

