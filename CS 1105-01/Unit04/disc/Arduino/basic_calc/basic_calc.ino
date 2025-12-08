#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

// ---------------- OLED DISPLAY SSD1306 ----------------
#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire);

// ---------------- 6x5 KEYPAD ----------------
const byte ROWS = 5;
const byte COLS = 4;

const char keys[ROWS][COLS] = {
  {'P', 'C', '(', ')'},
  {'7', '8', '9', '+'},
  {'4', '5', '6', '-'},
  {'1', '2', '3', '*'},
  {'0', '.', '=', '/'},
};

byte rowPins[ROWS] = {2, 3, 4, 5, 6,};
byte colPins[COLS] = {7, 8, 9, 10};

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);


// ---------------- CONFIGURATION ----------------
const uint8_t MAX_DIGITS = 13;  // Maximum 13 digits per number
const uint8_t LINE_MARGIN = 10; // Margin on each side for the horizontal line
const uint8_t LINE_Y = 40;      // Y position of the horizontal divider line

// ---------------- CALCULATOR VARIABLES ----------------
String input = "0";
double firstNumber = 0;
double secondNumber = 0;
double lastResult = 0;   // Store the last calculated result
char operation = '\0';
bool enteringSecondNumber = false;
bool resultDisplayed = false;
bool calculatorOn = false;

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

  if (key) {
    Serial.println(key);

    // P key: Power on/off
    if (key == 'P') {
      calculatorOn = !calculatorOn;
      if (calculatorOn) {
        input = "0";
        firstNumber = 0;
        secondNumber = 0;
        lastResult = 0;
        operation = '\0';
        enteringSecondNumber = false;
        resultDisplayed = false;
        updateDisplay();
      } else {
        showPowerOff();
      }
      delay(300);
      return;
    }

    if (!calculatorOn) return;

    // C key: Clear all
    if (key == 'C') {
      input = "0";
      firstNumber = 0;
      secondNumber = 0;
      lastResult = 0;
      operation = '\0';
      enteringSecondNumber = false;
      resultDisplayed = false;
      updateDisplay();
    }
    // Operators
    else if (key == '+' || key == '-' || key == '*' || key == '/') {
      if (input != "0") firstNumber = input.toDouble();
      else firstNumber = 0;
      operation = key;
      enteringSecondNumber = true;
      input = "0";
      resultDisplayed = false;
      updateDisplay();
    }
    // Equals
    else if (key == '=') {
      if (operation != '\0' && enteringSecondNumber) {
        secondNumber = input.toDouble();
        lastResult = calculateResult();
        resultDisplayed = true;
      }
      updateDisplay();
    }
    // Digits
    else {
      if (resultDisplayed) {
        // After result, new digit starts a new calculation
        input = "0";
        firstNumber = lastResult;  // Chain operations (optional)
        operation = '\0';
        enteringSecondNumber = false;
        resultDisplayed = false;
      }

      if (input.length() < MAX_DIGITS) {
        if (input == "0") input = String(key);
        else input += key;
      }
      updateDisplay();
    }
  }
}

// ---------------- CALCULATION ----------------
double calculateResult() {
  switch (operation) {
    case '+': return firstNumber + secondNumber;
    case '-': return (firstNumber >= secondNumber) ? firstNumber - secondNumber : 0;
    case '*': return firstNumber * secondNumber;
    case '/': return (secondNumber != 0) ? firstNumber / secondNumber : 0;
    default:  return firstNumber;
  }
}

// ---------------- SHOW POWER OFF ----------------
void showPowerOff() {
  display.clearDisplay();
  display.setTextSize(2);
  display.setCursor(centerText("OFF", 2), 20);
  display.println("OFF");
  display.display();
}

// ---------------- UNIFIED FONT SIZE ----------------
int getUnifiedFontSize(String line1, String line2) {
  int len1 = line1.length();
  int len2 = line2.length();
  int maxLen = max(len1, len2);

  if (maxLen <= 7)  return 2;
  if (maxLen <= 10) return 1;
  return 1;
}

// ---------------- CENTER TEXT ----------------
int centerText(String text, int fontSize) {
  int16_t x, y;
  uint16_t w, h;
  display.getTextBounds(text, 0, 0, &x, &y, &w, &h);
  return (SCREEN_WIDTH - w) / 2;
}

// ---------------- UPDATE DISPLAY ----------------
void updateDisplay() {
  display.clearDisplay();

  String topLine = "";
  String middleLine = "";
  String resultLine = "";

  // Always show first number on top
  if (!enteringSecondNumber && !resultDisplayed) {
    topLine = input;
  } else {
    topLine = String(firstNumber);
  }

  // Show operation + second number
  if (enteringSecondNumber) {
    middleLine = String(operation) + " " + input;
  }

  // Show result below the line when calculated
  if (resultDisplayed) {
    resultLine = String(lastResult);
  }

  // Unified size for the two operand lines
  int unifiedSize = getUnifiedFontSize(topLine, middleLine);

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
  if (middleLine != "") {
    display.setTextSize(unifiedSize);
    display.setCursor(centerText(middleLine, unifiedSize), yMiddle);
    display.println(middleLine);
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