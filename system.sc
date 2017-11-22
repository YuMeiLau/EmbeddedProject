#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sim.sh>


#include "drone.sh"

import "c_vec_queue";
import "c_ivec_queue";
import "c_ivec_array";
import "drone";
import "wireless"
import "monitor";

behavior Main
{
	wirelessBridge wireless_ch;
	c_mon_array drone_monitor;
	Drone drone(wireless_ch, drone_monitor);
	Monitor monitor(drone_monitor);
			
	int main(void){
		par 
		{
			drone.main();
			monitor.main();
		}
		return (0);
	}
};
