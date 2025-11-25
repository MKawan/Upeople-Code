// //code 5.

// #include <Wire.h>
// #include <Keypad.h>
// #include <Adafruit_GFX.h>
// #include <Adafruit_SSD1306.h>
// #include <Adafruit_ILI9341.h>

// // ------------------------------------------------------
// // BUZZER
// // ------------------------------------------------------
// #define BUZZER_PIN 6

// void beepButton() {
//   tone(BUZZER_PIN, 1800, 80);  // Beep curto
// }

// void beepFloor() {
//   tone(BUZZER_PIN, 1200, 120);
//   delay(150);
// }

// // ------------------------------------------------------
// // BOTÕES EXTERNOS (HALL BUTTONS)
// // ------------------------------------------------------
// #define HALL_UP_BUTTON 12
// #define HALL_DOWN_BUTTON 13

// // ------------------------------------------------------
// // ILI9341
// // ------------------------------------------------------
// #define TFT_CS 8
// #define TFT_RST 9
// #define TFT_DC 10
// Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);

// // ------------------------------------------------------
// // SSD1306
// // ------------------------------------------------------
// #define SCREEN_WIDTH 128
// #define SCREEN_HEIGHT 64
// Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

// // ------------------------------------------------------
// // Keypad
// // ------------------------------------------------------
// const byte ROWS = 4;
// const byte COLS = 4;

// char hexaKeys[ROWS][COLS] = {
//   { '1', '2', '3', 'C' },
//   { '4', '5', '6', 'T' },
//   { '7', '8', '9', 'S' },
//   { '*', '0', '#', 'P' }
// };

// byte rowPins[ROWS] = { 33, 35, 37, 39 };
// byte colPins[COLS] = { 41, 43, 45, 47 };
// Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

// // ------------------------------------------------------
// // Layout
// // ------------------------------------------------------
// const int PADDING = 20;
// const int ARROW_AREA_WIDTH = 20;
// const int TOP_AREA_HEIGHT = 140;
// const int UI_OFFSET_X = -40;
// const int ACTION_OFFSET_Y = 10;

// // ------------------------------------------------------
// // Elevador
// // ------------------------------------------------------
// int currentFloor = 0;
// int FLOOR_TIME = 3000;
// bool movingUp = false;
// bool movingDown = false;

// #define MAX_QUEUE 20
// int floorQueue[MAX_QUEUE];
// int queueSize = 0;

// // ------------------------------------------------------
// // FILA DO ELEVADOR
// // ------------------------------------------------------
// void enqueueFloor(int f) {
//   if (f < 0 || f > 10) return;
//   if (queueSize >= MAX_QUEUE) return;
//   for (int i = 0; i < queueSize; i++) {
//     if (floorQueue[i] == f) return;  // evita duplicata
//   }
//   floorQueue[queueSize++] = f;
// }

// void dequeueFloor() {
//   if (queueSize == 0) return;
//   for (int i = 1; i < queueSize; i++) {
//     floorQueue[i - 1] = floorQueue[i];
//   }
//   queueSize--;
// }


// // ------------------------------------------------------
// // UI
// // ------------------------------------------------------
// void drawTopText() {
//   tft.setTextSize(3);
//   tft.setTextColor(ILI9341_CYAN);

//   int16_t x1, y1;
//   uint16_t w, h;

//   tft.getTextBounds("UoPeople!", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING);
//   tft.println("UoPeople!");

//   tft.setTextSize(2);
//   tft.setTextColor(ILI9341_YELLOW);

//   tft.getTextBounds("ELEVATOR SYSTEM", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 40);
//   tft.println("ELEVATOR SYSTEM");

//   tft.getTextBounds("Welcome!", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 70);
//   tft.println("Welcome!");

//   tft.getTextBounds("Select a floor.", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 95);
//   tft.println("Select a floor.");
// }

// void clearActionArea() {
//   tft.fillRect(0, TOP_AREA_HEIGHT, tft.width(), tft.height() - TOP_AREA_HEIGHT, ILI9341_BLACK);
// }

// void clearArrows(int x, int y, int size = 40, int extra = 5) {
//   tft.fillRect(x - size/2 - extra, y - size - extra, size + 2*extra, 2*size + 2*extra, ILI9341_BLACK);
// }

// // ------------------------------------------------------
// // ANIMAÇÕES (NÃO ALTEREI NADA)
// // ------------------------------------------------------
// void animationUp(int x, int y, int size=40) {
//   clearArrows(x, y, size, 5);
//   unsigned long start = millis();

//   while (millis() - start < FLOOR_TIME) {
//     float t = float(millis() - start) / FLOOR_TIME;
//     int offset = -int(t * size);

//     tft.fillTriangle(x, y + offset, x - size/2, y + size + offset,
//                      x + size/2, y + size + offset, ILI9341_GREEN);
//     delay(30);
//     tft.fillTriangle(x, y + offset, x - size/2, y + size + offset,
//                      x + size/2, y + size + offset, ILI9341_BLACK);
//   }

//   tft.fillTriangle(x, y - size/2, x - size/2, y + size/2,
//                    x + size/2, y + size/2, ILI9341_GREEN);
// }


// void animationDown(int x, int y, int size=40) {
//   clearArrows(x, y, size, 5);
//   unsigned long start = millis();

