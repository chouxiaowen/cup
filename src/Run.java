

public class Run {
	/**
	 * @param args
	 */
	
	public static void loadFromText(String dir) {
//		ItemSet itemSet = new ItemSet(dir, "item.txt");
//		UserSet userSet = new UserSet(dir+"user_profile.txt", dir+"user_key_word.txt");
//		Graph graph = new Graph(dir+"user_sns.txt");
//		ActGraph agraph = new ActGraph(dir+"user_action.txt");
		LabelSet labelSet = new LabelSet(dir, "rec_log_train.txt", true);
		
//		DiskIO.writeObject(labelSet, dir, "Train.dat");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		loadFromText(args[0]);	
		
//		LabelSet labelSet = DiskIO.readObject(args[0], "Train.dat", LabelSet.class);
//		ItemSet itemSet = DiskIO.readObject(args[0], "Item.dat", ItemSet.class);
//		System.out.println(itemSet.size());
		
		
		
	}
}
