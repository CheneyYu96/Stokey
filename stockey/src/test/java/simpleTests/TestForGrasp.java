package simpleTests;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuminchen on 16/5/30.
 */
public class TestForGrasp {


    static String pathName = "/Users/yuminchen/Documents/mytech/workspace(java)/StockEy/stockey/src/main/java/org/xeon/stockey/businessLogic/user/UserImpl.java";

    static List<String> results = new ArrayList<>();

    public static void readSpring(){
        File file = new File(pathName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine())!=null){
                if(line.contains("@RequestMapping")){

                    int index = line.lastIndexOf("@RequestMapping");
                    String sub = line.substring(index);

                    int nextIndex = sub.indexOf("(");
                    int lastIndex = sub.indexOf(")");
                    String result = sub.substring(nextIndex+2,lastIndex-1);

                    results.add(result+":");
                }
                else if(line.contains("@RequestParam")){
                    int index = line.lastIndexOf("@RequestParam");
                    String sub = line.substring(index);

                    int nextIndex = sub.indexOf("(");
                    int lastIndex = sub.indexOf(")");
                    String result = sub.substring(nextIndex+2,lastIndex-1);

                    results.add("  "+result);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        for(String s : results){
            System.out.println(s);
        }

    }

    public static void main(String[] args) {
        TestForGrasp.readSpring();
    }
}
