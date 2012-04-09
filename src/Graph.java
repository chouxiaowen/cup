import java.util.*;
import java.io.*;

public class Graph implements Serializable {
	HashMap<Integer, ArrayList<Integer> > adjList;
	
	Graph() {
	}
	
	Graph(String fname) {
		loadFile(fname);
	}
	
	void loadFile(String fname) {
		System.out.print("Reading Graph from text...");
		adjList = new HashMap<Integer, ArrayList<Integer> > ();
		try {
			File fin = new File(fname);
			FileReader fread = new FileReader(fin);
			BufferedReader bread = new BufferedReader(fread);
		
			String line = bread.readLine();
			while (line != null) {
				String [] tmp = line.split("\t");
				
				int tmpKey = Integer.parseInt(tmp[0]);
				ArrayList<Integer> tmpVal = adjList.get(tmpKey);
			//	System.out.print(tmpVal);
				if (tmpVal == null) {
					tmpVal = new ArrayList<Integer> ();
				}
				tmpVal.add(Integer.parseInt(tmp[1]));		
				adjList.put(tmpKey, tmpVal);
				
				line = bread.readLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Done!");
	}
	
	public void writeObj(String fname) {
		DiskIO.writeObject(fname, this);
	}
}
