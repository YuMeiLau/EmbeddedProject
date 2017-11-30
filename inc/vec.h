#ifndef _VEC_H_
#define _VEC_H_

typedef long vec[3];
typedef long ivec[4];
#define _X 0
#define _Y 1
#define _Z 2
#define _ID 3
  
void vec_minus(vec* out_vec, vec a, vec b);        
void vec_add(vec* out_vec, vec a, vec b);  
void vec_mult(vec* out_vec, vec a, long b);  
long vec_mag(vec in_vec);
void vec_norm(vec* in_vec);
/* vec = 3D vector. vec[_X] = x value, ect */
/* ivec = 3D vector with id tag. ivec[_ID] = id tag */

#endif
