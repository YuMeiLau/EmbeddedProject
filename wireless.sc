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
#include<stdio.h>
#include <stddef.h>	// get size_t for memcpy declaration
extern void *memcpy(void*, const void*, size_t);
extern void abort(void);

interface i_wbridge_tranceiver
{
  int send(int addr, void *data, unsigned long len); 
  int receive(int *addr, void *data, unsigned long len);
};

channel wirelessBridgeLink(
		void *Data,
		int Addr,
		event sent)
   implements i_wbridge_tranceiver
{
  
  int send(int addr, void *data, unsigned long len){
     unsigned long msg_len;
     
     while (len > 0){	
        msg_len = (len > WB_MAX_PAYLOAD_SIZE)     ? WB_MAX_PAYLOAD_SIZE : len;
    	msg_len = (msg_len < WB_MIN_PAYLOAD_SIZE) ? WB_MIN_PAYLOAD_SIZE : msg_len;
        waitfor((1000 * (msg_len + WB_PACKET_HEADER_SIZE) * 8) / WB_BIT_RATE);
	Addr = addr;
        memcpy((char*)Data,(char*)data, len); 
        notify sent;
        len -= WB_MAX_PAYLOAD_SIZE;
	_PACKETS_SENT++;
     } 
  }

  int receive(int *addr, void *data, unsigned long len){
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
  int senderAddress;
  event sent; 
  
  wirelessBridgeLink linkAccess(D, senderAddress, sent);

  void init(void){
	D = Data;
  }
  
  int send(int addr, void *data, unsigned long len) {
    return linkAccess.send(addr, data, len); 
  }
  
  int receive(int *addr, void *data, unsigned long len) {
    return linkAccess.receive(addr, data, len); 
  }
};
