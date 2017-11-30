#include "drone.sh"
#include "stdio.h"
#include "iostream"
#include "string"
#include "sstream"
#include "fstream" /* Stream class to both read and write from/to files */

using namespace std;

import "c_ivec_array";

behavior DroneMonitor(i_mon_receive in_ivec)
{
	c_mon_array channel(MAX_NO_DRONES);
	vec droneVec[MAX_NO_DRONES],droneInitPos[MAX_NO_DRONES];			//this is a vector containing x,y,z co-ordinates for all drones; in the form vec[_X],vec[_Y],vec[_Z]
		
	void init()
	{
		fstream inFile;
		int initCount;
		string initLine;
		outFile.open("startposition.txt");
		for(initCount=0;initCount<MAX_NO_DRONES;initCount++)
		{
			getline(inFile,initLine)
			//std::replace(initLine.begin(),initLine.end(), ' ');
			stringstream ss(line);
			ss >> droneInitPos[initCount][_X];
			ss >> droneInitPos[initCount][_Y];
			ss >> droneInitPos[initCount][_Z];
		}
	}
	void main()
	{
		fstream outFile;
		int count;
		string line;
		outFile.fopen("droneposition.txt");
		while(1)
		{
			//waitfor(1000);    //check this number later!
			for(count=0;count<MAX_NO_DRONES;count++)
			{
				droneVec[count] = channel.receive(&in_ivec,count);
				droneVec[count][_X] += droneVec[count][_X] + droneInitPos[_X];
				droneVec[count][_Y] += droneVec[count][_Y] + droneInitPos[_Y];
				droneVec[count][_Z] += droneVec[count][_Z] + droneInitPos[_Z];
				outFile << droneVec[count][_X] << " " << droneVec[count][_Y] << " " << droneVec[count][_Z] << endl;
			}
		}				
	}
};
