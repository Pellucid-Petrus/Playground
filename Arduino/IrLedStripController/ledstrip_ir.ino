/*******************************************
 * Antonio Aloisio <gnuton@gnuton.org>
 * License: GPL v3
 * Requires https://github.com/shirriff/Arduino-IRremote
 *******************************************/
#include <IRremote.h>

int RECV_PIN = 2;
IRrecv irrecv(RECV_PIN);
decode_results results;

// REMOTE PROTOCOL FOR CHINESE REMOTE
const unsigned int C_NULL = 0xFFFFFFFF;

const unsigned int C_RED1 = 0x1AE5;
const unsigned int C_RED2 = 0x2AD5;
const unsigned int C_RED3 = 0xAF5;
const unsigned int C_RED4 = 0x38C7;
const unsigned int C_RED5 = 0x18E7;

const unsigned int C_GRE1 = 0xFFFF9A65;
const unsigned int C_GRE2 = 0xFFFFAA55;
const unsigned int C_GRE3 = 0xFFFF8A75;
const unsigned int C_GRE4 = 0xFFFFB847;
const unsigned int C_GRE5 = 0xFFFF9867;

const unsigned int C_BLU1 = 0xFFFFA25D;
const unsigned int C_BLU2 = 0xFFFF926D;
const unsigned int C_BLU3 = 0xFFFFB24D;
const unsigned int C_BLU4 = 0x7887;
const unsigned int C_BLU5 = 0x58A7;


const unsigned int C_WHI1 = 0x22DD;
const unsigned int C_WHI2 = 0x12ED;
const unsigned int C_WHI3 = 0x32CD;
const unsigned int C_WHI4 = 0xFFFFF807;
const unsigned int C_WHI5 = 0xFFFFD827;

const unsigned int C_PWR = 0x2FD;
const unsigned int C_PLAY = 0xFFFF827D;
const unsigned int C_BRUP = 0x3AC5; // BRIGHTNESS UP
const unsigned int C_BRDW = 0xFFFFBA45; // BRIGHTNESS DOWN


// REMOTE PROTOCOL FOR LG AKB69680403
const unsigned int C_LG_RED = 0x4EB1;
const unsigned int C_LG_BLU = 0xFFFF8679;
const unsigned int C_LG_YEL = 0xFFFFC639;  // power off
const unsigned int C_LG_GRE = 0xFFFF8E71;


// PINS
const unsigned int P_BLU = 9; // PWM pins
const unsigned int P_GRE = 10;
const unsigned int P_RED = 11;

// GLOBAL VARS
boolean lights_on = true;
int red_brightness = 255;
int blu_brightness = 255;
int gre_brightness = 255;

void setup()
{
  pinMode(P_BLU, OUTPUT);
  
  Serial.begin(9600);
  irrecv.enableIRIn(); // Start the receiver
  setBrightnessLed(P_BLU, 255);
}

void loop() {
  if (irrecv.decode(&results)) {
    int res = results.value;
    
    switch(res){
      
      // LG REMOTE
      case C_LG_RED: Serial.println("LG_RED"); setBrightnessLed(P_RED, analogRead(P_RED)==0 ? 255 : 0); break;
      case C_LG_BLU: Serial.println("LG_BLU"); setBrightnessLed(P_RED, analogRead(P_BLU)==0 ? 255 : 0); break;
      case C_LG_GRE: Serial.println("LG_GRE"); setBrightnessLed(P_RED, analogRead(P_GRE)==0 ? 255 : 0); break; 
      
      // CHINESE REMOTE
      case C_NULL: ; break;
      case C_RED1: Serial.println("RED1"); setBrightnessLed(P_RED, 255); break;
      case C_RED2: Serial.println("RED2"); setBrightnessLed(P_RED, 255 * 0.8); break;
      case C_RED3: Serial.println("RED3"); setBrightnessLed(P_RED, 255 * 0.6);break;
      case C_RED4: Serial.println("RED4"); setBrightnessLed(P_RED, 255 * 0.4);break;
      case C_RED5: Serial.println("RED5"); setBrightnessLed(P_RED, 255 * 0.2); break;
      
      case C_GRE1: Serial.println("GRE1"); setBrightnessLed(P_GRE, 255); break;
      case C_GRE2: Serial.println("GRE2"); setBrightnessLed(P_GRE, 255 * 0.8); break;
      case C_GRE3: Serial.println("GRE3"); setBrightnessLed(P_GRE, 255 * 0.6); break;
      case C_GRE4: Serial.println("GRE4"); setBrightnessLed(P_GRE, 255 * 0.4); break;
      case C_GRE5: Serial.println("GRE5"); setBrightnessLed(P_GRE, 255 * 0.2); break;
      
      case C_BLU1: Serial.println("BLU1"); setBrightnessLed(P_BLU, 255); break;
      case C_BLU2: Serial.println("BLU2"); setBrightnessLed(P_BLU, 255 * 0.8); break;
      case C_BLU3: Serial.println("BLU3"); setBrightnessLed(P_BLU, 255 * 0.6); break;
      case C_BLU4: Serial.println("BLU4"); setBrightnessLed(P_BLU, 255 * 0.4); break;
      case C_BLU5: Serial.println("BLU5"); setBrightnessLed(P_BLU, 255 * 0.2); break;
      
      case C_WHI1: Serial.println("WHI1"); break;
      case C_WHI2: Serial.println("WHI2"); break;
      case C_WHI3: Serial.println("WHI3"); break;
      case C_WHI4: Serial.println("WHI4"); break;
      case C_WHI5: Serial.println("WHI5"); break;
      
      case C_BRUP: Serial.println("BRUP"); break;
      case C_BRDW: Serial.println("BRDW"); break;
      case C_PLAY: Serial.println("PLAY"); break;
      
      case C_LG_YEL:
      case C_PWR:
        if (lights_on)
          Serial.println("POWER lights=ON");
        else
          Serial.println("POWER lights=OFF");
          
        lights_on = !lights_on;
        
        setBrightnessLed(P_BLU, lights_on ? 255 : 0);
        setBrightnessLed(P_RED, lights_on ? 255 : 0);
        setBrightnessLed(P_GRE, lights_on ? 255 : 0);
      break;
      
      default: Serial.println(res, HEX);    
    }
    
    irrecv.resume(); // Receive the next value
  }
}

// helper functions

void setBrightnessLed(int pin, int val) {
  Serial.println(val);
  switch (pin) {
    case P_BLU: blu_brightness = val; break;
    case P_GRE: gre_brightness = val; break;
    case P_RED: red_brightness = val; break;
  }
  analogWrite(pin, val);
}
