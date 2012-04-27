import java.util.*;
import java.io.*;

class SVM {
    
    public static void train(String dirTrain, String fileFv) throws IOException, InterruptedException {
        System.out.println("Start training SVM...");
        
        fileFv = dirTrain + fileFv;        
        String fileFvScale = fileFv+".scale";

       
        String argsTrain;
        argsTrain = "-b 1 "+fileFvScale+" "+fileFv+".model";
        svm_train.main(argsTrain.split(" "));
        
        System.out.println("Done!");
    }
    
    public static void pred(String dirTrain, String fileTestFv, String fileTrainFv, String fileRes) throws IOException, InterruptedException {
        System.out.println("Start prediction using SVM...");
        
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
    public static void prepSubmit(String dirData, String dirTrain, String fileTest, String fileRes, String fileSubmit) {
        LabelSet testSet = new LabelSet(dirTrain, fileTest, false);
        Graph graph = new Graph(dirData+"user_sns.txt");
        ItemSet itemSet = new ItemSet(dirData, "item.txt");
        
        testSet.updateLabels(dirTrain, fileRes);
//        testSet.abuseLabel(2, graph, itemSet);
        testSet.outputSubmitFile(dirTrain+fileSubmit);
    }
    
    public static void loadFromText(String dir) {
//      UserSet userSet = new UserSet(dir+"user_profile.txt", dir+"user_key_word.txt");
//      Graph graph = new Graph(dir+"user_sns.txt");
//      ActGraph agraph = new ActGraph(dir+"user_action.txt");
//      LabelSet labelSet = new LabelSet(dir, "rec_log_train.txt", true);
    }
    
    public static void buildFeatureMatrix(String dirData, String dirTrain, String fileFv) { 
      Graph graph = new Graph(dirData+"user_sns.txt");
      ItemSet itemSet = new ItemSet(dirData, "item.txt");
      UserSet userSet = new UserSet(dirData+"user_profile.txt", dirData+"user_key_word.txt");
      ActGraph actGraph = null; //new ActGraph(dirData+"user_action.txt");
      
      LabelSet labelSet;
      labelSet = new LabelSet(dirData, "rec_log_train.txt", true);
      Feature.outputFeatureMatrix(dirTrain+fileFv+".prescale", graph, itemSet, userSet, actGraph, labelSet, true);
      
      labelSet = new LabelSet(dirData, "rec_log_test.txt", false);
      Feature.outputFeatureMatrix(dirTrain+fileFv+".prescale", graph, itemSet, userSet, actGraph, labelSet, false);
      
      //* scale the feature vector files
      try {
          String [] c1 = {"/bin/sh", "-c", "svm-scale -l 0 -s "+ dirTrain+"tmp.scale " + dirTrain+fileFv+".prescale" + " > " + dirTrain+fileFv};
          System.out.println(c1[2]);
          Process p1 = Runtime.getRuntime().exec(c1);
          p1.waitFor();
      
          String [] c2 = {"/bin/sh", "-c", "svm-scale -l 0 -r "+ dirTrain+"tmp.scale " + dirTrain+fileFv+".prescale" + " > " + dirTrain+fileFv};
          System.out.println(c1[2]);
          Process p2 = Runtime.getRuntime().exec(c2);
          p2.waitFor();
          
          String [] c3 = {"/bin/sh", "-c", "rm *.prescale tmp.scale"};
          System.out.println(c1[2]);
          Process p3 = Runtime.getRuntime().exec(c3);
          p3.waitFor();
      } catch (Exception ex) {
          ex.printStackTrace();
      }
    }
    
    public static void main(String[] args) {
    // TODO Auto-generated method stub
        try {
            String dirData = "data/";
            String dirTrain = "train/";
            String fileTrainFv = "train_kernel_small.fv";
            String fileTestFv = "test_kernel_small.fv";

//          buildFeatureMatrix(dirData, dirTrain, fileTrainFv);
//          SVM.train(dirTrain, fileTrainFv);
            UserSet userSet = new UserSet(dirData+"user_profile.txt", dirData+"user_key_word.txt");
            Analysis.getAgeStats(userSet);
//          ItemSet itemSet = new ItemSet(dirData, "item.txt");

            //Analysis.getCatStats(itemSet);           
//          LabelSet testSet = new LabelSet(dirData, "rec_log_test.txt", false);
//          buildFeatureMatrix(dirData, dirTrain, fileTestFv, testSet, false);
//          SVM.pred(dirTrain, fileTestFv, fileTrainFv, fileTestFv+".res");
//          prepSubmit(dirData, dirTrain, "rec_log_test.txt", fileTestFv+".res", "submit2.0.csv");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class Analysis {
    //--------- In this class are simple data analysis functions that probably need to run once-in-a-life-time 
    
    //* Compare the active domain of user ids and item ids
    //* Result: EVERY ITEM IS A USER
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
    // Get to know the depth of category hierarchy. ONE TIME THING
    // RESULTS: THREE POSSIBLE DEPTHS 2, 3, 4. All depth share the same domain, e.g., 1.2.1.3 -- the first 1 and third 1 have nothing to do with each other
    public static void getCatStats(ItemSet itemSet) {
        ArrayList<HashSet<Short>> cnt = new ArrayList<HashSet<Short>> (Collections.nCopies(4, new HashSet<Short>()));
        for(Item item: itemSet.items.values()) {
            for(int i=0; i<item.getCat().size(); ++i) {
                HashSet<Short> tmpVal = cnt.get(i);
                tmpVal.add(item.getCat().get(i));
                cnt.set(i, tmpVal);
            }
        }
        for (int i=0; i<cnt.size(); i++) {
            System.out.println(cnt.get(i));
        }
//        System.out.println(cnt);
    }
    
    public static void getAgeStats(UserSet userSet) {
        ArrayList<User> tmp = new ArrayList<User>(userSet.users.values());
        ArrayList<Integer> years = new ArrayList<Integer>();
        long sumYear = 0;

        for (int i=0; i<tmp.size(); ++i) {
            int tmpYear = tmp.get(i).year;
            if (tmpYear > 1900) {
                if (tmpYear < 1950 || tmpYear > 2010) {
                    System.out.println(tmpYear);
                }
                years.add(tmpYear);
                sumYear += tmpYear;
            }
        }
        Collections.sort(years);
        System.out.println("# ppl have age information: " + years.size());
        System.out.println("age median: " + years.get(years.size()/2));
        System.out.println("age mean: " + (int)(sumYear*1.0/years.size()));
        
        LabelSet labelSet = new LabelSet("data/", "rec_log_train.txt", false);
        years = new ArrayList<Integer>();
        sumYear = 0;
        
        tmp = new ArrayList<User> ();
        for(LabelRec rec: labelSet.all) {
            if (rec.label > 0) {
                tmp.add(userSet.getUser(rec.userId));
            }
        }
        
        for (int i=0; i<tmp.size(); ++i) {
            int tmpYear = tmp.get(i).year;
            if (tmpYear > 1950 && tmpYear < 2010) {
                years.add(tmpYear);
                sumYear += tmpYear;
            }
        }
        Collections.sort(years);
        System.out.println("# ppl have age information: " + years.size());
        System.out.println("age median: " + years.get(years.size()/2));
        System.out.println("age mean: " + (int)(sumYear*1.0/years.size()));
        
    }
}
