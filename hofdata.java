/*
 * Michael Staudt
 * CS 1573 - Spring 2013
 * Sports Project
 * 
 */
package hofdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;


public class Hofdata {

    
    public static void main(String[] args) throws IOException {
        String file = "Batting.csv";
        
        File datafile = new File(file);
        if(!datafile.exists()) {
            System.out.println("Could not find data file, please check your directory.");
            System.exit(1);
        }
        
        //count the total number of lines that we will be dealing with for for loops
        int totalLines;
        totalLines = countLines(file);
        
        System.out.println("Total number of lines in Data File: " + totalLines);
       
        //get the first line of the data file
        //has column information
        BufferedReader br = new BufferedReader(new FileReader(file));
        String dataLine;
        String temp[];
        dataLine = br.readLine();//get first line in file
        //split by comma
        temp = dataLine.split(",");
        /*System.out.println("The Data Contains the Following (in this order)...");
        for(int x = 0; x < temp.length;x++){
            System.out.println(temp[x]);
        }*/
        
        //create matrix (lines-1 x data columns)
        String data[][] = new String[totalLines-1][temp.length];
        //fill the matrix
        String nextLine;
        for(int y = 0; y<totalLines-1;y++){
            String temp2[];//temp2 for each line
            nextLine = br.readLine();//get line
            temp2 = nextLine.split(",");//split line
            for(int z = 0; z < temp.length ; z++){
                data[y][z] = temp2[z];//add line to data matrix
            }
        }
        /*for(int q = 0;q<temp.length;q++){
            System.out.print(data[96599][q]+ " - ");
        }
        System.out.println("");*///Just Checked to make sure the last line was added correctly
        
        br.close();//done with file
        //so, now we have the data in the matrix called "data" and the column headers in the array
        //called "temp"
        
        //next, go through and find matching playerID's, total up the stats and make a
        //new data matrix
        //Since the actual number of unique players in unknown we need to continue with
        //ArrayList or arrays so the array of players can be dynamic
        ArrayList<String[]> newData = new ArrayList<String[]>();
        String current[];//for current player
        String next[];//array for next read in player
        int players = 0;//count number of players
        //read in first player from data
        current  = data[0];
        for(int w = 1; w < totalLines-1; w++){
            /*for every line check for the player id
            * since we know the ID's are in alphabetical order we can run 
            * through the data from start to finish
            * when we find an ID match for the current player, add the stats
            * for the current line to the players career stats
            * when we do not find a match, we know that it is the next player
            * in the list and we can add the current player to the ArrayList
            * and move the next player into the current array
            */
            next = data[w];
            int value;
            if(current[0].equals(next[0])){//ID match
                for(int a = 1; a < temp.length ; a++){
                    if(a<5){//first 4 columns dont really matter
                        current[a] = current[a] + next[a];//update stats
                    }
                    else{//after that we need to make sure we are adding the values
                        //and not the strings
                        value = Integer.parseInt(current[a]) + Integer.parseInt(next[a]);
                        current[a] = Integer.toString(value);
                        
                    }
                }
            }
            else{//Id does not match
                //System.out.println("Career Stats Calculated for " + current[0]);
                players++;//update player count so we know how bit the Array List is
                newData.add(current);//add current player to ArrayList
                current = next;//move next player to current player
            }
            
        }
        /*System.out.println("Career Stats for " + newData.get(0)[0]);
        for(int b = 1 ; b < temp.length ; b++){
            System.out.println(temp[b] + ": " + newData.get(0)[b]);
        }//Just used for a check of total*/
        System.out.println("Total Number of Players: " + players);
        //newData will now hold the career stats
        //so we can print out the data to a new file (separation = ",")
        
        //new file will throw out useless data [1]-[4]
        System.out.println("Making Comma Del File");
        FileWriter tf = new FileWriter("BattingCareer.txt");
            BufferedWriter newWrite = new BufferedWriter(tf);
            //write the header line
            for(int c = 0; c<temp.length; c++){
                if(c==0){
                    newWrite.write(temp[c] + ",");
                }
                else if(c>4 & c <temp.length-1){
                    newWrite.write(temp[c] + ",");
                }
                else if(c == temp.length-1){
                    newWrite.write(temp[c] + "\n");
                }
            }
            for(int d = 0; d<players;d++){
                for(int x = 0; x < temp.length ;x++){
                    if(x==0){
                        newWrite.write(newData.get(d)[x] + ",");
                    }
                    else if(x>4 & x <temp.length-1){
                        newWrite.write(newData.get(d)[x] + ",");
                    }
                    else if(x == temp.length-1){
                        newWrite.write(newData.get(d)[x] + "\n");
                    }
                }
            }
            newWrite.write("-\n");
            newWrite.close();
        
        
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
