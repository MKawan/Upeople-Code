#include <Keypad.h>

const byte ROWS = 4;
const byte COLS = 3;

char keys[ROWS][COLS] = {
  {'1','2','3'},
  {'4','5','6'},
  {'7','8','9'},
  {'*','0','#'}
};

byte rowPins[ROWS] = {2,3,4,5};
byte colPins[COLS] = {6,7,8};

Keypad keypad = Keypad(makeKeymap(keys), rowPins, colPins, ROWS, COLS);

// Pinos do demultiplexador
int S0 = 9, S1 = 10, S2 = 11;

void setup() {
  pinMode(S0, OUTPUT);
  pinMode(S1, OUTPUT);
  pinMode(S2, OUTPUT);
}

void selectOutput(byte n) {
  digitalWrite(S0, n & 1);
  digitalWrite(S1, (n >> 1) & 1);
  digitalWrite(S2, (n >> 2) & 1);
}

void loop() {
  char key = keypad.getKey();

  if (key) {
    int index;

    if (key >= '0' && key <= '9') index = key - '0';
    else index = 0;

    if (index < 8) selectOutput(index);
  }
}
