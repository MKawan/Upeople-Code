#include <Wire.h>
#include <Keypad.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

#define SCREEN_WIDTH 128
#define SCREEN_HEIGHT 64
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire);

// ---------------- TECLADO 6x5 ----------------
const byte ROWS = 6;
const byte COLS = 5;
const char keys[ROWS][COLS] = {
  {'P', 'B', 'O', 'H', 'A'},
  {'(', ')', '%', 'C', 'B'},
  {'7', '8', '9', '+', 'C'},
  {'4', '5', '6', '-', 'D'},
  {'1', '2', '3', '*', 'E'},
  {'0', '.', '=', 'D', 'F'}  // 'D' = divisão
};
byte rowPins[ROWS] = {2, 3, 4, 5, 6, 7};
byte colPins[COLS] = {8, 9, 10, 11, 12};
Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// ---------------- CONFIGURAÇÃO DISPLAY ----------------
const uint8_t LINE_MARGIN = 10;
const uint8_t LINE_Y = 40;

// ---------------- VARIÁVEIS ----------------
String input = "0";
String firstNum = "0";
String secondNum = "0";
String result = "";
char operation = '\0';
bool enteringSecond = false;
bool showResult = false;
bool calculatorOn = false;
String mode = "DEC";  // DEC, BIN, OCT, HEX

// -------------------------------------------------
void setup() {
  Serial.begin(9600);
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) {
    Serial.println(F("SSD1306 failed"));
    for (;;);
  }
  display.clearDisplay();
  display.setTextColor(SSD1306_WHITE);
  showPowerOff();
}

void loop() {
  char key = keypad.getKey();
  if (!key) return;

  // Power
  if (key == 'P') {
    calculatorOn = !calculatorOn;
    if (calculatorOn) resetCalc();
    else showPowerOff();
    delay(300);
    return;
  }
  if (!calculatorOn) return;

  // Modos
  if (key == 'B') { mode = (mode == "BIN") ? "DEC" : "BIN"; resetCalc(); return; }
  if (key == 'O') { mode = (mode == "OCT") ? "DEC" : "OCT"; resetCalc(); return; }
  if (key == 'H') { mode = (mode == "HEX") ? "DEC" : "HEX"; resetCalc(); return; }

  // Clear
  if (key == 'C') { resetCalc(); return; }

  // Porcentagem (só DEC)
  if (key == '%' && mode == "DEC" && enteringSecond && operation != '\0') {
    double a = firstNum.toDouble();
    double b = input.toDouble();
    result = formatNumber(a * b / 100.0);
    showResult = true;
    updateDisplay();
    return;
  }

  // Operadores
  if (key == '+' || key == '-' || key == '*' || key == 'D') {
    if (showResult) { firstNum = result; showResult = false; }
    else            { firstNum = input; }
    operation = (key == 'D') ? '/' : key;
    enteringSecond = true;
    input = "0";
    updateDisplay();
    return;
  }

  // Igual
  if (key == '=') {
    if (operation && enteringSecond) {
      secondNum = input;
      result = calculateCurrentMode(firstNum, secondNum, operation);
      showResult = true;
    }
    updateDisplay();
    return;
  }

  // Novo número após resultado (só se digitar dígito)
  if (showResult && isValidKey(key)) {
    resetCalc();
    input = String(key);
    updateDisplay();
    return;
  }

  // Entrada de dígitos
  if (isValidKey(key)) {
    if (input == "0" && key != '.' && !(mode != "DEC" && key == '0')) {
      input = String(key);
    } else if (input.length() < getMaxDigits()) {
      input += key;
    }
    updateDisplay();
  }
}

// ================================================
// FUNÇÕES DE CÁLCULO POR BASE
// ================================================
String calculateCurrentMode(String a, String b, char op) {
  if (mode == "DEC") return calcDEC(a, b, op);
  if (mode == "BIN") return calcBIN(a, b, op);
  if (mode == "OCT") return calcOCT(a, b, op);
  if (mode == "HEX") return calcHEX(a, b, op);
  return "ERR";
}

String calcDEC(String a, String b, char op) {
  double x = a.toDouble(), y = b.toDouble(), r = 0;
  switch (op) { case '+': r = x + y; break; case '-': r = (x >= y) ? x - y : 0; break;
                case '*': r = x * y; break; case '/': r = (y != 0) ? x / y : 0; break; }
  return formatNumber(r);
}

String calcBIN(String a, String b, char op) {
  unsigned long x = strtoul(a.c_str(), NULL, 2);
  unsigned long y = strtoul(b.c_str(), NULL, 2);
  unsigned long r = 0;
  switch (op) { case '+': r = x + y; break; case '-': r = (x >= y) ? x - y : 0; break;
                case '*': r = x * y; break; case '/': r = (y != 0) ? x / y : 0; break; }
  return ulongToBase(r, 2);
}

