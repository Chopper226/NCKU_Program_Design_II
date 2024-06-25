import java.lang.String;
import java.lang.Math;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Serializable;

public class Indexer implements Serializable {
	private static final long serialVersionUID = 1L;
    private transient String textFile;

    transient private ArrayList <Integer> docs = new ArrayList<>();
    transient private ArrayList <Trie> trie = new ArrayList<>();
    transient private ArrayList <ArrayList<String>> word = new ArrayList<>();
    ArrayList<HashMap<String, Double>> TFIDFList = new ArrayList<>();

    Indexer( String file ){
        this.textFile = file;
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(textFile) , "UTF-8");
            BufferedReader read = new BufferedReader(isr);
            int lineNum = 0;
            String line ;
            String tmpLine = "";
    
            while( (line = read.readLine()) != null ){
                lineNum++ ;
                tmpLine += line;
                if ( lineNum%5 == 0 ){ 
                    tmpLine = tmpLine.replaceAll("[^a-zA-Z]"," ");
                    tmpLine = tmpLine.toLowerCase();
                    tmpLine = tmpLine.replaceAll("\s+"," ");
                    tmpLine = tmpLine.replaceAll("\s+"," ");
                    tmpLine = tmpLine.strip();  
    
                    String[] ana = tmpLine.split(" ");
                    Trie texTrie = new Trie();
                    ArrayList <String> wordList = new ArrayList<String>();
    
                    for( int i = 0 ; i<ana.length ; i++ ){
                        if( texTrie.search(ana[i]) == false ) {
                            texTrie.insert(ana[i]);
                            wordList.add(ana[i]);
                        }
                        else texTrie.addCount(ana[i]);
                    }
    
                    docs.add(Integer.valueOf(ana.length));
                    trie.add(texTrie);
                    word.add(wordList);
                    tmpLine = "";
                }
            }
            read.close();
    
            System.out.println(123);
            ArrayList<String> term = new ArrayList<String>() ;
            ArrayList<Double> termTimes = new ArrayList<Double>();
    
            for( int i = 0 ; i<word.size() ; i++ ){
                HashMap<String, Double> map = new HashMap<>();
                for( int j = 0 ; j<word.get(i).size() ; j++ ){
                    int doc = i;
                    String text = word.get(i).get(j);
                    double tf = calculate.tf(trie , text , doc , docs.get(doc));
                    double tfidf = 0;
                    if( tf != 0 ){
                        if( !term.contains(text) ) {
                            term.add(text);
                            termTimes.add(calculate.idf(trie, text));
                        }
                        tfidf = tf*termTimes.get(term.indexOf(text));
                    }
                    map.put(text,tfidf);
                }
                TFIDFList.add(map);
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    
}


class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int count = 0;
}

class Trie {
    TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEndOfWord = true;
        node.count = 1 ;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return false;
            }
        }
        return node.isEndOfWord;
    }

    public void addCount(String word){
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
        }
        node.count++ ;
    }

    public int num(String word){
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
        }
        return node.count;
    }

}

class calculate{
    public static double tf( ArrayList <Trie> trie , String term , int num , int sum) {
        int countText = 0;
        if( trie.get(num).search(term) ) countText = trie.get(num).num(term);
        double ans = countText*1.0 / sum*1.0 ;
        return ans;
    }


    public static double idf(ArrayList <Trie> trie, String term ) {
        int num = 0;
        for( int i = 0 ; i<trie.size() ; i++ ){
            if( trie.get(i).search(term) ) num ++ ;
        }
        double ans = trie.size()*1.0 / num;
        return Math.log(ans);
    }
}