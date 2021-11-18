package WebCrawler;
import java.util.*;
import java.io.*;


public class TST<Value> {
    private int N;       // size
    private Node root;   // root of TST

    private class Node {
        private char c;                 // character
        private Node left, mid, right;  // left, middle, and right subtries
        private Value val;              // value associated with string
    }

    // return number of key-value pairs
    public int size() {
        return N;
    }

   /**************************************************************
    * Is string key in the symbol table?
    **************************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node get(Node x, String key, int d) {
        if (key == null) throw new NullPointerException();
        if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
        if (x == null) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }


   /**************************************************************
    * Insert string s into the symbol table.
    **************************************************************/
    public void put(String s, Value val) {
        if (!contains(s)) N++;
        root = put(root, s, val, 0);
    }

    private Node put(Node x, String s, Value val, int d) {
        char c = s.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if      (c < x.c)             x.left  = put(x.left,  s, val, d);
        else if (c > x.c)             x.right = put(x.right, s, val, d);
        else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, val, d+1);
        else                          x.val   = val;
        return x;
    }


   /**************************************************************
    * Find and return longest prefix of s in TST
    **************************************************************/
    public String longestPrefixOf(String s) {
        if (s == null || s.length() == 0) return null;
        int length = 0;
        Node x = root;
        int i = 0;
        while (x != null && i < s.length()) {
            char c = s.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return s.substring(0, length);
    }

    // all keys in symbol table
    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        collect(root, "", queue);
        return queue;
    }

    // all keys starting with given prefix
    public Iterable<String> prefixMatch(String prefix) {
        Queue<String> queue = new Queue<String>();
        Node x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, prefix, queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node x, String prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix,       queue);
        if (x.val != null) queue.enqueue(prefix + x.c);
        collect(x.mid,   prefix + x.c, queue);
        collect(x.right, prefix,       queue);
    }


    // return all keys matching given wildcard pattern
    public Iterable<String> wildcardMatch(String pat) {
        Queue<String> queue = new Queue<String>();
        collect(root, "", 0, pat, queue);
        return queue;
    }
 
    private void collect(Node x, String prefix, int i, String pat, Queue<String> q) {
        if (x == null) return;
        char c = pat.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pat, q);
        if (c == '.' || c == x.c) {
            if (i == pat.length() - 1 && x.val != null) q.enqueue(prefix + x.c);
            if (i < pat.length() - 1) collect(x.mid, prefix + x.c, i+1, pat, q);
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pat, q);
    }
    
    static int isSubstring(
            String s1, String s2)
        {
            int M = s1.length();
            int N = s2.length();
     
            /* A loop to slide pat[] one by one */
            for (int i = 0; i <= N - M; i++) {
                int j;
     
                /* For current index i, check for
                pattern match */
                for (j = 0; j < M; j++)
                    if (s2.charAt(i + j)
                        != s1.charAt(j))
                        break;
     
                if (j == M)
                    return i;
            }
     
            return -1;
        }


    // test client
    public static void main(String[] args) throws IOException {
    	while(true) {
    	System.out.println();
        System.out.println("**************WEB SEARCH ENGINE**************");
        System.out.println();
    	int [] q= new int[100000];
    	int [] q2= new int[100000];
    	int count2=0,count4=0;
    	Hashtable<Integer, String> ht = new Hashtable<>();
    	
    	Scanner sc= new Scanner(System.in);
    	System.out.print("What do you want to search: ");  
    	String key= sc.nextLine();
    	
    	
    	File file2 = new File("/Users/pawan/Downloads/ACCFinalProject-master/Data/");
        String[] fileList = file2.list();
        for(String name:fileList){
    	
    	File file = new File("/Users/pawan/Downloads/ACCFinalProject-master/Data/"+name);
    	int newcount=0;
    	
    	//Counts no. of words in a file
    	Scanner input1 = new Scanner(file); 
    	while (input1.hasNext()) {
            String word  = input1.next();
            newcount = newcount + 1;
          }
    	
    	input1.close();
    	
    	String keys[]= new String[newcount];
    	String s1="";
    	
        //Store keys in the variable
        Scanner input = new Scanner(file); 
        int count = 0;
        while (input.hasNext()) {
          String word  = input.next();
          keys[count]=word;
          count = count + 1;
        }
        
        input.close();
          
        
        //System.out.println("Word count: " + count);
  

    	// build symbol table from standard input
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < keys.length; i++) {
            //String key = In.readString();
            st.put(keys[i], i);
        }
       
        
        // checks if keywords are present in substrings of other strings
        int count3=0;
        
        for(int i=0 ; i < count ; i++) {
        	
        	String s2=keys[i];
        	
        	while(s2!="") {
        	
        	int res = isSubstring(key, s2);
        	 
            if (res == -1)
            	 break;
            else
            {
            	for( int j=res+key.length() ; j<s2.length() ; j++) {
            		
            		s1=s1+s2.charAt(j);
            	
            	}
            	
              q[count2]= i;
              count3=count3+1;
            	
              count2=count2+1;
            
            }
            	 
           
            s2=s1;
            s1="";
            
           
        }
        	
        }
       
        if(count3!=0) {
        q2[count4]=count3;
        System.out.println();
        System.out.println("The word '"+key+"' occurs "+count3+" times in file "+name+"");
        ht.put(count3,name);
        System.out.println();
        count4=count4+1;
        
        }
        
        }
        
       
        int count5=0;
        
        //finds length of array for merge sort
        for (int p=0 ; p<100000 ; p++) {
        	
        	if(q2[p]!=0) {
        		
        		count5=count5+1;
        		
        	}
        	
        }
        
        //final array to be used in merge sort
        long [] q3= new long[count5];
       
        for (int h = 0 ; h<count5 ; h++) {
        	
        	q3[h]=q2[h];
        	//System.out.print(" "+q3[h]);
        	
        }
        
        if(count5==0) {
           System.out.println();
           System.out.println("Sorry ! No matches were found !");
           System.out.println();
           
        }
        
        else {
        
        // sorts array using merge sort
        Sort.mergeSort(q3);
        
       /* for (int t=0 ; t<count5 ; t++) {
        	
        	System.out.print(" "+q3[t]);
        }*/
        
        System.out.println();
        System.out.println();
        System.out.println("**************Top 3 Search Results**************");
        System.out.println();
       
        int pointer=0;
       // System.out.println(count5);
        int count6=count5-1;
        
        //prints top 3 results
        while(pointer!=3) {
        	long x=q3[count6];
        	int x1= (int) (x);
        	String a = ht.get(x1);
        	System.out.println(a);
        	count6=count6-1;
        	//System.out.println(count6);
        	pointer=pointer+1;
        	
        }
       
        //prints total occurences of keyword
        System.out.println();
        System.out.println("The total no. of occurences of '"+key+"' are : "+count2);
        System.out.println();
        System.out.println("************************************************");
        System.out.println();
        /*System.out.print("The word '"+key+"' occur at :");
        
        if(q[0]==0)
        System.out.print(" "+q[0]);
        
        for(int k=0 ; k<q.length ; k++) {
       
        	if(q[k]!=0) {
        	System.out.print(" "+q[k]);
        	}      	
        		
        }*/
        
        }
        // loop to either continue searching or exit
        System.out.println("Do you want to continue searching ? Type 'no' to exit.");
        Scanner sc2= new Scanner(System.in);
        String output= sc2.nextLine();
        if(output.equals("no")) {
        	System.out.println("Exiting.......");
        	System.out.println();
        	break;
        }
        }
     
        
        }
}

