#include <Arduino.h>
#include "DHT.h"
#include "MQ135.h"
#include "ArduinoJson.h"
#include <WiFi.h>
#include <HTTPClient.h>
#include <SoftwareSerial.h>
#include <Wire.h> 

#include "RTClib.h"

#define ANALOGPIN 14
#define DHTPIN 4     
#define DHTTYPE DHT11

#define trigPin  2
#define echoPin  5
//propias del dispositivo
#define pisoMax 5
#define Dispositivo 1
//variable e enviar en json
int piso=0;
float temperatura=0;
float humedad=0;
float air_cual=0;


// defines variables

char responseBuffer[300];
WiFiClient client;

const char* SSID = "Gustavo";
const char* PASS = "lalalala";

const char* SERVER_IP = "192.168.43.158";
int SERVER_PORT = 8080;

DateTime now ;
unsigned long prevTiempo = 0;
const long intervalo = 1000; // tiempo en ms
long duration;
int distance;
int d=17;
int mov=0;
int first=0;

//funciones  
RTC_DS1307 RTC;
MQ135 gasSensor = MQ135(ANALOGPIN);
DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  WiFi.begin(SSID, PASS);

  Serial.print("Connecting...");
  while (WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print(".");
  }
  Serial.print("Connected, IP address: ");
  Serial.print(WiFi.localIP());
  Wire.begin(); 
  RTC.begin();
  //RTC.adjust(DateTime(2020, 5, 16, 18, 41, 15));
  
 // Serial.println(F("DHTxx test!"));
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
//  pinMode(boton,INPUT);
  pinMode(15, OUTPUT);//subir
  pinMode(17, OUTPUT);//bajar
  pinMode(27, INPUT);
  pinMode(25, INPUT);
  dht.begin();
}

void loop() {
  if(piso==pisoMax || piso==0){
    digitalWrite(15,LOW);
  digitalWrite(17,LOW);
    mov=0;
  }
  now = RTC.now();

  
  
  if(now.second()==0 && mov==0){
    mov=1;
    if(d==15){
    d=17; 
      }else{
    d=15;
    }
     leeTemyHum();
     leecalidad();
     ///////////////////////////////////Aqui enviar el json///////////////////////

 sendPostRequest();
  Serial.print("temperatura= ");
  Serial.println(temperatura);
  Serial.print("humedad= ");
  Serial.println(humedad);
  Serial.print("calidad del aire= ");
  Serial.println(air_cual);
  Serial.print("piso= ");
  Serial.println(piso);
  
    ////////////////////////////////////////////////////////////////////////////
    piso = -1;
    first = 1;
  digitalWrite(15,LOW);
  digitalWrite(17,LOW);
  digitalWrite(d,HIGH);
  }
  if((piso!=pisoMax && piso!=0) ){
    
   mov=1;
 
  
  leedistancia();
  //Serial.println(distance);
  if(distance<10){
    if(first==1){
      if (d==15){
      piso=0;}
      else{
        piso=pisoMax;
      }
      first=0;
    }
  digitalWrite(15,LOW);
  digitalWrite(17,LOW);
 
  delay(5000);
  if(d==15){
    piso++;
  }else{
    piso--;
  }
  leeTemyHum();
  leecalidad();
///////////////////////////////////Aqui enviar el json///////////////////////

 sendPostRequest();
Serial.print("temperatura= ");
  Serial.println(temperatura);
  Serial.print("humedad= ");
  Serial.println(humedad);
  Serial.print("calidad del aire= ");
  Serial.println(air_cual);
  Serial.print("piso= ");
  Serial.println(piso);
  Serial.print("Hora: ");
  Serial.println(now.unixtime());
  
  Serial.print(now.year());
  Serial.print("-");
  Serial.print(now.month());
  Serial.print(":");
  Serial.print(now.day());
  
////////////////////////////////////////////////////////////////////////////
  digitalWrite(d,HIGH);
  delay(2000);
 
  
  }
   

}


}






void leedistancia(){
  
// Clears the trigPin
digitalWrite(trigPin, LOW);
delayMicroseconds(2);

// Sets the trigPin on HIGH state for 10 micro seconds
digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);

// Reads the echoPin, returns the sound wave travel time in microseconds
duration = pulseIn(echoPin, HIGH);

// Calculating the distance
distance= duration*0.034/2;

// Prints the distance on the Serial Monitor

}
void leeTemyHum(){
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  if (isnan(h) || isnan(t) ) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }
  float hic = dht.computeHeatIndex(t, h, false);

  humedad=h;
  temperatura=t;
  
}
void leecalidad(){
  // put your main code here, to run repeatedly:
float rzero = gasSensor.getRZero(); //this to get the rzero value, uncomment this to get ppm value
float ppm = gasSensor.getPPM(); // this to get ppm value, uncomment this to get rzero value
//Serial.println(rzero); // this to display the rzero value continuously, uncomment this to get ppm value
air_cual=ppm; // this to display the ppm value continuously, uncomment this to get rzero value

}
void sendPostRequest(){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
   http.begin(client, SERVER_IP, SERVER_PORT, "/lap/sensor/values", true);
   Serial.println("hola");
    //http.begin("192.168.43.158:8080/lap/sensor/values");
   // http.startRequest(SERVER_IP,SERVER_PORT,"/v2/5185415ba171ea3a00704eed","POST",null)
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(7) + JSON_ARRAY_SIZE(7) + 60;
    DynamicJsonDocument doc(capacity);
    doc["idDispositivo"] = Dispositivo;
    doc["temperatura"] = temperatura;
    doc["humedad"] =humedad;
    doc["calidad_aire"] =air_cual;
    doc["timestamp"] = now.unixtime();
    doc["location"]= piso;
    String output;
    serializeJson(doc, output);
    int httpCode = http.PUT(output);
    //int httpCode = http.POST(output);
    Serial.print("Response code: " );
    Serial.println(httpCode);
    String payload = http.getString();

    Serial.print("Resultado: ");
    Serial.println( payload);
  }
  }
