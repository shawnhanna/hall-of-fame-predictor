
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 *
 * @author Michael Staudt
 * Program that reads in an ID3 configuration file and an ID3 data file
 * and turns it into 1 Weka .arff input file
 * April 2013
 */
public class ID3toWeka {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 2){
            System.out.println("Incorrect Number of Arguments");
            System.out.println("Need config file and data file");
            System.exit(1);
        }
        
        File config = new File(args[0]);
        File data = new File(args[1]);
        
        //error check for files
        if(!config.exists()){
            System.out.println("Could not find config file, please check your directory.");
            System.exit(1);
        }
        if(!data.exists()){
            System.out.println("Could not find data file, please check your directory.");
            System.exit(1);
        }
        
        //all clear
        //read in config file first
        int configLines;
        configLines = countLines(args[0]);
        System.out.println("Total Features: " + (configLines-2));
        
        BufferedReader br1 = new BufferedReader(new FileReader(config));
        //skip first line (function chooser for id3)
        String func = br1.readLine();
        //second line is labels
        String labelLine = br1.readLine();
        String labels[] = labelLine.split(",");
        
        String features[] = new String[configLines-2];
        for(int x = 0; x < configLines - 2; x++){
            features[x] = br1.readLine();
        }
        br1.close();
        
        //get data lines
        int dataLines = countLines(args[1]);
        String inputs[] = new String[dataLines];
        BufferedReader br2 = new BufferedReader(new FileReader(data));
        
        for(int y = 0; y < dataLines ; y++){
            inputs[y] = br2.readLine();
        }
        br2.close();
        
        //make Weka file
        FileWriter fw = new FileWriter("hof.arff");
        BufferedWriter weka = new BufferedWriter(fw);
        
        //could change this so the relation & header correspondes to the project
        //but for now we know we are using it for this
        weka.write("%HOF DATA FOR WEKA\n");
        weka.write("@relation HOF\n");
        String temp[];
        for(int a = 0; a < features.length; a++){
            temp = features[a].split(",");
            for(int b = 0; b<temp.length ; b++){
                if(b==0){
                    weka.write("@attribute '" + temp[b] + "' { ");
                }
                else if(b==(temp.length-1)){
                    weka.write("'"+ temp[b]+"'}\n");
                }
                else{
                    weka.write("'"+ temp[b]+"' , ");
                }
            }
        }
        
        weka.write("@attribute 'class' { ");
        for(int c = 0;c<labels.length;c++){
            if(c==(labels.length-1)){
                weka.write("'"+ labels[c]+"'}\n");
            }
            else{
                weka.write("'"+ labels[c]+"', ");
            }
        }
        weka.write("@data\n");
        String temp2[];
        for(int d = 0; d< inputs.length ; d++){
            temp2 = inputs[d].split(",");
            for(int e = 0; e < temp2.length ; e++){
                if(e==(temp2.length -1 )){
                    weka.write("'"+temp2[e]+"'\n");
                }
                else{
                    weka.write("'"+temp2[e]+"',");
                }
            }
        }
        weka.write("%END WEKA FILE\n");
        weka.close();
        fw.close();
    }
    
    //function to count the number of lines
    public static int countLines(String filename) throws IOException {
        LineNumberReader reader  = new LineNumberReader(new FileReader(filename));
        int cnt = 0;
        String lineRead = "";
        while ((lineRead = reader.readLine()) != null) {}

        cnt = reader.getLineNumber(); 
        reader.close();
        return cnt;
    }
}
