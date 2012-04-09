import java.util.*;
import java.io.*;

class Action implements Serializable {
	int numAt;
	int numRt;
	int numCmt;
	
	Action() {
	}
	
	Action(int na, int nr, int nc) {
		numAt = na;
		numRt = nr;
		numCmt = nc;
	}
}

// Action Graph
public class ActGraph implements Serializable {
	HashMap<Integer, HashMap<Integer, Action>> actions;
	
	ActGraph() {	
	}
	
	ActGraph(String fname) {
		loadFile(fname);
	}
	
	void loadFile(String fname){
		System.out.print("Reading ActGraph from text...");
		actions = new HashMap<Integer, HashMap<Integer, Action>> ();
		try {
			File fin = new File(fname);
			FileReader fread = new FileReader(fin);
			BufferedReader bread = new BufferedReader(fread);
			
			String line = bread.readLine();
			while (line != null) {
				String [] tmp = line.split("\t");
				Integer src = Integer.parseInt(tmp[0]);
				Integer des = Integer.parseInt(tmp[1]);
				
				HashMap<Integer, Action> tmpVal = actions.get(src);
				if (tmpVal == null) {
					tmpVal = new HashMap<Integer, Action>();
				}
				tmpVal.put(des, new Action(Integer.parseInt(tmp[2]), 
								Integer.parseInt(tmp[3]), 
								Integer.parseInt(tmp[4]))
								);	
				actions.put(src, tmpVal);				
	
				line = bread.readLine();
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}	
		System.out.println("Done!");
	}
	
	public void writeObj(String fname) {
		DiskIO.writeObject(fname, this);
	}
}