//   while (millis() - start < FLOOR_TIME) {
//     float t = float(millis() - start) / FLOOR_TIME;
//     int offset = int(t * size);

//     tft.fillTriangle(x, y + offset, x - size/2, y - size + offset,
//                      x + size/2, y - size + offset, ILI9341_BLUE);
//     delay(30);
//     tft.fillTriangle(x, y + offset, x - size/2, y - size + offset,
//                      x + size/2, y - size + offset, ILI9341_BLACK);
//   }

//   tft.fillTriangle(x, y + size/2, x - size/2, y - size/2,
//                    x + size/2, y - size/2, ILI9341_BLUE);
// }

// void showOLED(int floor, bool up, bool down) {
//   display.clearDisplay();
//   display.setTextSize(4);
//   display.setTextColor(WHITE);

//   display.setCursor((128 - 24)/2, 0);
//   display.print(floor);

//   if (up) display.fillTriangle(64, 50, 54, 63, 74, 63, WHITE);
//   else if (down) display.fillTriangle(64, 63, 54, 50, 74, 50, WHITE);

//   display.display();
// }

// void showTFT(int floor, bool up, bool down) {

//   if((up  == true) || (down == true)){
//     clearActionArea();
//   }

//   int centerX = tft.width() / 2;
//   int centerY = TOP_AREA_HEIGHT + 60;

//   tft.setTextSize(10);
//   tft.setTextColor(ILI9341_RED);

//   int16_t x1, y1;
//   uint16_t w, h;
//   char text[4];
//   sprintf(text, "%d", floor);

//   tft.getTextBounds(text, 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor(centerX - w/2, centerY - h/2);
//   tft.print(text);

//   if (up) {
//     tft.fillTriangle(centerX, centerY + 60,
//                      centerX - 20, centerY + 90,
//                      centerX + 20, centerY + 90, ILI9341_GREEN);
//   }
//   else if (down) {
//     tft.fillTriangle(centerX, centerY + 90,
//                      centerX - 20, centerY + 60,
//                      centerX + 20, centerY + 60, ILI9341_BLUE);
//   }
// }

// // ------------------------------------------------------
// // MOVIMENTO
// // ------------------------------------------------------
// void processElevator() {
//   if (queueSize == 0) return;

//   int target = floorQueue[0];

//   int arrowX = PADDING + ARROW_AREA_WIDTH/2 - UI_OFFSET_X;
//   int centerY = TOP_AREA_HEIGHT + (tft.height() - TOP_AREA_HEIGHT) / 2;

//   if (target > currentFloor) {
//     movingUp = true;
//     movingDown = false;
//     animationUp(arrowX, centerY);
//     currentFloor++;
//     beepFloor();
//   }
//   else if (target < currentFloor) {
//     movingDown = true;
//     movingUp = false;
//     animationDown(arrowX, centerY);
//     currentFloor--;
//     beepFloor();
//   }

//   showOLED(currentFloor, movingUp, movingDown);
//   showTFT(currentFloor, movingUp, movingDown);

//   if (currentFloor == target) {
//     dequeueFloor();
//     movingUp = movingDown = false;
//   }
// }

// // ------------------------------------------------------
// // SETUP
// // ------------------------------------------------------
// void setup() {
//   Wire.begin();

//   pinMode(BUZZER_PIN, OUTPUT);
//   pinMode(HALL_UP_BUTTON, INPUT_PULLUP);
//   pinMode(HALL_DOWN_BUTTON, INPUT_PULLUP);

//   if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
//     for (;;);
//   }

//   display.clearDisplay();
//   display.setTextSize(2);
//   display.setTextColor(WHITE);
//   display.setCursor(0, 0);
//   display.println("MEGA OK!");
//   display.display();

//   tft.begin();
//   tft.setRotation(3);
//   tft.fillScreen(ILI9341_BLACK);

//   drawTopText();
// }

// // ------------------------------------------------------
// // LOOP
// // ------------------------------------------------------
// void loop() {

//   // --------------------------------------------------
//   // HALL BUTTONS
//   // --------------------------------------------------
//   if (!digitalRead(HALL_UP_BUTTON)) {
//     enqueueFloor(0);
//     delay(300);
//   }

//   if (!digitalRead(HALL_DOWN_BUTTON)) {
//     enqueueFloor(0);
//     delay(300);
//   }

//   // --------------------------------------------------
//   // KEY PAD — CORRIGIDO (LEITURA CONFIÁVEL)
//   // --------------------------------------------------
//   if (customKeypad.getKeys()) {
//     for (int i = 0; i < LIST_MAX; i++) {
//       if (customKeypad.key[i].stateChanged && customKeypad.key[i].kstate == PRESSED) {

//         char key = customKeypad.key[i].kchar;
//         beepButton();

//         if (isDigit(key)) enqueueFloor(key - '0');
//         else if (key == 'T') enqueueFloor(0);
//         else if (key == 'C') enqueueFloor(10);
//         else if (key == 'S') enqueueFloor(-1);
//       }
//     }
//   }

//   processElevator();
// }

// // Code 6.

// #include <Wire.h>
// #include <Keypad.h>
// #include <Adafruit_GFX.h>
// #include <Adafruit_SSD1306.h>
// #include <Adafruit_ILI9341.h>

