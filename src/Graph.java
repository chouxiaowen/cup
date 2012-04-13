import java.util.*;
import java.io.*;

public class Graph implements Serializable {
    
    private static final long serialVersionUID = 1L;
    HashMap<Integer, HashSet<Integer>> adjList;
    HashMap<Integer, HashSet<Integer>> revAdjList;
    
    Graph() {
    }
    
    Graph(String fname) {
        loadFile(fname);
    }
    
    void loadFile(String fname) {
        System.out.print("Reading Graph from text...");
        adjList = new HashMap<Integer, HashSet<Integer>> ();
        try {
            File fin = new File(fname);
            FileReader fread = new FileReader(fin);
            BufferedReader bread = new BufferedReader(fread);
        
            String line = bread.readLine();
            while (line != null) {
                String [] tmp = line.split("\t");
                
                int tmpKey = Integer.parseInt(tmp[0]);
                HashSet<Integer> tmpVal = adjList.get(tmpKey);
            //  System.out.print(tmpVal);
                if (tmpVal == null) {
                    tmpVal = new HashSet<Integer>();
                }
                tmpVal.add(Integer.parseInt(tmp[1]));       
                adjList.put(tmpKey, tmpVal);
                
                line = bread.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Done!");
    }
    
    int getOutDegree(int u) {
        if (adjList.get(u) == null) {
            return 0;
        } else {
            return adjList.get(u).size();
        }
    }
    
    int getInDegree(int u) {
        if (revAdjList == null) {
            buildRevGraph();
        }
        if (revAdjList.get(u) == null) {
            return 0;
        } else {
            return revAdjList.get(u).size();
        }
    }
    
    void getStats() {
        Iterator<Map.Entry<Integer, HashSet<Integer>>> it = adjList.entrySet().iterator();
        int tmpSum = 0;
        int tmpCnt = 0;
        while (it.hasNext()) {
            Map.Entry<Integer, HashSet<Integer>> etr = it.next();
            tmpSum += etr.getValue().size();
            tmpCnt ++;
        }
        System.out.println(tmpCnt);
        System.out.println(tmpSum);
    }
    
    void buildRevGraph() {
        System.out.print("Constructing Reverse Graph...");
        if (revAdjList != null)
            return ;
        
        revAdjList = new HashMap<Integer, HashSet<Integer>> ();
        Iterator<Map.Entry<Integer, HashSet<Integer>>> it = adjList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, HashSet<Integer>> etr = it.next();       
            int curSrc = etr.getKey();
            Iterator<Integer> it2 = etr.getValue().iterator();
            while (it2.hasNext()) {
                int curDes = it2.next();
                HashSet<Integer> tmpVal = revAdjList.get(curDes);
                if (tmpVal == null) {
                    tmpVal = new HashSet<Integer> ();
                }
                tmpVal.add(curSrc);
                revAdjList.put(curDes, tmpVal);
            }           
        }
        System.out.println("Finished");
    }
}
