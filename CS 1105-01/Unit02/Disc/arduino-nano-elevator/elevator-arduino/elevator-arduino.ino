#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_ILI9341.h>

#define TFT_CS    8
#define TFT_RST   9
#define TFT_DC    10

#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64

const byte ROWS = 4;
const byte COLS = 4;

char hexaKeys[ROWS][COLS] = {
  {'1', '2', '3', 'C'},
  {'4', '5', '6', 'T'},
  {'7', '8', '9', 'S'},
  {'*', '0', '#', 'P'}
};

byte rowPins[ROWS] = {7, 6, 5, 4};
byte colPins[COLS] = {3, 2, A7, A6};

Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);
Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

void setup() {
  Wire.begin(); // só uma vez
  
  // Inicializa SSD1306
  if(!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) { for(;;); }
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(WHITE);
  display.setCursor(0, 0);
  display.println("Nano OK!");
  display.display();

  // Inicializa ILI9341
  tft.begin();
  tft.setRotation(3);           // RETRATO
  tft.fillScreen(ILI9341_BLACK);

  tft.setCursor(0, 0);
  tft.setTextColor(ILI9341_CYAN);
  tft.setTextSize(3);
  tft.println("AMPLIFICAST!");
  tft.setTextColor(ILI9341_YELLOW);
  tft.setTextSize(2);
  tft.println("ELEVATOR SISTEM UoPeople");
  tft.println("Welcome!");
  tft.println("Select a floor.");


}

void loop() {
  char customkey  = customKeypad.getKey();
  if(customkey){
      // Apaga a área onde o número vai aparecer
      tft.fillRect(0, 100, 320, 80, ILI9341_BLACK); // ajusta posição e tamanho

      // Define cursor e escreve novo número
      tft.setCursor(0, 100);
      tft.setTextColor(ILI9341_RED);
      tft.setTextSize(8);
      tft.print(customkey); // use print() ao invés de println()
  }
}
