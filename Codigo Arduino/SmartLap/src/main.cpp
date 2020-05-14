#include <Arduino.h>
#include "ArduinoJson.h"
#include <WiFi.h>
#include <HTTPClient.h>
//#include <SoftwareSerial.h>

char responseBuffer[300];
WiFiClient client;

String SSID = "HUAWEI-B318-1F26";
String PASS = "Y3LJM9DN8N6";

String SERVER_IP = "www.mocky.io";
int SERVER_PORT = 80;

void sendGetRequest();
void sendPostRequest();

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
}

void loop() {
  sendGetRequest();
  delay(3000);
  //sendPostRequest();
  //delay(3000);
}

void sendGetRequest(){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/v2/5eb930c82f00006c003c2ec9", true);
    int httpCode = http.GET();

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);
    if (error){
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
      return;
    }
    Serial.println(F("Response:"));
    int sensor = doc["idSensor"].as<int>();
    int dispositivo = doc["idDispositivo"].as<int>();
    float temp = doc["temperatura"].as<float>();
    float hum = doc["humedad"].as<float>();
    float air = doc["calidad_aire"].as<float>();
    long time = doc["timestamp"].as<long>();
    float loc = doc["location"].as<float>();

    Serial.println("Sensor name: " + String(sensor));
    Serial.println("Time: " + String(dispositivo));
    Serial.println("Data: " + String(temp));
    Serial.println("Time: " + String(humedad));
    Serial.println("Data: " + String(air));
    Serial.println("Time: " + String(time));
    Serial.println("Data: " + String(loc));
  }
}



void sendPostRequest(){
  if (WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    http.begin(client, SERVER_IP, SERVER_PORT, "/v2/5e91859a3300005d00e9cf04", true);
    http.addHeader("Content-Type", "application/json");

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);
    doc["temperature"] = 18;
    doc["humidity"] = 78;
    doc["timestamp"] = 124123123;
    doc["name"] = "sensor1";

    String output;
    serializeJson(doc, output);

    int httpCode = http.PUT(output);

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    Serial.println("Resultado: " + payload);
  }
}