// // ------------------------------------------------------
// // BUZZER
// // ------------------------------------------------------
// #define BUZZER_PIN 6
// void beepButton() { tone(BUZZER_PIN, 1800, 80); }
// void beepFloor()  { tone(BUZZER_PIN, 1200, 120); delay(150); }

// // ------------------------------------------------------
// // BOTÕES EXTERNOS (HALL BUTTONS)
// // ------------------------------------------------------
// #define HALL_UP_BUTTON   12
// #define HALL_DOWN_BUTTON 13

// // ------------------------------------------------------
// // ILI9341
// // ------------------------------------------------------
// #define TFT_CS  8
// #define TFT_RST 9
// #define TFT_DC  10
// Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);

// // ------------------------------------------------------
// // SSD1306
// // ------------------------------------------------------
// #define SCREEN_WIDTH  128
// #define SCREEN_HEIGHT 64
// Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

// // ------------------------------------------------------
// // Keypad
// // ------------------------------------------------------
// const byte ROWS = 4;
// const byte COLS = 4;
// char hexaKeys[ROWS][COLS] = {
//   { '1', '2', '3', 'C' },
//   { '4', '5', '6', 'T' },
//   { '7', '8', '9', 'S' },
//   { '*', '0', '#', 'P' }
// };
// byte rowPins[ROWS] = { 33, 35, 37, 39 };
// byte colPins[COLS] = { 41, 43, 45, 47 };
// Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

// // ------------------------------------------------------
// // Layout
// // ------------------------------------------------------
// const int PADDING          = 20;
// const int ARROW_AREA_WIDTH = 20;
// const int TOP_AREA_HEIGHT  = 140;
// const int UI_OFFSET_X      = -40;
// const int ACTION_OFFSET_Y  = 10;

// // ------------------------------------------------------
// // Elevador
// // ------------------------------------------------------
// int  currentFloor = 0;
// int  FLOOR_TIME   = 3000;
// bool movingUp     = false;
// bool movingDown   = false;

// #define MAX_FLOORS 20
// int  floorList[MAX_FLOORS];   // agora é só um array simples
// int  floorCount = 0;          // quantos andares estão na lista

// // ------------------------------------------------------
// // NOVAS FUNÇÕES DO ARRAY (substituem enqueue/dequeue)
// // ------------------------------------------------------
// void addFloor(int f) {
//   // ignora duplicatas
//   for (int i = 0; i < floorCount; i++) {
//     if (floorList[i] == f) return;
//   }
//   // adiciona no final se ainda houver espaço
//   if (floorCount < MAX_FLOORS) {
//     floorList[floorCount++] = f;
//   }
// }

// // remove o primeiro andar da lista (o que está sendo atendido)
// void removeCurrentTarget() {
//   if (floorCount == 0) return;
//   // desloca todos os elementos uma posição para a esquerda
//   for (int i = 1; i < floorCount; i++) {
//     floorList[i - 1] = floorList[i];
//   }
//   floorCount--;
// }

// // ------------------------------------------------------
// // UI (sem alterações)
// // ------------------------------------------------------
// void drawTopText() {
//   tft.setTextSize(3);
//   tft.setTextColor(ILI9341_CYAN);
//   int16_t x1, y1; uint16_t w, h;
//   tft.getTextBounds("UoPeople!", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING);
//   tft.println("UoPeople!");

//   tft.setTextSize(2);
//   tft.setTextColor(ILI9341_YELLOW);
//   tft.getTextBounds("ELEVATOR SYSTEM", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 40);
//   tft.println("ELEVATOR SYSTEM");
//   tft.getTextBounds("Welcome!", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 70);
//   tft.println("Welcome!");
//   tft.getTextBounds("Select a floor.", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 95);
//   tft.println("Select a floor.");
// }

// void clearActionArea() {
//   tft.fillRect(0, TOP_AREA_HEIGHT, tft.width(), tft.height() - TOP_AREA_HEIGHT, ILI9341_BLACK);
// }
// void clearArrows(int x, int y, int size = 40, int extra = 5) {
//   tft.fillRect(x - size/2 - extra, y - size - extra, size + 2*extra, 2*size + 2*extra, ILI9341_BLACK);
// }

