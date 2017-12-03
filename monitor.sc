#include "drone.sh"
#include "inc/vec.h"
#include "stdio.h"
#include "stdlib.h"
#include "inc/log.h"

import "c_ivec_array";
import "c_vec_queue";

behavior DroneMonitor(i_mon_receive in_ivec)
{
	vec droneVec[MAX_NO_DRONES],droneInitPos[MAX_NO_DRONES],droneRelativeVec[MAX_NO_DRONES];	//this is a vector containing x,y,z co-ordinates for all drones; in the form vec[_X],vec[_Y],vec[_Z]
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
		fclose(inFile);	   //required?
	}
	void main(void)
	{
		FILE *outFile;
		int count,initCount,colCount;
		FILE *inFile;
		bool flag=false;
		LOG("Starting Monitor\n");
		init();														//read initial position of the drones from the file startposition.txt
		while(1)
		{
			//waitfor(1000);
			outFile = fopen("droneposition.txt","w+");
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				in_ivec.receive(&droneRelativeVec[count],count);						//receive the relative X,Y,Z positions from controller 
				droneVec[count][_X] = droneInitPos[count][_X] + droneRelativeVec[count][_X];			//get the new position of each drone
                                droneVec[count][_Y] = droneInitPos[count][_Y] + droneRelativeVec[count][_Y];
                                droneVec[count][_Z] = droneInitPos[count][_Z] + droneRelativeVec[count][_Z];

				//check for collision avoidance before writing to file
				flag = false;
				for(colCount=0;colCount<MAX_NO_DRONES;colCount++)
				{
					if(flag == true)
					{
						break;
					}
					else
					{
						if(count != colCount)									//compare with other drones only
						{
							if(((droneVec[count][_X] - droneVec[colCount][_X]) < SAFE_DISTANCE) || ((droneVec[count][_Y] - droneVec[colCount][_Y]) < SAFE_DISTANCE) || ((droneVec[count][_Z] - droneVec[colCount][_Z]) < SAFE_DISTANCE))
							{
								flag = true;
								droneVec[count][_X] = droneInitPos[count][_X] - droneRelativeVec[count][_X];
			                                	droneVec[count][_Y] = droneInitPos[count][_Y] - droneRelativeVec[count][_Y];
                        			        	droneVec[count][_Z] = droneInitPos[count][_Z] - droneRelativeVec[count][_Z];		
							}
						}	
					}		
				}
				fprintf(outFile, "%d %d %d\n",droneVec[count][_X],droneVec[count][_Y],droneVec[count][_Z]);	//write the new position to file for 3d display
			}
			LOG("Monitor: Positions Updated\n");
			fclose(outFile);   //required?

			//read the updated drone position into droneInitPos array
                	/*inFile = fopen("droneposition.txt","r+");
                	while(!feof(inFile))
                	{
                		for(initCount=0;initCount<MAX_NO_DRONES;initCount++)
				{
					fscanf(inFile, "%d %d %d",&droneInitPos[initCount][_X],&droneInitPos[initCount][_Y],&droneInitPos[initCount][_Z]);		
				}
			}*/
		}
	}
};
