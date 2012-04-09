import java.io.*;
import java.util.*;

class Item implements Serializable {
	
	int id;
	ArrayList<String> cat;
	ArrayList<Integer> keywords;
	
	public Item () {
	}
	
	public Item (String ii, String cc, String kk) {
		
		id = Integer.parseInt(ii);
		
		cat = new ArrayList<String> ();	
		Collections.addAll(cat, cc.split("\\."));
		
		keywords = new ArrayList<Integer> ();
		String [] tmpList = kk.split(";");
		for (String tmpStr: tmpList){
			keywords.add(Integer.parseInt(tmpStr));
		}
	}
}

class ItemSet implements Serializable {
	
	ArrayList<Item> items;
//	static final long serialVersionUID;
	
	public ItemSet() {
	}
	
	public ItemSet(String dir, String fname) {
		items = new ArrayList<Item> ();
		loadFromText(dir, fname);
	}	
	
	public void loadFromText(String dir, String fname) {
		items = new ArrayList<Item>();
		try {
			File fin = new File(dir+fname);
			FileReader fread = new FileReader(fin);
			BufferedReader bread = new BufferedReader(fread);
			
			String line = null;
			while ((line = bread.readLine()) != null)
			{
				String [] tmp = line.split("\t");
				items.add(new Item(tmp[0], tmp[1], tmp[2]));
			}
			bread.close();
			
			// serialize the object to a .dat file
//			DiskIO.writeObject(dir+"Item.dat", this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void writeObj(String fname) {
		DiskIO.writeObject(fname, this);
	}
}