// // ------------------------------------------------------
// // ANIMAÇÕES (intocadas)
// // ------------------------------------------------------
// void animationUp(int x, int y, int size=40) {
//   clearArrows(x, y, size, 5);
//   unsigned long start = millis();
//   while (millis() - start < FLOOR_TIME) {
//     float t = float(millis() - start) / FLOOR_TIME;
//     int offset = -int(t * size);
//     tft.fillTriangle(x, y + offset, x - size/2, y + size + offset,
//                      x + size/2, y + size + offset, ILI9341_GREEN);
//     delay(30);
//     tft.fillTriangle(x, y + offset, x - size/2, y + size + offset,
//                      x + size/2, y + size + offset, ILI9341_BLACK);
//   }
//   tft.fillTriangle(x, y - size/2, x - size/2, y + size/2,
//                    x + size/2, y + size/2, ILI9341_GREEN);
// }
// void animationDown(int x, int y, int size=40) {
//   clearArrows(x, y, size, 5);
//   unsigned long start = millis();
//   while (millis() - start < FLOOR_TIME) {
//     float t = float(millis() - start) / FLOOR_TIME;
//     int offset = int(t * size);
//     tft.fillTriangle(x, y + offset, x - size/2, y - size + offset,
//                      x + size/2, y - size + offset, ILI9341_BLUE);
//     delay(30);
//     tft.fillTriangle(x, y + offset, x - size/2, y - size + offset,
//                      x + size/2, y - size + offset, ILI9341_BLACK);
//   }
//   tft.fillTriangle(x, y + size/2, x - size/2, y - size/2,
//                    x + size/2, y - size/2, ILI9341_BLUE);
// }
// void showOLED(int floor, bool up, bool down) {
//   display.clearDisplay();
//   display.setTextSize(4);
//   display.setTextColor(WHITE);
//   display.setCursor((128 - 24)/2, 0);
//   display.print(floor);
//   if (up)   display.fillTriangle(64, 50, 54, 63, 74, 63, WHITE);
//   if (down) display.fillTriangle(64, 63, 54, 50, 74, 50, WHITE);
//   display.display();
// }
// void showTFT(int floor, bool up, bool down) {
//   if (up || down) clearActionArea();
//   int centerX = tft.width() / 2;
//   int centerY = TOP_AREA_HEIGHT + 60;
//   tft.setTextSize(10);
//   tft.setTextColor(ILI9341_RED);
//   int16_t x1, y1; uint16_t w, h;
//   char text[4]; sprintf(text, "%d", floor);
//   tft.getTextBounds(text, 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor(centerX - w/2, centerY - h/2);
//   tft.print(text);
//   if (up) {
//     tft.fillTriangle(centerX, centerY + 60,
//                      centerX - 20, centerY + 90,
//                      centerX + 20, centerY + 90, ILI9341_GREEN);
//   } else if (down) {
//     tft.fillTriangle(centerX, centerY + 90,
//                      centerX - 20, centerY + 60,
//                      centerX + 20, centerY + 60, ILI9341_BLUE);
//   }
// }

// // ------------------------------------------------------
// // MOVIMENTO - REESCRITO PARA USAR ARRAY SIMPLES
// // ------------------------------------------------------
// void processElevator() {
//   if (floorCount == 0) {               // nada na lista → parado
//     movingUp = movingDown = false;
//     return;
//   }

//   int target = floorList[0];            // primeiro andar da lista é o alvo atual
//   int arrowX = PADDING + ARROW_AREA_WIDTH/2 - UI_OFFSET_X;
//   int centerY = TOP_AREA_HEIGHT + (tft.height() - TOP_AREA_HEIGHT) / 2;

//   if (target > currentFloor) {
//     movingUp   = true;
//     movingDown = false;
//     animationUp(arrowX, centerY);
//     currentFloor++;
//     beepFloor();
//   }
//   else if (target < currentFloor) {
//     movingDown = true;
//     movingUp   = false;
//     animationDown(arrowX, centerY);
//     currentFloor--;
//     beepFloor();
//   }

//   // atualiza displays
//   showOLED(currentFloor, movingUp, movingDown);
//   showTFT(currentFloor, movingUp, movingDown);

//   // chegou ao andar solicitado?
//   if (currentFloor == target) {
//     removeCurrentTarget();              // remove o primeiro da lista
//     movingUp = movingDown = false;      // para as setas
//   }
// }

// // ------------------------------------------------------
// // SETUP
// // ------------------------------------------------------
// void setup() {
//   Wire.begin();
//   pinMode(BUZZER_PIN, OUTPUT);
//   pinMode(HALL_UP_BUTTON,   INPUT_PULLUP);
//   pinMode(HALL_DOWN_BUTTON, INPUT_PULLUP);

//   if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) { for(;;); }
//   display.clearDisplay();
//   display.setTextSize(2); display.setTextColor(WHITE);
//   display.setCursor(0,0); display.println("MEGA OK!");
//   display.display();

//   tft.begin();
//   tft.setRotation(3);
//   tft.fillScreen(ILI9341_BLACK);
//   drawTopText();
// }

// // ------------------------------------------------------
// // LOOP
// // ------------------------------------------------------
// bool lastUpState   = HIGH;
// bool lastDownState = HIGH;

// void loop() {
//   // HALL BUTTONS (térreo)
//   // if (!digitalRead(HALL_UP_BUTTON))   { addFloor(0); delay(300); }
//   // if (!digitalRead(HALL_DOWN_BUTTON)) { addFloor(0); delay(300); }
//   bool upState   = digitalRead(HALL_UP_BUTTON);
//   bool downState = digitalRead(HALL_DOWN_BUTTON);

//   // Detecta apenas quando o botão é PRESSIONADO (transição HIGH -> LOW)
//   if (lastUpState == HIGH && upState == LOW) {
//     addFloor(0);
//     beepButton();
//   }

//   if (lastDownState == HIGH && downState == LOW) {
//     addFloor(0);
//     beepButton();
//   }

//   lastUpState = upState;
//   lastDownState = downState;

//   // KEYPAD
//   if (customKeypad.getKeys()) {
//     for (int i = 0; i < LIST_MAX; i++) {
//       if (customKeypad.key[i].stateChanged && customKeypad.key[i].kstate == PRESSED) {
//         char key = customKeypad.key[i].kchar;
//         beepButton();
//         if (isDigit(key))          addFloor(key - '0');
//         else if (key == 'T')       addFloor(0);   // Térreo
//         else if (key == 'C')       addFloor(10);  // Cobertura
//         else if (key == 'S')       addFloor(-1);  // Subsolo
//       }
//     }
//   }

