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
  {'P', 'X', 'O', 'H', 'A'}, // X -> binary, O -> octal, H -> hexadecimal, A-F for hex digits
  {'(', ')', '%', 'L', 'B'}, // L -> clear, B -> backspace
  {'7', '8', '9', '+', 'C'},
  {'4', '5', '6', '-', 'D'},
  {'1', '2', '3', '*', 'E'},
  {'0', '.', '=', '/', 'F'}
};

byte rowPins[ROWS] = {2, 3, 4, 5, 6, 7};
byte colPins[COLS] = {8, 9, 10, 11, 12};

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

// ---------------- NUMBER SYSTEM VARIABLES ----------------
enum NumberSystem { DECIMAL, BINARY, OCTAL, HEXADECIMAL };
NumberSystem currentSystem = DECIMAL;
NumberSystem activeSystem = DECIMAL; // System currently selected via button

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
    Serial.print("Key pressed: ");
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
        currentSystem = DECIMAL;
        activeSystem = DECIMAL;
        updateDisplay();
      } else {
        showPowerOff();
      }
      delay(300);
      return;
    }

    if (!calculatorOn) return;

    // Number system selection: X(BIN), O(OCT), H(HEX)
    if (key == 'X' || key == 'O' || key == 'H') {
      handleNumberSystemSelection(key);
      updateDisplay();
      return;
    }

    // C key: Clear all (full reset)
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
    // L key: Clear current input (soft clear)
    else if (key == 'L') {
      input = "0";
      resultDisplayed = false;
      updateDisplay();
    }
    // Operators
    else if (key == '+' || key == '-' || key == '*' || key == '/') {
      // Save current input as first number
      if (input != "" && input != "0") {
        firstNumber = inputToDecimal(input);
        Serial.print("First number saved: ");
        Serial.println(firstNumber);
      } else if (resultDisplayed) {
        // If result was displayed, use it as first number
        firstNumber = lastResult;
        Serial.print("Using last result as first number: ");
        Serial.println(firstNumber);
      }
      operation = key;
      enteringSecondNumber = true;
      resultDisplayed = false;
      input = "0";  // Reset input for second number
      updateDisplay();
    }
    // Equals
    else if (key == '=') {
      if (operation != '\0') {
        if (enteringSecondNumber) {
          secondNumber = inputToDecimal(input);
        }
        lastResult = calculateResult();
        resultDisplayed = true;
        enteringSecondNumber = false;
        Serial.print("Result calculated: ");
        Serial.println(lastResult);
      }
      updateDisplay();
    }
    // Digits and hex letters
    else if (isValidInput(key)) {
      if (resultDisplayed) {
        // After result, new digit starts a new calculation
        input = String(key);
        firstNumber = lastResult;  // Chain operations (optional)
        operation = '\0';
        enteringSecondNumber = false;
        resultDisplayed = false;
      } else {
        if (input.length() < MAX_DIGITS) {
          if (input == "0") {
            input = String(key);
          } else {
            // Check if adding this digit keeps the number valid for current system
            String testInput = input + key;
            if (isValidNumberForSystem(testInput, activeSystem)) {
              input += key;
            }
          }
        }
      }
      updateDisplay();
    }
  }
}

// ---------------- NUMBER SYSTEM SELECTION ----------------
void handleNumberSystemSelection(char key) {
  // Convert current input to decimal before changing system
  double currentValue = 0;
  if (input != "" && input != "0") {
    currentValue = inputToDecimal(input);
  } else if (resultDisplayed) {
    currentValue = lastResult;
  } else if (firstNumber != 0) {
    currentValue = firstNumber;
  }
  
  // Toggle between selected system and DECIMAL
  if ((key == 'X' && activeSystem == BINARY) ||
      (key == 'O' && activeSystem == OCTAL) ||
      (key == 'H' && activeSystem == HEXADECIMAL)) {
    // If already active, toggle back to DECIMAL
    activeSystem = DECIMAL;
    currentSystem = DECIMAL;
  } else {
    // Set new active system
    if (key == 'X') {
      activeSystem = BINARY;
      currentSystem = BINARY;
    } else if (key == 'O') {
      activeSystem = OCTAL;
      currentSystem = OCTAL;
    } else if (key == 'H') {
      activeSystem = HEXADECIMAL;
      currentSystem = HEXADECIMAL;
    }
  }
  
  // Convert the value to new system
  if (resultDisplayed) {
    lastResult = currentValue;
  } else if (enteringSecondNumber) {
    firstNumber = currentValue;
  } else if (input != "" && input != "0") {
    input = decimalToSystem(currentValue);
  } else if (firstNumber != 0) {
    firstNumber = currentValue;
  }
  updateDisplay();
}

