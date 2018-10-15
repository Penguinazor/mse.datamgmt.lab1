package ch.heigvd.iict.dmg.labo1.queries;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QueriesPerformer {
	
	private Analyzer		analyzer		= null;
	private IndexReader 	indexReader 	= null;
	private IndexSearcher 	indexSearcher 	= null;

	public QueriesPerformer(Analyzer analyzer, Similarity similarity) {
		this.analyzer = analyzer;
		Path path = FileSystems.getDefault().getPath("index");
		Directory dir;
		try {
			dir = FSDirectory.open(path);
			this.indexReader = DirectoryReader.open(dir);
			this.indexSearcher = new IndexSearcher(indexReader);
			if(similarity != null)
				this.indexSearcher.setSimilarity(similarity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printTopRankingTerms(String field, int numTerms) {
		// TODO student
		// This methods print the top ranking term for a field.
		// See "Reading Index".

		try {
			TermStats[] termStats = HighFreqTerms.getHighFreqTerms(indexReader, numTerms, field, new HighFreqTerms.TotalTermFreqComparator());

			System.out.println("Top ranking terms for field ["  + field +"] are: ");

			for (TermStats term : termStats) {
				System.out.println(term.docFreq + " " + term.termtext.utf8ToString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	
	public void query(String q) {
		// TODO student
		// See "Searching" section

		System.out.println("Searching for [" + q +"]");
		ComplexPhraseQueryParser parser = new ComplexPhraseQueryParser("summary",this.analyzer);
		try {
			Query query = parser.parse(q);
			try {
                TopDocs search = this.indexSearcher.search(query, 10);

                System.out.printf("Total Results : %d\n",search.totalHits);

                for (int i=0; i< 10 ; i++){
                    System.out.printf("%s : %s (%f)\n",search.scoreDocs[i].doc,this.indexReader.document(search.scoreDocs[i].doc).get("title"),search.scoreDocs[i].score);
                }
			}
			catch (IOException ex_search){
				System.out.println(ex_search);
			}
		}
		catch (ParseException ex_parse){
			System.out.println(ex_parse);
		}



	}
	 
	public void close() {
		if(this.indexReader != null)
			try { this.indexReader.close(); } catch(IOException e) { /* BEST EFFORT */ }
	}
	
}
