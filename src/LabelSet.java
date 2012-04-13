import java.util.*;
import java.io.*;

class LabelRec {
    int userId;
    int itemId;
    int label;
    long tstamp;
    
    LabelRec() {
    }
    
    LabelRec(String line) {
        String [] tmp = line.split("\t");
        
        userId = Integer.parseInt(tmp[0]);
        itemId = Integer.parseInt(tmp[1]);
        label = Integer.parseInt(tmp[2]);
        tstamp = Integer.parseInt(tmp[3]);  
    }
}

public class LabelSet { 
    ArrayList<LabelRec> trainSet;
    ArrayList<LabelRec> testSet;
    
    LabelSet() {
    }
    
    LabelSet(String dir, String fname, Boolean flag) {
        loadFile(dir, fname, flag);
    }

    LabelSet(String dir, String fnameTrain, String fnameTest) {
        loadFile(dir, fnameTrain, true);
        loadFile(dir, fnameTrain, false);
    }
    
    void loadFile(String dir, String fname, Boolean flag) {
        
        ArrayList<LabelRec> tmpPtr;
        
        if (flag) {
            System.out.print("Reading training set from text: ");
            trainSet = new ArrayList<LabelRec>();
            tmpPtr = trainSet;
        } else {
            System.out.print("Reading testing set from text: ");
            testSet = new ArrayList<LabelRec>();
            tmpPtr = testSet;
        }
        
        try {
            File fin = new File(dir+fname);
            FileReader fread = new FileReader(fin);
            BufferedReader bread = new BufferedReader(fread);
            
            String line = bread.readLine();
            while (line != null) {
                tmpPtr.add(new LabelRec(line));
                
                line = bread.readLine();
            }
            System.out.println("Done!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    void getStats() {
        System.out.println("--- Getting statistics about training data --- ");
        int cntPos = 0;
        int cntNeg = 0;
        for (LabelRec r: trainSet) {
            if (r.label == 1) {
                cntPos++;
            }
            else if (r.label == -1) {
                cntNeg++;
            }
            else {
                System.out.println(r.label);
            }
        }
        
        HashMap<Integer, Integer> cntItemPos = new HashMap<Integer, Integer> ();
        HashMap<Integer, Integer> cntItemNeg = new HashMap<Integer, Integer> ();
        
        for (LabelRec r: trainSet) {            
            if (r.label == 1) {
                Integer val = cntItemPos.get(r.itemId);
                if (val != null)
                    cntItemPos.put(r.itemId, val+1);
                else
                    cntItemPos.put(r.itemId, 1);
            }
            else {
                Integer val = cntItemNeg.get(r.itemId);
                if(val != null)
                    cntItemNeg.put(r.itemId, val+1);
                else
                    cntItemNeg.put(r.itemId, 1);
            }
        }
        
        Iterator<Map.Entry<Integer, Integer>> it = cntItemPos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> etr = it.next();
            if (etr.getValue() > 1) { 
                System.out.println(etr.getValue() + " " + etr.getKey());
            }
        }
        
        System.out.println("Size of training set" + trainSet.size());
        System.out.println("Number of positive examples: " + cntPos);
        System.out.println("Number of negative examples: " + cntNeg);
        System.out.println("--- Done --- ");
    }
}
