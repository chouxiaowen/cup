
public class Run {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String dir = args[0];
		
//		ItemSet itemSet = new ItemSet(dir+"item.txt");
//		UserSet userSet = new UserSet(dir+"user_profile.txt");//, "data/user_key_word.txt");
//		Graph graph = new Graph(dir+"user_sns.txt");
		AGraph agraph = new AGraph(dir+"user_action.txt");
		
	}
}
