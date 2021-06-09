
#include <Wire.h>
#include<SoftwareSerial.h>
#include <LiquidCrystal_I2C.h> // Liquid Crystal_I2C library
#include "HX711.h"  //You must have this library in your arduino library folder
#define DOUT  2
#define CLK  3
#define sw 4
#define BT_SERIAL_RX 8
#define BT_SERIAL_TX 7

HX711 scale;
LiquidCrystal_I2C lcd(0x27,16,2);
SoftwareSerial bluetoothSerial(BT_SERIAL_TX, BT_SERIAL_RX);
//Change this calibration factor as per your load cell once it is found you may need to vary it in thousands
float calibration_factor = -2032405;
float loadcell_data;
bool timer_on = false;
bool countdown_on = false;
signed short minutes, seconds;
signed short seconds40 = 40;
char timeline[16];
unsigned long previousMillisScale = 0, previousMillisTimer = 0;        // will store last time LED was updated
const long intervalTimer = 1000;  
const long intervalScale = 200;
unsigned long currentMillis;
bool buttoninc = 0;

 
void setup() {
  pinMode(sw, INPUT);
  pinMode(8, INPUT);
  bluetoothSerial.begin(9600);
  Serial.begin(9600);
  Serial.println("HX711 Calibration");
  Serial.println("Remove all weight from scale");
  Serial.println("After readings begin, place known weight on scale");
  Serial.println("Press a,s,d,f to increase calibration factor by 10,100,1000,10000 respectively");
  Serial.println("Press z,x,c,v to decrease calibration factor by 10,100,1000,10000 respectively");
  Serial.println("Press t for tare");
  scale.begin(2, 3);
  scale.tare(); //Reset the scale to 0
  long zero_factor = scale.read_average(); //Get a baseline reading
  Serial.print("Zero factor: "); //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
  Serial.println(zero_factor);
  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  lcd.init();
  lcd.backlight();
  lcd.begin(16,2);
  lcd.setCursor(0,0);
  lcd.print("Time");
  lcd.setCursor(0,1);
  lcd.print("00:00");
  lcd.setCursor(10,0);
  lcd.print("Weight");
}

void loop() {
   getWeight();

   if (bluetoothSerial.available()) {
    char command = bluetoothSerial.read();
    Serial.print(command);
    Serial.println();
    if(command  == 's'){
      timer_on = true;      
    }
    if(command == 'r'){
      timer_on = false;
      resetTimer();      
    }
    if(command == 't'){
      scale.tare();
    }
    if(command == 'c'){
      countdown_on = true;
    }
  }
   countdown40();
   setTimer();
}

void getWeight(){
   currentMillis = millis();
   if (currentMillis - previousMillisScale >= intervalScale) {
      previousMillisScale = currentMillis;
      lcd.setCursor(11,1);
      loadcell_data = scale.get_units()*1000; //conversia in grame
      if(loadcell_data >= -0.5 && loadcell_data <= 0.2 ){
        lcd.setCursor(11,1);
        loadcell_data = 0;
        lcd.print("0.0   ");
      }
      if(loadcell_data >= 1000){
        lcd.setCursor(11,1);
        //loadcell_data = 0;
        lcd.print("ERROR");
        bluetoothSerial.print("ERROR");
        bluetoothSerial.print('\n');
      }
      lcd.setCursor(11,1);
      lcd.print(loadcell_data,1);
      bluetoothSerial.print(loadcell_data,1);
      bluetoothSerial.print('\n');
   }
}

void setTimer(){
      currentMillis = millis();
      if(timer_on == true){
      if (currentMillis - previousMillisTimer >= intervalTimer) {
        previousMillisTimer = currentMillis;
        lcd.setCursor(0, 1);
        seconds++;
        sprintf(timeline,"%0.2d:%0.2d", minutes, seconds);
        lcd.print(timeline);
        if (seconds == 60)
        {
          seconds = 0;
          minutes ++;
        }
        if(minutes == 99 && seconds == 59){
          minutes = 0;
          seconds = 0;
        }
    }
  }    
}

void resetTimer(){
    seconds = 0;
    minutes = 0;
    lcd.setCursor(0,1);
    lcd.print("00:00");
}

void countdown40(){ 
  currentMillis = millis();
  if(countdown_on == true){
      if (currentMillis - previousMillisTimer >= intervalTimer) {
        minutes = 0;
        previousMillisTimer = currentMillis;
        lcd.setCursor(0, 1);
        if(seconds40 != 0)
        seconds40--;
        sprintf(timeline,"%0.2d:%0.2d", minutes, seconds40);
        lcd.print(timeline);
      }
  } 
}

void calibrate(){
  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  loadcell_data = scale.get_units()*1000.; //conversia in grame
  if(loadcell_data > -0.2 && loadcell_data<=0.2)
    loadcell_data = 0;
  Serial.print("Reading: ");
  Serial.print(loadcell_data, 1);
  Serial.print(" g");
  Serial.print(" calibration_factor: ");
  Serial.print(calibration_factor);
  Serial.println();
 
  if(Serial.available())
  {
    char temp = Serial.read();
    if(temp == '+' || temp == 'a')
      calibration_factor += 10;
    else if(temp == '-' || temp == 'z')
      calibration_factor -= 10;
    else if(temp == 's')
      calibration_factor += 100;  
    else if(temp == 'x')
      calibration_factor -= 100;  
    else if(temp == 'd')
      calibration_factor += 1000;  
    else if(temp == 'c')
      calibration_factor -= 1000;
    else if(temp == 'f')
      calibration_factor += 10000;  
    else if(temp == 'v')
      calibration_factor -= 10000;  
    else if(temp == 't')
      scale.tare();  //Reset the scale to zero
  }
}
