#include "drone.sh"
#include "inc/vec.h"
import "c_vec_queue";


behavior ImageCruncher(vec rel_position)
{
	void main(void)
	{
		rel_position[_X] = 0;
		rel_position[_Y] = 0;
		rel_position[_Z] = 0;

	}
};

behavior ToNic(i_vec_sender vec_to_nic, vec rel_position)
{
	void main(void)
	{
		vec_to_nic.send(rel_position);
	}
};


behavior ToFormation(i_vec_sender vec_to_formation, vec rel_position)
{
	void main(void)
	{
		vec_to_formation.send(rel_position);
	}
};

behavior Vision(i_vec_sender vec_to_nic, i_vec_sender vec_to_formation)
{
	vec rel_position;
	ImageCruncher rel_pos_algorithm(rel_position);
	ToNic nic_sender(vec_to_nic, rel_position); 
	ToFormation formation_sender(vec_to_formation, rel_position); 
        void main(void)
        {
		par{
			rel_pos_algorithm;
			nic_sender;
			formation_sender;
		}
        }
};
