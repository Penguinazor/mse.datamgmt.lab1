package ch.heigvd.iict.dmg.labo1.indexer;

import ch.heigvd.iict.dmg.labo1.parsers.ParserListener;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class CACMIndexer implements ParserListener {
	
	private Directory 	dir 			= null;
	private IndexWriter indexWriter 	= null;
	
	private Analyzer 	analyzer 		= null;
	private Similarity 	similarity 		= null;
	
	public CACMIndexer(Analyzer analyzer, Similarity similarity) {
		this.analyzer = analyzer;
		this.similarity = similarity;
	}
	
	public void openIndex() {
		// 1.2. create an index writer config
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE); // create and replace existing index
		iwc.setUseCompoundFile(false); // not pack newly written segments in a compound file: 
		//keep all segments of index separately on disk
		if(similarity != null)
			iwc.setSimilarity(similarity);
		// 1.3. create index writer
		Path path = FileSystems.getDefault().getPath("index");
		try {
			this.dir = FSDirectory.open(path);
			this.indexWriter = new IndexWriter(dir, iwc);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onNewDocument(Long id, String authors, String title, String summary) {
		Document doc = new Document();

		// TODO student: add to the document "doc" the fields given in
		// parameters. You job is to use the right Field and FieldType
		// for these parameters.
		//

		//doc.add(id);
		doc.add(new StoredField("id", id));

		//doc.add(authors);
		FieldType fieldType = new FieldType();
		fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		fieldType.setStored(true);
		fieldType.setStoreTermVectors(true);
		fieldType.setTokenized(false);
		if (authors != null) {
			for (String author : authors.split(";")) {
				if (!author.trim().equals("")) {
					doc.add(new Field("authors", author, fieldType));
				}
			}
		}

		FieldType fieldType2 = new FieldType();
		//fieldType2.setDimensions(int dimensionCount, int dimensionNumBytes) //Enables points indexing.
		//fieldType2.setDocValuesType(DocValuesType type) //Sets the field's DocValuesType
		fieldType2.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS); //Sets the indexing options for the field:
		//fieldType2.setOmitNorms(boolean value) // Set to true to omit normalization values for the field.
		fieldType2.setStored(true); //Set to true to store this field.
		fieldType2.setStoreTermVectorOffsets(true); //Set to true to also store token character offsets into the term vector for this field.
		fieldType2.setStoreTermVectorPayloads(true); //Set to true to also store token payloads into the term vector for this field.
		fieldType2.setStoreTermVectorPositions(true); //Set to true to also store token positions into the term vector for this field.
		fieldType2.setStoreTermVectors(true); //Set to true if this field's indexed form should be also stored into term vectors.
		fieldType2.setTokenized(true); //Set to true to tokenize this field's contents via the configured Analyzer.

		//doc.add(title);
		doc.add(new Field("title", title, fieldType2));

		//doc.add(summary);
		if (summary != null) {
			doc.add(new Field("summary", summary, fieldType2));
		}

		try {
			this.indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void finalizeIndex() {
		if(this.indexWriter != null)
			try { this.indexWriter.close(); } catch(IOException e) { /* BEST EFFORT */ }
		if(this.dir != null)
			try { this.dir.close(); } catch(IOException e) { /* BEST EFFORT */ }
	}
}
