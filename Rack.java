
public class Rack implements Comparable<Rack>{
	public int n; // the number of servers in a rack
	public int s; // the number of VMs in a server
	public int remaining; // the number of remaining VMs in the rack
	public int index;
	public Rack(int n, int s, int index) {
		this.n = n; 
		this.s = s;
		this.remaining = n * s; // initialized as ns
		this.index = index;
	}
	
	@Override
	public int compareTo(Rack obj) {
		if (this.remaining < obj.remaining)
			return -1;
		else if (this.remaining == obj.remaining)
			return 0;
		else return 1;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Index: ");
		sb.append(index);
		sb.append("; Remaining VMs: ");
		sb.append(remaining);		
		return new String(sb);
	}
}
