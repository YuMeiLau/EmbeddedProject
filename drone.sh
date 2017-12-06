#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sim.sh>


#ifndef _DRONE_SH_
#define _DRONE_SH_

#define PRINT_METRICS 1
#define MAX_NO_DRONES 10
#define TARGET_SPEED 10
#define TARGET_BOUNDS 5000
 
/****************************************************/
/****       Wireless Bridge Configuration        ****/
/****************************************************/
/* UDP header: 8 bytes                               *
 * IP header: 20 bytes                               *
 * L2 header: 26 bytes (including FCS&preamble)*
 * Standard total header: 54 bytes but modify below  *
 * definition if custom/different protocol is used   */
#define WB_PACKET_HEADER_SIZE 54
#define WB_MAX_PAYLOAD_SIZE 1480
#define WB_MIN_PAYLOAD_SIZE 46   
/* TODO maybe. model packet loss based on bitrate,   *
 * distance, and power                               *
 * Bit Rate is Mbps                                  */ 
#define WB_BIT_RATE 11  


/****************************************************/
/****       Drone Algorithm Configuration        ****/
/****************************************************/
/* Wireless Bridge shared via round robin 
 * Timestep = amount of time allotted to each drone
 * Timestep of 0 allots one packet to each drone and 
 * attempts to fully utilize the channel
 * Timeout defines the amount of time a drone waits
 * for a packet before giving it up as dropped      */
#define WB_ROUND_TIMESTEP 0
#define WB_ROUND_TIMEOUT (8000 * (WB_MAX_PAYLOAD_SIZE + WB_PACKET_HEADER_SIZE) / WB_BIT_RATE)
#define FORMATION_HEIGHT 5000
#define TIME_STEP_HZ 60	      /* added for ease of use */
#define TIME_STEP 16666666    /* In nanoseconds: 16666666=60Hz */

/****************************************************/
/****            Camera Configuration            ****/
/****************************************************/
#define FRAME_RATE 33333333   /* In nanoseconds: 33333333=30HZ */

/****************************************************/
/****     Drone Physics Model Configuration      ****/
/****************************************************/
#define DR_MAX_ACC 20000      /* milimeters / square second */
#define DR_MAX_VEL 16000      /* milimeters / second */
#define SAFE_DISTANCE 2000    /* milimeters */
#define COLLISION_DISTANCE 300



#endif
