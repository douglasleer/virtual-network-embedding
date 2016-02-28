import java.util.ArrayList;

public class DataCenter {
	public int m; // the number of core level switches
	public int r; // the number of racks
	public int n; // the number of servers in each rack
	public int s; // the number of VMs in each server, i.e., the size of each server
	public ArrayList<Rack> rackList;
	public int bandwidth;
	
	public DataCenter(int m, int r, int n, int s, int bandwidth) {
		this.m = m;
		this.r = r;
		this.n = n;
		this.s = s;
		rackList = new ArrayList<Rack>();
		for (int i = 0; i < r; i ++) {
			Rack aRack = new Rack(n, s, i);
			rackList.add(aRack);
		}
		this.bandwidth = bandwidth;
	}
	
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("The number of core switches, racks, servers (in a rack), and VMs (in a server):\n");
		sb.append(m); sb.append(" "); sb.append(r); sb.append(" "); sb.append(n); sb.append(" ");
		sb.append(s); sb.append("\n"); 
		for (int i = 0; i < r; i ++) {
			sb.append(rackList.get(i).toString());
			sb.append("\n");
		}
		return new String(sb);
	}
}