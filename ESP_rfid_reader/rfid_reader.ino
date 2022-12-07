#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN 3
#define RST_PIN 5

MFRC522 rfid(SS_PIN, RST_PIN);

MFRC522::MIFARE_Key key;

byte nuidPICC[4];

void setup(){
  Serial.begin(9600);
  SPI.begin(); // start SPI bus
  rfid.PCD_Init(); // start MFRC522

  for(byte i = 0; i < 6; i++){
    key.keyByte[i] = 0xFF;
  }

  
  Serial.println(F("This code scan the MIFARE Classsic NUID."));
  Serial.print(F("Using the following key:"));
  printHex(key.keyByte, MFRC522::MF_KEY_SIZE);
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

    Serial.println(F("A new card has been detected."));

    // Store NUID into nuidPICC array
    readCard();

    Serial.println(F("The NUID tag is:"));
    Serial.print(F("In dec: "));
    printDec(rfid.uid.uidByte, rfid.uid.size);
    Serial.println();


  }else Serial.println(F("Card read previously."));

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();

}

void readCard(){
  for (byte i = 0; i < 4; i++) {
      nuidPICC[i] = rfid.uid.uidByte[i];
      Serial.println(nuidPICC[i]);
    }
}


void printDec(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
  }
}
