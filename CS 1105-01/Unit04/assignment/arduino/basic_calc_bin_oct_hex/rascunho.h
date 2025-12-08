#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

// ---------------- OLED DISPLAY SSD1306 ----------------
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire);

// ---------------- 6x5 KEYPAD ----------------
const byte ROWS = 6;
const byte COLS = 5;

const char keys[ROWS][COLS] = {
  {'P', 'X', 'O', 'H', 'A'},
  {'(', ')', '%', 'L', 'B'},
  {'7', '8', '9', '+', 'C'},
  {'4', '5', '6', '-', 'D'},
  {'1', '2', '3', '*', 'E'},
  {'0', '.', '=', '/', 'F'}
};

byte rowPins[ROWS] = {2, 3, 4, 5, 6, 7};
byte colPins[COLS] = {8, 9, 10, 11, 12};

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// ---------------- CONFIGURATION ----------------
const uint8_t MAX_DIGITS = 13;

// ---------------- CALCULATOR VARIABLES ----------------
String input = "";

// ⚠️ NOVO: armazenar números como STRING
String firstNumberStr = "";
String secondNumberStr = "";

double firstNumber = 0;
double secondNumber = 0;
double lastResult = 0;

char operation = '\0';
bool enteringSecondNumber = false;
bool resultDisplayed = false;
bool calculatorOn = false;

// ---------------- NUMBER SYSTEM MODE ----------------
enum NumMode { MODE_DEC, MODE_BIN, MODE_OCT, MODE_HEX };
NumMode currentMode = MODE_DEC;

// ---------------- CONVERSION HELPERS ----------------
long convertInputToDecimal(const String &value, NumMode mode) {
  if (value.length() == 0) return 0;  // correto
  switch (mode) {
    case MODE_BIN: return strtol(value.c_str(), NULL, 2);
    case MODE_OCT: return strtol(value.c_str(), NULL, 8);
    case MODE_HEX: return strtol(value.c_str(), NULL, 16);
    default:       return (long) value.toDouble();
  }
}

String convertDecimalToMode(double value, NumMode mode) {
  long v = (long)value;

  switch (mode) {
    case MODE_BIN: return String(v, BIN);
    case MODE_OCT: return String(v, OCT);
    case MODE_HEX: return String(v, HEX);
    default:       return String(value);
  }
}

// ---------------- TOGGLE NUMBER SYSTEM ----------------
void toggleMode(NumMode target) {
  if (currentMode == target)
    currentMode = MODE_DEC;
  else
    currentMode = target;
  updateDisplay();
}

// ---------------- SETUP ----------------
void setup() {
  Serial.begin(9600);

  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    Serial.println(F("SSD1306 display failed"));
    for (;;);
  }
  display.clearDisplay();
  display.setTextColor(SSD1306_WHITE);
  showPowerOff();
}

// ---------------- MAIN LOOP ----------------
void loop() {
  char key = keypad.getKey();
  if (!key) return;

  // Power On/Off
  if (key == 'P') {
    calculatorOn = !calculatorOn;
    if (calculatorOn) {
      input = "0";
      firstNumberStr = "";
      secondNumberStr = "";
      firstNumber = 0;
      secondNumber = 0;
      operation = '\0';
      enteringSecondNumber = false;
      resultDisplayed = false;
      currentMode = MODE_DEC;
      updateDisplay();
    } else {
      showPowerOff();
    }
    delay(200);
    return;
  }

  if (!calculatorOn) return;

  // Mode switching
  if (key == 'X') { toggleMode(MODE_BIN); return; }
  if (key == 'O') { toggleMode(MODE_OCT); return; }
  if (key == 'H') { toggleMode(MODE_HEX); return; }

  // Clear
  if (key == 'L') {
    input = "0";
    firstNumberStr = "";
    secondNumberStr = "";
    firstNumber = 0;
    secondNumber = 0;
    operation = '\0';
    enteringSecondNumber = false;
    resultDisplayed = false;
    updateDisplay();
    return;
  }

  // ----------- OPERATOR PRESSED --------------
  if (key == '+' || key == '-' || key == '*' || key == '/') {

    // ⚠️ AQUI ESTÁ A CORREÇÃO PRINCIPAL
    // Guardar o número em STRING sem converter agora
    firstNumberStr = input;

    operation = key;
    enteringSecondNumber = true;

    input = "0";
    resultDisplayed = false;
    updateDisplay();
    return;
  }

  // ----------- EQUALS ----------------
  if (key == '=') {
    if (operation != '\0' && enteringSecondNumber) {

      // ⚠️ CONVERTE SOMENTE AQUI AGORA
      firstNumber = convertInputToDecimal(firstNumberStr, currentMode);
      secondNumber = convertInputToDecimal(input, currentMode);

      lastResult = calculateResult(firstNumber, secondNumber, operation);
      resultDisplayed = true;
    }
    updateDisplay();
    return;
  }

  // ----------- DIGITS ----------------

  // Reset if showing result
  if (resultDisplayed) {
    input = "0";
    resultDisplayed = false;
  }

  // Validate allowed chars
  if (currentMode == MODE_BIN && !(key == '0' || key == '1')) return;
  if (currentMode == MODE_OCT && !(key >= '0' && key <= '7')) return;
  if (currentMode == MODE_HEX && !((key >= '0' && key <= '9') || (key >= 'A' && key <= 'F'))) return;

  if (input == "0")
    input = key;
  else
    input += key;

  updateDisplay();
}

// ---------------- CALCULATION ----------------
double calculateResult(double a, double b, char op) {
  switch (op) {
    case '+': return a + b;
    case '-': return a - b;
    case '*': return a * b;
    case '/': return (b != 0) ? a / b : 0;
  }
  return a;
}

// ---------------- DISPLAY POWER OFF ----------------
void showPowerOff() {
  display.clearDisplay();
  display.setTextSize(2);
  display.setCursor(40, 20);
  display.println("OFF");
  display.display();
}

// ---------------- UPDATE DISPLAY ----------------
void updateDisplay() {
  display.clearDisplay();

  // Mode
  display.setTextSize(1);
  display.setCursor(0, 0);
  switch (currentMode) {
    case MODE_DEC: display.print("DEC"); break;
    case MODE_BIN: display.print("BIN"); break;
    case MODE_OCT: display.print("OCT"); break;
    case MODE_HEX: display.print("HEX"); break;
  }

  // --- TOP LINE ---
  String topLine =
      (!enteringSecondNumber && !resultDisplayed)
      ? input
      : convertDecimalToMode(convertInputToDecimal(firstNumberStr, currentMode), currentMode);

  // --- MIDDLE LINE ---
  String middleLine =
      (enteringSecondNumber)
      ? String(operation) + " " + input
      : "";

  // --- RESULT LINE ---
  String resultLine =
      (resultDisplayed)
      ? convertDecimalToMode(lastResult, currentMode)
      : "";

  display.setCursor(0, 14);
  display.setTextSize(1);
  display.println(topLine);

  if (middleLine.length() > 0) {
    display.setCursor(0, 28);
    display.println(middleLine);
  }

  if (resultLine.length() > 0) {
    display.setCursor(0, 50);
    display.println(resultLine);
  }

  display.display();
}
