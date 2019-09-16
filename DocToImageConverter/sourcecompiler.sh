export JAVA_HOME=/c/'Program Files'/Java/jdk1.8.0_161
export LIB_PATH=/c/Users/Jayaprakash/Desktop/DocToImageConverter/lib

export CLASSPATH=.:${LIB_PATH}/Custom.jar:${LIB_PATH}/commons-io-2.6.jar:${LIB_PATH}/commons-lang3-3.7.jar:${LIB_PATH}/gson-2.8.5.jar:${LIB_PATH}/jodconverter-core-4.2.2.jar:${LIB_PATH}/jodconverter-local-4.2.2.jar:${LIB_PATH}/juh-4.1.2.jar:${LIB_PATH}/jurt-4.1.2.jar:${LIB_PATH}/ridl-4.1.2.jar:${LIB_PATH}/slf4j-api-1.7.25.jar:${LIB_PATH}/unoil-4.1.2.jar:classes

echo $CLASSPATH

javac -d classes ./src/ntp/utils/IniFile.java
javac -d classes ./src/ntp/test/DocToJpeg.java
javac -d classes ./src/ntp/test/DocToImageConverter.java

cd classes
jar -cvf Custom.jar ntp