#include "drone.sh"
#include "inc/vec.h"
#include "stdio.h"
#include "stdlib.h"

import "c_ivec_array";
import "c_vec_queue";

behavior DroneMonitor(i_mon_receive in_ivec)
{
	//c_mon_array channel(MAX_NO_DRONES);
	int id;
	vec droneVec[MAX_NO_DRONES],droneInitPos[MAX_NO_DRONES];		//this is a vector containing x,y,z co-ordinates for all drones; in the form vec[_X],vec[_Y],vec[_Z]
        void init()
        {
		FILE* inFile;
                int initCount;
                inFile = fopen("startposition.txt","r+");
                while(!feof(inFile))
                {
                	for(initCount=0;initCount<MAX_NO_DRONES;initCount++)
			{
				fscanf(inFile, "%d %d %d",&droneInitPos[initCount][_X],&droneInitPos[initCount][_Y],&droneInitPos[initCount][_Z]);							
			}
		}
	}
	void main(void)
	{
		FILE *outFile;
		int count;
		outFile = fopen("droneposition,txt","w+");
		while(1)
		{
			//waitfor(1000);
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				in_ivec.receive(&droneVec[count],id);	//can do this?
				droneVec[count][_X] = droneVec[count][_X] + droneInitPos[count][_X];
                                droneVec[count][_Y] = droneVec[count][_Y] + droneInitPos[count][_Y];
                                droneVec[count][_Z] = droneVec[count][_Z] + droneInitPos[count][_Z];
				fprintf(outFile, "%d %d %d\n",droneVec[count][_X],droneVec[count][_Y],droneVec[count][_Z]);

			}
		}
	}
};
