#include "drone.sh"
#include "inc/vec.h"
#include "stdio.h"
#include "stdlib.h"
#include "inc/log.h"
#include "inc/vec.h"
#include "metrics.sh"
#include <unistd.h>

import "c_ivec_array";
import "c_vec_queue";

behavior DroneMonitor(i_mon_receive in_ivec, struct metric_logger metric)
{
	int droneCollision = 0;
	vec droneRelativeVec[MAX_NO_DRONES];
	bool droneCollisions[MAX_NO_DRONES];	
        vec droneColCheck;
	void init()
        {
		int droneNoCount = 0;
		FILE *inFile;
                int initCount;
                inFile = fopen("startposition.txt","r+");
                while(!feof(inFile))
                {
			droneNoCount++;
			if(droneNoCount == (MAX_NO_DRONES-1))
			{
				break;
			}
			else
			{
                		for(initCount=0;initCount<MAX_NO_DRONES;initCount++)
				{
					droneCollisions[initCount] = false;
					fscanf(inFile, "%ld %ld %ld",&(metric._DRONE_POSITIONS[initCount][_X]),&(metric._DRONE_POSITIONS[initCount][_Y]),&(metric._DRONE_POSITIONS[initCount][_Z]));		
				}
			}
		}
		fclose(inFile);	   //required?
	}
	void main(void)
	{
		FILE *outFile;
		int count,initCount,colCount;
		FILE *inFile;
		sim_time first_col_time;
		bool flag=false;
		int last_col_num;
        	long swarm_diameter = 0;
		long min_drone_dist, ave_min_drone_dist, tmp_dist;
		double swarm_radius_meters;
		double density, min_density, max_density;
		max_density = 0.0;
		min_density = 99999;
		LOG("Starting Monitor\n");
		init();														//read initial position of the drones from the file startposition
		printf("\033[2J");
		while(1)
		{
			if (PRINT_METRICS){
				
				printf("\033[2J");
				printf("\n\n********COLLISION AVOIDANCE INFORMATION********\n");
				printf("Number of Drones: %d\n", MAX_NO_DRONES);
				printf("Colisions: %d\n", droneCollision);
				printf("Swarm Diameter: %d milimeters\n", swarm_diameter);
				swarm_radius_meters = (double)swarm_diameter / 2000;
				density = (double) (MAX_NO_DRONES / (4.1888 * swarm_radius_meters * swarm_radius_meters * swarm_radius_meters));
				max_density = (density < 9999 && density > max_density) ? density : max_density; 
				min_density = (density < min_density) ? density : min_density; 
				printf("Swarm Density: %f drones per sq meter\n", density);
				printf("Min Density: %f drones per sq meter\n", min_density);
				printf("Max Density: %f drones per sq meter\n", max_density);
				printf("Ave Min Drone Dist: %d milimeters\n", ave_min_drone_dist);
				if (droneCollision > 0){
					if (last_col_num == 0){
						first_col_time = now();
					}
					printf("First Collision: %d miliseconds", (first_col_time/1000000));
				}
				printf("\n\n**********WIRELESS NETWORK STATISTICS**********\n");
				printf("Packets Sent: %ld\n", metric._PACKETS_SENT);	
				printf("Packets Dropped: %ld\n", metric._PACKETS_DROPPED);
                                printf("time:%d\n",(now()/1000000000));	
				last_col_num = droneCollision;
			}

			waitfor(TIME_STEP);
			outFile = fopen("droneposition.txt","w+");
			swarm_diameter = 0;
			ave_min_drone_dist = 0;
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				in_ivec.receive(&droneRelativeVec[count],count);						//receive the relative X,Y,Z positions from controller 
				metric._DRONE_POSITIONS[count][_X] = metric._DRONE_POSITIONS[count][_X] + droneRelativeVec[count][_X];			//get the new position of each drone
                                metric._DRONE_POSITIONS[count][_Y] = metric._DRONE_POSITIONS[count][_Y] + droneRelativeVec[count][_Y];
                                metric._DRONE_POSITIONS[count][_Z] = metric._DRONE_POSITIONS[count][_Z] + droneRelativeVec[count][_Z];
				min_drone_dist = 9999;

				//check for collision avoidance before writing to file
				flag = false;
				for(colCount=0;colCount<MAX_NO_DRONES;colCount++)
				{
					if(count != colCount)				
					{
						vec_minus(&droneColCheck,metric._DRONE_POSITIONS[count],metric._DRONE_POSITIONS[colCount]);
						tmp_dist = vec_mag(droneColCheck);
						if (tmp_dist > swarm_diameter){
							swarm_diameter = tmp_dist;
						}
						if (tmp_dist < min_drone_dist)
							min_drone_dist = tmp_dist;
						if(vec_mag(droneColCheck) < COLLISION_DISTANCE)
						{
							flag = true;
							if (droneCollisions[count] == false){
								droneCollisions[count] = true;
								droneCollision++;
							}
						}
					}	
					if(flag == true)
					{
						break;
					}
				}
				if (flag == false) droneCollisions[count] == false;
				ave_min_drone_dist += min_drone_dist;
				fprintf(outFile, "%ld %ld %ld\n",metric._DRONE_POSITIONS[count][_X],metric._DRONE_POSITIONS[count][_Y],metric._DRONE_POSITIONS[count][_Z]);	//write the new position to file for 3d display
			}
			ave_min_drone_dist = ave_min_drone_dist / MAX_NO_DRONES;
			LOG("Monitor: Positions Updated\n");
			fclose(outFile);   //required?
		}
	}
};
