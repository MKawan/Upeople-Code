// ======================================================
// ELEVATOR CONTROL SYSTEM - MKawan -V0.1.0
// Uses ILI9341 TFT, SSD1306 OLED, Keypad, Buzzer and LEDs
// ======================================================

#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_ILI9341.h>

// ------------------------------------------------------
// BUZZER
// ------------------------------------------------------
#define BUZZER_PIN 6
// Short beep when a key/button is pressed
void beepButton() { tone(BUZZER_PIN, 1800, 80); }
// Longer beep when arriving at a floor
void beepFloor() { tone(BUZZER_PIN, 1200, 120); delay(150); }

// ------------------------------------------------------
// EXTERNAL HALL CALL BUTTONS (Up and Down from outside)
// ------------------------------------------------------
#define HALL_UP_BUTTON 12
#define HALL_DOWN_BUTTON 13

// ------------------------------------------------------
// ILI9341 TFT DISPLAY (Main large color screen)
// ------------------------------------------------------
#define TFT_CS 8
#define TFT_RST 9
#define TFT_DC 10
Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);

// ------------------------------------------------------
// SSD1306 OLED DISPLAY (Small monochrome floor indicator)
// ------------------------------------------------------
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

// ------------------------------------------------------
// 4x4 MATRIX KEYPAD (Inside the elevator)
// ------------------------------------------------------
const byte ROWS = 4;
const byte COLS = 4;
// Key layout: numbers + special keys
// C = Rooftop (10), T = Ground floor (0), S = Basement (-1)
char hexaKeys[ROWS][COLS] = {
  { '1', '2', '3', 'C' },
  { '4', '5', '6', 'T' },
  { '7', '8', '9', 'S' },
  { '*', '0', '#', 'P' }
};
byte rowPins[ROWS] = { 33, 35, 37, 39 };    // Row pins
byte colPins[COLS] = { 41, 43, 45, 47 };    // Column pins
Keypad customKeypad = Keypad(makeKeymap(hexaKeys), rowPins, colPins, ROWS, COLS);

// ------------------------------------------------------
// UI LAYOUT CONSTANTS (for TFT screen positioning)
// ------------------------------------------------------
const int PADDING = 20;
const int ARROW_AREA_WIDTH = 20;
const int TOP_AREA_HEIGHT = 160;   // Height of header area
const int UI_OFFSET_X = 0;
const int ACTION_OFFSET_Y = 10;
const int OFFSET_X = 40;           // Horizontal offset for animations

// ------------------------------------------------------
// ELEVATOR STATE VARIABLES
// ------------------------------------------------------
int currentFloor = 0;              // Current floor the elevator is on
int FLOOR_TIME = 3000;             // Time (ms) to simulate moving one floor
bool movingUp = false;             // Direction flags
bool movingDown = false;

// DOOR STATUS LEDs
#define LED_PORTA_ABERTA 48   // Green LED - Door Open
#define LED_PORTA_FECHADA 46  // Red LED   - Door Closed

#define MAX_FLOORS 20              // Max number of floors that can be queued
int floorList[MAX_FLOORS];         // Simple array acting as a queue
int floorCount = 0;                // Number of pending floors

// ------------------------------------------------------
// FLOOR QUEUE MANAGEMENT (replaces old enqueue/dequeue)
// ------------------------------------------------------
void addFloor(int f) {
  // Ignore duplicate requests
  for (int i = 0; i < floorCount; i++) {
    if (floorList[i] == f) return;
  }
  // Add floor to the end of the list if space available
  if (floorCount < MAX_FLOORS) {
    floorList[floorCount++] = f;
  }
}

// Remove the floor that has just been served (first in list)
void removeCurrentTarget() {
  if (floorCount == 0) return;
  // Shift all elements left by one
  for (int i = 1; i < floorCount; i++) {
    floorList[i - 1] = floorList[i];
  }
  floorCount--;
}

