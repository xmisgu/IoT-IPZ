#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <ArduinoJson.h>
#include <HTTPClient.h>
#include <WiFiMulti.h>
#include <SSD1306.h>

#define SEALEVELPRESSURE_HPA (1013.25)
#define TIME_INTERVAL 5000
const char *AP_SSID = "MikroTik123";
const char *AP_PWD = "12345678";
unsigned long timeCur;
unsigned long timePrev;




String temp = "";
String hum = "";
String pres = "";
Adafruit_BME280 bme; // I2C
WiFiMulti wifiMulti;
SSD1306  display(0x3c, 5, 4);
void setup() {
    //////////////OLED////////////////
    display.init();
    display.flipScreenVertically();
    display.setFont(ArialMT_Plain_10);
    //////////////////////////////////
    Wire.begin(5,4);
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
    timeCur = millis(); 
    timePrev = 0;
}

void wifiHandle(){
  if (wifiMulti.run() == WL_CONNECTED) {
    HTTPClient http;
    http.begin("http://192.168.89.2:8081/send");  
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
}

void loop() { 
  timeCur = millis();
  if(timeCur - timePrev > TIME_INTERVAL){
    
    timePrev = timeCur;
    display.clear();
    display.setColor(WHITE);
    display.setTextAlignment(TEXT_ALIGN_CENTER);
    temp += "temperature: ";
    temp += String(bme.readTemperature());
    temp += "°C";
    display.drawString(64, 15, temp);
    hum += "humidity: ";
    hum += String(bme.readHumidity());
    hum += "%";
    display.drawString(64, 30, hum);
    pres += "pressure: ";
    pres += String(bme.readPressure() / 100.F);
    pres += "hPa";
    display.drawString(64, 45, pres);
    display.setFont(ArialMT_Plain_10);
    display.display();
    wifiHandle();
    temp = "";
    hum = "";
    pres = "";
  }
  
}