//   processElevator();
// }

//

//// Code 07

// #include <Wire.h>
// #include <Keypad.h>
// #include <Adafruit_GFX.h>
// #include <Adafruit_SSD1306.h>
// #include <Adafruit_ILI9341.h>

// // ------------------------------------------------------
// // BUZZER
// // ------------------------------------------------------
// #define BUZZER_PIN 6
// void beepButton() { tone(BUZZER_PIN, 1800, 80); }
// void beepFloor()  { tone(BUZZER_PIN, 1200, 120); delay(150); }

// // ------------------------------------------------------
// // BOTÕES EXTERNOS (HALL BUTTONS)
// // ------------------------------------------------------
// #define HALL_UP_BUTTON   12
// #define HALL_DOWN_BUTTON 13

// // ------------------------------------------------------
// // ILI9341
// // ------------------------------------------------------
// #define TFT_CS  8
// #define TFT_RST 9
// #define TFT_DC  10
// Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);

// // ------------------------------------------------------
// // SSD1306
// // ------------------------------------------------------
// #define SCREEN_WIDTH  128
// #define SCREEN_HEIGHT 64
// Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

// // ------------------------------------------------------
// // Keypad
// // ------------------------------------------------------
// const byte ROWS = 4;
// const byte COLS = 4;
// char hexaKeys[ROWS][COLS] = {
//   { '1', '2', '3', 'C' },
//   { '4', '5', '6', 'T' },
//   { '7', '8', '9', 'S' },
//   { '*', '0', '#', 'P' }
// };

// byte rowPins[ROWS] = { 33, 35, 37, 39 };
// byte colPins[COLS] = { 41, 43, 45, 47 };
// Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

// // ------------------------------------------------------
// // Layout
// // ------------------------------------------------------
// const int PADDING          = 20;
// const int ARROW_AREA_WIDTH = 20;
// const int TOP_AREA_HEIGHT  = 160;
// const int UI_OFFSET_X      = 0;
// const int ACTION_OFFSET_Y  = 10;
// const int OFFSET_X = 40;

// // ------------------------------------------------------
// // Elevador
// // ------------------------------------------------------
// int  currentFloor = 0;
// int  FLOOR_TIME   = 3000;
// bool movingUp     = false;
// bool movingDown   = false;

// //DOOR OPEN OR CLOSE
// #define LED_PORTA_ABERTA  48   // Verde
// #define LED_PORTA_FECHADA 46   // Vermelho

// #define MAX_FLOORS 20
// int  floorList[MAX_FLOORS];   // agora é só um array simples
// int  floorCount = 0;          // quantos andares estão na lista

// // ------------------------------------------------------
// // NOVAS FUNÇÕES DO ARRAY (substituem enqueue/dequeue)
// // ------------------------------------------------------
// void addFloor(int f) {
//   // ignora duplicatas
//   for (int i = 0; i < floorCount; i++) {
//     if (floorList[i] == f) return;
//   }
//   // adiciona no final se ainda houver espaço
//   if (floorCount < MAX_FLOORS) {
//     floorList[floorCount++] = f;
//   }
// }

// // remove o primeiro andar da lista (o que está sendo atendido)
// void removeCurrentTarget() {
//   if (floorCount == 0) return;
//   // desloca todos os elementos uma posição para a esquerda
//   for (int i = 1; i < floorCount; i++) {
//     floorList[i - 1] = floorList[i];
//   }
//   floorCount--;
// }

// // ------------------------------------------------------
// // UI (sem alterações)
// // ------------------------------------------------------
// void drawTopText() {
//   tft.setTextSize(3);
//   tft.setTextColor(ILI9341_CYAN);
//   int16_t x1, y1; uint16_t w, h;
//   tft.getTextBounds("UoPeople!", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING);
//   tft.println("UoPeople!");

//   tft.setTextSize(2);
//   tft.setTextColor(ILI9341_YELLOW);
//   tft.getTextBounds("ELEVATOR SYSTEM", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 40);
//   tft.println("ELEVATOR SYSTEM");
//   tft.getTextBounds("Welcome!", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 70);
//   tft.println("Welcome!");
//   tft.getTextBounds("Select a floor.", 0, 0, &x1, &y1, &w, &h);
//   tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 95);
//   tft.println("Select a floor.");
// }

// void clearActionArea() {
//   tft.fillRect(0, TOP_AREA_HEIGHT, tft.width(), tft.height() - TOP_AREA_HEIGHT, ILI9341_BLACK);
// }

// void clearArrows(int x, int y, int size = 40, int extra = 5) {
//   tft.fillRect(x - size/2 - extra, y - size - extra, size + 2*extra, 2*size + 2*extra, ILI9341_BLACK);
// }

// void checkKeypad(){
//     if (customKeypad.getKeys()) {
//     closeDoor();
//     for (int i = 0; i < LIST_MAX; i++) {
//       if (customKeypad.key[i].stateChanged && customKeypad.key[i].kstate == PRESSED) {
//         char key = customKeypad.key[i].kchar;
//         beepButton();
//         if (isDigit(key))          addFloor(key - '0');
//         else if (key == 'T')       addFloor(0);   // Térreo
//         else if (key == 'C')       addFloor(10);  // Cobertura
//         else if (key == 'S')       addFloor(-1);  // Subsolo
//       }
//     }
//   }
// }

