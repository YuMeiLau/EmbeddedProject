#include "drone.sh"
#include "inc/vec.h"
#include "metrics.sh"
import "c_vec_queue";


behavior ImageCruncher(vec rel_position, long ID)
{
	void main(void)
	{
		rel_position[_X] = _DRONE_POSITIONS[ID][_X];
		rel_position[_Y] = _DRONE_POSITIONS[ID][_Y];
		rel_position[_Z] = _DRONE_POSITIONS[ID][_Z];

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

interface V_Init{ void init(long); };

behavior Vision(i_vec_sender vec_to_nic, i_vec_sender vec_to_formation)
{
	long ID;
	vec rel_position;
	ImageCruncher rel_pos_algorithm(rel_position, ID);
	ToNic nic_sender(vec_to_nic, rel_position); 
	ToFormation formation_sender(vec_to_formation, rel_position); 

	void init(long id){
		ID = id;
	}

        void main(void)
        {
		par{
			rel_pos_algorithm;
			nic_sender;
			formation_sender;
		}
        }
};
