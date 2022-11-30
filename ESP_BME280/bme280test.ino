#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <ArduinoJson.h>
#include <HTTPClient.h>
#include <WiFiMulti.h>


#define SEALEVELPRESSURE_HPA (1013.25)
const char *AP_SSID = "palceholder";
const char *AP_PWD = "placeholder";


Adafruit_BME280 bme; // I2C
WiFiMulti wifiMulti;

void setup() {
    Serial.begin(9600);
    Serial.println(F("BME280 test"));

    unsigned status;
    
    // default settings
    status = bme.begin(0x76);  

    if (!status) {
        while (1){
          Serial.println("Cannot connect");
          delay(1000);
        } 
    }
    wifiMulti.addAP(AP_SSID, AP_PWD);
}

void loop() { 

  if (wifiMulti.run() == WL_CONNECTED) {
    HTTPClient http;

    http.begin("http://tutaj_ip:5000/send");  
    http.addHeader("Content-Type", "application/json");
    StaticJsonDocument<200> doc;
    doc["temp"] = bme.readTemperature();
    doc["humidity"] = bme.readHumidity();
    doc["pressure"] = bme.readPressure() / 100.0F;

    String requestBody;
    serializeJson(doc, requestBody);

    int httpResponseCode = http.POST(requestBody);

    if(httpResponseCode > 0){
      String response = http.getString();

      Serial.println(httpResponseCode);
      Serial.println(response);
    }else{
      Serial.println("Error occured");
    }


  }
  delay(30000);
}

