import java.lang.String;
import java.lang.Math;
import java.util.ArrayList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.BufferedReader;


public class TFIDFCalculator  {

    public static void main(String[] args) {
        String textFile = args[0];
        String tcFile = args[1];
        ArrayList <Integer> docs = new ArrayList<Integer>();
        ArrayList <Trie> trie = new ArrayList<Trie>();

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
                    for( int i = 0 ; i<ana.length ; i++ ){
                        if( texTrie.search(ana[i]) == false ) texTrie.insert(ana[i]);
                        else texTrie.addCount(ana[i]);
                    }
                    docs.add(Integer.valueOf(ana.length));
                    trie.add(texTrie);
                    tmpLine = "";
                }
            }
            read.close();

            read = new BufferedReader(new FileReader(tcFile));
            String[] text = read.readLine().split(" ");
            String[] fileNum = read.readLine().split(" ");
            read.close();

            String ans = "" ;
            ArrayList<String> term = new ArrayList<String>() ;
            ArrayList<Double> termTimes = new ArrayList<Double>();

            for( int i = 0 ; i<text.length ; i++ ){
                int tmp = Integer.parseInt(fileNum[i]);
                double tf = calculate.tf(trie , text[i] , tmp , docs.get(tmp));
                if( tf != 0 ){
                    if( !term.contains(text[i]) ) {
                        term.add(text[i]);
                        termTimes.add(calculate.idf(trie, text[i]));
                    }
                    ans += String.format("%.5f", tf*termTimes.get(term.indexOf(text[i])));
                }
                else ans += "0.00000";
                if( i < text.length-1 ) ans += " ";
            }

            File file = new File("output.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(ans);
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
        //System.out.println(ans);
        return ans;
    }


    public static double idf(ArrayList <Trie> trie, String term ) {
        int num = 0;
        for( int i = 0 ; i<trie.size() ; i++ ){
            if( trie.get(i).search(term) ) num ++ ;
        }
        double ans = trie.size()*1.0 / num;
        //System.out.println(Math.log(ans));
        return Math.log(ans);
    }
}

