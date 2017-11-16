#include "drone.sh"
#include "stdio.h"

import "c_vec_array";

behavior DroneMonitor(i_mon_receive in_ivec)
{
	c_vec_array channel;
	/*	
	void init()
	{
		int count;
		FILE *inFile;
		inFile = fopen("input.txt", "r");
		for(count=0;count<MAX_NO_DRONES;count=count+3)
			fgetc("%d",arr_x[count]);
	}*/
	void main()
	{
		FILE *outFile;
		vec droneVec;
		int count;
		while(1)
		{
			outFile = fopen("droneposition.txt", "w+");
			waitfor(1000);    //check this number later!
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				droneVec = channel.receive(&in_ivec,count);
			}
			
		}				
	}
};
