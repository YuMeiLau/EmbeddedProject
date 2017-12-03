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
	Drone drone0(wireless_ch, drone_monitor, 0l);
	Drone drone1(wireless_ch, drone_monitor, 1l);
	Drone drone2(wireless_ch, drone_monitor, 2l);
	Drone drone3(wireless_ch, drone_monitor, 3l);
	Drone drone4(wireless_ch, drone_monitor, 4l);
	Drone drone5(wireless_ch, drone_monitor, 5l);
	Drone drone6(wireless_ch, drone_monitor, 6l);
	Drone drone7(wireless_ch, drone_monitor, 7l);
	Drone drone8(wireless_ch, drone_monitor, 8l);
	Drone drone9(wireless_ch, drone_monitor, 9l);
	DroneMonitor monitor(drone_monitor);
			
	int main(void){
		LOG("Staring System\n");
		wireless_ch.init();
		drone_monitor.setup();
		par 
		{
			drone0.main();
			drone1.main();
			drone2.main();
			drone3.main();
			drone4.main();
			drone5.main();
			drone6.main();
			drone7.main();
			drone8.main();
			drone9.main();
			monitor.main();
		}
		return (0);
	}
};
