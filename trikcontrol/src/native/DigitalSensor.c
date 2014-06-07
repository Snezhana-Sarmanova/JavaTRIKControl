#include <kni.h>
#include <pcsl_string.h>

#include <commonKNIMacros.h>

#include <midpUtilKni.h>
#include <midpError.h>

#include <unistd.h>
#include <stdio.h>
#include <fcntl.h>
#include <linux/input.h>
#include <string.h> 
#include <stdlib.h>

//static native int read0(String devFile);
KNIEXPORT KNI_RETURNTYPE_INT
Java_com_trikset_control_DigitalSensor_read0()
{	
	char* file;
	int res;	
	KNI_StartHandles(1);
	
 	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);		
	} RELEASE_PCSL_STRING_PARAMETER   		 
	  	
	FILE *fp = fopen(file, "w");
	if(fp == NULL) {
		printf("Can't open digital sensor file %s\n", file);
		KNI_ThrowNew(midpIOException, NULL);
	}else{
		fscanf(fp, "%i", &res);
		fclose(fp);
	}	
	
	KNI_EndHandles();
	KNI_ReturnInt((jint)res);		 
} 
