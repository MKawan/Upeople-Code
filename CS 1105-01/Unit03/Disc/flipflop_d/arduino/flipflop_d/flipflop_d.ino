#define SEG_A 31
#define SEG_B 33
#define SEG_C 35
#define SEG_D 37
#define SEG_E 39
#define SEG_F 41
#define SEG_G 43

#define BOTAO 2       // botão de incremento (INPUT_PULLUP, ativo LOW)
#define PIN_CLEAR 3   // botão de reset (INPUT_PULLUP, ativo LOW)
#define PIN_CLOCK 52   // clock dos flip-flops (OUTPUT)

int contador = 0;

void setup() {
  // configura pinos dos segmentos como saída
  pinMode(SEG_A, OUTPUT); pinMode(SEG_B, OUTPUT);
  pinMode(SEG_C, OUTPUT); pinMode(SEG_D, OUTPUT);
  pinMode(SEG_E, OUTPUT); pinMode(SEG_F, OUTPUT);
  pinMode(SEG_G, OUTPUT);

  // clock como saída
  pinMode(PIN_CLOCK, OUTPUT);
  digitalWrite(PIN_CLOCK, LOW);

  // inicializa botões com pull-up
  pinMode(BOTAO, INPUT_PULLUP);

  // STARTUP RESET: pulso elétrico curto para limpar os flip-flops ao ligar
  // Fazemos isso temporariamente dirigindo o pino como OUTPUT LOW, depois voltamos pra INPUT_PULLUP
  pinMode(PIN_CLEAR, OUTPUT);
  digitalWrite(PIN_CLEAR, LOW);
  delay(10);                      // garante reset curto no power-on
  pinMode(PIN_CLEAR, INPUT_PULLUP); // agora o pino vira botão (pull-up interno)

  // inicial mostra 0 por segurança (só setamos segmentos e damos clock)
  contador = 0;
  setSegments(contador);
  clockPulse();

  Serial.begin(9600);
  Serial.println("Sistema iniciado (CLEAR por botão).");
}

/* ---------- Funções utilitárias ---------- */

void clockPulse() {
  digitalWrite(PIN_CLOCK, LOW);
  delayMicroseconds(200);
  digitalWrite(PIN_CLOCK, HIGH);   // borda de subida que o FF reconhece
  delayMicroseconds(200);
  digitalWrite(PIN_CLOCK, LOW);
  delayMicroseconds(100);
}

// Common Cathode — HIGH acende
void setSegments(int n) {
  // 1 = aceso, 0 = apagado
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

void mostrar() {
  contador++;
  if (contador > 9) contador = 0;

  // Coloca os bits nas entradas D (segmentos) ANTES do clock
  setSegments(contador);

  // Pulso de clock que faz o FlipFlop capturar os D's
  clockPulse();

  Serial.print("Mostrado: ");
  Serial.println(contador);
}

/* ---------- Loop principal ---------- */
void loop() {
  // ---- Botão CLEAR (reset) ----
  if (digitalRead(PIN_CLEAR) == LOW) { // botão pressionado (ativo LOW)
    delay(20); // debounce simples
    if (digitalRead(PIN_CLEAR) == LOW) {
      // aqui o botão fisicamente puxa a linha para GND → /CLR ativo no flip-flop
      // aguardamos a liberação para evitar contagens indesejadas
      Serial.println("RESET (CLEAR) pressionado");
      while (digitalRead(PIN_CLEAR) == LOW) { delay(10); } // espera soltar
      delay(30);
      // opcional: após soltar, fazemos uma leitura inicial (não necessário)
    }
  }

  // ---- Botão increment (BOTAO) ----
  if (digitalRead(BOTAO) == LOW) { // ativo LOW
    delay(20); // debounce
    if (digitalRead(BOTAO) == LOW) {
      mostrar();
      // espera soltar para evitar múltiplos triggers
      while (digitalRead(BOTAO) == LOW) { delay(10); }
      delay(20);
    }
  }

  // pouco tempo de iddle
  delay(5);
}
