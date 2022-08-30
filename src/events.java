/*
 * @author Peguero Marmolejos, Emily
 */

public class events{
	public String type = "", name = "";
	public float numb = 0;
	public int count;
	//public float arrival_Time = 0;
	
	public events(float numb, String type) {
		this.type = type;
		this.numb = numb;
		
		
	}

	public void set_Name(int a) {
		this.count = a;
		this.name = type + " Event " + a;
	}
}