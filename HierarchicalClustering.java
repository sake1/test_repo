import java.io.*;
import java.util.*;

class Coordinate {

    private static final boolean MAX_DISTANCE = false;
    public String name;
    public double a;
    public double b;
    public double c;
    public double d;
    public double e;
    public ArrayList<Coordinate> group;
    
    public Coordinate(String name, double a, double b, double c, double d, double e) {
	this.name = name;
	this.a = a;
	this.b = b;
	this.c = c;
	this.d = d;
	this.e = e;
	group = new ArrayList<>();
	group.add(this);
    }

    public double extreme_point(Coordinate other_group) {
	double extreme_point = distance(group.get(0), other_group.group.get(0));
	for(int i = 0; i < group.size(); i++) {
	    for(int j = 0; j < other_group.group.size(); j++) {
		if((MAX_DISTANCE && extreme_point < distance(group.get(i), other_group.group.get(j))) ||
		   !MAX_DISTANCE && extreme_point > distance(group.get(i), other_group.group.get(j))) {
		    extreme_point = distance(group.get(i), other_group.group.get(j));
		}
	    }
	}
	return similarity(extreme_point);
    }

    public static double distance(Coordinate x, Coordinate y) {
	return Math.sqrt(Math.pow(x.a - y.a, 2) + Math.pow(x.b - y.b, 2) + Math.pow(x.c - y.c, 2) +
			 Math.pow(x.d - y.d, 2) + Math.pow(x.e - y.e, 2));
    }
    
    public static double similarity(double d) {
	return Math.pow(d+1, -1);
    }
}

public class HierarchicalClustering {

    private static final boolean MAX_DISTANCE = false;
    
    private static ArrayList<Coordinate> getInputDataStream(String location) {
	ArrayList<Coordinate> output = new ArrayList<>();
	try {
	    File file = new File(location);
	    FileReader fileReader = new FileReader(file);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    String line;
	    int itr = 1;
	    while ((line = bufferedReader.readLine()) != null) {
		output.add(new Coordinate("X" + itr++,                              Double.parseDouble(line.split("\\s")[0]),
					  Double.parseDouble(line.split("\\s")[1]), Double.parseDouble(line.split("\\s")[2]),
					  Double.parseDouble(line.split("\\s")[3]), Double.parseDouble(line.split("\\s")[4])));
	    }
	    fileReader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	}
	return output;
    }

    private static void prettyPrint(ArrayList<Coordinate> data, int itr) {
	System.out.println("Iteration " + itr);
	System.out.printf("%-18s", "");
	for(int i = 0; i < data.size(); i++) {
	    System.out.printf("%-13s", data.get(i).name);
	}
	System.out.println();
	for(int i = 0; i < data.size(); i++) {
	    for(int j = 0; j < data.size(); j++) {
		if(j == 0) {
		    System.out.printf("%-10s", data.get(i).name);
		}
		if(j >= i) {
		    System.out.printf("%13.3f", 1.0);
		    break;
		}
		System.out.printf("%13.3f", data.get(i).extreme_point(data.get(j)));
	    }
	    System.out.println();
	}
	System.out.println();
    }
    
    public static void main(String[] args) {
	ArrayList<Coordinate> data = getInputDataStream(args[0]);
	int itr = 0;
	prettyPrint(data, itr);
	while(data.size() > 1) {
	    itr++;
	    double max_score = 0;
	    int x = 0, y = 0;
	    for(int i = 0; i < data.size(); i++) {
		for(int j = 0; j < data.size(); j++) {
		    if(j >= i) {
			break;
		    }
		    if(max_score < data.get(i).extreme_point(data.get(j))) {
			max_score = data.get(i).extreme_point(data.get(j));
			x = i;
			y = j;
		    }
		}
	    }
	    data.get(x).group.add(data.get(y));
	    data.get(x).name += "|" + data.get(y).name.substring(1);
	    data.remove(y);
	    prettyPrint(data, itr);
	}
    }
}
