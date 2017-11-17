#include "drone.sh"

import "c_vec_queue";
import "c_ivec_queue";
import "c_ivec_array";
import "wireless";

behavior NIC(i_tranceiver wic, i_sender to_formation)
{
	void main(void)
	{
		
	}
};

behavior Vision(i_sender vec_to_nic, i_sender vec_to_formation)
{
	void main(void)
	{

	}
};

behavior Formation(i_receiver self_vec, i_receiver other_ivec, i_sender out_a)
{
	void main(void)
	{

	}
}; 

behavior Controller(i_receiver in_a, i_mon_send out_v)
{
	void main(void)
	{

	}
};

behavior Drone(i_tranceiver wic, i_mon_send out_v)
{
	c_vec_queue vision_nic(1ul);
	c_vec_queue vision_formation(1ul);
	c_vec_queue formation_controller(1ul);
	c_ivec_queue nic_formation(1ul);
	NIC nic(wic, nic_formation);
	Vision vision(vision_nic, vision_formation);
	Formation formation(vision_formation, nic_formation, formation_controller);
	Controller controller(formation_controller, out_v);

	void init(void)
	{
	
	}

	void main(void)
	{
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
