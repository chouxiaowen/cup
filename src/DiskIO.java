import java.io.*;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;

public class DiskIO {
	
	public static ActGraph loadActGraph(String dir, String fname) {
		try {
			Kryo kryo = new Kryo();
			Input input;
			
			input = new Input(new FileInputStream(dir+fname));
			ActGraph actGraph = kryo.readObject(input, ActGraph.class);
			input.close();
			
			return actGraph;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static <T> T readObject(String dir, String fname, Class<T> type) {
		System.out.print("Loading object from file " + dir + fname + "...");
		try {
			Kryo kryo = new Kryo();
			Input input;
			
			input = new Input(new FileInputStream(dir+fname));
			T obj =	kryo.readObject(input, type);
			input.close();
			System.out.println("Finished!");
			return obj;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static <T> void writeObject(T obj, String dir, String fname) {
		try {
			Kryo kryo = new Kryo();
			Output output = new Output(new FileOutputStream(dir+fname));
			kryo.writeObject(output, obj);
			output.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

