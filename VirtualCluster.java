public class VirtualCluster implements Comparable<VirtualCluster>{
	public int n; // the number of VMs in the virtual cluster
	public double b; // the multicast bandwidth requirement of the virtual cluster
	public int remaining;
	public VirtualCluster(int n, double b) {
		this.n = n;
		this.b = b;
		remaining = n;
	}
	
	@Override 
	public int compareTo(VirtualCluster vc) {
		if (b < vc.b) return 1;
		else if (b == vc.b) return 0;
		else return -1;
	}
}