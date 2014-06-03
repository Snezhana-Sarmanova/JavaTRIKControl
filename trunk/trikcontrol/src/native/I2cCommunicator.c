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

#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <linux/input.h>
#include <sys/ioctl.h>
#include <linux/i2c-dev.h>
#include <linux/i2c.h>
#include <string.h> 
#include <stdlib.h>
#include <sys/types.h>
#include <sys/file.h>
#include <sys/time.h>

typedef signed int __s32;
typedef unsigned char __u8;
typedef unsigned short __u16;

static inline __s32 i2c_smbus_access(int file, char read_write, __u8 command, int size, union i2c_smbus_data *data)
{
	struct i2c_smbus_ioctl_data args;

	args.read_write = read_write;
	args.command = command;
	args.size = size;
	args.data = data;
	return ioctl(file, I2C_SMBUS, &args);
}

static inline __s32 i2c_smbus_read_word_data(int file, __u8 command)
{
	union i2c_smbus_data data;
	
	if (i2c_smbus_access(file,I2C_SMBUS_READ,command,
						 I2C_SMBUS_WORD_DATA,&data))
		return -1;
	else
		return 0x0FFFF & data.word;
}

static inline __s32 i2c_smbus_read_i2c_block_data(int file, __u8 command, __u8 length, __u8 *values)
{
		union i2c_smbus_data data;
		int i;

		if (length > 32)
				length = 32;
		data.block[0] = length;
		if (i2c_smbus_access(file,I2C_SMBUS_READ,command,
							 length == 32 ? I2C_SMBUS_I2C_BLOCK_BROKEN :
							  I2C_SMBUS_I2C_BLOCK_DATA,&data))
				return -1;
		else {
				for (i = 1; i <= data.block[0]; i++)
						values[i-1] = data.block[i];
				return data.block[0];
		}
}

static inline __s32 i2c_smbus_write_word_data(int file, __u8 command, __u16 value)
{
	union i2c_smbus_data data;
	data.word = value;
	return i2c_smbus_access(file, I2C_SMBUS_WRITE, command, I2C_SMBUS_WORD_DATA, &data);
}
static inline __s32 i2c_smbus_write_byte_data(int file, __u8 command, __u8 value)
{
	union i2c_smbus_data data;
	data.byte = value;
	return i2c_smbus_access(file,I2C_SMBUS_WRITE,command, I2C_SMBUS_BYTE_DATA, &data);
}

//--------------------------------------------------------------------------------------------------

//native void send0(char[] data, int deviceFileDescriptor);
KNIEXPORT KNI_RETURNTYPE_VOID Java_com_trikset_control_I2cCommunicator_send0() {
    int deviceFileDescriptor = KNI_GetParameterAsInt(2);	
	KNI_StartHandles(2);
    KNI_DeclareHandle(stringObj);
    KNI_GetParameterAsObject(1, stringObj);		
	char *data = NULL;
	
 	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		data = (char*) pcsl_string_get_utf8_data(&arg);
	} RELEASE_PCSL_STRING_PARAMETER  
	
	printf("sizeof data in send i2c = %i \n", sizeof(data));
	
	if ((sizeof(data)/2) == 2) {
		i2c_smbus_write_byte_data(deviceFileDescriptor, data[0], data[1]);
	} else {
		i2c_smbus_write_word_data(deviceFileDescriptor, data[0], data[1] | (data[2] << 8));
	}
	
	KNI_EndHandles();
	KNI_ReturnVoid();
}

//native int read0(char[] data, int deviceFileDescriptor);
KNIEXPORT KNI_RETURNTYPE_INT Java_com_trikset_control_I2cCommunicator_read0() {
    int res;
	int deviceFileDescriptor = KNI_GetParameterAsInt(2);	
	KNI_StartHandles(2);
    KNI_DeclareHandle(stringObj);
    KNI_GetParameterAsObject(1, stringObj);	
   
    char *data = NULL;
	
 	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		data = (char*) pcsl_string_get_utf8_data(&arg);
	} RELEASE_PCSL_STRING_PARAMETER   
		
	if ((sizeof(data)/2) == 1)
	{
		res = i2c_smbus_read_word_data(deviceFileDescriptor, data[0]);
	} else {
		__u8 buffer[4] = {0};
		i2c_smbus_read_i2c_block_data(deviceFileDescriptor, data[0], 4, buffer);
		res = (buffer[3] << 24 | buffer[2] <<  16 | buffer[1] << 8 | buffer[0]);		
	}
	
	KNI_EndHandles();
	KNI_ReturnInt((jint)res); 
}

//native int connect0(String devicePath, int deviceId);
KNIEXPORT KNI_RETURNTYPE_INT Java_com_trikset_control_I2cCommunicator_connect0() {
	int deviceFileDescriptor;
    int deviceId = KNI_GetParameterAsInt(2);	
	KNI_StartHandles(2);
    KNI_DeclareHandle(stringObj);
    KNI_GetParameterAsObject(1, stringObj);	
	char* file  = NULL;
	
 	GET_PARAMETER_AS_PCSL_STRING(1, arg) {		
		file = (char*) pcsl_string_get_utf8_data(&arg);
	} RELEASE_PCSL_STRING_PARAMETER       
	    	
    deviceFileDescriptor = open(file, O_RDWR);
	if (deviceFileDescriptor < 0) {
		printf("Failed to open I2C device file %s \n", file);
		KNI_ThrowNew(midpIOException, NULL);
		KNI_ReleaseHandle(deviceFileDescriptor);
	}

	if (ioctl(deviceFileDescriptor, I2C_SLAVE, deviceId)) {
		printf("ioctl( %i , I2C_SLAVE, %i) failed \n", deviceFileDescriptor, deviceId);
		KNI_ThrowNew(midpIOException, NULL);
		KNI_ReleaseHandle(deviceFileDescriptor);
	}  	
    	
    KNI_EndHandles();
	KNI_ReturnInt((jint) deviceFileDescriptor);  
}

//native void disconnect0(int deviceFileDescriptor);
KNIEXPORT KNI_RETURNTYPE_VOID Java_com_trikset_control_I2cCommunicator_disconnect0() {
	int deviceFileDescriptor = KNI_GetParameterAsInt(1);	
	close(deviceFileDescriptor);
	
    KNI_ReturnVoid();
}
