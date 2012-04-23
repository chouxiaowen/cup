import java.util.*;
import java.io.*;

public class Run {
    
    public static void loadFromText(String dir) {
//      ItemSet itemSet = new ItemSet(dir, "item.txt");
//      UserSet userSet = new UserSet(dir+"user_profile.txt", dir+"user_key_word.txt");
//      Graph graph = new Graph(dir+"user_sns.txt");
//      ActGraph agraph = new ActGraph(dir+"user_action.txt");
//      LabelSet labelSet = new LabelSet(dir, "rec_log_train.txt", true);
    }
    
    public static void runSVM(String fileFv) throws IOException, InterruptedException {

        System.out.println("Start running SVM...");

        String fileFvScale = fileFv+".scale";

        String [] cmd = {"/bin/sh", "-c", "svm-scale " + fileFv + " > " + fileFvScale};
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
                    
        String svmArgs;
        svmArgs = "-t 0 -v 2 "+fileFvScale;
        svm_train.main(svmArgs.split(" "));

        System.out.println("Done!");
    }
    
    public static void main(String[] args) {
    // TODO Auto-generated method stub
        
        try {
            String dir = "data/";
            String dirTrain = "train/";
            String fileFv = dirTrain+args[0];
            
//          LabelSet trainSet = new LabelSet(dir, "rec_log_train.txt", true);        
            LabelSet testSet = new LabelSet(dir, "rec_log_test.txt", false);
            testSet.outputSubmitFile(dirTrain+"submit.csv");
//          Graph graph = new Graph(dir+"user_sns.txt");        
//          Feature.outputFeatureMatrix(fileFv, graph, labelSet);
            
       
//      Analysis.cmpDomain(userSet, itemSet, graph);
//      System.out.println(itemSet.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class Analysis {
    
    // Compare the active domain of user ids and item ids
    // Result: EVERY ITEM IS A USER
    
    public static void cmpDomain(UserSet userSet, ItemSet itemSet, Graph graph) {
        HashMap<Integer, Item> items = itemSet.items;
        HashMap<Integer, User> users = userSet.users;
        
        Iterator<Map.Entry<Integer, Item>> it = items.entrySet().iterator();
        
        int cntInter = 0;
        int cntDiff = 0;
        
        while (it.hasNext()) {
            Map.Entry<Integer, Item> etr = it.next();
            if (users.get(etr.getKey()) != null) {
                cntInter++;
                
                System.out.println(graph.getInDegree(etr.getKey()));
            }
            else {
                cntDiff++;
            }
        }
        System.out.println("Number of item ids also in user ids: " + cntInter + " while " + cntDiff + "not.");
    }
}
