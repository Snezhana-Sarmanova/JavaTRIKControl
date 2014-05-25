#include "inc/Sensor.h"

#include <kni.h>
#include <sni.h>
#include <jvm.h>
#include <pcsl_string.h>

#include <commonKNIMacros.h>
#include <ROMStructs.h>

#include <midpMalloc.h>
#include <midpRMS.h>
#include <midpUtilKni.h>
#include <midpError.h>
#include <midpServices.h>

#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <linux/input.h>
#include <string.h> 
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/time.h>


typedef int bool;
int array[3];
int fd;
bool flag, flagX, flagY, flagZ;
char* EVENTFILE;

//static native int[] read(String eventFile);
KNIEXPORT KNI_RETURNTYPE_OBJECT
Java_com_trikset_control_Sensor3d_read()
{			
	KNI_StartHandles(2);
	KNI_DeclareHandle(returnArray);
	char* file;
	
 	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);		
	} RELEASE_PCSL_STRING_PARAMETER   	
	
	printf("here\n"); 	
	connect(file);
	SNI_NewArray(SNI_INT_ARRAY, 3, returnArray);
	
	if (fd < 0) {
		printf("Failed to open Sensor device file %s \n", file);
		//KNI_ThrowNew(midpIOException, NULL);
	}else{	 		
		KNI_SetIntArrayElement(returnArray, 0, array[0]);      
		KNI_SetIntArrayElement(returnArray, 1, array[1]);
		KNI_SetIntArrayElement(returnArray, 2, array[2]);  
	}

	KNI_EndHandlesAndReturnObject(returnArray);		 
} 

void readFile() 
{
	struct input_event ev;	
	read(fd, &ev, sizeof(ev));
	switch (ev.type) {		   
		case EV_ABS:
			switch (ev.code) {
				case ABS_X:
					array[0] = ev.value;
					flagX = 1;
					break;
				case ABS_Y:
					array[1] = ev.value;
					flagY = 1;
					break;
				case ABS_Z:
					array[2] = ev.value;
					flagZ = 1;
					break;
				}
		case EV_SYN:
			if(flagX & flagY & flagZ){
				flag = 0; 
			}
			break;				
	}
}

void connect(char* ef)
{ 		
	EVENTFILE = ef;
	fd_set wfds;
	struct timeval tv;
	int retval;
	flag = 1;
	flagX = 0; flagY = 0; flagZ = 0;

	fd = open(EVENTFILE, O_RDONLY);
    FD_ZERO(&wfds);
    FD_SET(fd, &wfds);
    
   	 if(FD_ISSET(fd, &wfds) == 0){
			return;
	 }
	
	tv.tv_sec = 1;
	tv.tv_usec = 0;
	
	while(flag){
		retval = select(fd+1, &wfds, &wfds, NULL, &tv);
		
		if (retval>0){
			readFile();
		}
	}
	close(fd);
}
