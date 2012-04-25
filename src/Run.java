import java.util.*;
import java.io.*;

class SVM {
    
    public static void train(String dirTrain, String fileFv) throws IOException, InterruptedException {
        System.out.println("Start training SVM...");
        
        fileFv = dirTrain + fileFv;        
        String fileFvScale = fileFv+".scale";

        String [] cmd = {"/bin/sh", "-c", "svm-scale " + fileFv + " > " + fileFvScale};
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        
        String argsTrain;
        argsTrain = "-b 1 -t 0 "+fileFvScale+" "+fileFv+".model";
        svm_train.main(argsTrain.split(" "));
        
        System.out.println("Done!");
    }
    
    public static void pred(String dirTrain, String fileTestFv, String fileTrainFv, String fileRes) throws IOException, InterruptedException {
        System.out.println("Start testing SVM...");
        
        fileTestFv = dirTrain + fileTestFv;
        fileTrainFv = dirTrain + fileTrainFv;
        fileRes = dirTrain + fileRes;
        
        String argsPred = "-b 1 " + fileTestFv + " " + fileTrainFv+".model " + fileRes;
        System.out.print(argsPred);
        svm_predict.main(argsPred.split(" "));
        System.out.println("Done!");
    }
}

public class Run {
    // prepare a submission file fSubmit out of file fRes, both in folder dirTrain
    public static void prepSubmit(String dirData, String dirTrain, String fRes, String fSubmit) {
        LabelSet testSet = new LabelSet(dirTrain, fRes, false);
        Graph graph = new Graph(dirData+"user_sns.txt");
        testSet.abuseLabel(1, graph);
        testSet.outputSubmitFile(dirTrain+fSubmit);
    }
    
    public static void loadFromText(String dir) {
//      ItemSet itemSet = new ItemSet(dir, "item.txt");
//      UserSet userSet = new UserSet(dir+"user_profile.txt", dir+"user_key_word.txt");
//      Graph graph = new Graph(dir+"user_sns.txt");
//      ActGraph agraph = new ActGraph(dir+"user_action.txt");
//      LabelSet labelSet = new LabelSet(dir, "rec_log_train.txt", true);
    }
    
    public static void buildFeatureMatrix(String dirData, String dirTrain, String fileFv, LabelSet labelSet) { 
      Graph graph = new Graph(dirData+"user_sns.txt");
      Feature.outputFeatureMatrix(dirTrain+fileFv, graph, labelSet);
    }
    
    public static void main(String[] args) {
    // TODO Auto-generated method stub
        try {
            String dirData = "data/";
            String dirTrain = "train/";
            String fileTrainFv = "train.fv";
            String fileTestFv = "test.fv";
            
//          prepSubmit(dirData, dirTrain, "rec_log_test.txt", "submit1.csv");
//          buildFeatureMatrix(dirData, dirTrain, fileFv);
//          SVM.train(dirTrain, fileFv);
            
            LabelSet testSet = new LabelSet(dirData, "rec_log_test.txt", false);
            buildFeatureMatrix(dirData, dirTrain, fileTestFv, testSet);
            SVM.pred(dirTrain, fileTestFv, fileTrainFv, "test1.res");

//          Analysis.cmpDomain(userSet, itemSet, graph);
//          System.out.println(itemSet.size());
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
