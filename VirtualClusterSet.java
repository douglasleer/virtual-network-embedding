import java.util.*;
public class VirtualClusterSet {
	public ArrayList<VirtualCluster> al;
	public int size;
	public VirtualClusterSet(int n, int nLow, int nHigh, double bLow, double bHigh) {
		// to generate the requirements of all the virtual clusters.
		// n is the total number of virtual clusters to be embedded.
		// nLow is the lowest number of VMs that a virtual can have.
		// nHigh is the high number of VMs that a virtual cluster can have.
		// bLow, bHigh: the lowest bandwidth, and the highest bandwidth.
		Random randomGenerator = new Random();
		size = n;
		al = new ArrayList<VirtualCluster>();
		for (int i = 0; i < n; i ++) {
			int num = nLow + randomGenerator.nextInt(nHigh - nLow);
			double bandwidth = bLow + randomGenerator.nextDouble() * (bHigh - bLow);
			VirtualCluster vc = new VirtualCluster(num, bandwidth);
			al.add(vc);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < al.size(); i ++) {
			sb.append(al.get(i).n);
			sb.append(" ");
			sb.append(al.get(i).b);
			sb.append(" ");			
			sb.append(al.get(i).remaining);
			sb.append("\n");
		}
		return new String(sb);
	}
}
