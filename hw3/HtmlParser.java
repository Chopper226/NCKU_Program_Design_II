import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class HtmlParser {
    public static void main(String[] args) {
        try {
            String mode = "";
            String task = "";
            String stock = "";
            int start = 0;
            int end = 0 ;
            
            mode = args[0];

            if( mode.equals("0")){
                Document doc = Jsoup.connect("https://pd2-hw3.netdb.csie.ncku.edu.tw/").get(); 
                Elements table = doc.select("table");
                Elements trs = table.select("tr");
                Elements ths = trs.select("th");
                Elements tds = trs.select("td");
                String title = doc.title();

                String th = "";
                String td = "";

                for (int i = 0 ; i<ths.size() ; i++ ) {
                    th += ths.get(i).text();
                    td += tds.get(i).text();

                    if( i<ths.size() -1 ){
                        th += ",";
                        td += ",";
                    }
                    
                }
                if( title.equals("day1")){
                    writeFile.writeName("Name " + th + "\n");
                }
                writeFile.writeData(title + " " + td + "\n");
            }
            else{
                BufferedReader read = new BufferedReader(new FileReader("data.csv"));
                String line = null;
                List<String> stockName = new ArrayList<>();
                List<List<String>> stockPrice = new ArrayList<>();

                while(( line = read.readLine()) != null ){
                    String[] tmp = line.split(" ");
                    if( tmp[0].equals("Name")){
                        String[] nameTmp = tmp[1].split(",");
                        for( int i = 0 ; i<nameTmp.length ; i++ ){
                            stockName.add(nameTmp[i]);
                        } 
                    }
                    else{
                        String[] priceTmp = tmp[1].split(",");
                        List<String> dayPrice = new ArrayList<>() ;
                        for( int i = 0 ; i<priceTmp.length ; i++ ){
                            dayPrice.add(priceTmp[i]);
                        } 
                        stockPrice.add(dayPrice);
                    }
                }
                
                read.close();

                task = args[1];
                if( task.equals("0") ){
                    tasks.task0(stockName, stockPrice);
                }
                else{
                    stock = args[2];
                    start = Integer.valueOf(args[3]);
                    end = Integer.valueOf(args[4]);

                    if( task.equals("1") ){
                        int index = stockName.indexOf(stock);
                        tasks.task1(stock, stockPrice , index , start-1, end-1);
                    }
                    else if( task.equals("2") ){
                        int index = stockName.indexOf(stock);
                        tasks.task2(stock, stockPrice , index , start-1 , end-1);
                    }
                    else if( task.equals("3") ){
                        tasks.task3(stockName, stockPrice, start-1 , end-1);
                    }
                    else if( task.equals("4") ){
                        int index = stockName.indexOf(stock);
                        tasks.task4(stock, stockPrice, index , start-1 , end-1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class tasks{
    public static void task0(List<String> name , List<List<String>> price){
        String s = "";
        for( int i = 0 ; i<name.size() ; i++ ){
            s += name.get(i);
            if( i<name.size()-1 ) s+=",";
        }
        s+="\n";
        for( int i = 0 ; i<price.size() ; i++ ){
            for( int j = 0 ; j<price.get(i).size() ; j++ ){
                s+= price.get(i).get(j);
                if( j<price.get(i).size()-1 ) s+=",";
            }
            if( i<price.size()-1 ) s+="\n";
        }
        writeFile.writeOutput(s);
    }

    public static void task1(String name , List<List<String>> price , int index ,  int start , int end){
        String s = "";
        double tmp = 0;

        s += name + "," + Integer.toString(start+1) + "," + Integer.toString(end+1) + "\n";
        for( int i = start ; i<= start+4 ; i++ ){
            tmp += Double.parseDouble(price.get(i).get(index));
        }
        for( int i = start+4 ; i<=end ; i++ ){
            if( i != start+4 ) tmp = tmp - Double.parseDouble(price.get(i-5).get(index)) + Double.parseDouble(price.get(i).get(index));
            
            String result = String.format("%.2f", tmp/5);
            if( result.substring( result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
            if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
            s+=result;
            if( i <= end-1 ) s+=",";
        } 

        writeFile.writeOutput(s+"\n");
    }

    public static void task2(String name , List<List<String>> price , int index , int start , int end){
        double priceSum = 0;
        double priceAvg = 0;
        double tmp = 0;
        for( int i = start ; i<=end ; i++ ){
            priceSum += Double.parseDouble(price.get(i).get(index));
        }
        priceAvg = priceSum / (end-start+1);
        for( int i = start ; i<=end ; i++ ){
            double now = Double.parseDouble(price.get(i).get(index));
            tmp =  tmp + ( now - priceAvg )*( now - priceAvg );
        }

        tmp = tmp/(end-start);
        double x = 1.0;
        double p = 1e-4;
        while( !(p*(-1) < (x*x - tmp) && (x*x - tmp) < p )){
            x = (x + tmp/ x) / 2.0;
        }
        
        String s = "";
        s += name + "," + Integer.toString(start+1) + "," + Integer.toString(end+1) + "\n";
        String result = String.format("%.2f", x);
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
        s+=result;
        writeFile.writeOutput(s+"\n");
    }

    public static void task3(List<String> name , List<List<String>> price , int start , int end ){
        List<Double> all = new ArrayList<>(); 
        for( int i = 0 ; i<price.get(0).size() ; i++ ){
            double priceSum = 0;
            double priceAvg = 0;
            double tmp = 0;
            for( int j = start ; j<=end ; j++ ){
                priceSum += Double.parseDouble(price.get(j).get(i));
            }
            priceAvg = priceSum / (end-start+1);
            for( int j = start ; j<=end ; j++ ){
                double now = Double.parseDouble(price.get(j).get(i));
                tmp =  tmp + ( now - priceAvg )*( now - priceAvg );
            }
            tmp = tmp/(end-start);
            double x = 1.0;
            double p = 1e-4;
            while( !(p*(-1) < (x*x - tmp) && (x*x - tmp) < p )){
                x = (x + tmp/ x) / 2.0;
            }
            all.add(x);
            
        }

        String s = "";
        double top1 = 0 , top2 = 0 , top3 = 0;
        int index = 0;

        top1 = Collections.max(all);
        index = all.indexOf(top1);
        s += name.get(index) + ",";
        all.remove(index);
        name.remove(index);

        
        top2 = Collections.max(all);
        index = all.indexOf(top2);
        s += name.get(index) + ",";
        all.remove(index);
        name.remove(index);

        top3 = Collections.max(all);
        index = all.indexOf(top3);
        s += name.get(index) + ",";
        
        s += Integer.toString(start+1) + "," + Integer.toString(end+1) + "\n";
        String result = "";

        result = String.format("%.2f", top1);
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
        s += result + "," ;

        result = String.format("%.2f", top2);
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
        s += result + "," ;

        result = String.format("%.2f", top3);
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
        s += result ;

        writeFile.writeOutput(s+"\n");
    }

    public static void task4(String name , List<List<String>> price , int index , int start , int end){
        double priceAvg = 0;
        double timeAvg = 0;
        for( int i = start ; i<=end ; i++ ){
            priceAvg += Double.parseDouble(price.get(i).get(index));
            timeAvg += i+1 ;
        }

        priceAvg /= ( end - start + 1 ) ;
        timeAvg /= ( end - start +1 );
        
        double tmp1 = 0;
        double tmp2 = 0;
        for( int i = start ; i<=end ; i++ ){
            double tmpPrice = Double.parseDouble(price.get(i).get(index)) - priceAvg ;
            double tmpTime =  ( i+1-timeAvg );
            tmp1 += tmpTime*tmpPrice;
            tmp2 += tmpTime*tmpTime;
        }

        double b1 = tmp1/tmp2;
        double b0 = priceAvg - b1*timeAvg ;

        String s = "";
        s += name + "," + Integer.toString(start+1) + "," + Integer.toString(end+1) + "\n";
        String result = "";
        result = String.format("%.2f", b1);
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
        s+=result+",";
        result = String.format("%.2f", b0);
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-1) ;
        if( result.substring(result.length()-1,result.length()).equals("0") ) result = result.substring(0, result.length()-2) ;
        s+=result;

        writeFile.writeOutput(s+"\n");
    }
}

class writeFile {
    public static void writeName(String args){
        try {
            String fileName = "data.csv";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(args);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeData(String args){
        try {
            String fileName = "data.csv";
            File file = new File(fileName);
            try (BufferedWriter sw = new BufferedWriter(new FileWriter(file, true))) {
                sw.write(args);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeOutput(String args){
        try {
            String content = args;
            String fileName = "output.csv";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
                bw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}