// void showDoorStatus(const char* status) {
//   static bool toggleColor = false;
//   static unsigned long lastChange = 0;

//   uint16_t cor;

//   if (strcmp(status, "DOOR OPEN") == 0) {
//     cor = ILI9341_GREEN;
//   } else if (strcmp(status, "DOOR CLOSE") == 0) {
//     cor = ILI9341_RED;
//   }

//   tft.setTextSize(2);
//   tft.setTextColor(cor, ILI9341_BLACK);

//   int16_t x1, y1;
//   uint16_t w, h;

//   tft.getTextBounds(status, 0, 0, &x1, &y1, &w, &h);

//   int posY = 140;
//   int posX = (tft.width() - w) / 2;

//   tft.fillRect(0, posY, tft.width(), h + 6, ILI9341_BLACK);
//   tft.setCursor(posX, posY);
//   tft.print(status);
// }

// void openDoor() {
//   digitalWrite(LED_PORTA_ABERTA, HIGH);
//   digitalWrite(LED_PORTA_FECHADA, LOW);
//   showDoorStatus("DOOR OPEN");
// }

// void closeDoor() {
//   digitalWrite(LED_PORTA_ABERTA, LOW);
//   digitalWrite(LED_PORTA_FECHADA, HIGH);
//   showDoorStatus("DOOR CLOSE");
// }

// void pauseInFloor(unsigned long times) {
//   unsigned long inicio = millis();
//   openDoor();

//   while (millis() - inicio < times) {
//     checkKeypad(); // continua aceitando novos andares
//   }

//   closeDoor();
// }

// // ------------------------------------------------------
// // ANIMAÇÕES (intocadas)
// // ------------------------------------------------------
// void animationUp(int x, int y, int size=40) {

//   x += OFFSET_X;

//   clearArrows(x, y, size, 5);

//   unsigned long start = millis();

//   while (millis() - start < FLOOR_TIME) {
//     checkKeypad();

//     float t = float(millis() - start) / FLOOR_TIME;
//     int offset = -int(t * size);

//     tft.fillTriangle(x, y + offset,
//                      x - size/2, y + size + offset,
//                      x + size/2, y + size + offset, ILI9341_GREEN);

//     delay(30);

//     tft.fillTriangle(x, y + offset,
//                      x - size/2, y + size + offset,
//                      x + size/2, y + size + offset, ILI9341_BLACK);
//   }

//   tft.fillTriangle(x, y - size/2,
//                    x - size/2, y + size/2,
//                    x + size/2, y + size/2, ILI9341_GREEN);
// }

// void animationDown(int x, int y, int size=40) {

//   x += OFFSET_X;

//   clearArrows(x, y, size, 5);

//   unsigned long start = millis();

//   while (millis() - start < FLOOR_TIME) {
//     checkKeypad();

//     float t = float(millis() - start) / FLOOR_TIME;
//     int offset = int(t * size);

//     tft.fillTriangle(x, y + offset,
//                      x - size/2, y - size + offset,
//                      x + size/2, y - size + offset, ILI9341_BLUE);

//     delay(30);

//     tft.fillTriangle(x, y + offset,
//                      x - size/2, y - size + offset,
//                      x + size/2, y - size + offset, ILI9341_BLACK);
//   }

//   tft.fillTriangle(x, y + size/2,
//                    x - size/2, y - size/2,
//                    x + size/2, y - size/2, ILI9341_BLUE);
// }


// void showOLED(int floor, bool up, bool down) {
//   display.clearDisplay();
//   display.setTextSize(4);
//   display.setTextColor(WHITE);
//   display.setCursor((128 - 24)/2, 0);
//   display.print(floor);
//   if (up)   display.fillTriangle(64, 50, 54, 63, 74, 63, WHITE);
//   if (down) display.fillTriangle(64, 63, 54, 50, 74, 50, WHITE);
//   display.display();
// }

// void showTFT(int floor, bool up, bool down) {

//   if (up || down) clearActionArea();
//   int centerX = tft.width() / 2;
//   int centerY = TOP_AREA_HEIGHT + 60;
//   tft.setTextSize(9);
//   tft.setTextColor(ILI9341_RED);
//   int16_t x1, y1; uint16_t w, h;
//   char text[4]; sprintf(text, "%d", floor);
//   tft.getTextBounds(text, 0, 0, &x1, &y1, &w, &h);
//   // Texto
//   tft.setCursor((centerX - w/2) + OFFSET_X, centerY - h/2);
//   tft.print(text);

//   // Setas
//   if (up) {
//     tft.fillTriangle(centerX + OFFSET_X, centerY + 60,
//                     centerX - 20 + OFFSET_X, centerY + 90,
//                     centerX + 20 + OFFSET_X, centerY + 90, ILI9341_GREEN);
//   } 
//   else if (down) {
//     tft.fillTriangle(centerX + OFFSET_X, centerY + 90,
//                     centerX - 20 + OFFSET_X, centerY + 60,
//                     centerX + 20 + OFFSET_X, centerY + 60, ILI9341_BLUE);
//   }
// }
// // ------------------------------------------------------
// // MOVIMENTO - REESCRITO PARA USAR ARRAY SIMPLES
// // ------------------------------------------------------
// void processElevator() {
//   if (floorCount == 0) {               // nada na lista → parado
//     movingUp = movingDown = false;
//     return;
//   }

