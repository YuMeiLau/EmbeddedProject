#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sim.sh>


#include "drone.sh"
#include "metrics.c"
#include "inc/log.h"
#include "inc/log.c"
import "c_vec_queue";
import "c_ivec_queue";
import "c_ivec_array";
import "drone";
import "wireless";
import "monitor";


behavior Main
{	
	const unsigned long queuesize = MAX_NO_DRONES;
	wirelessBridge wireless_ch;
	c_mon_array drone_monitor(queuesize);
	Drone drone(wireless_ch, drone_monitor, 1l);
	DroneMonitor monitor(drone_monitor);
			
	int main(void){
		LOG("Staring System\n");
		wireless_ch.init();
		drone_monitor.setup();
		par 
		{
			drone.main();
			monitor.main();
		}
		return (0);
	}
};
