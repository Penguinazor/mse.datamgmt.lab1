package ch.heigvd.iict.dmg.labo1.similarities;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.ClassicSimilarity;

public class MySimilarity extends ClassicSimilarity {

	// TODO student
	// Implement the functions described in section "Tuning the Lucene Score"

    //tf : 1+log(⁡freq)
    @Override
    public float tf(float freq){
        return (float) (1 + Math.log(freq));
    }

    //idf : log(numDocs/docFreq+1)+1
    @Override
    public float idf(long docFreq, long numDocs){
        return (float) (Math.log(numDocs / (docFreq + 1)) + 1);
    }

    //lengthNorm : 1
    @Override
    public float lengthNorm(FieldInvertState fieldInvertState) {
        return (float) 1;
    }

    //coord : squart(overlap/maxOverlap)
    @Override
    public float coord(int overlap, int maxOverlap){
        return (float) Math.sqrt(overlap / maxOverlap);
    }
}
