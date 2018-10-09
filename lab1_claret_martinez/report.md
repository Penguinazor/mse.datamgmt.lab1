# Data Management
## Laboratory 01
###### Students: *Romain Claret & Simon Martinez*
###### Professor: *Dr. Fatemeh Borran*
###### Assistant: *Gary Marigliano*
###### Due-date: *Monday 15 October 2018*
<br/><br/>

---

### *(D)* Understanding the Lucene API

1. Yes, the demo uses the default stopword removal
    - QueryParser takes as argument a [StandardAnalyzer()](http://lucene.apache.org/core/6_6_1/core/org/apache/lucene/analysis/standard/StandardAnalyzer.html?is-external=true) which is built by default with a default list of stopwords [`STOP_WORDS_SET`](http://lucene.apache.org/core/6_6_1/core/org/apache/lucene/analysis/standard/StandardAnalyzer.html#STOP_WORDS_SET)
    - Proof: "frame" and "the frame" is giving the same output

2. The command line demo don't use stemming, indeed : "information" and "informative" don't give the same output

3. The command line demo is case insensitive, indeed : "test" and "TEST" give the same output  
   StandarAnalyser use LowerCaseFilter filter. This filter lowcases the queries.
4. It matter because words like "been", "being" will be transform as "be" and thus considered as stopwords. 

--- 
# TODO

### *(E)* Using Luke
### *(F)* Indexing and Searching the CACM collection
#### Indexing
#### Using different Analyzers
#### Reading Index
#### Searching
#### Tuning the Lucene Score
