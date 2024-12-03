package resources.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * NOTE: This class is the same as the MyUtils.MyFile class we made in
 * class. I was having issues getting the .jar file to cooperate
 */
public class FileManip {
    // reads from a file and returns data as an ArrayList
    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> myList = new ArrayList<>();
        
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;

            while ( (str = br.readLine()) != null) {
                myList.add(str);
            }

            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return myList;
    }

    // writes a list of data to a given file
    public static void writeFile(ArrayList<String> list, String filename) {
        try {
            File file = new File(filename);
            PrintWriter pw = new PrintWriter(file);

            for (int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }

            pw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
