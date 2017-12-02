#include "vec.h"
#include "math.h"
void vec_minus(vec* out_vec, vec a, vec b){        
  (*out_vec)[_X] = a[_X] - b[_X];    
  (*out_vec)[_Y] = a[_Y] - b[_Y];    
  (*out_vec)[_Z] = a[_Z] - b[_Z];    
}

void vec_add(vec* out_vec, vec a, vec b){    
  (*out_vec)[_X] = a[_X] + b[_X];    
  (*out_vec)[_Y] = a[_Y] + b[_Y];    
  (*out_vec)[_Z] = a[_Z] + b[_Z];    
}

void vec_mult(vec* out_vec, vec a, long b){  
  (*out_vec)[_X] = a[_X] * b;        
  (*out_vec)[_Y] = a[_Y] * b;        
  (*out_vec)[_Z] = a[_Z] * b;        
}

void vec_div(vec* out_vec, vec a, long b){  
  (*out_vec)[_X] = a[_X] / b;        
  (*out_vec)[_Y] = a[_Y] / b;        
  (*out_vec)[_Z] = a[_Z] / b;        
}

long vec_mag(vec in_vec){
  long xS, yS, zS; 
  xS = in_vec[_X] * in_vec[_X];	
  yS = in_vec[_Y] * in_vec[_Y];	
  zS = in_vec[_Z] * in_vec[_Z];
  return sqrtl(xS + yS + zS);	
}

long vec_hor_mag(vec in_vec){
  long xS, yS; 
  xS = in_vec[_X] * in_vec[_X];	
  yS = in_vec[_Y] * in_vec[_Y];	
  return sqrtl(xS + yS);	
}

void vec_norm(vec* in_vec){
  long mag;
  mag = vec_mag(*in_vec);
  (*in_vec)[_X] = (*in_vec)[_X] / mag;
  (*in_vec)[_Y] = (*in_vec)[_Y] / mag;
  (*in_vec)[_Z] = (*in_vec)[_Z] / mag;
}