// ---------------- VALID INPUT CHECK ----------------
bool isValidInput(char key) {
  if (key == '.') {
    // Only allow decimal point in decimal mode
    return (activeSystem == DECIMAL);
  }
  
  if (key >= '0' && key <= '9') {
    // For numbers 0-9, check based on current system
    int digit = key - '0';
    
    switch (activeSystem) {
      case BINARY:
        return (digit == 0 || digit == 1); // Only 0 and 1 allowed
      case OCTAL:
        return (digit >= 0 && digit <= 7); // Only 0-7 allowed
      case HEXADECIMAL:
      case DECIMAL:
        return true; // All digits allowed
      default:
        return false;
    }
  }
  
  // Hex digits only allowed in hexadecimal mode (uppercase and lowercase)
  if (activeSystem == HEXADECIMAL) {
    if ((key >= 'A' && key <= 'F') || (key >= 'a' && key <= 'f')) {
      return true;
    }
  }
  
  return false;
}

// ---------------- VALID NUMBER CHECK FOR SYSTEM ----------------
bool isValidNumberForSystem(String number, NumberSystem system) {
  if (number == "") return false;
  
  for (int i = 0; i < number.length(); i++) {
    char ch = number.charAt(i);
    
    if (ch == '.') {
      // Decimal point only allowed in decimal mode
      if (system != DECIMAL) return false;
      continue;
    }
    
    int digit;
    if (ch >= '0' && ch <= '9') {
      digit = ch - '0';
    } else if (ch >= 'A' && ch <= 'F') {
      digit = ch - 'A' + 10;
    } else if (ch >= 'a' && ch <= 'f') {
      digit = ch - 'a' + 10;
    } else {
      return false;
    }
    
    // Check digit validity for each system
    switch (system) {
      case BINARY:
        if (digit != 0 && digit != 1) return false;
        break;
      case OCTAL:
        if (digit > 7) return false;
        break;
      case HEXADECIMAL:
        if (digit > 15) return false;
        break;
      case DECIMAL:
        // All digits 0-9 allowed
        break;
    }
  }
  
  return true;
}

// ---------------- CONVERT INPUT TO DECIMAL ----------------
double inputToDecimal(String value) {
  if (value == "" || value == "0") return 0;
  
  switch (activeSystem) {
    case DECIMAL:
      return value.toDouble();
    case BINARY:
      return binaryToDecimal(value);
    case OCTAL:
      return octalToDecimal(value);
    case HEXADECIMAL:
      return hexToDecimal(value);
    default:
      return value.toDouble();
  }
}

// ---------------- CONVERT DECIMAL TO CURRENT SYSTEM ----------------
String decimalToSystem(double value) {
  long longValue = (long)value;
  
  switch (activeSystem) {
    case DECIMAL:
      return String(longValue);
    case BINARY:
      return decimalToBinary(longValue);
    case OCTAL:
      return decimalToOctal(longValue);
    case HEXADECIMAL:
      return decimalToHex(longValue);
    default:
      return String(longValue);
  }
}

// ---------------- BINARY CONVERSIONS ----------------
double binaryToDecimal(String binaryStr) {
  long decimal = 0;
  long base = 1;
  int len = binaryStr.length();
  
  for (int i = len - 1; i >= 0; i--) {
    if (binaryStr.charAt(i) == '1') {
      decimal += base;
    } else if (binaryStr.charAt(i) != '0') {
      // Invalid binary digit
      return 0;
    }
    base = base * 2;
  }
  
  return (double)decimal;
}

String decimalToBinary(long decimal) {
  if (decimal == 0) return "0";
  
  String binary = "";
  while (decimal > 0) {
    binary = String(decimal % 2) + binary;
    decimal /= 2;
  }
  return binary;
}

// ---------------- OCTAL CONVERSIONS ----------------
double octalToDecimal(String octalStr) {
  long decimal = 0;
  long base = 1;
  int len = octalStr.length();
  
  for (int i = len - 1; i >= 0; i--) {
    int digit = octalStr.charAt(i) - '0';
    if (digit >= 0 && digit <= 7) {
      decimal += digit * base;
    } else {
      return 0;
    }
    base = base * 8;
  }
  
  return (double)decimal;
}

String decimalToOctal(long decimal) {
  if (decimal == 0) return "0";
  
  String octal = "";
  while (decimal > 0) {
    octal = String(decimal % 8) + octal;
    decimal /= 8;
  }
  return octal;
}

