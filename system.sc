#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sim.sh>


#include "drone.sh"
#include "inc/log.h"
#include "inc/log.c"
import "c_vec_queue";
import "c_ivec_queue";
import "c_ivec_array";
import "drone";
import "wireless";
import "monitor";
import "stimulus";


behavior Main
{	
	const unsigned long queuesize = MAX_NO_DRONES;
	struct metric_logger metric;
	wirelessBridge wireless_ch(metric);
	c_mon_array drone_monitor(queuesize);
	Stimulus stimulus(metric);
	Drone drone0(wireless_ch, drone_monitor, 0l, metric);
	Drone drone1(wireless_ch, drone_monitor, 1l, metric);
	Drone drone2(wireless_ch, drone_monitor, 2l, metric);
	Drone drone3(wireless_ch, drone_monitor, 3l, metric);
	Drone drone4(wireless_ch, drone_monitor, 4l, metric);
	Drone drone5(wireless_ch, drone_monitor, 5l, metric);
	Drone drone6(wireless_ch, drone_monitor, 6l, metric);
	Drone drone7(wireless_ch, drone_monitor, 7l, metric);
	Drone drone8(wireless_ch, drone_monitor, 8l, metric);
	Drone drone9(wireless_ch, drone_monitor, 9l, metric);
	Drone drone10(wireless_ch, drone_monitor, 10l, metric);
	Drone drone11(wireless_ch, drone_monitor, 11l, metric);
	Drone drone12(wireless_ch, drone_monitor, 12l, metric);
	Drone drone13(wireless_ch, drone_monitor, 13l, metric);
	Drone drone14(wireless_ch, drone_monitor, 14l, metric);
	Drone drone15(wireless_ch, drone_monitor, 15l, metric);
	Drone drone16(wireless_ch, drone_monitor, 16l, metric);
	Drone drone17(wireless_ch, drone_monitor, 17l, metric);
	Drone drone18(wireless_ch, drone_monitor, 18l, metric);
	Drone drone19(wireless_ch, drone_monitor, 19l, metric);
	Drone drone20(wireless_ch, drone_monitor, 10l, metric);
	Drone drone21(wireless_ch, drone_monitor, 21l, metric);
	Drone drone22(wireless_ch, drone_monitor, 22l, metric);
	Drone drone23(wireless_ch, drone_monitor, 23l, metric);
	Drone drone24(wireless_ch, drone_monitor, 24l, metric);
	Drone drone25(wireless_ch, drone_monitor, 25l, metric);
	Drone drone26(wireless_ch, drone_monitor, 26l, metric);
	Drone drone27(wireless_ch, drone_monitor, 27l, metric);
	Drone drone28(wireless_ch, drone_monitor, 28l, metric);
	Drone drone29(wireless_ch, drone_monitor, 29l, metric);
	Drone drone30(wireless_ch, drone_monitor, 20l, metric);
	Drone drone31(wireless_ch, drone_monitor, 31l, metric);
	Drone drone32(wireless_ch, drone_monitor, 32l, metric);
	Drone drone33(wireless_ch, drone_monitor, 33l, metric);
	Drone drone34(wireless_ch, drone_monitor, 34l, metric);
	Drone drone35(wireless_ch, drone_monitor, 35l, metric);
	Drone drone36(wireless_ch, drone_monitor, 36l, metric);
	Drone drone37(wireless_ch, drone_monitor, 37l, metric);
	Drone drone38(wireless_ch, drone_monitor, 38l, metric);
	Drone drone39(wireless_ch, drone_monitor, 39l, metric);
	Drone drone40(wireless_ch, drone_monitor, 40l, metric);
	Drone drone41(wireless_ch, drone_monitor, 41l, metric);
	Drone drone42(wireless_ch, drone_monitor, 42l, metric);
	Drone drone43(wireless_ch, drone_monitor, 43l, metric);
	Drone drone44(wireless_ch, drone_monitor, 44l, metric);
	Drone drone45(wireless_ch, drone_monitor, 45l, metric);
	Drone drone46(wireless_ch, drone_monitor, 46l, metric);
	Drone drone47(wireless_ch, drone_monitor, 47l, metric);
	Drone drone48(wireless_ch, drone_monitor, 48l, metric);
	Drone drone49(wireless_ch, drone_monitor, 49l, metric);
	DroneMonitor monitor(drone_monitor, metric);
	
	int main(void){
		LOG("Staring System\n");
		wireless_ch.init();
		drone_monitor.setup();
		par 
		{
			stimulus.main();
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
			drone10.main();
			drone11.main();
			drone12.main();
			drone13.main();
			drone14.main();
			drone15.main();
			drone16.main();
			drone17.main();
			drone18.main();
			drone19.main();
			drone20.main();
			drone21.main();
			drone22.main();
			drone23.main();
			drone24.main();
			drone25.main();
			drone26.main();
			drone27.main();
			drone28.main();
			drone29.main();
			drone30.main();
			drone31.main();
			drone32.main();
			drone33.main();
			drone34.main();
			drone35.main();
			drone36.main();
			drone37.main();
			drone38.main();
			drone39.main();
			drone40.main();
			drone41.main();
			drone42.main();
			drone43.main();
			drone44.main();
			drone45.main();
			drone46.main();
			drone47.main();
			drone48.main();
			drone49.main();
			monitor.main();
		}
		return (0);
	}
};
