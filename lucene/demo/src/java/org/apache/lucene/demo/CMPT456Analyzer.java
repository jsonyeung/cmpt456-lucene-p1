package org.apache.lucene.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.CharArraySet;

class CMPT456Analyzer extends Analyzer {
  private final static String STOP_PATH = "/lucene-solr/stopwords.txt";
  private CharArraySet stopSet;

  public CMPT456Analyzer() throws FileNotFoundException, IOException {
    // create list based on 'stopwords.txt'
    ArrayList<String> stopWords = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(STOP_PATH))) {
      while (br.ready()) { stopWords.add(br.readLine()); }
    }

    // build stopwords using list
    stopSet = CharArraySet.unmodifiableSet(
      new CharArraySet(stopWords, false)
    );
  }

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    StandardTokenizer src = new StandardTokenizer();    

    // apply filters to tokens
    TokenStream res = new StandardFilter(src);
    res = new LowerCaseFilter(res);
    res = new StopFilter(res, stopSet);
    res = new PorterStemFilter(res);

    return new TokenStreamComponents(src, res) {
      @Override
      protected void setReader(final Reader reader) {
        // set max token
        src.setMaxTokenLength(255);
        super.setReader(reader);
      }
    };
  }
}