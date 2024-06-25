import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class CodeGenerator {
    public static void main(String[] args) {
		    
        String fileName = args[0];
        String code = "";
        List<List<String>> ClassCon = new ArrayList<>();
        List<String> ClassName = new ArrayList<>();
        for(int i = 0 ; i<3 ; i++){
            ClassCon.add( new ArrayList<>() );
        }

        try {
            code = Files.readString(Paths.get(fileName));
            //System.out.println(code);
            String[] text = code.split("\\n");
            
            boolean check = false;
            int index = 0;

            AnaClass ana = new AnaClass() ;
            for( int i = 0 ; i<text.length ; i++ ){
                String s = text[i];
                String[] s1 = s.split(" ");
                String[] line = new String[s1.length+1];
                int count=0;
                line[0]="";

                for( int j = 0 ; j<s1.length ; j++ ){
                    if(s1[j].length() == 0) continue;
                    if( count != 0 && line[count-1].substring(line[count-1].length()-1,line[count-1].length()).equals("+") ){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( count != 0 && line[count-1].substring(line[count-1].length()-1,line[count-1].length()).equals("-") ){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( s1[j].substring(0,1).equals("(") ){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( s1[j].substring(0,1).equals("[") ){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( count != 0 && line[count-1].substring(line[count-1].length()-1,line[count-1].length()).equals("(") ){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( count != 0 && line[count-1].substring(line[count-1].length()-1,line[count-1].length()).equals("[") ){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( s1[j].equals(",")){
                        line[count-1] += s1[j];
                        count -= 1;
                    }
                    else if( s1[j].equals(")") ){
                        line[count-1] += s1[j];
                        count -= 1 ;
                    }
                    else if( s1[j].equals("]") ){
                        line[count-1] += s1[j];
                        count -= 1 ;
                    }
                    else line[count] = s1[j];
                    //System.out.println(line[count]);
                    count++;
                }

                if( line[0].equals("}") ){
                    check = false;
                    continue;
                }
                if(check){
                    ClassCon.get(index).add(ana.Com(line));
                    continue;
                }

                if( line[0].equals("class") ){
                    if( ClassName.indexOf(line[1]) == -1 ) ClassName.add(line[1]);
                    index = ClassName.indexOf(line[1]);
                    if( line.length >2 && line[2] != null ) check = true;
                    //System.out.println(index);
                }
                else if( ClassName.indexOf(line[0]) != -1 ){
                    index = ClassName.indexOf(line[0]);
                    String[] tmp = new String[line.length-2];
                    for( int j = 0 ; j<line.length-2 ; j++ ){
                        tmp[j] = line[j+2];
                        
                    }
                    ClassCon.get(index).add(ana.Com(tmp));
                }
            }
            String[] wr = new String[2];
            for( int i = 0 ; i < ClassName.size() ; i++ ){
                String tmp1 = ClassName.get(i);
                String tmp2 = "";

                tmp2+="public class " ;
                tmp2+=ClassName.get(i);
                tmp2+=" {\n";
                for( int j = 0 ; j < ClassCon.get(i).size() ; j++ ){
                    tmp2+=ClassCon.get(i).get(j);
                    tmp2+='\n';
                }
                tmp2+="}";
                wr[0] = tmp1;
                wr[1] = tmp2;
                WriteFile.Write(wr);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
    }
    
}
class AnaClass{
    public String Com(String[] args){
        Map<String, String> map = new HashMap<>();
        map.put("+", "public");
        map.put("-", "private");
        map.put("int", "return 0;}");
        map.put("String","return \"\";}");
        map.put("boolean", "return false;}");

        String s = "    ";
        for( int i = 0 ; i < args.length ; i++ ){
            String tmp = args[i];
            if(tmp != null && tmp.length() != 0 ){
                if(tmp.substring(0,1).equals("+") || tmp.substring(0,1).equals("-")){
                    s+=map.get(tmp.substring(0, 1));

                    if( tmp.contains("(")  ){
                        String com = tmp.substring(1, tmp.length());
                        int var = 0;
                        if( com.contains(")")){
                            var = i+1;
                        }
                        else{
                            for( int j = i+1 ; j<args.length ; j++ ){
                                if( args[j] == null ) continue;
                                com += " ";
                                com += args[j];
                                if( args[j].contains(")") ){
                                    var = j+1;
                                    break;
                                }
                            }
                        }
                        if( args[var] == null ) args[var] = "void";
                        s+=" ";
                        s+=args[var];
                        s+=" ";
                        s+=com;
                        s+=" {";
                        
                        //System.err.println(s);
                        if(tmp.substring(1,1+3).equals("set") ){
                            s+='\n';
                            s+= "        this.";
                            s+=tmp.substring(1+3,1+3+1).toLowerCase();
                            s+=tmp.substring(1+3+1,tmp.indexOf('('));
                            s+= " = ";
                            s+= args[i+1].substring(0,args[i+1].indexOf(")"));
                            s+=";\n";
                            s+="    }";
                        }
                        else if(tmp.substring(1,1+3).equals("get") ){
                            s+='\n';
                            s+="        ";
                            s+="return ";
                            s+=tmp.substring(1+3,1+3+1).toLowerCase();
                            s+=tmp.substring(1+3+1, tmp.length()-2);
                            s+=";";
                            s+='\n';
                            s+="    }";
                        }
                        else {
                            if( map.containsKey(args[var]) ) s+=map.get(args[var]);
                            else s+=";}";
                        }
                        
                        return s;
                    }
                    else if( tmp.length() >1 ){
                        s+=" ";
                        s+=tmp.substring(1, tmp.length());
                    }

                }
                else{
                    s+=" ";
                    s+=tmp;
                }
            }
        }
        s+=";";
        return s;
    }
}

class WriteFile {
    public static void Write(String[] args){
        try {

            String output =args[0];
            output+=".java";

            String content = args[1];
            
            File file = new File(output);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
