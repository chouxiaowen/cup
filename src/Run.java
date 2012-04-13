import java.util.*;

public class Run {
    
    public static void loadFromText(String dir) {
//      ItemSet itemSet = new ItemSet(dir, "item.txt");
//      UserSet userSet = new UserSet(dir+"user_profile.txt", dir+"user_key_word.txt");
//      Graph graph = new Graph(dir+"user_sns.txt");
//      ActGraph agraph = new ActGraph(dir+"user_action.txt");
//      LabelSet labelSet = new LabelSet(dir, "rec_log_train.txt", true);
        
        
//      DiskIO.writeObject(labelSet, dir, "Train.dat");
//      DiskIO.writeObject(userSet, dir, "User.dat");   
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    //  loadFromText(args[0]);  
        String dir = args[0];
//       LabelSet labelSet = new LabelSet(dir, "rec_log_train.txt", true);
//      DiskIO.writeObject(labelSet, dir, "Train.dat"); 
//      LabelSet labelSet = DiskIO.readObject(args[0], "Train.dat", LabelSet.class);
        //labelSet.getStats();
      
 //     ItemSet itemSet = DiskIO.readObject(args[0], "Item.dat", ItemSet.class);
  
        ItemSet itemSet = new ItemSet(dir, "item.txt");
        DiskIO.writeObject(itemSet, dir, "Item.dat");
        
        UserSet userSet = new UserSet(dir+"user_profile.txt");//, dir+"user_key_word.txt");
        
        Graph graph = new Graph(dir+"user_sns.txt");
//      DiskIO.writeObject(graph, dir, "Graph.dat");
        
        Analysis.cmpDomain(userSet, itemSet, graph);
//      System.out.println(itemSet.size());
    }
}

class Analysis {
    
    // Compare the active domain of user ids and item ids
    // Result: EVERY ITEM IS A USER
    public static void cmpDomain(UserSet userSet, ItemSet itemSet, Graph graph) {
        HashMap<Integer, Item> items = itemSet.items;
        HashMap<Integer, User> users = userSet.users;
        
        Iterator<Map.Entry<Integer, Item>> it = items.entrySet().iterator();
        
        int cntInter = 0;
        int cntDiff = 0;
        
        while (it.hasNext()) {
            Map.Entry<Integer, Item> etr = it.next();
            if (users.get(etr.getKey()) != null) {
                cntInter++;
                
                System.out.print(graph.getInDegree(etr.getKey()));
            }
            else {
                cntDiff++;
            }
        }
        System.out.println("Number of item ids also in user ids: " + cntInter + " while " + cntDiff + "not.");
    }
}