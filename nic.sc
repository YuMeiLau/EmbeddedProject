#include "drone.sh"
#include "metrics.sh"
#include "inc/log.h"
import "wireless";
import "c_vec_queue";
import "c_ivec_queue";

interface NIC_INIT
{
	void init(long id);
};

behavior NIC_QUEUE(i_vec_receiver from_camera, vec my_data, int counter)
{	
   /* For RT algorithms: if outgoing data accumulates faster than the 
	   round robin wireless bridge can send it, only the newest data will
	   be sent (old data is overwritten). Dropped data is counted via
	   _WB_OUTGOING_DATA_DROPPED counter */ 
	void main(void){
		while(1){
			from_camera.receive(&my_data);
			counter++;
		}
	}
};

behavior NIC_RROBIN_MNGR(i_wbridge_tranceiver wic, vec data_out, i_ivec_sender to_formation, int counter, long id) 
{
   long last_received;
	int write;
	/* only sending predefined structures to avoid parsing. defined below */
	ivec data_in, idata_out;

	void main(void)
	{
		if (id == 0) 
			write = 1;
		while(1){
			if (write){
				if (counter > 1) _WB_OUTGOING_DATA_DROPPED += (counter - 1);
				idata_out[_X] = data_out[_X];
				idata_out[_Y] = data_out[_Y];
				idata_out[_Z] = data_out[_Z];
				idata_out[_ID] = id;
				wic.send(id, &idata_out, 4*sizeof(long));
				LOGL_VERBOSE("NIC: Drone %ld sent position over wifi\n", id);
				counter = 0;
				write = 0;
			}
			else if (!write){
				if (wic.receive(&(data_in[3]), &(data_in[0]), 4*sizeof(long))){
					last_received = data_in[3];
					to_formation.send(data_in);
					if ((last_received+1)%MAX_NO_DRONES==id){
						write = 1;
					}
				} else {
					if ((last_received+1)%MAX_NO_DRONES==id){
						waitfor(WB_ROUND_TIMEOUT);
						write = 1;	
					} 
				}
				LOGL_VERBOSE("NIC: Drone %ld received over wifi\n", id);
			}
		}
	}
};

behavior NIC(i_wbridge_tranceiver wic, i_vec_receiver from_camera, i_ivec_sender to_formation) implements NIC_INIT
{
	long id;
	int counter;
	vec my_data;
	NIC_QUEUE nicq(from_camera, my_data, counter);
	NIC_RROBIN_MNGR  mngr(wic, my_data, to_formation, counter, id);	

	void init(long ID){
		counter = 0;
		id = ID;
	}
	void main(void){	
		par {
			nicq;
			mngr;
		}
	}
}; 
