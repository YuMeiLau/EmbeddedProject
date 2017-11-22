#include "drone.sh"
#include "stdio.h"
#include "iostream.h"
#include "string.h"
#include "sstream.h"

import "c_ivec_array";

behavior DroneMonitor(i_mon_receive in_ivec)
{
	c_mon_array channel(MAX_NO_DRONES);
	/*	
	void init()
	{
		int count;
		FILE *inFile;
		inFile = fopen("input.txt", "r");
		for(count=0;count<100;count=count+3)
			fgetc("%d",arr_x[count]);
	}*/
	void main()
	{
		FILE *outFile;
		vec droneVec[MAX_NO_DRONES];			//this is a vector containing x,y,z co-ordinates for all drones; in the form vec[_X],vec[_Y],vec[_Z]
		int count;
		while(1)
		{
			outFile = fopen("droneposition.txt", "w+");
			waitfor(1000);    //check this number later!
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				droneVec[count] = channel.receive(&in_ivec,count);
				while(
			}
		}				
	}
};
