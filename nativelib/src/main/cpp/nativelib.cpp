#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "NativeLib"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

typedef struct NativeLibData {
    int i;
    long l;
    std::string s;
    float f;
    short st;
    double d;
    bool b;
    char c;
    char *array;
} data_t;

extern "C" JNIEXPORT jstring JNICALL
Java_com_virogu_android_nativelib_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from";
    hello.append(" C++");
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jobject JNICALL
Java_com_virogu_android_nativelib_NativeLib_testObject(JNIEnv *env, jobject thiz, jobject arg) {
    jclass dataCls = env->GetObjectClass(arg);
    //jclass dataCls = env->FindClass("com/virogu/android/nativelib/Data");

    jfieldID j_int = env->GetFieldID(dataCls, "j_int", "I");
    jfieldID j_long = env->GetFieldID(dataCls, "j_long", "J");
    jfieldID j_string = env->GetFieldID(dataCls, "j_string", "Ljava/lang/String;");
    jfieldID j_float = env->GetFieldID(dataCls, "j_float", "F");
    jfieldID j_short = env->GetFieldID(dataCls, "j_short", "S");
    jfieldID j_double = env->GetFieldID(dataCls, "j_double", "D");
    jfieldID j_boolean = env->GetFieldID(dataCls, "j_boolean", "Z");
    jfieldID j_char = env->GetFieldID(dataCls, "j_char", "C");
    jfieldID j_byteArray = env->GetFieldID(dataCls, "j_byteArray", "[B");

    auto *nativeLibData = new NativeLibData;
    nativeLibData->i = env->GetIntField(arg, j_int);
    nativeLibData->l = env->GetLongField(arg, j_long);

    auto jstring1 = reinterpret_cast<jstring>(env->GetObjectField(arg, j_string));
    char *string = (char *) env->GetStringUTFChars(jstring1, nullptr);
    nativeLibData->s.clear();
    nativeLibData->s.append(string);
    env->ReleaseStringUTFChars(jstring1, string);

    nativeLibData->f = env->GetFloatField(arg, j_float);
    nativeLibData->st = env->GetShortField(arg, j_short);
    nativeLibData->d = env->GetDoubleField(arg, j_double);
    nativeLibData->b = env->GetBooleanField(arg, j_boolean);
    nativeLibData->c = env->GetCharField(arg, j_char);

    auto jbyteArray1 = reinterpret_cast<jbyteArray>(env->GetObjectField(arg, j_byteArray));
    jbyte *bytes;
    bytes = env->GetByteArrayElements(jbyteArray1, nullptr);
    int chars_len = env->GetArrayLength(jbyteArray1);
    nativeLibData->array = new char[chars_len + 1];
    memset(nativeLibData->array, 0, chars_len + 1);
    memcpy(nativeLibData->array, bytes, chars_len);
    nativeLibData->array[chars_len] = 0;
    env->ReleaseByteArrayElements(jbyteArray1, bytes, 0);

    LOGE("%d, %ld, %s, %f, %d, %f, %d, %c",
         nativeLibData->i, nativeLibData->l, nativeLibData->s.c_str(),
         nativeLibData->f, nativeLibData->st, nativeLibData->d, nativeLibData->b, nativeLibData->c);

    jmethodID j_init = env->GetMethodID(dataCls, "<init>", "(IJLjava/lang/String;FSDZC[B)V");

    jint new_int = 222;
    jlong new_long = 222;
    jstring new_string = env->NewStringUTF("222");
    jfloat new_float = 222.2;
    jshort new_short = 2;
    jdouble new_double = 2.22;
    jboolean new_boolean = true;
    jchar new_char = '2';
    jbyteArray new_byteArray = env->NewByteArray(20);

    jobject newObj = env->NewObject(dataCls, j_init, new_int, new_long, new_string, new_float,
                                    new_short, new_double, new_boolean, new_char, new_byteArray);
    return newObj;
}