// ------------------------------------------------------
// TFT USER INTERFACE FUNCTIONS
// ------------------------------------------------------
void drawTopText() {
  tft.setTextSize(3);
  tft.setTextColor(ILI9341_CYAN);
  int16_t x1, y1; uint16_t w, h;
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

void clearActionArea() {
  tft.fillRect(0, TOP_AREA_HEIGHT, tft.width(), tft.height() - TOP_AREA_HEIGHT, ILI9341_BLACK);
}

void clearArrows(int x, int y, int size = 40, int extra = 5) {
  tft.fillRect(x - size/2 - extra, y - size - extra, size + 2*extra, 2*size + 2*extra, ILI9341_BLACK);
}

// Check for pressed keys on the internal keypad
void checkKeypad(){
    if (customKeypad.getKeys()) {
    closeDoor();  // Close door as soon as someone presses a key
    for (int i = 0; i < LIST_MAX; i++) {
      if (customKeypad.key[i].stateChanged && customKeypad.key[i].kstate == PRESSED) {
        char key = customKeypad.key[i].kchar;
        beepButton();
        if (isDigit(key)) addFloor(key - '0');           // Regular floor 0-9
        else if (key == 'T') addFloor(0);                // T = Ground floor
        else if (key == 'C') addFloor(10);               // C = Rooftop
        else if (key == 'S') addFloor(-1);               // S = Basement
      }
    }
  }
}

// Display current door status on TFT
void showDoorStatus(const char* status) {
  static bool toggleColor = false;
  static unsigned long lastChange = 0;
  uint16_t cor;
  if (strcmp(status, "DOOR OPEN") == 0) {
    cor = ILI9341_GREEN;
  } else if (strcmp(status, "DOOR CLOSE") == 0) {
    cor = ILI9341_RED;
  }
  tft.setTextSize(2);
  tft.setTextColor(cor, ILI9341_BLACK);
  int16_t x1, y1;
  uint16_t w, h;
  tft.getTextBounds(status, 0, 0, &x1, &y1, &w, &h);
  int posY = 140;
  int posX = (tft.width() - w) / 2;
  tft.fillRect(0, posY, tft.width(), h + 6, ILI9341_BLACK);
  tft.setCursor(posX, posY);
  tft.print(status);
}

void openDoor() {
  digitalWrite(LED_PORTA_ABERTA, HIGH);
  digitalWrite(LED_PORTA_FECHADA, LOW);
  showDoorStatus("DOOR OPEN");
}

void closeDoor() {
  digitalWrite(LED_PORTA_ABERTA, LOW);
  digitalWrite(LED_PORTA_FECHADA, HIGH);
  showDoorStatus("DOOR CLOSE");
}

// Keep door open for a set time while still accepting new inputs
void pauseInFloor(unsigned long times) {
  unsigned long inicio = millis();
  openDoor();
  while (millis() - inicio < times) {
    checkKeypad(); // Still allow new floor requests during pause
  }
  closeDoor();
}

// ------------------------------------------------------
// ANIMATIONS (Up and Down moving arrows)
// ------------------------------------------------------
void animationUp(int x, int y, int size=40) {
  x += OFFSET_X;
  clearArrows(x, y, size, 5);
  unsigned long start = millis();
  while (millis() - start < FLOOR_TIME) {
    checkKeypad();
    float t = float(millis() - start) / FLOOR_TIME;
    int offset = -int(t * size);
    tft.fillTriangle(x, y + offset,
                     x - size/2, y + size + offset,
                     x + size/2, y + size + offset, ILI9341_GREEN);
    delay(30);
    tft.fillTriangle(x, y + offset,
                     x - size/2, y + size + offset,
                     x + size/2, y + size + offset, ILI9341_BLACK);
  }
  tft.fillTriangle(x, y - size/2,
                   x - size/2, y + size/2,
                   x + size/2, y + size/2, ILI9341_GREEN);
}

void animationDown(int x, int y, int size=40) {
  x += OFFSET_X;
  clearArrows(x, y, size, 5);
  unsigned long start = millis();
  while (millis() - start < FLOOR_TIME) {
    checkKeypad();
    float t = float(millis() - start) / FLOOR_TIME;
    int offset = int(t * size);
    tft.fillTriangle(x, y + offset,
                     x - size/2, y - size + offset,
                     x + size/2, y - size + offset, ILI9341_BLUE);
    delay(30);
    tft.fillTriangle(x, y + offset,
                     x - size/2, y - size + offset,
                     x + size/2, y - size + offset, ILI9341_BLACK);
  }
  tft.fillTriangle(x, y + size/2,
                   x - size/2, y - size/2,
                   x + size/2, y - size/2, ILI9341_BLUE);
}

// Update small OLED display (floor number + direction arrows)
void showOLED(int floor, bool up, bool down) {
  display.clearDisplay();
  display.setTextSize(4);
  display.setTextColor(WHITE);
  display.setCursor((128 - 24)/2, 0);
  display.print(floor);
  if (up) display.fillTriangle(64, 50, 54, 63, 74, 63, WHITE);
  if (down) display.fillTriangle(64, 63, 54, 50, 74, 50, WHITE);
  display.display();
}

// Update large TFT display (big floor number + direction arrow)
void showTFT(int floor, bool up, bool down) {
  if (up || down) clearActionArea();
  int centerX = tft.width() / 2;
  int centerY = TOP_AREA_HEIGHT + 60;
  tft.setTextSize(9);
  tft.setTextColor(ILI9341_RED);
  int16_t x1, y1; uint16_t w, h;
  char text[4]; sprintf(text, "%d", floor);
  tft.getTextBounds(text, 0, 0, &x1, &y1, &w, &h);
  tft.setCursor((centerX - w/2) + OFFSET_X, centerY - h/2);
  tft.print(text);
  if (up) {
    tft.fillTriangle(centerX + OFFSET_X, centerY + 60,
                    centerX - 20 + OFFSET_X, centerY + 90,
                    centerX + 20 + OFFSET_X, centerY + 90, ILI9341_GREEN);
  }
  else if (down) {
    tft.fillTriangle(centerX + OFFSET_X, centerY + 90,
                    centerX - 20 + OFFSET_X, centerY + 60,
                    centerX + 20 + OFFSET_X, centerY + 60, ILI9341_BLUE);
  }
}

// ------------------------------------------------------
// MAIN ELEVATOR MOVEMENT LOGIC (using simple array)
// ------------------------------------------------------
void processElevator() {
  if (floorCount == 0) { // No pending floors → idle
    movingUp = movingDown = false;
    return;
  }
  int target = floorList[0]; // Next destination
  int arrowX = PADDING + ARROW_AREA_WIDTH/2 - UI_OFFSET_X;
  int centerY = TOP_AREA_HEIGHT + (tft.height() - TOP_AREA_HEIGHT) / 2;

  if (target > currentFloor) {
    movingUp = true;
    movingDown = false;
    animationUp(arrowX, centerY);
    currentFloor++;
    beepFloor();
  }
  else if (target < currentFloor) {
    movingDown = true;
    movingUp = false;
    animationDown(arrowX, centerY);
    currentFloor--;
    beepFloor();
  }

  // Update both displays
  showOLED(currentFloor, movingUp, movingDown);
  showTFT(currentFloor, movingUp, movingDown);

  // Arrived at requested floor?
  if (currentFloor == target){
    openDoor();
    beepFloor();
    pauseInFloor(3000);        // Wait 3 seconds with door open
    removeCurrentTarget();     // Remove served floor from list
    movingUp = movingDown = false;
    closeDoor();
  }
}

// ------------------------------------------------------
// SETUP - Runs once at startup
// ------------------------------------------------------
void setup() {
  Wire.begin();
  // Buzzer
  pinMode(BUZZER_PIN, OUTPUT);
  // External hall buttons
  pinMode(HALL_UP_BUTTON, INPUT_PULLUP);
  pinMode(HALL_DOWN_BUTTON, INPUT_PULLUP);
  // Door status LEDs (start closed)
  pinMode(LED_PORTA_ABERTA, OUTPUT);
  pinMode(LED_PORTA_FECHADA, OUTPUT);
  digitalWrite(LED_PORTA_ABERTA, LOW);
  digitalWrite(LED_PORTA_FECHADA, HIGH);

  // Initialize OLED
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) { for(;;); }
  display.clearDisplay();
  display.setTextSize(0);
  display.setTextColor(WHITE);
  display.setCursor(0,0); display.println("MEGA OK!");
  display.display();

  // Initialize TFT
  tft.begin();
  tft.setRotation(0);
  tft.fillScreen(ILI9341_BLACK);
  drawTopText();
}

// ------------------------------------------------------
// MAIN LOOP - Runs continuously
// ------------------------------------------------------
bool lastUpState = HIGH;
bool lastDownState = HIGH;

void loop() {
  // Read external hall buttons (both call ground floor)
  bool upState = digitalRead(HALL_UP_BUTTON);
  bool downState = digitalRead(HALL_DOWN_BUTTON);

  checkKeypad();  // Check internal keypad

  // Detect button press (falling edge)
  if (lastUpState == HIGH && upState == LOW) {
    addFloor(0);
    beepButton();
  }
  if (lastDownState == HIGH && downState == LOW) {
    addFloor(0);
    beepButton();
  }

  lastUpState = upState;
  lastDownState = downState;

  processElevator();  // Handle movement and floor requests
}