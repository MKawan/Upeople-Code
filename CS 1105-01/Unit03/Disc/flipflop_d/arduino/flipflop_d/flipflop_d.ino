/* ---------------------------------------------------------------------------
   Arduino 7-Segment Display Controller Using D Flip-Flops (SimulIDE Compatible)
   ---------------------------------------------------------------------------
   - SEG_A … SEG_G : Segment pins for the 7-segment display (common cathode)
   - BOTAO         : Increment button (INPUT_PULLUP, active LOW)
   - PIN_CLEAR     : Reset button (INPUT_PULLUP, active LOW)
   - PIN_CLOCK     : Clock pulse output for external D flip-flops

   This program:
     • Drives all 7 segments using Arduino pins
     • Sends their logic state into the D inputs of external flip-flops
     • Generates a clean CLOCK pulse so flip-flops capture the new state
     • Uses one button to increment the displayed digit (0 → 9 loop)
     • Uses one button for asynchronous reset (/CLR) of the flip-flops
--------------------------------------------------------------------------- */

// ---- 7-segment pins (common cathode, HIGH turns segment ON) ----
#define SEG_A 31
#define SEG_B 33
#define SEG_C 35
#define SEG_D 37
#define SEG_E 39
#define SEG_F 41
#define SEG_G 43

// ---- Button pins ----
#define BUTTON 2        // Increment button (INPUT_PULLUP, active LOW)
#define PIN_CLEAR 3    // Reset button for /CLR (INPUT_PULLUP, active LOW)

// ---- Flip-Flop clock pin ----
#define PIN_CLOCK 52   // Clock output to all D flip-flops

// Counter variable (0–9)
int cont = 0;

void setup() {

  // -------------------------------------------------------------
  // Configure the 7-segment pins as outputs
  // -------------------------------------------------------------
  pinMode(SEG_A, OUTPUT); pinMode(SEG_B, OUTPUT);
  pinMode(SEG_C, OUTPUT); pinMode(SEG_D, OUTPUT);
  pinMode(SEG_E, OUTPUT); pinMode(SEG_F, OUTPUT);
  pinMode(SEG_G, OUTPUT);

  // -------------------------------------------------------------
  // Configure the clock pin (starts LOW)
  // -------------------------------------------------------------
  pinMode(PIN_CLOCK, OUTPUT);
  digitalWrite(PIN_CLOCK, LOW);

  // -------------------------------------------------------------
  // Increment button uses pull-up resistor (active LOW)
  // -------------------------------------------------------------
  pinMode(BUTTON, INPUT_PULLUP);

  // -------------------------------------------------------------
  // Startup RESET:
  // Send a short LOW pulse to /CLR to initialize all flip-flops
  // Flip-flops boot in undefined states, so this ensures "0"
  // -------------------------------------------------------------
  pinMode(PIN_CLEAR, OUTPUT);    // Drive /CLR directly
  digitalWrite(PIN_CLEAR, LOW);  // Assert reset
  delay(10);                     // Keep reset active briefly
  pinMode(PIN_CLEAR, INPUT_PULLUP); // Return pin to button mode

  // -------------------------------------------------------------
  // Initialize display at "0"
  // Load segments → send clock → flip-flops latch the value
  // -------------------------------------------------------------
  cont = 0;
  setSegments(cont);
  clockPulse();

  Serial.begin(9600);
  Serial.println("System started (CLEAR uses button).");
}

/* ---------------------------------------------------------------------------
   Generates a clean clock pulse for flip-flops.
   Rising edge is what the flip-flops detect.
--------------------------------------------------------------------------- */
void clockPulse() {
  digitalWrite(PIN_CLOCK, LOW);
  delayMicroseconds(200);
  
  digitalWrite(PIN_CLOCK, HIGH);   // Rising edge → flip-flops latch input
  delayMicroseconds(200);
  
  digitalWrite(PIN_CLOCK, LOW);
  delayMicroseconds(100);
}

