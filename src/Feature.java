import java.io.*;
import java.util.*;

class FeatureVec {
    double label;
    ArrayList<Double> fv;
    
    FeatureVec() {
    }
    
    FeatureVec(double lbl, ArrayList<Double> vec) {
        label = lbl;
        fv = vec;
    }
    
    public String toString() {
        String ret = "";
        ret += Double.toString(label);
        for(int i=0; i<fv.size(); ++i) {
            ret += " " + Integer.toString(i+1) + ":" + fv.get(i).toString();
        }
        return ret;
    }
}

public class Feature {
    
    Feature() {
    }
    
    // return a similarity feature vector
    public static ArrayList<Double> extractFeatureVector(int userId, int itemId, Graph graph) {
        ArrayList<Double> fv = new ArrayList<Double>();
        
        //// ---- Add a bunch of features ----
        
        // Add Feature: # u's follows
        int numFlwU = graph.getOutDegree(userId);
        fv.add((double)numFlwU);
        
        // Add Feature: # i's fans
        int numFansI = graph.getInDegree(itemId);
        fv.add((double)numFansI);
        
        // Add Feature: # u's follows that follow i
        int numIndFlwUI = graph.getNumIndFlw(userId, itemId);
        fv.add((double)numIndFlwUI);
        
        // Add Feature: # normalize the above by numFlows of u  
        fv.add(numFlwU == 0 ? 0 : (double)numIndFlwUI / numFlwU);
        
        return fv;
    }
   
    public static void outputFeatureMatrix(String fname, Graph graph, LabelSet train) {
        System.out.println("Building feature matrix for training data...");
        try {
            File fout = new File(fname);
            FileWriter fw = new FileWriter(fout);
            BufferedWriter bw = new BufferedWriter(fw);
                
            for (int i=0; i<train.trainSet.size(); ++i) {
                if (i > 0 && i % 100000 == 0) {
                    System.out.println(i);
                    break;
                }
                LabelRec rec = train.trainSet.get(i);
                int uid = rec.userId;
                int iid = rec.itemId;
                ArrayList<Double> fv = extractFeatureVector(uid, iid, graph);
                bw.write(new FeatureVec(rec.label, fv).toString() + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Done!");
    }
}