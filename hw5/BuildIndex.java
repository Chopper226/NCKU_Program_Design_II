import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class BuildIndex {
    public static void main(String[] args) {

        Indexer idx = new Indexer(args[0]);
        String output = getOutputFileName(args[0]);

        try {
            FileOutputStream fos = new FileOutputStream(output);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(idx);
            oos.close();
            fos.close();
        } 
        catch (IOException e) {
            e.printStackTrace();	
        }
    }

    private static String getOutputFileName(String inputFileName ) {
        File file = new File(inputFileName);
        String baseName = file.getName();
        String outputFileName = baseName.replaceAll("\\.txt$", ".ser");
        return outputFileName;
    }
}
