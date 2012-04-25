import java.util.*;
import java.io.*;

public class Graph implements Serializable {
    
    private static final long serialVersionUID = 1L;
    HashMap<Integer, HashSet<Integer>> adjList;
    HashMap<Integer, HashSet<Integer>> revAdjList;

    
//// -------- Graph structure construction functions -------
    
    Graph() {
    }
    
    Graph(String fname) {
        loadFile(fname);
        buildRevGraph();
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
            bread.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Done!");
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
    
//// -------- Queries on the Graph --------
    
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
    
    
    int getSizeIntersect(HashSet<Integer> s1, HashSet<Integer> s2) {
        HashSet<Integer> tmpSet;
        
        if (s1.size() < s2.size()) { 
            tmpSet = new HashSet<Integer>(s1);
            tmpSet.retainAll(s2);
        } else {
            tmpSet = new HashSet<Integer>(s2);
            tmpSet.retainAll(s1);
        }
        return tmpSet.size();
    }
    
    // Count the common in-neighbors of u and v
    int getNumCmnInNB(int u, int v) {
        if (revAdjList.get(u) == null || revAdjList.get(v) == null)
            return 0;
            
        return getSizeIntersect(revAdjList.get(u), revAdjList.get(v));
    }
    
    // Count the common out-neighbors of u and v
    int getNumCmnOutNB(int u, int v) {
        if (adjList.get(u) == null || adjList.get(v) == null) 
            return 0;
        
        return getSizeIntersect(adjList.get(u), adjList.get(v));
    }
    
    // Count the intersection of u's out-neighbor and v's in-neighbor
    // application: count the number of users u is following that follows v
    int getNumIndFlw(int u, int v) {
        if (adjList.get(u) == null || revAdjList.get(v) == null)
            return 0;
        return getSizeIntersect(adjList.get(u), revAdjList.get(v));    
    }
    
}
