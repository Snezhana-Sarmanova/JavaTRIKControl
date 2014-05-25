#include <stdio.h>
#include <string.h> 
#include <stdlib.h>	

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


// native void init(int period, String file);
KNIEXPORT KNI_RETURNTYPE_VOID
Java_com_trikset_control_ServoMotor_init0()
{	
	int period = KNI_GetParameterAsInt(1);	
	char* file;
	KNI_StartHandles(1);
	
	GET_PARAMETER_AS_PCSL_STRING(2, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);
	} RELEASE_PCSL_STRING_PARAMETER 
	
    char PeriodFile[80];	
	strcpy (PeriodFile, file);
	strcat (PeriodFile, "/period_ns");
	
	FILE *fp = fopen(PeriodFile, "w");
	if(fp == NULL) {
		printf("Can't open motor period file %s\n", PeriodFile);
		KNI_ThrowNew(midpIOException, NULL);
	}else{
		fprintf(fp, "%i", period);
		fclose(fp);
	}
    
    KNI_EndHandles();
	KNI_ReturnVoid();	
}

//native void setPower(int power, String file);
KNIEXPORT KNI_RETURNTYPE_VOID
Java_com_trikset_control_ServoMotor_setPower0()
{	
	int duty = KNI_GetParameterAsInt(1);	
	char* file = NULL;
	KNI_StartHandles(1);

	GET_PARAMETER_AS_PCSL_STRING(2, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);
	} RELEASE_PCSL_STRING_PARAMETER 

    char DutyFile[80];
	strcpy (DutyFile, file);
	strcat (DutyFile, "/duty_ns");
		
    FILE *fp = fopen(DutyFile, "w");
    if(fp == NULL) {
		printf("Can't open motor control file %s\n", DutyFile);
		KNI_ThrowNew(midpIOException, NULL);
	}else{
		fprintf(fp, "%i", duty);
		fclose(fp);
	}
    	
	char RunFile[80];
	strcpy (RunFile, file);
	strcat (RunFile, "/run");
  
	FILE *fr = fopen(RunFile, "w");
	if(fr == NULL) {
		printf("Can't open motor run file %s\n", RunFile);
		KNI_ThrowNew(midpIOException, NULL);
	}else{	
		int flag = 1;
		fprintf(fr, "%i", flag);
		fclose(fr);
	}
	
	KNI_EndHandles();
	KNI_ReturnVoid();
}

//native void powerOff(String file);
KNIEXPORT KNI_RETURNTYPE_VOID
Java_com_trikset_control_ServoMotor_powerOff0()
{
	char* file;
	KNI_StartHandles(1);
	
	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);		
	} RELEASE_PCSL_STRING_PARAMETER 
	
	char RunFile[80];
	strcpy (RunFile, file);
	strcat (RunFile, "/run");
	
	FILE *fr = fopen(RunFile, "w");
	if(fr == NULL) {
		printf("Can't open motor run file %s\n", RunFile);
		KNI_ThrowNew(midpIOException, NULL);
	}else{		
		int flag = 0;
		fprintf(fr, "%i", flag);
		fclose(fr);  
    }
    
    KNI_EndHandles();
	KNI_ReturnVoid(); 
}
