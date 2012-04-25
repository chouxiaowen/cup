import java.util.*;
import java.io.*;

class Action implements Serializable {
    
    private static final long serialVersionUID = 1L;
    int numAt;
    int numRt;
    int numCmt;
    
    Action() {
    }
    
    Action(int na, int nr, int nc) {
        numAt = na;
        numRt = nr;
        numCmt = nc;
    }
}

// Action Graph
public class ActGraph implements Serializable {
    
    private static final long serialVersionUID = 1L;
    HashMap<Integer, HashMap<Integer, Action>> actions;
    
    ActGraph() {    
    }
    
    ActGraph(String fname) {
        loadFile(fname);
    }
    
    void loadFile(String fname){
        System.out.print("Reading ActGraph from text...");
        actions = new HashMap<Integer, HashMap<Integer, Action>> ();
        try {
            File fin = new File(fname);
            FileReader fread = new FileReader(fin);
            BufferedReader bread = new BufferedReader(fread);
            
            String line = bread.readLine();
            while (line != null) {
                String [] tmp = line.split("\t");
                Integer src = Integer.parseInt(tmp[0]);
                Integer des = Integer.parseInt(tmp[1]);
                
                HashMap<Integer, Action> tmpVal = actions.get(src);
                if (tmpVal == null) {
                    tmpVal = new HashMap<Integer, Action>();
                }
                tmpVal.put(des, new Action(Integer.parseInt(tmp[2]), 
                                Integer.parseInt(tmp[3]), 
                                Integer.parseInt(tmp[4]))
                                );  
                actions.put(src, tmpVal);               
    
                line = bread.readLine();
            }
            bread.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }   
        System.out.println("Done!");
    }
    
    void getStats() {
        Iterator<Map.Entry<Integer, HashMap<Integer, Action>>> it = actions.entrySet().iterator();
        
        int tmpSum = 0;
        int tmpCnt = 0;
        while(it.hasNext()) {
            Map.Entry<Integer, HashMap<Integer, Action>> etr = it.next();
            
            tmpSum += etr.getValue().size();
            tmpCnt++;
        }   
        System.out.println(tmpSum);
        System.out.println(tmpCnt);
    }
}