//   int target = floorList[0];            // primeiro andar da lista é o alvo atual
//   int arrowX = PADDING + ARROW_AREA_WIDTH/2 - UI_OFFSET_X;
//   int centerY = TOP_AREA_HEIGHT + (tft.height() - TOP_AREA_HEIGHT) / 2;

//   if (target > currentFloor) {
//     movingUp   = true;
//     movingDown = false;
//     animationUp(arrowX, centerY);
//     currentFloor++;
//     beepFloor();
//   }
//   else if (target < currentFloor) {
//     movingDown = true;
//     movingUp   = false;
//     animationDown(arrowX, centerY);
//     currentFloor--;
//     beepFloor();
//   }

//   // atualiza displays
//   showOLED(currentFloor, movingUp, movingDown);
//   showTFT(currentFloor, movingUp, movingDown);

//   // chegou ao andar solicitado?
//   if (currentFloor == target){

//     openDoor();
//     beepFloor();

//     pauseInFloor(3000);   // ⏸️ 3 segundos parado no andar

//     removeCurrentTarget();              // remove o primeiro da lista
//     movingUp = movingDown = false;      // para as setas
//     closeDoor();
//   }
// }

// // ------------------------------------------------------
// // SETUP
// // ------------------------------------------------------
// void setup() {
//   Wire.begin();

//   //BUZZER
//   pinMode(BUZZER_PIN, OUTPUT);

//   //BUTTON OPEN AND CLOSE DOOR
//   pinMode(HALL_UP_BUTTON,   INPUT_PULLUP);
//   pinMode(HALL_DOWN_BUTTON, INPUT_PULLUP);

//   //LED DOOR
//   pinMode(LED_PORTA_ABERTA, OUTPUT);
//   pinMode(LED_PORTA_FECHADA, OUTPUT);

//   digitalWrite(LED_PORTA_ABERTA, LOW);
//   digitalWrite(LED_PORTA_FECHADA, HIGH);

//   if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) { for(;;); }
//   display.clearDisplay();
//   display.setTextSize(0);
//   display.setTextColor(WHITE);
//   display.setCursor(0,0); display.println("MEGA OK!");
//   display.display();

//   tft.begin();
//   tft.setRotation(0);
//   tft.fillScreen(ILI9341_BLACK);
//   drawTopText();
// }

// // ------------------------------------------------------
// // LOOP
// // ------------------------------------------------------
// bool lastUpState   = HIGH;
// bool lastDownState = HIGH;

// void loop() {
//   // HALL BUTTONS (térreo)
//   bool upState   = digitalRead(HALL_UP_BUTTON);
//   bool downState = digitalRead(HALL_DOWN_BUTTON);

//   checkKeypad();

//   // Detecta apenas quando o botão é PRESSIONADO (transição HIGH -> LOW)
//   if (lastUpState == HIGH && upState == LOW) {
//     addFloor(0);
//     beepButton();
//   }

//   if (lastDownState == HIGH && downState == LOW) {
//     addFloor(0);
//     beepButton();
//   }

//   lastUpState = upState;
//   lastDownState = downState;

//   processElevator();
// }

////Final

#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_ILI9341.h>

// ------------------------------------------------------
// BUZZER
// ------------------------------------------------------
#define BUZZER_PIN 6

// Short beep for button press
void beepButton() { 
  tone(BUZZER_PIN, 1800, 80); 
}

// Longer beep when arriving at floor
void beepFloor()  { 
  tone(BUZZER_PIN, 1200, 120); 
  delay(150); 
}

// ------------------------------------------------------
// EXTERNAL HALL BUTTONS
// ------------------------------------------------------
#define HALL_UP_BUTTON   12
#define HALL_DOWN_BUTTON 13

// ------------------------------------------------------
// ILI9341 TFT DISPLAY
// ------------------------------------------------------
#define TFT_CS  8
#define TFT_RST 9
#define TFT_DC  10
Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);

// ------------------------------------------------------
// SSD1306 OLED DISPLAY
// ------------------------------------------------------
#define SCREEN_WIDTH  128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

// ------------------------------------------------------
// KEYPAD SETTINGS
// ------------------------------------------------------
const byte ROWS = 4;
const byte COLS = 4;

char hexaKeys[ROWS][COLS] = {
  { '1', '2', '3', 'C' },
  { '4', '5', '6', 'T' },
  { '7', '8', '9', 'S' },
  { '*', '0', '#', 'P' }
};

byte rowPins[ROWS] = { 33, 35, 37, 39 };
byte colPins[COLS] = { 41, 43, 45, 47 };

Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

// ------------------------------------------------------
// UI LAYOUT CONSTANTS
// ------------------------------------------------------
const int PADDING          = 20;
const int ARROW_AREA_WIDTH = 20;
const int TOP_AREA_HEIGHT  = 160;
const int UI_OFFSET_X      = 0;
const int ACTION_OFFSET_Y  = 10;
const int OFFSET_X = 40;

// ------------------------------------------------------
// ELEVATOR STATE
// ------------------------------------------------------
int  currentFloor = 0;      // current floor
int  FLOOR_TIME   = 3000;  // travel time per floor
bool movingUp     = false;
bool movingDown   = false;

// ------------------------------------------------------
// DOOR LED INDICATORS
// ------------------------------------------------------
#define LED_DOOR_OPEN   48   // Green LED
#define LED_DOOR_CLOSED 46   // Red LED

