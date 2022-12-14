#include <SPI.h>
#include <MFRC522.h>
#include <ArduinoJson.h>
#include <HTTPClient.h>
#include <WiFiMulti.h>

#define SS_PIN 3
#define RST_PIN 5
#define BTN_PIN 6

const char *AP_SSID = "MikroTik123";
const char *AP_PWD = "12345678";

MFRC522 rfid(SS_PIN, RST_PIN);
WiFiMulti wifiMulti;

MFRC522::MIFARE_Key key;



byte nuidPICC[4];

void setup(){
  Serial.begin(9600);
  SPI.begin(); // start SPI bus
  rfid.PCD_Init(); // start MFRC522

  for(byte i = 0; i < 6; i++){
    key.keyByte[i] = 0xFF;
  }

  pinMode(BTN_PIN, INPUT_PULLUP);
  wifiMulti.addAP(AP_SSID, AP_PWD);
}


void loop(){

  // reset the main loop if new card is present
  if(!rfid.PICC_IsNewCardPresent())
    return;

  // verify if NUID has been readed
  if(!rfid.PICC_ReadCardSerial())
    return;

  Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  Serial.println(rfid.PICC_GetTypeName(piccType));

  // check if the PICC of Classic MIFARE type
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI && 
    piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
    piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }

  if (rfid.uid.uidByte[0] != nuidPICC[0] || 
    rfid.uid.uidByte[1] != nuidPICC[1] || 
    rfid.uid.uidByte[2] != nuidPICC[2] || 
    rfid.uid.uidByte[3] != nuidPICC[3] ) {

    readCard();

    if(digitalRead(BTN_PIN)== LOW){

      if(addCard())
        Serial.println("Card Added");
      else
        Serial.println("Error Adding");
    
    }else{ 
      if(checkIsValid())
        Serial.println("Access granted");
      else
        Serial.println("Access not granted");
    }
    

  }else Serial.println(F("Card read previously."));

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
}

bool checkIsValid(){
  int value;
  memcpy(&value, nuidPICC, sizeof(int));
  
  Serial.println(value);
  String serverName = "http://192.168.89.2:8081/checkRFID";
  if (wifiMulti.run() == WL_CONNECTED) {

    HTTPClient http;
    String serverPath = serverName;
    http.begin(serverName);

    http.addHeader("Content-Type", "application/json");
    StaticJsonDocument<200> doc;
    doc["rfid"] = value;
    String requestBody;
    serializeJson(doc, requestBody);

    int httpResponseCode = http.POST(requestBody);
    
    if (httpResponseCode>0) {
        String payload = "{}";
        payload = http.getString();
        if(payload == "true")
          return true;
    }
      http.end();
    
  }else {
      Serial.println("WiFi Disconnected");
  }  
  return false;  
}

bool addCard(){
  int value;
  memcpy(&value, nuidPICC, sizeof(int));
  String serverName = "http://192.168.89.2:8081/sendRFID";
  HTTPClient http;
  http.begin(serverName);
  
  http.addHeader("Content-Type", "application/json");
  StaticJsonDocument<200> doc;
  doc["rfid"] = value;
  String requestBody;
  serializeJson(doc, requestBody);

  int httpResponseCode = http.POST(requestBody);

  if (httpResponseCode>0){
     return true;
  }
      http.end();
    
  Serial.println("WiFi Disconnected"); 
  return false; 
}

void readCard(){
  for (byte i = 0; i < 4; i++)
    nuidPICC[i] = rfid.uid.uidByte[i];
  
  Serial.println("Card readed");
}

void printDec(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
  }
}