String calcOCT(String a, String b, char op) {
  unsigned long x = strtoul(a.c_str(), NULL, 8);
  unsigned long y = strtoul(b.c_str(), NULL, 8);
  unsigned long r = 0;
  switch (op) { case '+': r = x + y; break; case '-': r = (x >= y) ? x - y : 0; break;
                case '*': r = x * y; break; case '/': r = (y != 0) ? x / y : 0; break; }
  return ulongToBase(r, 8);
}

String calcHEX(String a, String b, char op) {
  unsigned long x = strtoul(a.c_str(), NULL, 16);
  unsigned long y = strtoul(b.c_str(), NULL, 16);
  unsigned long r = 0;
  switch (op) { case '+': r = x + y; break; case '-': r = (x >= y) ? x - y : 0; break;
                case '*': r = x * y; break; case '/': r = (y != 0) ? x / y : 0; break; }
  return ulongToBase(r, 16);
}

// ================================================
void resetCalc() {
  input = "0";
  firstNum = "0";
  secondNum = "0";
  result = "";
  operation = '\0';
  enteringSecond = false;
  showResult = false;
  updateDisplay();
}

uint8_t getMaxDigits() {
  if (mode == "BIN") return 32;
  if (mode == "OCT") return 11;
  if (mode == "HEX") return 8;
  return 13;
}

bool isValidKey(char k) {
  if (k == '.') return (mode == "DEC" && input.indexOf('.') == -1);
  if (k >= '0' && k <= '9') {
    if (mode == "BIN" && k > '1') return false;
    if (mode == "OCT" && k > '7') return false;
    return true;
  }
  if (mode == "HEX") return (toupper(k) >= 'A' && toupper(k) <= 'F');
  return false;
}

String ulongToBase(unsigned long n, int base) {
  if (n == 0) return "0";
  String s = "";
  while (n > 0) {
    int d = n % base;
    s = char(d < 10 ? '0' + d : 'A' + d - 10) + s;
    n /= base;
  }
  return s;
}

String formatNumber(double val) {
  if (val == (long)val) return String((long)val);
  String s = String(val, 6);
  while (s.length() > 1 && (s.endsWith("0") || s.endsWith("."))) s.remove(s.length() - 1);
  return s;
}

// ---------------- DISPLAY EXATAMENTE COMO O MODELO ----------------
void showPowerOff() {
  display.clearDisplay();
  display.setTextSize(2);
  display.setCursor(centerText("OFF", 2), 20);
  display.println("OFF");
  display.display();
}

int getUnifiedFontSize(String line1, String line2) {
  int maxLen = max(line1.length(), line2.length());
  if (maxLen <= 7) return 2;
  if (maxLen <= 10) return 1;
  return 1;
}

int centerText(String text, int fontSize) {
  int16_t x, y; uint16_t w, h;
  display.getTextBounds(text.c_str(), 0, 0, &x, &y, &w, &h);
  int pos = (SCREEN_WIDTH - w) / 2;
  return (pos >= 0) ? pos : 0;  // nunca negativo
}

void updateDisplay() {
  display.clearDisplay();

  // Modo no canto
  display.setTextSize(1);
  display.setCursor(SCREEN_WIDTH - 28, 0);
  display.print(mode);

  String topLine    = "";
  String middleLine = "";
  String resultLine = "";

  if (!enteringSecond && !showResult) {
    topLine = input;
  } else {
    topLine = firstNum;
  }

  if (enteringSecond) {
    middleLine = String(getOpSymbol()) + " " + input;
  }

  if (showResult) {
    resultLine = result;
  }

  int unifiedSize = getUnifiedFontSize(topLine, middleLine);

  // Top line
  if (topLine != "") {
    display.setTextSize(unifiedSize);
    display.setCursor(centerText(topLine, unifiedSize), 4);
    display.println(topLine);
  }

  // Middle line
  if (middleLine != "") {
    display.setTextSize(unifiedSize);
    display.setCursor(centerText(middleLine, unifiedSize), 24);
    display.println(middleLine);
  }

  // Linha divisória
  if (enteringSecond || showResult) {
    display.drawFastHLine(LINE_MARGIN, LINE_Y, SCREEN_WIDTH - 2 * LINE_MARGIN, SSD1306_WHITE);
  }

  // Resultado
  if (resultLine != "") {
    int resSize = getUnifiedFontSize(resultLine, "");
    display.setTextSize(resSize);
    display.setCursor(centerText(resultLine, resSize), 46);
    display.println(resultLine);
  }

  display.display();
}

String getOpSymbol() {
  switch (operation) {
    case '+': return "+";
    case '-': return "-";
    case '*': return "x";
    case '/': return String((char)247);
    default: return "";
  }
}