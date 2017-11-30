#include "vec.h"
#include "math.h"
void vec_minus(vec* out_vec, vec a, vec b){        
  (*out_vec)[_X] = a[_X] - b[_X];    
  (*out_vec)[_Y] = a[_X] - b[_Y];    
  (*out_vec)[_Z] = a[_X] - b[_Z];    
}

void vec_add(vec* out_vec, vec a, vec b){    
  (*out_vec)[_X] = a[_X] + b[_X];    
  (*out_vec)[_Y] = a[_X] + b[_Y];    
  (*out_vec)[_Z] = a[_X] + b[_Z];    
}

void vec_mult(vec* out_vec, vec a, long b){  
  (*out_vec)[_X] = a[_X] * b;        
  (*out_vec)[_Y] = a[_X] * b;        
  (*out_vec)[_Z] = a[_X] * b;        
}

long vec_mag(vec in_vec){
  long xS, yS, zS; 
  xS = in_vec[_X] * in_vec[_X];	
  yS = in_vec[_Y] * in_vec[_Y];	
  zS = in_vec[_Z] * in_vec[_Z];
  return sqrtl(xS + yS + zS);	
}
