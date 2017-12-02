#include "drone.sh"
#include "inc/vec.h"
#include "stdio.h"
#include "stdlib.h"

import "c_ivec_array";
import "c_vec_queue";

behavior DroneMonitor(i_mon_receive in_ivec)
{
	vec droneVec[MAX_NO_DRONES],droneInitPos[MAX_NO_DRONES];		//this is a vector containing x,y,z co-ordinates for all drones; in the form vec[_X],vec[_Y],vec[_Z]
        void init()
        {
		FILE *inFile;
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
		FILE *inFile;
                int initCount;
		init();														//read initial position of the drones from the file startposition.txt
		outFile = fopen("droneposition,txt","w+");
		while(1)
		{
			//waitfor(1000);
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				in_ivec.receive(&droneVec[count],count);	//receive the relative X,Y,Z positions from controller 
				droneVec[count][_X] = droneVec[count][_X] + droneInitPos[count][_X];				//get the new position of each drone
                                droneVec[count][_Y] = droneVec[count][_Y] + droneInitPos[count][_Y];
                                droneVec[count][_Z] = droneVec[count][_Z] + droneInitPos[count][_Z];

				//check for collision avoidance before writing to file
				


				fprintf(outFile, "%d %d %d\n",droneVec[count][_X],droneVec[count][_Y],droneVec[count][_Z]);	//write the new position to file for 3d display
			}
			fclose(outFile);   //required?

			//read the updated drone position into droneInitPos array
                	inFile = fopen("droneposition.txt","r+");
                	while(!feof(inFile))
                	{
                		for(initCount=0;initCount<MAX_NO_DRONES;initCount++)
				{
					fscanf(inFile, "%d %d %d",&droneInitPos[initCount][_X],&droneInitPos[initCount][_Y],&droneInitPos[initCount][_Z]);		
				}
			}
			fclose(inFile);	   //required?
		}
	}
};
