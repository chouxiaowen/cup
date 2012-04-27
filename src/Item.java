import java.io.*;
import java.util.*;

class Item implements Serializable {
    
    private static final long serialVersionUID = 1L;
    int id;
    ArrayList<Short> cat;
    ArrayList<Integer> keywords;
    
    public Item() {
    }
    
    public Item(String ii, String cc, String kk) {
        id = Integer.parseInt(ii);        
        cat = new ArrayList<Short> ();
        
        String [] tmp = cc.split("\\.");
        for (String s: tmp) {
            cat.add(Short.parseShort(s));
        }
        
        keywords = new ArrayList<Integer> ();
        String [] tmpList = kk.split(";");
        for (String tmpStr: tmpList) {
            keywords.add(Integer.parseInt(tmpStr));
        }
    }
    
    public ArrayList<Short> getCat() {
        return cat;
    }
}

class ItemSet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    HashMap<Integer, Item> items;
    
    public ItemSet() {
    }
    
    public ItemSet(String dir, String fname) {
        items = new HashMap<Integer, Item> ();
        loadFromText(dir, fname);
    }
    
    void loadFromText(String dir, String fname) {
        items = new HashMap<Integer, Item>();
        try {
            File fin = new File(dir+fname);
            FileReader fread = new FileReader(fin);
            BufferedReader bread = new BufferedReader(fread);
            
            String line = null;
            while ((line = bread.readLine()) != null) {
                String [] tmp = line.split("\t");
                items.put(Integer.parseInt(tmp[0]), new Item(tmp[0], tmp[1], tmp[2]));
            }
            bread.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean hasThis(int itemId) {
        return items.get(itemId) != null;
    }
    
    public Item getItem(int itemId) {
        return items.get(itemId);
    }
    
    public int size() {
        return items.size();
    }
}
