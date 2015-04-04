/*
  Blink
  Turns on an LED on for one second, then off for one second, repeatedly.
 
  This example code is in the public domain.
 */
 
// Pin 13 has an LED connected on most Arduino boards.
// give it a name:
int led = 4;
 
// the setup routine runs once when you press reset:
void setup() {                
  // initialize the digital pin as an output.
  pinMode(0, OUTPUT);
  pinMode(1, OUTPUT);
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
}

// the loop routine runs over and over again forever:
void loop() {
  int i= 0;
  while (i < 155){
    i +=1;
    analogWrite(0, i);
    analogWrite(1, i);
    analogWrite(2, i);
    analogWrite(3, i);
    analogWrite(4, i);
  } 
  /*
  digitalWrite(led, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(1000);               // wait for a second
  analogWrite(led, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);               // wait for a second*/
}
