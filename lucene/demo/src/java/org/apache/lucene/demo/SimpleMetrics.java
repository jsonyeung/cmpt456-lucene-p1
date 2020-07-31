package org.apache.lucene.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;

public class SimpleMetrics {
  private SimpleMetrics() {}

  /* Command-line to find term & doc frequency. */
  /* Note: Assumes search terms are single words */
  public static void main(String[] args) throws Exception {
    // get variables
    final String INDEX_PATH = "index";
    final String SEARCH_FIELD = "contents";
    
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_PATH)));

    while (true) {
      // prompt the user
      System.out.println("Enter query: ");
      String line = in.readLine();

      // break on empty query
      if (line == null || line.length() == -1) { break; }
      line = line.toLowerCase().trim();
      if (line.length() == 0) { break; }

      // run query
      Term termInstance = new Term(SEARCH_FIELD, line);                              
      long docCount = reader.docFreq(termInstance);
      long termFreq = reader.totalTermFreq(termInstance);
  
      // print document frequency & term frequency
      System.out.println("⇒ \"" + line + "\"");
      System.out.println("Total matching documents: " + docCount);
      System.out.println("Term frequency: " + termFreq);
      System.out.println();
    }

    reader.close();
  }
}