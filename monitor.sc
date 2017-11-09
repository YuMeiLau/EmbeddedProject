#include "drone.sh"
#include "stdio.h"

import "c_vec_queue";

behavior DroneMonitor(i_receive in_pos, i_send out_pos)
{
	void init()
	{
		int count;
		FILE *inFile;
		inFile = fopen("input.txt", "r");
		for(count=0;count<MAX_NO_DRONES;count=count+3)
			fgetc("%d",arr_x[count]);
	}
	void main()
	{
		FILE *outFile;
		outFile = fopen("dronepostion.txt", "w+");
				
	}
};
