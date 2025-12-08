#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

// ---------------- OLED DISPLAY ----------------
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire);

// ---------------- 6x5 KEYPAD ----------------
const byte ROWS = 6;
const byte COLS = 5;

const char keys[ROWS][COLS] = {
  {'P', 'X', 'O', 'H', 'A'},   // X -> Binary mode
  {'(', ')', '%', 'L', 'B'},   // L -> Clear
  {'7', '8', '9', '+', 'C'},
  {'4', '5', '6', '-', 'D'},
  {'1', '2', '3', '*', 'E'},
  {'0', '.', '=', '/', 'F'}
};

byte rowPins[ROWS] = {2, 3, 4, 5, 6, 7};
byte colPins[COLS] = {8, 9, 10, 11, 12};

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// ---------------- CONFIG ----------------
const uint8_t MAX_DIGITS = 13;
const uint8_t LINE_MARGIN = 10;
const uint8_t LINE_Y = 40;

// ---------------- CALCULATOR STATE ----------------
enum NumberMode { MODE_DEC, MODE_BIN, MODE_OCT, MODE_HEX };
NumberMode currentMode = MODE_DEC;

String input = "0";
String fistInput = "0";

double firstNumber = 0;
double secondNumber = 0;
double lastResult = 0;

char operation = '\0';
bool enteringSecondNumber = false;
bool resultDisplayed = false;
bool calculatorOn = false;

// ===============================================================
// ----------------------- MODE SWITCH ---------------------------
// ===============================================================
void toggleMode(NumberMode target) {
  if (currentMode == target) {
    currentMode = MODE_DEC;
  } else {
    currentMode = target;
  }

  input = "";
  enteringSecondNumber = false;
  operation = '\0';
  resultDisplayed = false;

  updateDisplay();
}

// ===============================================================
// ---------------------- CONVERSION HELPERS ---------------------
// ===============================================================
unsigned long parseInputNumber(String v) {
  switch (currentMode) {
    case MODE_DEC: return strtoul(v.c_str(), NULL, 10);
    case MODE_BIN: return strtoul(v.c_str(), NULL, 2);
    case MODE_OCT: return strtoul(v.c_str(), NULL, 8);
    case MODE_HEX: return strtoul(v.c_str(), NULL, 16);
  }
  return 0;
}

String toBaseString(unsigned long num) {
  switch (currentMode) {
    case MODE_DEC: return String(num, 10);
    case MODE_BIN: return String(num, 2);
    case MODE_OCT: return String(num, 8);
    case MODE_HEX: {
      String s = String(num, 16);
      s.toUpperCase();
      return s;
    }
  }
  return "";
}

// ===============================================================
// ---------------------- CALCULATION ----------------------------
// ===============================================================
double calculateResult() {
  switch (operation) {
    case '+': return firstNumber + secondNumber;
    case '-': return firstNumber - secondNumber;
    case '*': return firstNumber * secondNumber;
    case '/': return (secondNumber != 0) ? firstNumber / secondNumber : 0;
    default:  return firstNumber;
  }
}

// ===============================================================
// ------------------------ POWER OFF ----------------------------
// ===============================================================
void showPowerOff() {
  display.clearDisplay();
  display.setTextSize(2);
  display.setCursor(centerText("OFF", 2), 20);
  display.println("OFF");
  display.display();
}

// ===============================================================
// --------------------- FONT HELPERS ----------------------------
// ===============================================================
int getUnifiedFontSize(String a, String b) {
  int m = max(a.length(), b.length());
  // if (m <= 7) return 2;
  // if (m <= 10) return 1;
  return 1;
}

int centerText(String text, int size) {
  int16_t x, y;
  uint16_t w, h;
  display.setTextSize(size);
  display.getTextBounds(text, 0, 0, &x, &y, &w, &h);
  return (SCREEN_WIDTH - w) / 2;
}

