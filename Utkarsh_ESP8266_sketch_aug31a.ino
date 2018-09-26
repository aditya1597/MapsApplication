#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>    

//firebase authentication
#define FIREBASE_HOST "codebreakers-db989.firebaseio.com"
#define FIREBASE_AUTH "vQMXuMuzCSpP9jJClMca8yZdqm0DVOUu3uDPWbyp"
#define WIFI_SSID "BLACK_HACKER.ME"
#define WIFI_PASSWORD "PASS@#WORD@#"

//trigger and echo pin values
const int trigPin = 5;
const int echoPin = 4;

const float dist50 = 50.00;
float duration;
float dist;
float longitude;
float latitude;

void setup() {
  //digitalWrite(LED_BUILTIN, HIGH);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
  digitalWrite(LED_BUILTIN, HIGH);  
      delay(500);                      
      digitalWrite(LED_BUILTIN, LOW);
      delay(500); 
      Serial.print(".");
      delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  // wifiConnect();
  //  mqttConnect();


}
int n = 0;


void loop() {
  longitude = 19.1326441;
  latitude = 72.9255183;
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  dist = duration * 0.034 / 2;
  dist = dist50- dist;
  if (dist<0)
    dist = 00.00;
  //distString=dist;
  //longSring=longitude;
  //latString=latitude;
  Serial.print("Distance: ");
  Serial.println(dist);
  Firebase.setFloat("Distance", dist);
 //Firebase.setString("Distance_in_string", dist);
  Firebase.setFloat("Longitude", longitude);
  Firebase.setFloat("Latitude", latitude);
  if (dist >= 40.00) {
    Firebase.setString("Depth", "Danger!! Water Level is very high");
      digitalWrite(LED_BUILTIN, HIGH);  
      delay(60);                      
      digitalWrite(LED_BUILTIN, LOW); 
       delay(60);  
  }
  else if (dist >= 20.00 && dist < 30.00) {
    Firebase.setString("Depth", "Alert Water Level is rising very fast");
    digitalWrite(LED_BUILTIN, HIGH);
  }
  else if (dist < 20.00) {
    Firebase.setString("Depth", "Water level is safe!");
     digitalWrite(LED_BUILTIN, HIGH);

  }
}


