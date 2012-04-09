import java.io.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.kryo.io.*;

public class Run {
	/**
	 * @param args
	 */
	
	public static void loadAllFromText(String dir) {
		ItemSet itemSet = new ItemSet(dir, "item.txt");
		UserSet userSet = new UserSet(dir+"user_profile.txt", dir+"user_key_word.txt");
		Graph graph = new Graph(dir+"user_sns.txt");
		ActGraph agraph = new ActGraph(dir+"user_action.txt");
//		try {
//			Kryo kryo = new Kryo();
			// ...
			
//			Output output;
			
//			output = new Output(new FileOutputStream(dir+"Item.dat"));
//			kryo.writeObject(output, itemSet);
//			output.close();
			
//			output = new Output(new FileOutputStream(dir+"User.dat"));
//			kryo.writeObject(output, userSet);
//			output.close();
			
//			output = new Output(new FileOutputStream(dir+"Graph.dat"));
//			kryo.writeObject(output, graph);
//			output.close();
			
//			output = new Output(new FileOutputStream(dir+"ActGraph.dat"));
//			kryo.writeObject(output, agraph);
//			output.close();
						
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		
	}
	public static void loadAllData(String dir) {
		try {
			Kryo kryo = new Kryo();
			
			Input input;
			input = new Input(new FileInputStream(dir+"Graph.dat"));
			Graph graph = kryo.readObject(input, Graph.class);
			input.close();
			
			input = new Input(new FileInputStream(dir+"Item.dat"));
			ItemSet itemSet = kryo.readObject(input, ItemSet.class);
			input.close();
			
//			input = new Input(new FileInputStream(dir+"User.dat"));
//			UserSet userSet = kryo.readObject(input, UserSet.class);
//			input.close();
			
			input = new Input(new FileInputStream(dir+"ActGraph.dat"));
			ActGraph actGraph = kryo.readObject(input, ActGraph.class);
			input.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void writeAllData(String dir) {
//		ItemSet itemSet = (ItemSet) DiskIO.writeObject(dir+"Item.dat");
//		UserSet userSet = (UserSet) DiskIO.writeObject(dir+"User.dat"); 
//		graph DiskIO.writeObject(dir+"Graph.dat");
//		ActGraph agraph = (ActGraph) DiskIO.writeObject(dir+"ActGraph.dat");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub			
		loadAllFromText(args[0]);
//		loadAllData(args[0]);
	}
}
