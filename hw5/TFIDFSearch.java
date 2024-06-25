import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TFIDFSearch{
    public static void main(String[] args) {
        String fileName = args[0] + ".ser";
        String testFile = args[1];
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Indexer index = (Indexer) ois.readObject();
            ois.close();
            fis.close();

            InputStreamReader isr = new InputStreamReader(new FileInputStream(testFile) , "UTF-8");
            BufferedReader read = new BufferedReader(isr);
            String line = read.readLine();
            int docSize = Integer.parseInt(line);
            String ans = "";

            while( ( line = read.readLine() ) != null ) {
                String[] text = line.split(" ");
                HashMap<Integer , Double> tfidf =  new HashMap<Integer,Double>();
                ArrayList <String> term = new ArrayList<>();

                if( text.length == 1 ){
                    String tex = text[0];
                    for( int i = 0 ; i<index.TFIDFList.size() ; i++){
                        if( index.TFIDFList.get(i).containsKey(tex) ){
                            tfidf.put( Integer.valueOf(i) , index.TFIDFList.get(i).get(tex) );
                        }
                    }
                }
                else{
                    HashMap <String,Integer> map = new HashMap<>();
                    for( int i = 0 ; i<text.length ; i+=2 ){
                        if( !(term.contains(text[i])) ) term.add(text[i]);
                        if( map.containsKey(text[i]) ) map.put(text[i], map.get(text[i])+1);
                        else map.put(text[i],1);
                    }

                    if( text[1].equals("AND") ){
                        for( int i = 0 ; i<index.TFIDFList.size() ; i++ ){
                            double add = 0;
                            boolean check = true;
                            for( int j = 0 ; j<term.size() ; j++ ){
                                String tex = term.get(j);
                                if( index.TFIDFList.get(i).containsKey(tex) ){
                                    add += index.TFIDFList.get(i).get(tex)*map.get(tex) ;
                                }
                                else {
                                    check = false;
                                    break;
                                }
                            }
                            if( check && add != 0 ) tfidf.put( Integer.valueOf(i) ,  add );
                        }
                    }
                    else{
                        for( int i = 0 ; i<index.TFIDFList.size() ; i++ ){
                            double add = 0;
                            for( int j = 0 ; j<term.size() ; j++ ){
                                String tex = term.get(j);
                                if( index.TFIDFList.get(i).containsKey(tex) ){
                                    add += index.TFIDFList.get(i).get(tex)*map.get(tex) ;
                                }
                            }
                            if( add != 0 ) tfidf.put(Integer.valueOf(i) , add );
                        }
                    }

                }
                
                ArrayList<Map.Entry<Integer,Double>> list = new ArrayList<>(tfidf.entrySet());
                list.sort(new Comparator<Map.Entry<Integer,Double>>() {
                    public int compare(Map.Entry<Integer,Double> entry1, Map.Entry<Integer,Double> entry2) {
                        int cmp = entry2.getValue().compareTo(entry1.getValue()); 
                        if (cmp == 0) {
                            return entry1.getKey().compareTo(entry2.getKey()); 
                        }
                        return cmp;
                    }
                });
                
                String tmp = "";
                for( int i = 0 ; i<docSize ; i++ ){
                    if( i < list.size() ) tmp += list.get(i).getKey();
                    else tmp += "-1";
                    if( i<docSize-1 ) tmp += " ";
                }
                ans += tmp + "\n";
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
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }
}