// ------------------------------------------------------
// FLOOR QUEUE (SIMPLE FIFO LIST)
// ------------------------------------------------------
#define MAX_FLOORS 20
int  floorList[MAX_FLOORS];
int  floorCount = 0;         // how many floors in queue

// Add a floor to queue (ignore duplicates)
void addFloor(int f) {
  for (int i = 0; i < floorCount; i++) {
    if (floorList[i] == f) return;
  }
  if (floorCount < MAX_FLOORS) {
    floorList[floorCount++] = f;
  }
}

// Remove current target from queue
void removeCurrentTarget() {
  if (floorCount == 0) return;
  for (int i = 1; i < floorCount; i++) {
    floorList[i - 1] = floorList[i];
  }
  floorCount--;
}

// ------------------------------------------------------
// TOP STATIC TEXT (HEADER)
// ------------------------------------------------------
void drawTopText() {
  tft.setTextSize(3);
  tft.setTextColor(ILI9341_CYAN);

  int16_t x1, y1; 
  uint16_t w, h;

  tft.getTextBounds("UoPeople!", 0, 0, &x1, &y1, &w, &h);
  tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING);
  tft.println("UoPeople!");

  tft.setTextSize(2);
  tft.setTextColor(ILI9341_YELLOW);
  tft.getTextBounds("ELEVATOR SYSTEM", 0, 0, &x1, &y1, &w, &h);
  tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 40);
  tft.println("ELEVATOR SYSTEM");

  tft.getTextBounds("Welcome!", 0, 0, &x1, &y1, &w, &h);
  tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 70);
  tft.println("Welcome!");

  tft.getTextBounds("Select a floor.", 0, 0, &x1, &y1, &w, &h);
  tft.setCursor((tft.width() - w) / 2 + UI_OFFSET_X, PADDING + 95);
  tft.println("Select a floor.");
}

// Clear lower action area
void clearActionArea() {
  tft.fillRect(0, TOP_AREA_HEIGHT, tft.width(), tft.height() - TOP_AREA_HEIGHT, ILI9341_BLACK);
}

// ------------------------------------------------------
// DOOR STATUS TEXT
// ------------------------------------------------------
void showDoorStatus(const char* status) {
  uint16_t color;

  if (strcmp(status, "DOOR OPEN") == 0)
    color = ILI9341_GREEN;
  else
    color = ILI9341_RED;

  tft.setTextSize(2);
  tft.setTextColor(color, ILI9341_BLACK);

  int16_t x1, y1;
  uint16_t w, h;

  tft.getTextBounds(status, 0, 0, &x1, &y1, &w, &h);

  int posY = 140;
  int posX = (tft.width() - w) / 2;

  tft.fillRect(0, posY, tft.width(), h + 6, ILI9341_BLACK);
  tft.setCursor(posX, posY);
  tft.print(status);
}

// Open elevator door
void openDoor() {
  digitalWrite(LED_DOOR_OPEN, HIGH);
  digitalWrite(LED_DOOR_CLOSED, LOW);
  showDoorStatus("DOOR OPEN");
}

// Close elevator door
void closeDoor() {
  digitalWrite(LED_DOOR_OPEN, LOW);
  digitalWrite(LED_DOOR_CLOSED, HIGH);
  showDoorStatus("DOOR CLOSE");
}

// Pause at floor with door open
void pauseInFloor(unsigned long timeMs) {
  unsigned long start = millis();
  openDoor();

  while (millis() - start < timeMs) {
    // still accepting keypad input
  }

  closeDoor();
}

// ------------------------------------------------------
// MAIN ELEVATOR PROCESS
// ------------------------------------------------------
void processElevator() {
  if (floorCount == 0) {
    movingUp = movingDown = false;
    return;
  }

  int target = floorList[0];

  if (target > currentFloor) {
    movingUp = true;
    movingDown = false;
    currentFloor++;
    beepFloor();
  }
  else if (target < currentFloor) {
    movingDown = true;
    movingUp = false;
    currentFloor--;
    beepFloor();
  }

  if (currentFloor == target) {
    pauseInFloor(3000);
    removeCurrentTarget();
    movingUp = movingDown = false;
  }
}

// ------------------------------------------------------
// SETUP
// ------------------------------------------------------
void setup() {
  Wire.begin();

  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(HALL_UP_BUTTON, INPUT_PULLUP);
  pinMode(HALL_DOWN_BUTTON, INPUT_PULLUP);

  pinMode(LED_DOOR_OPEN, OUTPUT);
  pinMode(LED_DOOR_CLOSED, OUTPUT);

  digitalWrite(LED_DOOR_OPEN, LOW);
  digitalWrite(LED_DOOR_CLOSED, HIGH);

  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.clearDisplay();
  display.setTextColor(WHITE);
  display.setCursor(0, 0);
  display.println("SYSTEM READY");
  display.display();

  tft.begin();
  tft.setRotation(0);
  tft.fillScreen(ILI9341_BLACK);
  drawTopText();
}

// ------------------------------------------------------
// MAIN LOOP
// ------------------------------------------------------
void loop() {
  char key = customKeypad.getKey();

  if (key && isDigit(key)) {
    addFloor(key - '0');
    beepButton();
  }

  processElevator();
}