import java.util.*;
import java.io.*;

class LabelRec {
    int userId;
    int itemId;
    double label;
    long tstamp;
    
    LabelRec() {
    }
    
    LabelRec(String line) {
        String [] tmp = line.split("\t");
        
        userId = Integer.parseInt(tmp[0]);
        itemId = Integer.parseInt(tmp[1]);
        label = Double.parseDouble(tmp[2]);
        tstamp = Integer.parseInt(tmp[3]);  
    }
}

class ItemRecomm implements Comparable<ItemRecomm> {
    int itemId;
    double prob;
    
    ItemRecomm() {
        
    }
    
    ItemRecomm(int i, double p) {
        itemId = i;
        prob = p;
    }
    
    
    public int compareTo(ItemRecomm cmp) { 
        //ascending order
        return (int)(cmp.prob - this.prob); 
    }
}

public class LabelSet { 
    ArrayList<LabelRec> trainSet;
    ArrayList<LabelRec> testSetPub;
    ArrayList<LabelRec> testSetFinal;
    
    HashMap<Integer, Integer> itemCnt;
    HashMap<Integer, Integer> itemAcCnt;
    
    LabelSet() {
    }
    
    LabelSet(String dir, String fname, Boolean flag) {
        loadFile(dir, fname, flag);
        init();
    }

    LabelSet(String dir, String fnameTrain, String fnameTest) {
        loadFile(dir, fnameTrain, true);
        loadFile(dir, fnameTrain, false);
        init();
    }
    
    void init() {
        if (trainSet != null)
            buildItemCnts();
        if (testSetPub != null) {
            splitTestSet();
        }
    }
    
    void splitTestSet() {
        for (int i=0; i<testSetPub.size(); ++i) {
            if(testSetPub.get(i).tstamp >= 1321891200) {
                List<LabelRec> half = testSetPub.subList(i, testSetPub.size());
                testSetFinal = new ArrayList<LabelRec>(half);
                half.clear();
                break;
            } 
        }
    }
    
    void loadFile(String dir, String fname, Boolean flag) {
        ArrayList<LabelRec> tmpPtr;
        
        if (flag) {
            System.out.println("Reading training set from text... ");
            trainSet = new ArrayList<LabelRec>();
            tmpPtr = trainSet;
        } else {
            System.out.println("Reading testing set from text... ");
            testSetPub = new ArrayList<LabelRec>();
            tmpPtr = testSetPub;
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
    
    void outputSubmitFile(String fname) {
        outputSubmitFile(fname, testSetPub, false);
//        outputSubmitFile(fname, testSetFinal, true);
    }
    
    void outputSubmitFile(String fname, ArrayList<LabelRec> testSet, boolean appendFlag) {
        if (testSet == null) {
            System.out.print("Cannot output submission file when there is no test results!");
            return ;
        }
        
        SortedMap<Integer, ArrayList<ItemRecomm>> recomms = new TreeMap<Integer, ArrayList<ItemRecomm>>();
        for (int i=0; i<testSet.size(); ++i) {
            LabelRec rec = testSet.get(i);
            int u = rec.userId;
            ItemRecomm recomm = new ItemRecomm(rec.itemId, rec.label);
            
            ArrayList<ItemRecomm> tmpList = recomms.get(u);
            if (tmpList == null) {
                tmpList = new ArrayList<ItemRecomm>();
                tmpList.add(recomm);
            } else {
                tmpList.add(recomm);   
            }
            recomms.put(u, tmpList);
        }
        
        try {
//            File fout = new File(fname);
            FileWriter fw = new FileWriter(fname, appendFlag);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            
            
            Iterator<Map.Entry<Integer, ArrayList<ItemRecomm>>> it = recomms.entrySet().iterator();
            int cnt = 0;
            while(it.hasNext()) { 
                Map.Entry<Integer, ArrayList<ItemRecomm>> etr = it.next();
            //    ArrayList<ItemRecomm> tmpList = etr.getValue();
            //    Collections.sort(tmpList);
            //    bw.write(etr.getKey().toString() + ",");
            //    for (int i=0; i<tmpList.size(); ++i) {
            //        if(i > 2) {
            //            break;
            //        }
             //       if (i > 0) {
            //            bw.write(" ");
            //        }
            //        bw.write(Integer.toString(tmpList.get(i).itemId));
             //   }
                pw.print("\n");
            //  bw.write("\n");
                cnt++;
            }
            System.out.println(cnt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//**  
// For each item appeared in the train data, count its # occurrences and # being accepted
//**
    void buildItemCnts() {
        itemCnt = new HashMap<Integer, Integer>();
        itemAcCnt = new HashMap<Integer, Integer>(); 
        
        for (LabelRec rec: trainSet) {
            Integer tmpVal = itemCnt.get(rec.itemId);
            if (tmpVal == null) {
                itemCnt.put(rec.itemId, 1);
            } else {
                itemCnt.put(rec.itemId, tmpVal+1);
            }
            
            if(rec.label == -1) {
                continue;
            }
            
            tmpVal = itemAcCnt.get(rec.itemId);
            if (tmpVal == null) {
                itemAcCnt.put(rec.itemId, 1);
            } else {
                itemAcCnt.put(rec.itemId, tmpVal+1);
            }
        }
        
//        Iterator<Map.Entry<Integer, Integer> > it = itemCnt.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<Integer, Integer> etr = it.next();
//            int tmpK = etr.getKey();
//            int tmpV = etr.getValue();
//            System.out.println(tmpK + "\t" + tmpV + "\t" + (itemAcCnt.get(tmpK) != null ? itemAcCnt.get(tmpK) : 0));      
//        }
    }

    
    
////////////////----------------- Queries ----------------------   

    public int getItemCnt(int itemId) {
        Integer val = itemCnt.get(itemId);
        if (val == null) {
            return 0;
        } else {
            return val;
        }
    }
    
    public int getItemAcCnt(int itemId) {
        Integer val = itemAcCnt.get(itemId);
        if (val == null) {
            return 0;
        } else {
            return val;
        }
    }
    
    public void getStats() {
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
                if (val != null)
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
