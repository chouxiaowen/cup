import java.io.*;


public class DiskIO {
	
	public static Object loadObject(String fname) {
		try {
			FileInputStream fis = new FileInputStream(fname);
			ObjectInputStream ois = new ObjectInputStream(fis);
			return ois.readObject();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Boolean writeObject(String fname, Object obj) {
		try {
			FileOutputStream fos = new FileOutputStream(fname);
			ObjectOutputStream oos = new ObjectOutputStream(fos);		
			oos.writeObject(obj);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
