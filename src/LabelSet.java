import java.util.*;
import java.io.*;

class LabelRec {
	int userId;
	int itemId;
	int label;
	int tstamp;
	
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
			trainSet = new ArrayList<LabelRec>();
			tmpPtr = trainSet;
		} else {
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void getStats() {
		System.out.println(trainSet.size());
	}
}
