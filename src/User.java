import java.util.*;
import java.io.*;

class User {
	int id;
	int year;
	int gender;
	int numTweets;
	ArrayList<Integer> tags;
	Map<Integer, Double> kws;
	
	User(String profile) {
		String [] tmpList = profile.split("\t");
		for (int i = 0; i < tmpList.length; ++i) {
			if (i == 0) {
				id = Integer.parseInt(tmpList[i]);
			} else if (i == 1) {
				try {	// there are invalid entries observed in the data file, e.g., "0-0-"
					year = Integer.parseInt(tmpList[i]);
				} catch (NumberFormatException ex) {
					year = 0;	
				}
			} else if (i == 2) {
				gender = Integer.parseInt(tmpList[i]);
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
}

class UserSet {
	
	ArrayList<User> users;
	
	UserSet() {
	}
	
	UserSet(String fname) {
		users = new ArrayList<User> ();
		loadProfileFile(fname);
	}
	
	void loadProfileFile(String fname)
	{	
		try {
			File fin = new File(fname);
			FileReader fread = new FileReader(fin);
			BufferedReader bread = new BufferedReader(fread);
			
			String line = null;
			while((line = bread.readLine()) != null)
			{
				users.add(new User(line));
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
