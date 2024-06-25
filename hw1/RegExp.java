import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RegExp {
    
    public static void main(String[] args) {
        String str1 = args[1];
        String str2 = args[2];
        int s2Count = Integer.parseInt(args[3]);

        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray(); 

        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String s;
            while ((s = reader.readLine()) != null) {
                
                s = s.toLowerCase();
                char[] arr = s.toCharArray();

                boolean check1 = true , check2 = false , check3 = false , check4 = false ;

                // check1
                for( int i = 0 ; i<(arr.length+1)/2 ; i++ ){
                    if( arr[i] != arr[arr.length-1-i] ) {
                        check1 = false ;
                        break;
                    }
                }

                //check2
                for( int i = 0 ; i<arr.length-arr1.length+1 ; i++ ){
                    boolean tmp = true;
                    for( int j = 0 ; j<arr1.length ; j++ ){
                        if( arr[i+j] != arr1[j] ) {
                            tmp = false;
                            break;
                        }
                    }
                    if(tmp){
                        check2 = true ;
                        break;
                    }
                }

                //check3
                int cnt = 0 ;
                for( int i = 0 ; i<arr.length-arr2.length+1 ; i++ ){
                    boolean tmp = true;
                    for( int j = 0 ; j<arr2.length ; j++ ){
                        if( arr[i+j] != arr2[j] ) {
                            tmp = false;
                            break;
                        }
                    }
                    if(tmp) cnt++ ;
                }
                if( cnt >= s2Count ) check3 = true;
                
                //check4
                boolean checka = false ;
                for( int i = 0 ; i<arr.length-1 ; i++ ){
                    if( arr[i] == 'a' ) checka = true;
                    if( checka ){
                        if( arr[i] == 'b' && arr[i+1] == 'b' ){
                            check4 = true ;
                            break;
                        }
                    }
                }

                // ans
                if( check1 ) System.out.print("Y,");
                else System.out.print("N,");
                if( check2 ) System.out.print("Y,");
                else System.out.print("N,");
                if( check3 ) System.out.print("Y,");
                else System.out.print("N,");
                if( check4 ) System.out.println("Y");
                else System.out.println("N");
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


