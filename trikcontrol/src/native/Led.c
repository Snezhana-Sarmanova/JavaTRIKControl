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


// native void write0(String file, String command);
KNIEXPORT KNI_RETURNTYPE_VOID
Java_com_trikset_control_Led_write0()
{
	char* file;
	char* command;
	KNI_StartHandles(2);
	
	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);		
	} RELEASE_PCSL_STRING_PARAMETER 
	
	GET_PARAMETER_AS_PCSL_STRING(2, arg) {		
		command = (char*) pcsl_string_get_utf8_data(&arg);		
	} RELEASE_PCSL_STRING_PARAMETER 
	
	FILE *fp = fopen(file, "w");
	if(fp != NULL) {
		fprintf(fp, "%s", command);
		fclose(fp);  
    }
    
    KNI_EndHandles();
    KNI_ReturnVoid(); 
}
