#ifndef _LOG_H_
#define _LOG_H

#define DISPLAY_LOGS 1
#define DISPLAY_VERBOSE_LOGS 0

void LOG(const char*);
void LOGL(const char*, long);
void LOG_VERBOSE(const char*);
void LOGL_VERBOSE(const char*, long);

#endif 
