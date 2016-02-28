
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VirtualClusterEmbedding {
	
	public static int AVERAGING_TIME = 1000;
	public static int METHOD_COUNT = 6;
	public static int RACK_NUM_OFFSET = 3; // if we consider 7 racks, RACK_NUM_OFFSET = 3.
	public static int RACK_NUM_STEP = 2;
	public static void main(String[] args) {		
		System.out.println("First Simulation Set: ");
		simulationSet1();
		System.out.println();
		
		System.out.println("Second Simulation Set: ");
		simulationSet2();
		System.out.println();	
		
		System.out.println("Third Simulation Set: ");		
		simulationSet3();
	}
	
	private static void simulationSet1() {
		// the virtual cluster set to be considered
		int numVirtualClusters = 16;
		int numVMsLowerBound = 10;
		int numVMsUpperBound = 30;
		double bandwidthLowerBound = 1.0;
		double bandwidthUpperBound = 10.0;
		
		// the data center to be considered
		int mCore = 40; // the number of core switches
		int rRack = 40; // the number of racks
		int nServer = 4; // the number of servers.
		int sSize = 2; // the number of VMs that each server have.
		// total resource: 40 * 8 = 320.
		// vary the number of racks.
		int bandwidth = 10;
		
		// we vary the number of racks, choose 7 different number of racks
		// for each rack number, we do 100 ~ 1000 simulations, and then take the average
		for (int rackNum = rRack - RACK_NUM_OFFSET*RACK_NUM_STEP; 
				rackNum <= rRack + RACK_NUM_OFFSET*RACK_NUM_STEP;
				rackNum = rackNum + RACK_NUM_STEP) {
			DataCenter dc = new DataCenter(mCore, rackNum, nServer, sSize, bandwidth);
			int averagingTime = 0;
			double[] bandwidthUsage = new double[METHOD_COUNT]; // totally, six values
			// 0 : ideal
			// 1 : compact
			// 2 : worst fit
			// 3 : worst fit with sort
			// 4 : combined
			// 5 : combined with sort
			for (int i = 0; i < METHOD_COUNT; ++i) {
				bandwidthUsage[i] = 0;
			}
			
			while (averagingTime < AVERAGING_TIME) {
				VirtualClusterSet vcs = new VirtualClusterSet(numVirtualClusters, 
						numVMsLowerBound, numVMsUpperBound, 
						bandwidthLowerBound, bandwidthUpperBound);
				double checkBandwidth = idealOptimalBandwidthUsage(dc, vcs);
				if (checkBandwidth < 0) {}
				else {
					bandwidthUsage[0] += idealOptimalBandwidthUsage(dc, vcs);
					bandwidthUsage[1] += compactPlacement(dc, vcs);
					bandwidthUsage[2] += worstFitPlacement(dc, vcs);
					bandwidthUsage[3] += worstFitPlacementWithSorting(dc, vcs);
					bandwidthUsage[4] += combinedWorstFitAndBestFit(dc, vcs);
					bandwidthUsage[5] += combinedWithSorting(dc, vcs);
					
					++averagingTime;
				}
			}
			// print the simulation for this number of racks
			for (int i = 0; i < METHOD_COUNT; ++i) {
				System.out.format("%.5f", bandwidthUsage[i]/AVERAGING_TIME);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	private static void simulationSet2() {
		// the virtual cluster set to be considered:
		int numVirtualClusters = 32;
		int numVMsLowerBound = 5;
		int numVMsUpperBound = 15;
		double bandwidthLowerBound = 1.0;
		double bandwidthUpperBound = 10.0;
		// total request : (10 + 30)/2 * 16 = 320.
		// System.out.println(vcs);
		
		// the data center to be considered:
		int mCore = 40; // the number of core switches
		int rRack = 40; // the number of racks
		int nServer = 4; // the number of servers.
		int sSize = 2; // the number of VMs that each server have.
		// total resource: 40 * 8 = 320.
		// vary the number of racks.
		int bandwidth = 10;

		// System.out.println(dc);
		
		// we vary the number of racks, choose 7 different number of racks.
		// for each rack number, we do 100 ~ 1000 simulations, and then take the average.

		for (int rackNum = rRack - RACK_NUM_OFFSET*RACK_NUM_STEP; 
				rackNum <= rRack + RACK_NUM_OFFSET*RACK_NUM_STEP;
				rackNum = rackNum + RACK_NUM_STEP) {
			DataCenter dc = new DataCenter(mCore, rackNum, nServer, sSize, bandwidth);
			int averagingTime = 0;
			double[] bandwidthUsage = new double[METHOD_COUNT]; // totally, six values.
			// 0 : ideal
			// 1 : compact
			// 2 : worst fit
			// 3 : worst fit with sort
			// 4 : combined
			// 5 : combined with sort
			for (int i = 0; i < METHOD_COUNT; ++i) {
				bandwidthUsage[i] = 0;
			}
			
			while (averagingTime < AVERAGING_TIME) {
				VirtualClusterSet vcs = new VirtualClusterSet(numVirtualClusters, 
						numVMsLowerBound, numVMsUpperBound, 
						bandwidthLowerBound, bandwidthUpperBound);
				double checkBandwidth = idealOptimalBandwidthUsage(dc, vcs);
				if (checkBandwidth < 0) {} // do not consider invalid cases;
				else {
					bandwidthUsage[0] += idealOptimalBandwidthUsage(dc, vcs);
					bandwidthUsage[1] += compactPlacement(dc, vcs);
					bandwidthUsage[2] += worstFitPlacement(dc, vcs);
					bandwidthUsage[3] += worstFitPlacementWithSorting(dc, vcs);
					bandwidthUsage[4] += combinedWorstFitAndBestFit(dc, vcs);
					bandwidthUsage[5] += combinedWithSorting(dc, vcs);
					
					++averagingTime;
				}
			}
			// print the simulation for this number of racks
			for (int i = 0; i < METHOD_COUNT; ++i) {
				System.out.format("%.5f", bandwidthUsage[i]/AVERAGING_TIME);
				System.out.print(" ");
			}
			System.out.println();
		}	
	}	
	
	private static void simulationSetAbandoned() {
		// the virtual cluster set to be considered:
		int numVirtualClusters = 32;
		int numVMsLowerBound = 5;
		int numVMsUpperBound = 15;
		double bandwidthLowerBound = 1.0;
		double bandwidthUpperBound = 10.0;
		VirtualClusterSet vcs = new VirtualClusterSet(numVirtualClusters, numVMsLowerBound, 
				numVMsUpperBound, bandwidthLowerBound, bandwidthUpperBound);
		//System.out.println(vcs);
		
		// the data center to be considered:
		int mCore = 40;
		int rRack = 40;
		int nServer = 4;
		int sSize = 2;
		int bandwidth = 10;
		DataCenter dc = new DataCenter(mCore, rRack, nServer, sSize, bandwidth);
		
		System.out.format("%.5f", idealOptimalBandwidthUsage(dc, vcs));
		System.out.print(" ");
		// System.out.println("Compact Placement Bandwidth Usage: ");
		System.out.format("%.5f", compactPlacement(dc, vcs));
		System.out.print(" ");
		// System.out.println("Worst Fit Placement Bandwidth Usage: ");
		System.out.format("%.5f", worstFitPlacement(dc, vcs));
		System.out.print(" ");
		// System.out.println("Worst Fit Placement With Sorting Bandwidth Usage: ");
		// notice that sorting means sort the virtual clusters
		System.out.format("%.5f", worstFitPlacementWithSorting(dc, vcs));
		System.out.print(" ");
		// System.out.println("Combined Worst Fit and Best Fit Bandwidth Usage: ");
		System.out.format("%.5f", combinedWorstFitAndBestFit(dc, vcs));
		System.out.print(" ");
		// System.out.println("Combined Worst Fit and Best Fit With Sorting: ");
		// notice that sorting means sort the virtual clusters
		System.out.format("%.5f", combinedWithSorting(dc, vcs)); 
		System.out.print(" ");
	}

	private static void simulationSet3() {
		// the virtual cluster set to be considered:
		int numVirtualClusters = 32;
		int numVMsLowerBound = 5;
		int numVMsUpperBound = 15;
		double bandwidthLowerBound = 1.0;
		double bandwidthUpperBound = 10.0;
		// total request : (10 + 30)/2 * 16 = 320.
		// System.out.println(vcs);
		
		// the data center to be considered:
		int mCore = 40; // the number of core switches
		int rRack = 32; // the number of racks
		int nServer = 5; // the number of servers.
		int sSize = 2; // the number of VMs that each server have.
		// total resource: 40 * 8 = 320.
		// vary the number of racks.
		int bandwidth = 10;

		// System.out.println(dc);
		
		// we vary the number of racks, choose 7 different number of racks.
		// for each rack number, we do 100 ~ 1000 simulations, and then take the average.

		for (int rackNum = rRack - RACK_NUM_OFFSET*RACK_NUM_STEP; 
				rackNum <= rRack + RACK_NUM_OFFSET*RACK_NUM_STEP;
				rackNum = rackNum + RACK_NUM_STEP) {
			DataCenter dc = new DataCenter(mCore, rackNum, nServer, sSize, bandwidth);
			int averagingTime = 0;
			double[] bandwidthUsage = new double[METHOD_COUNT]; // totally, six values.
			// 0 : ideal
			// 1 : compact
			// 2 : worst fit
			// 3 : worst fit with sort
			// 4 : combined
			// 5 : combined with sort
			for (int i = 0; i < METHOD_COUNT; ++i) {
				bandwidthUsage[i] = 0;
			}
			
			while (averagingTime < AVERAGING_TIME) {
				VirtualClusterSet vcs = new VirtualClusterSet(numVirtualClusters, 
						numVMsLowerBound, numVMsUpperBound, 
						bandwidthLowerBound, bandwidthUpperBound);
				double checkBandwidth = idealOptimalBandwidthUsage(dc, vcs);
				if (checkBandwidth < 0) {} // do not consider invalid cases;
				else {
					bandwidthUsage[0] += idealOptimalBandwidthUsage(dc, vcs);
					bandwidthUsage[1] += compactPlacement(dc, vcs);
					bandwidthUsage[2] += worstFitPlacement(dc, vcs);
					bandwidthUsage[3] += worstFitPlacementWithSorting(dc, vcs);
					bandwidthUsage[4] += combinedWorstFitAndBestFit(dc, vcs);
					bandwidthUsage[5] += combinedWithSorting(dc, vcs);
					
					++averagingTime;
				}
			}
			// print the simulation for this number of racks
			for (int i = 0; i < METHOD_COUNT; ++i) {
				System.out.format("%.5f", bandwidthUsage[i] / AVERAGING_TIME);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	private static double idealOptimalBandwidthUsage(DataCenter dc, VirtualClusterSet vcs) {
		// did not change the vcs, dc.
		// if we have enough racks and enough core level switches.
		int sumOfRequest = 0;
		for (int i = 0; i < vcs.size; ++i) {
			sumOfRequest += vcs.al.get(i).n;
		}
		
		if (sumOfRequest > dc.r*dc.n*dc.s) {
			return -1; // not a valid placement.
		}
		
		int[] numOfRackSpan = new int[vcs.size];
		for (int i = 0; i < vcs.size; ++i) {
			numOfRackSpan[i] = 0;
		}
		for (int i = 0; i < vcs.size; ++i) {
			if (vcs.al.get(i).n % (dc.n * dc.s) == 0) {
				numOfRackSpan[i] = vcs.al.get(i).n / (dc.n * dc.s);
			} else {
				numOfRackSpan[i] = vcs.al.get(i).n / (dc.n * dc.s) + 1;
			}
		}
		
		double bandwidth = 0;
		for (int i = 0; i < vcs.size; ++i) {
			if (numOfRackSpan[i] == 1) {}
			else {
				bandwidth += numOfRackSpan[i] * vcs.al.get(i).b;
			}
		}
		return bandwidth;
	}

	private static double compactPlacement(DataCenter dc, VirtualClusterSet vcs) {
		int sumOfRequest = 0;
		for (int i = 0; i < vcs.size; ++i) {
			sumOfRequest += vcs.al.get(i).n;
		}
		if (sumOfRequest > dc.r*dc.n*dc.s) {
			return -1; // not a valid placement.
		}

		int[] numOfRackSpan = new int[vcs.size];
		for (int i = 0; i < vcs.size; ++i) {
			numOfRackSpan[i] = 0;
		}
		int i = 0; // the ith rack of the data center.
		int j = 0; // the jth virtual cluster to be consider.
		while (j < vcs.size) {
			++numOfRackSpan[j];
			if (vcs.al.get(j).remaining == dc.rackList.get(i).remaining) {
				++j;
				++i;
			} else if (vcs.al.get(j).remaining < dc.rackList.get(i).remaining) {
				dc.rackList.get(i).remaining = dc.rackList.get(i).remaining 
						- vcs.al.get(j).remaining;
				++j;
			} else { // vcs.al.get(j).remaining > dc.rackList.get(i).remaining.
				vcs.al.get(j).remaining -= dc.rackList.get(i).remaining;
				++i;
			}
		}

		double bandwidth = 0;
		for (int k = 0; k < vcs.size; ++k) {
			if (numOfRackSpan[k] == 1) {}
			else {
				bandwidth += numOfRackSpan[k] * vcs.al.get(k).b;
			}
		}
		
		// reset to original state
		for (int k = 0; k < vcs.size; ++k) {
			vcs.al.get(k).remaining = vcs.al.get(k).n;
		}
		for (int k = 0; k < dc.rackList.size(); ++k) {
			dc.rackList.get(k).remaining = dc.n * dc.s;
		}		
		return bandwidth;
	}	

	private static double worstFitPlacement(DataCenter dc, VirtualClusterSet vcs) {
		int sumOfRequest = 0;
		for (int i = 0; i < vcs.size; ++i) {
			sumOfRequest += vcs.al.get(i).n;
		}
		
		if (sumOfRequest > dc.r * dc.n * dc.s) {
			return -1; // not a valid placement.
		}
		
		int j = 0;
		int[] numOfRackSpan = new int[vcs.size];
		for (int i = 0; i < vcs.size; ++i) {
			numOfRackSpan[i] = 0;
		}
		while (j < vcs.size) {
			// find the most vacant rack.
			int maxSize = 0;
			int maxIndex = 0;
			for (int i = 0; i < dc.r; ++i) {
				if (maxSize < dc.rackList.get(i).remaining) {
					maxIndex = i;
					maxSize = dc.rackList.get(i).remaining;
				}
			}
			if (vcs.al.get(j).remaining <= dc.rackList.get(maxIndex).remaining) {
				++numOfRackSpan[j];
				dc.rackList.get(maxIndex).remaining = 
						dc.rackList.get(maxIndex).remaining - vcs.al.get(j).remaining;
				++j;
			} else {
				// do not increase j, because we still need to consider it.
				vcs.al.get(j).remaining = vcs.al.get(j).remaining
						- dc.rackList.get(maxIndex).remaining;
				dc.rackList.get(maxIndex).remaining = 0;
				++numOfRackSpan[j];
			}
		}
		
		// now, we have the number of racks that each virtual cluster uses.
		// we can calculate the total bandwidth usage;
		double bandwidth = 0;
		for (int i = 0; i < vcs.size; ++i) {
			if (numOfRackSpan[i] == 1){}
			else {
				bandwidth += numOfRackSpan[i] * vcs.al.get(i).b;
			}
		}
		
		
		// reset to original state
		for (int i = 0; i < vcs.size; ++i) {
			vcs.al.get(i).remaining = vcs.al.get(i).n;
		}
		//
		for (int i = 0; i < dc.rackList.size(); ++i) {
			dc.rackList.get(i).remaining = dc.n * dc.s;
		}
		return bandwidth;
	}
	
	// consider the virtual cluster with the largest bandwidth first.
	private static double worstFitPlacementWithSorting(DataCenter dc, VirtualClusterSet vcs) {
		int sumOfRequest = 0;
		for (int i = 0; i < vcs.size; ++i) {
			sumOfRequest += vcs.al.get(i).n;
		}
		
		if (sumOfRequest > dc.r * dc.n * dc.s) {
			return -1; // not a valid placement.
		}
		ArrayList<VirtualCluster> vcl = new ArrayList<VirtualCluster>();
		// for backup.
		for (int i = 0; i < vcs.size; ++i) {
			VirtualCluster vc = new VirtualCluster(vcs.al.get(i).n, vcs.al.get(i).b);
			vcl.add(vc);
		}
		
		Collections.sort(vcs.al);
		//System.out.println("Sorted Virtual Clusters: ");
		//System.out.println(vcs);
		double bandwidth =  worstFitPlacement(dc, vcs);
		vcs.al = vcl;
		//System.out.println("Order restored: ");
		//System.out.println(vcs);
		
		return bandwidth;
	}

	private static double combinedWorstFitAndBestFit(DataCenter dc, VirtualClusterSet vcs) {
		int sumOfRequest = 0;
		for (int i = 0; i < vcs.size; ++i) {
			sumOfRequest += vcs.al.get(i).n;
		}
		
		if (sumOfRequest > dc.r * dc.n * dc.s) {
			return -1;
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter("log.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int[] numOfRackSpan = new int[vcs.size];
		for (int i = 0; i < vcs.size; ++i) {
			numOfRackSpan[i] = 0;
		}
		
		int j = 0; // the virtual cluster to be considered.		
		while (j < vcs.size) {
			++numOfRackSpan[j];
			// sort the racks according to the available number of resources.
			Collections.sort(dc.rackList);
			try {
				fw.write(dc.toString());
				fw.write("After Sort In the Combined Method.");
				fw.write(j);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// find the rack that can host the virtual
			int ri = 0;
			while (ri < dc.rackList.size() 
					&& vcs.al.get(j).remaining > dc.rackList.get(ri).remaining) {
				++ri;
			}
			try {
				fw.write(ri);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (ri == dc.rackList.size()) {
				vcs.al.get(j).remaining -= dc.rackList.get(ri - 1).remaining;
				dc.rackList.get(ri - 1).remaining = 0;
			} else { 
				// ri can host all the remaining VMs of the virtual cluster.
				dc.rackList.get(ri).remaining -= vcs.al.get(j).remaining;
				vcs.al.get(j).remaining = 0;
				++j; // finish the current cluster and go on to the next one.
			}
		}
		
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// calculate the bandwidth usage:
		double bandwidth = 0;
		for (int k = 0; k < vcs.size; ++k) {
			if (numOfRackSpan[k] == 1) {}
			else {
				bandwidth += numOfRackSpan[k] * vcs.al.get(k).b;
			}
		}

		// reset virtual clusters, to original state
		for (int k = 0; k < vcs.size; ++k) {
			vcs.al.get(k).remaining = vcs.al.get(k).n;
		}
		// reset data center to original state.
		for (int k = 0; k < dc.rackList.size(); ++k) {
			dc.rackList.get(k).remaining = dc.n * dc.s;
		}
		return bandwidth;
	}
	
	// consider the virtual cluster with the largest bandwidth first.
	private static double combinedWithSorting(DataCenter dc, VirtualClusterSet vcs) {
		ArrayList<VirtualCluster> vcl = new ArrayList<VirtualCluster>();
		// for backup.
		for (int i = 0; i < vcs.size; ++i) {
			VirtualCluster vc = new VirtualCluster(vcs.al.get(i).n, vcs.al.get(i).b);
			vcl.add(vc);
		}
		
		Collections.sort(vcs.al);
		double bandwidth = combinedWorstFitAndBestFit(dc, vcs);
		vcs.al = vcl;
		return bandwidth;
	}

}
