import java.util.*;
import java.io.*;

class User implements Serializable {

    private static final long serialVersionUID = 1L;
    int id;
    short year;
    byte gender;
    int numTweets;
    ArrayList<Integer> tags;
    HashMap<Integer, Double> kws;
    
    User() {
    }
    
    User(String profile) {
        String [] tmpList = profile.split("\t");
        for (int i = 0; i < tmpList.length; ++i) {
            if (i == 0) {
                if(tmpList[i].length() > 8 )
                    System.out.println(tmpList[i]);
                id = Integer.parseInt(tmpList[i]);
            } else if (i == 1) {
                try {   // there are invalid entries observed in the data file, e.g., "0-0-"
                    year = Short.parseShort(tmpList[i]);
                } catch (NumberFormatException ex) {
                    year = 0;   
                }
            } else if (i == 2) {
                gender = Byte.parseByte(tmpList[i]);
            } else if (i == 3) {
                numTweets = Integer.parseInt(tmpList[i]);           
            } else if (i == 4) {
                tags = new ArrayList<Integer>();        
                if (tmpList[i] == "0") {
                    continue;
                }
                String [] tmpList2 = tmpList[i].split(";");
                for (String s: tmpList2) {
                    tags.add(Integer.parseInt(s));
                }
            }
        }
    }
    
    int getId()
    {
        return id;
    }
    
    void addKeywords(String line)
    {
        kws = new HashMap<Integer, Double>();
        String [] kwList = line.split(";");
        for (String tmp: kwList) {
            String [] pair = tmp.split(":");
            kws.put(Integer.parseInt(pair[0]), Double.parseDouble(pair[1]));
        }
    }
}

class UserSet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    HashMap<Integer, User> users;
    
    UserSet() {
    }
    
    UserSet(String fnameProfile) {
        users = new HashMap<Integer, User> ();
        loadProfileFile(fnameProfile);
    }
    
    UserSet(String fnameProfile, String fnameKeywords) {
        users = new HashMap<Integer, User> ();
        loadProfileFile(fnameProfile);
        loadKeywordFile(fnameKeywords);
    }
    
    void loadProfileFile(String fname)
    {   
        System.out.print("Loading user profile from text...");
        try {
            File fin = new File(fname);
            FileReader fread = new FileReader(fin);
            BufferedReader bread = new BufferedReader(fread);
            
            String line = null;
            while((line = bread.readLine()) != null)
            {
                User user = new User(line);
                users.put(user.getId(), user);
            }
            System.out.println("Done!");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    void loadKeywordFile(String fname)
    {
        System.out.print("Loading user keyword file from text...");
        try {
            File fin = new File(fname);
            FileReader fread = new FileReader(fin);
            BufferedReader bread = new BufferedReader(fread);
            
            String line = null;
            while((line=bread.readLine()) != null)
            {
                String [] tmpPair = line.split("\t");
                
                int tmpId = Integer.parseInt(tmpPair[0]);
                User tmpUser = users.get(tmpId);
                tmpUser.addKeywords(tmpPair[1]);
                users.put(tmpId, tmpUser);
            }
            bread.close();
            System.out.println("Done!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public User getUser(int userId) {
        return users.get(userId);
    }
}
