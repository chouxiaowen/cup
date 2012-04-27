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
    
    // Measure how much the category of item i is overlapped with that of u's currently following items
    public static ArrayList<Double> getItemCatOverlap(int userId, int itemId, Graph graph, ItemSet itemSet) {
        
        HashSet<Integer> tmp = graph.getFlwItems(userId);
        ArrayList<Double> scores = new ArrayList<Double>(Collections.nCopies(4, 0.0));
        if(tmp == null) {
            return scores;
        }
        
        ArrayList<Short> newItemCat = itemSet.getItem(itemId).getCat();
        Iterator<Integer> it = tmp.iterator();
        while(it.hasNext()) {
            int des = it.next();
            Item tmpItem = itemSet.getItem(des);
            if (tmpItem == null) {
                System.out.println("Item info is missing -- make sure it's really an item id.");
            }
            ArrayList<Short> tmpItemCat = tmpItem.getCat();
            for (int i=0; i<scores.size(); ++i) {
                if (i < tmpItemCat.size() && i < newItemCat.size() && tmpItemCat.get(i) == newItemCat.get(i)) {
                    scores.set(i, scores.get(i) + 1.0);
                } else {
                    break;
                }
            }
        }
        return scores;
    }
    
    // Return a similarity feature vector
    public static ArrayList<Double> extractFeatureVector(int userId, int itemId, Graph graph, ItemSet itemSet, UserSet userSet, ActGraph actGraph) {
        ArrayList<Double> fv = new ArrayList<Double>();
        
        //// ---- Add User's features ----
        
        // Add Feature: user's info
        fv.add((double)userSet.getUser(userId).gender);
        fv.add((double)userSet.getUser(userId).numTweets);
        fv.add((double)userSet.getUser(userId).year);
        
        // Add Feature: # u's follows
        int numFlwU = graph.getOutDegree(userId);
        fv.add((double)numFlwU);
        
        // Add Feature: # i's fans
        int numFansI = graph.getInDegree(itemId);
        fv.add((double)numFansI);
        
        // Add Feature: # u's follows that follow i
        int numIndFlwUI = graph.getNumIndFlw(userId, itemId);
        fv.add((double)numIndFlwUI);
        
        // Add Feature: normalize the above # by numFlows of u  
        fv.add(numFlwU == 0 ? 0 : (double)numIndFlwUI / numFlwU);
                
        // Add Feature: # items u is following
        fv.add((double)graph.getNumIndFlwItem(userId, itemId, itemSet));
        
        // Add Feature: how much category overlap is u's following item similar to the new item
        fv.addAll(getItemCatOverlap(userId, itemId, graph, itemSet));
        
        
        return fv;
    }
    
    public static void outputFeatureMatrix(String fname, Graph graph, ItemSet itemSet, UserSet userSet, ActGraph actGraph, LabelSet labelSet, boolean sample) {
        System.out.println("Building feature matrix...");
        try {
            FileWriter fw = new FileWriter(fname);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);    
            
            for (int i=0; i<labelSet.all.size(); ++i) {
                if (i > 0 && i % 100000 == 0) {
                    System.out.println(i*1.0/labelSet.all.size()*100 + "% completed");
                }
                
                LabelRec rec = labelSet.all.get(i);
                int uid = rec.userId;
                int iid = rec.itemId;
                
                if (sample) {
                    Random rand = new Random();
                    int chance = 0;
                    if(rec.label > 0) 
                        chance = rand.nextInt(1000);
                    else
                        chance = rand.nextInt(10000);
                
                    if(chance != 0)
                        continue;
                }
                ArrayList<Double> fv = extractFeatureVector(uid, iid, graph, itemSet, userSet, actGraph);
                pw.println(new FeatureVec(rec.label, fv).toString());
            }
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Done!");
    }
}