// ---------------- HEXADECIMAL CONVERSIONS ----------------
double hexToDecimal(String hexStr) {
  long decimal = 0;
  long base = 1;
  int len = hexStr.length();
  
  for (int i = len - 1; i >= 0; i--) {
    char ch = hexStr.charAt(i);
    int digit;
    
    if (ch >= '0' && ch <= '9') {
      digit = ch - '0';
    } else if (ch >= 'A' && ch <= 'F') {
      digit = ch - 'A' + 10;
    } else if (ch >= 'a' && ch <= 'f') {
      digit = ch - 'a' + 10;
    } else {
      return 0;
    }
    
    decimal += digit * base;
    base = base * 16;
  }
  
  return (double)decimal;
}

String decimalToHex(long decimal) {
  if (decimal == 0) return "0";
  
  String hex = "";
  while (decimal > 0) {
    int remainder = decimal % 16;
    if (remainder < 10) {
      hex = String(remainder) + hex;
    } else {
      hex = char('A' + (remainder - 10)) + hex;
    }
    decimal /= 16;
  }
  return hex;
}

// ---------------- CALCULATION ----------------
double calculateResult() {
  // All calculations are performed in decimal
  // The result will be converted back to the active system for display
  
  switch (operation) {
    case '+': 
      return firstNumber + secondNumber;
    case '-': 
      return (firstNumber >= secondNumber) ? firstNumber - secondNumber : 0;
    case '*': 
      return firstNumber * secondNumber;
    case '/': 
      return (secondNumber != 0) ? firstNumber / secondNumber : 0;
    default:  
      return firstNumber;
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

// ---------------- CENTER TEXT ----------------
int centerText(String text, int fontSize) {
  int16_t x, y;
  uint16_t w, h;
  display.setTextSize(fontSize);
  display.getTextBounds(text, 0, 0, &x, &y, &w, &h);
  return (SCREEN_WIDTH - w) / 2;
}

// ---------------- GET SYSTEM LABEL ----------------
String getSystemLabel() {
  switch (activeSystem) {
    case DECIMAL:     return "DEC";
    case BINARY:      return "BIN";
    case OCTAL:       return "OCT";
    case HEXADECIMAL: return "HEX";
    default:          return "DEC";
  }
}

// ---------------- UPDATE DISPLAY ----------------
void updateDisplay() {
  display.clearDisplay();
  display.setTextSize(1); // Fixed font size 1

  // Display current number system at top left (aligned left)
  display.setCursor(5, 2);
  display.println(getSystemLabel());

  // Calculate positions
  int yLine = 40;      // Y position of the horizontal divider line
  int yTop = 15;       // Y position for first number
  int yMiddle = 25;    // Y position for operation + second number
  int yResult = 46;    // Y position for result (below line)

  String topLine = "";
  String middleLine = "";
  String resultLine = "";

  // DEBUG: Print values to Serial
  Serial.print("updateDisplay - firstNumber: ");
  Serial.print(firstNumber);
  Serial.print(", resultDisplayed: ");
  Serial.print(resultDisplayed);
  Serial.print(", lastResult: ");
  Serial.println(lastResult);

  // First number - determine what to show
  if (enteringSecondNumber || resultDisplayed) {
    // When entering second number or result is displayed, show firstNumber
    topLine = decimalToSystem(firstNumber);
  } else {
    // Otherwise show current input
    topLine = input;
  }

  // Operation + second number
  if (enteringSecondNumber) {
    // Show operation and the second number being entered
    middleLine = String(operation) + " " + input;
  } else if (resultDisplayed) {
    // After calculation, show the full operation with second number
    middleLine = String(operation) + " " + decimalToSystem(secondNumber);
  }

  // Result
  if (resultDisplayed) {
    resultLine = decimalToSystem(lastResult);
    Serial.print("Result line: ");
    Serial.println(resultLine);
  }

  // Display top line (first number) - left aligned
  if (topLine != "") {
    display.setCursor(5, yTop);
    display.println(topLine);
    Serial.print("Top line: ");
    Serial.println(topLine);
  }

  // Display middle line (operation + second number) - left aligned
  if (middleLine != "") {
    display.setCursor(5, yMiddle);
    display.println(middleLine);
    Serial.print("Middle line: ");
    Serial.println(middleLine);
  }

  // Draw horizontal divider line (only when there's an operation or result)
  if (enteringSecondNumber || resultDisplayed) {
    int lineStartX = LINE_MARGIN;
    int lineEndX = SCREEN_WIDTH - LINE_MARGIN - 1;
    display.drawFastHLine(lineStartX, yLine, lineEndX - lineStartX + 1, SSD1306_WHITE);
  }

  // Display result below the line - left aligned
  if (resultLine != "") {
    display.setCursor(5, yResult);
    display.println(resultLine);
    Serial.print("Result displayed: ");
    Serial.println(resultLine);
  }

  display.display();
}