/* ---------------------------------------------------------------------------
   Writes the correct pattern to the 7-segment display.
   HIGH = segment ON (common cathode display)
--------------------------------------------------------------------------- */
void setSegments(int n) {
  // Each segment must be HIGH (ON) or LOW (OFF)
  switch (n) {
    case 0: digitalWrite(SEG_A,1); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,1); digitalWrite(SEG_F,1);
            digitalWrite(SEG_G,0); break;

    case 1: digitalWrite(SEG_A,0); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,0); digitalWrite(SEG_E,0); digitalWrite(SEG_F,0);
            digitalWrite(SEG_G,0); break;

    case 2: digitalWrite(SEG_A,1); digitalWrite(SEG_B,1); digitalWrite(SEG_C,0);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,1); digitalWrite(SEG_F,0);
            digitalWrite(SEG_G,1); break;

    case 3: digitalWrite(SEG_A,1); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,0); digitalWrite(SEG_F,0);
            digitalWrite(SEG_G,1); break;

    case 4: digitalWrite(SEG_A,0); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,0); digitalWrite(SEG_E,0); digitalWrite(SEG_F,1);
            digitalWrite(SEG_G,1); break;

    case 5: digitalWrite(SEG_A,1); digitalWrite(SEG_B,0); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,0); digitalWrite(SEG_F,1);
            digitalWrite(SEG_G,1); break;

    case 6: digitalWrite(SEG_A,1); digitalWrite(SEG_B,0); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,1); digitalWrite(SEG_F,1);
            digitalWrite(SEG_G,1); break;

    case 7: digitalWrite(SEG_A,1); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,0); digitalWrite(SEG_E,0); digitalWrite(SEG_F,0);
            digitalWrite(SEG_G,0); break;

    case 8: digitalWrite(SEG_A,1); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,1); digitalWrite(SEG_F,1);
            digitalWrite(SEG_G,1); break;

    case 9: digitalWrite(SEG_A,1); digitalWrite(SEG_B,1); digitalWrite(SEG_C,1);
            digitalWrite(SEG_D,1); digitalWrite(SEG_E,0); digitalWrite(SEG_F,1);
            digitalWrite(SEG_G,1); break;
  }
}

/* ---------------------------------------------------------------------------
   Increments the digit (0 → 9 → 0), updates segments,
   and sends a clock pulse so flip-flops latch the new state.
--------------------------------------------------------------------------- */
void mostrar() {
  cont++;
  if (cont > 9) cont = 0;   // Wrap around

  setSegments(cont);            // Update D inputs
  clockPulse();                     // Flip-flops capture new value

  Serial.print("Displayed: ");
  Serial.println(cont);
}

/* ---------------------------------------------------------------------------
   MAIN LOOP
--------------------------------------------------------------------------- */
void loop() {

  // -------------------------------------------------------------
  // CLEAR button (active LOW, resets flip-flops via hardware /CLR)
  // -------------------------------------------------------------
  if (digitalRead(PIN_CLEAR) == LOW) {
    delay(20); // Debounce
    if (digitalRead(PIN_CLEAR) == LOW) {

      Serial.println("RESET button pressed");

      // Wait until released — prevents multiple resets
      while (digitalRead(PIN_CLEAR) == LOW) {
        delay(10);
      }

      delay(30);  // Additional debounce safety
    }
  }

  // -------------------------------------------------------------
  // INCREMENT button (active LOW)
  // -------------------------------------------------------------
  if (digitalRead(BUTTON) == LOW) {
    delay(20); // Debounce

    if (digitalRead(BUTTON) == LOW) {
      mostrar();     // Show next number

      // Wait for button release
      while (digitalRead(BUTTON) == LOW) {
        delay(10);
      }

      delay(20); // Debounce after release
    }
  }

  // Small idle delay (prevents loop from running too fast)
  delay(5);
}
