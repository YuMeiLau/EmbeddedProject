#include "drone.sh"

import "c_vec_queue";
import "c_ivec_queue";
import "c_ivec_array";
import "wireless";
import "nic";
import "vision";
import "formation";
import "controller";

interface Drone_Init{ void init(void); };

behavior Drone(i_wbridge_tranceiver wic, i_mon_send out_v, in const long network_id) implements Drone_Init
{
	long ID;
	c_vec_queue vision_nic(1ul);
	c_vec_queue vision_formation(1ul);
	c_vec_queue formation_controller(1ul);
	c_ivec_queue nic_formation(1ul);
	NIC nic(wic, vision_nic, nic_formation);
	Vision vision(vision_nic, vision_formation);
	Formation formation(vision_formation, nic_formation, formation_controller);
	Controller controller(formation_controller, out_v);

	void init(void)
	{	
		ID = network_id;
		nic.init(ID);
		formation.init(ID);	
	}

	void main(void)
	{
		if (ID >= MAX_NO_DRONES) return;
		init();		
		par 
		{
			nic.main();
			vision.main();
			formation.main();
			controller.main();
		}
	}
};