// ===============================================================
// ----------------------- DISPLAY UPDATE ------------------------
// ===============================================================
void updateDisplay() {
  display.clearDisplay();

  // ---- Show MODE on top-left ----
  display.setTextSize(1);
  display.setCursor(0, 0);
  switch (currentMode) {
    case MODE_DEC: display.print("DEC"); break;
    case MODE_BIN: display.print("BIN"); break;
    case MODE_OCT: display.print("OCT"); break;
    case MODE_HEX: display.print("HEX"); break;
  }

  // ---------------------------
  // Montagem das 3 linhas
  // ---------------------------
  String topLine = "";
  String midLine = "";
  String resultLine = "";

  // Primeiro número (sempre aparece)
  topLine = fistInput;

  // Quando está digitando o primeiro número
  if (!enteringSecondNumber && !resultDisplayed)
    topLine = input;

  // Linha do operador + segundo número
  if (enteringSecondNumber) {
    midLine = String(operation);

    // Mostrar segundo número apenas se estiver sendo digitado
    if (input != "")
      midLine += " " + input;
  }

  // Resultado
  if (resultDisplayed)
    resultLine = toBaseString((unsigned long)lastResult);

  // ---------------------------
  // Desenho realmente na tela
  // ---------------------------
  int unifiedSize = 1;

  int yTop = 4;
  int yMiddle = 24;
  int yLine = 40;
  int yResult = 46;

  // Top line (first number)
  if (topLine != "") {
    display.setTextSize(unifiedSize);
    display.setCursor(centerText(topLine, unifiedSize), yTop);
    display.println(topLine);
  }

  // Middle line (operator + second number)
  if (midLine != "") {
    display.setTextSize(unifiedSize);
    display.setCursor(centerText(midLine, unifiedSize), yMiddle);
    display.println(midLine);
  }

  // Horizontal divider line (only when there's an operation)
  if (enteringSecondNumber || resultDisplayed) {
    int lineStartX = LINE_MARGIN;
    int lineEndX = SCREEN_WIDTH - LINE_MARGIN - 1;
    display.drawFastHLine(lineStartX, yLine, lineEndX - lineStartX + 1, SSD1306_WHITE);
  }

  // Result below the line
  if (resultLine != "") {
    int resultSize = getUnifiedFontSize(resultLine, "");
    display.setTextSize(resultSize);
    display.setCursor(centerText(resultLine, resultSize), yResult);
    display.println(resultLine);
  }

  display.display();
}

// ===============================================================
// ---------------------------- SETUP ----------------------------
// ===============================================================
void setup() {
  Serial.begin(9600);

  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    Serial.println("Display error");
    for (;;) ;
  }

  display.clearDisplay();
  display.setTextColor(SSD1306_WHITE);
  showPowerOff();
}

// ===============================================================
// ----------------------------- LOOP ----------------------------
// ===============================================================
void loop() {
  char key = keypad.getKey();
  if (!key) return;

  Serial.println(key);

  // ----- POWER BUTTON -----
  if (key == 'P') {
    calculatorOn = !calculatorOn;
    if (!calculatorOn) {
      showPowerOff();
    } else {
      input = "";
      firstNumber = 0;
      secondNumber = 0;
      lastResult = 0;
      operation = '\0';
      enteringSecondNumber = false;
      resultDisplayed = false;
      currentMode = MODE_DEC;
      updateDisplay();
    }
    delay(200);
    return;
  }

  if (!calculatorOn) return;

  // ----- MODE SWITCH -----
  if (key == 'X') { toggleMode(MODE_BIN); return; }
  if (key == 'O') { toggleMode(MODE_OCT); return; }
  if (key == 'H') { toggleMode(MODE_HEX); return; }

  // ----- CLEAR -----
  if (key == 'L') {
    input = "";
    firstNumber = 0;
    secondNumber = 0;
    lastResult = 0;
    enteringSecondNumber = false;
    resultDisplayed = false;
    operation = '\0';
    updateDisplay();
    return;
  }

  // ----- OPERATORS -----
  if (key=='+' || key=='-' || key=='*' || key=='/') {
    firstNumber = parseInputNumber(input);
    fistInput = String(toBaseString((unsigned long)firstNumber));
    operation = key;
    enteringSecondNumber = true;
    resultDisplayed = false;
    input = "";
    updateDisplay();
    return;
  }

  // ----- EQUALS -----
  if (key == '=') {
    if (operation != '\0' && enteringSecondNumber) {
      secondNumber = parseInputNumber(input);
      lastResult = calculateResult();
      resultDisplayed = true;
    }
    updateDisplay();
    return;
  }

  // ----- DIGITS (0–9, A–F) -----
  key = toupper(key);

  bool validDigit = false;

  if (currentMode == MODE_DEC) {
    validDigit = (key >= '0' && key <= '9');
  } else if (currentMode == MODE_BIN) {
    validDigit = (key == '0' || key == '1');
  } else if (currentMode == MODE_OCT) {
    validDigit = (key >= '0' && key <= '7');
  } else if (currentMode == MODE_HEX) {
    validDigit = ((key >= '0' && key <= '9') || (key >= 'A' && key <= 'F'));
  }

  if (!validDigit) return;

  if (resultDisplayed) {
    input = "";
    enteringSecondNumber = false;
    resultDisplayed = false;
    operation = '\0';
  }

  if (input.length() < MAX_DIGITS) {
    if (input == "") input = String(key);
    else input += key;
  }

  updateDisplay();
}
