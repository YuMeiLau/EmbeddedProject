#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <sim.sh>


#ifndef _DRONE_SH_
#define _DRONE_SH_

typedef int vec[3];
typedef int ivec[4];
#define _X 0
#define _Y 1
#define _Z 2
#define _ID 3
#define MAX_NO_DRONES 10

/* vec = 3D vector. vec[_X] = x value, ect */
/* ivec = 3D vector with id tag. ivec[_ID] = id tag */
 
/****************************************************/
/****       Wireless Bridge Configuration        ****/
/****************************************************/
/* UDP header: 8 bytes                               *
 * IP header: 20 bytes                               *
 * Ethernet header: 26 bytes (including FCS&preamble)*
 * Standard total header: 54 bytes but modify below  *
 * definition if custom/different protocol is used   */
#define WB_PACKET_HEADER_SIZE 54
#define WB_MAX_PAYLOAD_SIZE 1480
#define WB_MIN_PAYLOAD_SIZE 46   
/* TODO maybe. model packet loss based on bitrate,   *
 * distance, and power                               *
 * Bit Rate is Mbps                                  */ 
#define WB_BIT_RATE 20  


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


/****************************************************/
/****            Camera Configuration            ****/
/****************************************************/
#define TIME_STEP 16666666    /* In nanoseconds: 16666666=60Hz */


/****************************************************/
/****     Drone Physics Model Configuration      ****/
/****************************************************/
#define FRAME_RATE 33333333   /* In nanoseconds: 33333333=30HZ */







#endif
