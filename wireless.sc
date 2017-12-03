/****************** Wireless Bridge ***********************/
/*
 * Wireless Bridge models an ad-hoc wireless network. 
 * It currently models WiFiNodes
 * communicate purely via MAC-broadcast messages (most
 * algorithms seem to be one-many data transmission) 
 * Transmission time determined by device bitrate, max
 * packet size, and packet header size in drone.sh file 
*/ 

#include "drone.sh"
#include "metrics.sh"
#include "inc/log.h"
#include<stdio.h>
#include <stddef.h>	// get size_t for memcpy declaration
extern void *memcpy(void*, const void*, size_t);
extern void abort(void);

interface i_wbridge_tranceiver
{
  int send(long addr, void *data, unsigned long len); 
  int receive(long *addr, void *data, unsigned long len);
};

channel wirelessBridgeLink(
		void *Data,
		long Addr,
		event sent)
   implements i_wbridge_tranceiver
{
  
  int send(long addr, void *data, unsigned long len){
     unsigned long msg_len, total_msg_len, i;
    LOGL("NETWORK:1 Drone %ld sending packet\n", addr); 
     for (i = 0; i < len; i += WB_MAX_PAYLOAD_SIZE){	
	total_msg_len = len - i;
    LOGL("NETWORK:2 Drone %ld sending packet\n", addr); 
printf("LEN=%ld\n",total_msg_len); 
        msg_len = (total_msg_len > WB_MAX_PAYLOAD_SIZE)     ? WB_MAX_PAYLOAD_SIZE : total_msg_len;
    	msg_len = (msg_len < WB_MIN_PAYLOAD_SIZE) ? WB_MIN_PAYLOAD_SIZE : msg_len;
        waitfor((1000 * (msg_len + WB_PACKET_HEADER_SIZE) * 8) / WB_BIT_RATE);
	Addr = addr;
        memcpy((char*)Data,(char*)data, len); 
        notify sent;
        total_msg_len -= WB_MAX_PAYLOAD_SIZE;
	_PACKETS_SENT++;
     } 
    LOGL("NETWORK:11 Drone %ld sending packet\n", addr); 
  }

  int receive(long *addr, void *data, unsigned long len){
     unsigned long msg_len;
     while (len > 0){	
        msg_len = (len > WB_MAX_PAYLOAD_SIZE)     ? WB_MAX_PAYLOAD_SIZE : len;
    	msg_len = (msg_len < WB_MIN_PAYLOAD_SIZE) ? WB_MIN_PAYLOAD_SIZE : msg_len;
     	wait sent;
        *addr = Addr;
     	memcpy((char*)data,(char*)Data, len); 
        len -= WB_MAX_PAYLOAD_SIZE;
     }
  }

};

interface Init
{
  void init(void);
};
  
channel wirelessBridge()
  implements i_wbridge_tranceiver, Init
{
  unsigned char Data[WB_MAX_PAYLOAD_SIZE];
  void *D;
  long senderAddress;
  event sent; 
  
  wirelessBridgeLink linkAccess(D, senderAddress, sent);

  void init(void){
	D = Data;
  }
  
  int send(long addr, void *data, unsigned long len) {
    LOGL("NETWORK:0 Drone %ld sending packet\n", addr); 
    return linkAccess.send(addr, data, len); 
  }
  
  int receive(long *addr, void *data, unsigned long len) {
    LOG("NETWORK: Drone receiving packet\n"); 
    return linkAccess.receive(addr, data, len); 
  }
};
