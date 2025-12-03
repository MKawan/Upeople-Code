// -------------------------------------------------------------
// Tetris for Arduino + ILI9341 Display (240x320 vertical) 
// Flicker-free rendering - optimized cell-level redraw
// Tile size is adjustable at the top for quick testing.
// -------------------------------------------------------------

// Libraries
#include <Adafruit_GFX.h>
#include <Adafruit_ILI9341.h>

// ----------------- DISPLAY CONFIGURATION -----------------
#define TFT_CS 10
#define TFT_DC 9
#define TFT_RST 8
Adafruit_ILI9341 tft = Adafruit_ILI9341(TFT_CS, TFT_DC, TFT_RST);

// Grid configuration (change tile size here)
const uint8_t cols = 10;
const uint8_t rows = 20;
uint8_t tile = 14;           // <--- adjustable tile size (12, 14, 16...)
int xOffset = 0;             // offset to center grid horizontally
int yOffset = 0;             // offset to center grid vertically

// Button pins (adapted for Arduino Mega)
const uint8_t leftPin   = 35;
const uint8_t rightPin  = 31;
const uint8_t downPin   = 37;
const uint8_t rotatePin = 41;
const uint8_t playPin   = 39;
const uint8_t buzzerPin = 7;
const uint8_t soundPin  = 43;

// Timing control
unsigned long lastDrop = 0;
unsigned long dropInterval = 600;  // gravity interval (ms)

// Game state
uint8_t field[rows][cols];   // game grid (0 = empty, 1..7 = color index)
int score = 0;
bool running = false;
bool needRedraw = true;
bool paused = false;

// Colors (palette)
uint16_t GRAY_MEDIUM; // defined inside setup()

const uint16_t pal[] = {
  ILI9341_WHITE,   // 0 (unused)
  ILI9341_CYAN,    // I piece
  ILI9341_BLUE,    // J
  ILI9341_ORANGE,  // L
  ILI9341_YELLOW,  // O
  ILI9341_GREEN,   // S
  ILI9341_MAGENTA, // T
  ILI9341_RED      // Z
};

// Tetromino definitions (4x4 masks)
const uint16_t pieces[7][4] = {
  { 0x0F00, 0x2222, 0x00F0, 0x4444 }, // I
  { 0x8E00, 0x6440, 0x0E20, 0x44C0 }, // J
  { 0x2E00, 0x4460, 0x0E80, 0xC440 }, // L
  { 0x6600, 0x6600, 0x6600, 0x6600 }, // O
  { 0x6C00, 0x4620, 0x06C0, 0x8C40 }, // S
  { 0x4E00, 0x4640, 0x0E40, 0x4C40 }, // T
  { 0xC600, 0x2640, 0x0C60, 0x4C80 }  // Z
};

// Piece structure (active piece)
struct Piece {
  uint8_t type;   // 0..6
  uint8_t rot;    // 0..3
  int8_t x;       // grid x
  int8_t y;       // grid y
} cur;

// Button debouncing
unsigned long lastDebLeft=0, lastDebRight=0, lastDebDown=0;
unsigned long lastDebRot=0, lastDebPlay=0;
const unsigned long debounceDelay = 120;

// Simple music system
const int melody[] = { 262, 294, 330, 349, 392, 440, 494, 523 };
const int melodyLen = sizeof(melody) / sizeof(melody[0]);
int melodyIndex = 0;
unsigned long lastNoteMillis = 0;
const unsigned long noteDur = 350;  
bool musicOn = true;

// ----------------- Helper Functions -----------------

// Reads a button (active LOW)
inline bool readButton(uint8_t pin) { 
  return digitalRead(pin) == LOW; 
}

// Non-blocking tone playback (no duration control here)
void playToneNonBlocking(int freq, unsigned long dur) {
  if(freq <= 0) {
    noTone(buzzerPin);
    return;
  }
  tone(buzzerPin, freq);
}

void stopTone() { 
  noTone(buzzerPin); 
}

// Returns bitmask for a piece rotation
uint16_t getPieceMask(uint8_t type, uint8_t rot) {
  return pieces[type][rot & 3];
}

// Recalculate screen offsets when tile size changes
void updateLayout() {
  xOffset = (240 - cols * tile) / 2;
  yOffset = (320 - rows * tile) / 2;
  if (xOffset < 0) xOffset = 0;
  if (yOffset < 0) yOffset = 0;
}

// -------------------------------------------------------
// Rendering helpers (cell-based, flicker-free)
// -------------------------------------------------------

// Draws a single cell (gx = column, gy = row)
void drawCell(int gx, int gy) {
  if (gx < 0 || gx >= cols || gy < 0 || gy >= rows) return;

  int x = xOffset + gx * tile;
  int y = yOffset + gy * tile;

  if (field[gy][gx] == 0) {
    // empty cell
    tft.fillRect(x+1, y+1, tile-2, tile-2, ILI9341_BLACK);
    tft.drawRect(x, y, tile, tile, GRAY_MEDIUM);
  } else {
    // filled cell
    uint16_t color = pal[field[gy][gx]];
    tft.fillRect(x+1, y+1, tile-2, tile-2, color);
    tft.drawRect(x, y, tile, tile, ILI9341_BLACK);
  }
}

// Draw entire game field once
void drawField(){
  tft.fillRect(xOffset-2, yOffset-2, cols*tile+4, rows*tile+4, ILI9341_BLACK);
  for(int r=0;r<rows;r++){
    for(int c=0;c<cols;c++){
      drawCell(c,r);
    }
  }
}

// -------------------------------------------------------
// Collision detection
// checkingSpawn=true \u2192 strict TOP-row check for game over
// -------------------------------------------------------
bool checkCollision(int nx, int ny, uint8_t type, uint8_t rot, bool checkingSpawn=false) {
  uint16_t mask = getPieceMask(type, rot);

  for (int py = 0; py < 4; py++) {
    for (int px = 0; px < 4; px++) {
      if (mask & (0x8000 >> (py*4 + px))) {

        int fx = nx + px;
        int fy = ny + py;

        // Ignore above-screen rows unless checking spawn
        if (!checkingSpawn && fy < 0) continue;

        if (fx < 0 || fx >= cols || fy >= rows) return true;

        if (fy >= 0 && field[fy][fx]) return true;
      }
    }
  }

  return false;
}

// -------------------------------------------------------
// GAME OVER screen
// -------------------------------------------------------
void showGameOver(){
  tft.fillRect(0, 100, 240, 80, ILI9341_BLACK);
  tft.setTextSize(3);
  tft.setTextColor(ILI9341_RED);

  int16_t x1,y1; 
  uint16_t w,h;

  String go = "GAME OVER";
  tft.getTextBounds(go,0,0,&x1,&y1,&w,&h);
  tft.setCursor((240-w)/2, 120);
  tft.print(go);

  tft.setTextSize(1);
  tft.setTextColor(GRAY_MEDIUM);

  String rep = "Press PLAY to restart";
  tft.getTextBounds(rep,0,0,&x1,&y1,&w,&h);
  tft.setCursor((240-w)/2, 160);
  tft.print(rep);
}

// -------------------------------------------------------
// Spawn new piece
// -------------------------------------------------------
void spawnPiece() {
  cur.type = random(0, 7);
  cur.rot  = 0;
  cur.x = (cols - 4) / 2;
  cur.y = -2;

  // strict spawn collision = game over
  if (checkCollision(cur.x, cur.y, cur.type, cur.rot, true)) {
    running = false;
    showGameOver();
    return;
  }
}

// -------------------------------------------------------
// Clear line (only redraw minimal required cells)
// -------------------------------------------------------
void clearLine(int y) {
  // Move all rows downward
  for (int row = y; row > 0; row--) {
    for (int c = 0; c < cols; c++) {
      field[row][c] = field[row-1][c];
    }
  }

  // Clear top row
  for (int c = 0; c < cols; c++) field[0][c] = 0;

  // Redraw only necessary cells
  for (int row = 0; row <= y; row++) {
    for (int c = 0; c < cols; c++) {
      drawCell(c, row);
    }
  }
}

// -------------------------------------------------------
// Clear line visual effect
// -------------------------------------------------------
void clearLinesEffects(int line){
  for(int t=0;t<4;t++){
    uint16_t color = (t%2==0)? ILI9341_WHITE : ILI9341_BLACK;

    for(int c=0;c<cols;c++){
      int x=xOffset + c*tile;
      int y=yOffset + line*tile;
      tft.fillRect(x+1,y+1,tile-2,tile-2,color);
    }

    delay(50);
  }

  // Particle color splash
  for(int p=0;p<28;p++){
    int c = random(0,cols);
    int xx = xOffset + c*tile + random(1,tile-2);
    int yy = yOffset + line*tile + random(1,tile-2);
    tft.fillRect(xx,yy,2,2, pal[random(1,8)]);
  }
}

// -------------------------------------------------------
// Check for completed lines and clear them
// -------------------------------------------------------
void checkAndClearLines() {
  int cleared = 0;

  for (int r = rows - 1; r >= 0; r--) {
    bool full = true;

    for (int c = 0; c < cols; c++)
      if (field[r][c] == 0) { full = false; break; }

    if (full) {
      clearLinesEffects(r);
      clearLine(r);
      cleared++;
      r++;  // Re-check same row index after shifting
    }
  }

  if (cleared > 0) {
    score += cleared * 100;
    playToneNonBlocking(800, 120);
    delay(120);
    stopTone();
    drawHUD();
  }
}

// -------------------------------------------------------
// Draw or erase current falling piece
// -------------------------------------------------------
void drawCurrentPiece(bool erase=false){
  uint16_t mask = getPieceMask(cur.type, cur.rot);

  for(int py=0; py<4; py++){
    for(int px=0; px<4; px++){
      if(mask & (0x8000 >> (py*4 + px))) {

        int fx = cur.x + px;
        int fy = cur.y + py;

        if(fy>=0 && fx>=0 && fx<cols && fy<rows){

          int x = xOffset + fx * tile;
          int y = yOffset + fy * tile;

          if(erase) {
            // draw empty cell
            tft.fillRect(x+1, y+1, tile-2, tile-2, ILI9341_BLACK);
            tft.drawRect(x, y, tile, tile, GRAY_MEDIUM);
          } else {
            uint16_t color = pal[cur.type+1];
            tft.fillRect(x+1, y+1, tile-2, tile-2, color);
            tft.drawRect(x, y, tile, tile, ILI9341_BLACK);
          }
        }
      }
    }
  }
}

// -------------------------------------------------------
// Lock current piece into field
// -------------------------------------------------------
void placePiece(){
  uint16_t mask = getPieceMask(cur.type, cur.rot);

  for(int py=0; py<4; py++){
    for(int px=0; px<4; px++){
      if(mask & (0x8000 >> (py*4 + px))) {

        int fx = cur.x + px;
        int fy = cur.y + py;

        if (fy < 0) {
          running = false;
          showGameOver();
          return;
        }

        if(fx>=0 && fx<cols && fy>=0 && fy<rows){
          field[fy][fx] = cur.type + 1;
          drawCell(fx, fy);
        }
      }
    }
  }

  drawHUD();
  checkAndClearLines();

  if (running)
    spawnPiece();
}

// -------------------------------------------------------
// HUD
// -------------------------------------------------------
void drawHUD(){
  tft.fillRect(0, 0, 240, 20, ILI9341_BLACK);
  tft.setTextSize(1);
  tft.setTextColor(GRAY_MEDIUM, ILI9341_BLACK);
  tft.setCursor(6, 4);
  tft.print("Score: ");
  tft.print(score);
}

// -------------------------------------------------------
// Opening screen
// -------------------------------------------------------
void showOpening(){
  tft.fillScreen(ILI9341_BLACK);

  tft.setTextSize(3);
  tft.setTextColor(ILI9341_WHITE);

  String title = "TETRIS";
  int16_t x1,y1; 
  uint16_t w,h;

  tft.getTextBounds(title,0,0,&x1,&y1,&w,&h);
  tft.setCursor((240-w)/2, 80);
  tft.print(title);

  tft.setTextSize(2);
  String sub = "Press PLAY to start";
  tft.getTextBounds(sub,0,0,&x1,&y1,&w,&h);
  tft.setCursor((240-w)/2, 140);
  tft.print(sub);

  // background particles
  for(int i=0;i<30;i++){
    int rx=random(0,240);
    int ry=random(0,320);
    tft.fillRect(rx,ry,2,2, pal[random(1,8)]);
  }
}

// -------------------------------------------------------
// Reset field
// -------------------------------------------------------
void clearField(){
  for(int r=0;r<rows;r++)
    for(int c=0;c<cols;c++)
      field[r][c] = 0;
}

// -------------------------------------------------------
// Start new game
// -------------------------------------------------------
void startGame(){
  clearField();
  score = 0;
  running = true;

  updateLayout();
  tft.fillScreen(ILI9341_BLACK);

  drawField();
  drawHUD();
  spawnPiece();
  lastDrop = millis();
}

// -------------------------------------------------------
// Pause / Play button logic
// -------------------------------------------------------
void checkPauseButton() {
  unsigned long now = millis();

  if(readButton(playPin) && now - lastDebPlay > debounceDelay){
    lastDebPlay = now;

    if(!running) {
      // start new game
      startGame();
      paused = false;
      playToneNonBlocking(1000,120);
      stopTone();
      return;
    }

    // Toggle pause/unpause during the game
    paused = !paused;

    if (paused) {
      // Show PAUSED message
      tft.fillRect(0, 140, 240, 40, ILI9341_BLACK);
      tft.setTextSize(3);
      tft.setTextColor(ILI9341_YELLOW);
      String msg = "PAUSED";
      int16_t x1,y1; uint16_t w,h;
      tft.getTextBounds(msg,0,0,&x1,&y1,&w,&h);
      tft.setCursor((240 - w)/2, 150);
      tft.print(msg);

      playToneNonBlocking(800,120);
      delay(120);
      stopTone();
    } else {
      // Remove pause message by redrawing top area + field partially
      drawHUD();
      drawField();
      drawCurrentPiece(false);

      playToneNonBlocking(1200,120);
      delay(120);
      stopTone();
    }
  }
}

// ----------------- LOOP -----------------
void setup(){
  Serial.begin(115200);
  randomSeed(analogRead(A0));
  pinMode(leftPin, INPUT_PULLUP);
  pinMode(rightPin, INPUT_PULLUP);
  pinMode(downPin, INPUT_PULLUP);
  pinMode(rotatePin, INPUT_PULLUP);
  pinMode(playPin, INPUT_PULLUP);
  pinMode(buzzerPin, OUTPUT);

  tft.begin();
  tft.setRotation(0); 
  // definer GRAY_MEDIUM
  GRAY_MEDIUM = tft.color565(128,128,128);

  updateLayout();
  showOpening();
}


// ----------------- LOOP (continua��o natural) -----------------
void loop(){
  unsigned long now = millis();

  // music only when allowed
  if(musicOn && running && !paused){
    if(now - lastNoteMillis >= noteDur){
      int f = melody[melodyIndex % melodyLen];
      tone(buzzerPin, f);
      lastNoteMillis = now;
      melodyIndex++;
      if(melodyIndex >= melodyLen) melodyIndex = 0;
    }
  }

  checkPauseButton();

  // BLOCK GAME WHEN PAUSED
  if(paused){
    return; // \u2190 pausa real, congelando tudo
  }

  // toggle music
  if(readButton(soundPin)){
      musicOn = !musicOn;
      if(!musicOn) stopTone();
  }

  // Inputs only if running
  if(running){

    // left
    if(readButton(leftPin) && now - lastDebLeft > debounceDelay){
      lastDebLeft = now;
      if(!checkCollision(cur.x-1,cur.y,cur.type,cur.rot)){
        drawCurrentPiece(true);
        cur.x--;
        drawCurrentPiece(false);
        playToneNonBlocking(400,60);
        delay(60);
        stopTone();
      }
    }

    // right
    if(readButton(rightPin) && now - lastDebRight > debounceDelay){
      lastDebRight = now;
      if(!checkCollision(cur.x+1,cur.y,cur.type,cur.rot)){
        drawCurrentPiece(true);
        cur.x++;
        drawCurrentPiece(false);
        playToneNonBlocking(400,60);
        delay(60);
        stopTone();
      }
    }

    // down
    if(readButton(downPin) && now - lastDebDown > debounceDelay){
      lastDebDown = now;
      if(!checkCollision(cur.x,cur.y+1,cur.type,cur.rot)){
        drawCurrentPiece(true);
        cur.y++;
        drawCurrentPiece(false);
        score += 1;
      } else {
        drawCurrentPiece(true);
        placePiece();
      }
    }

    // rotate
    if(readButton(rotatePin) && now - lastDebRot > debounceDelay){
      lastDebRot = now;
      uint8_t newrot = (cur.rot+1)&3;
      if(!checkCollision(cur.x,cur.y,cur.type,newrot)){
        drawCurrentPiece(true);
        cur.rot = newrot;
        drawCurrentPiece(false);
        playToneNonBlocking(600,80);
        delay(80);
        stopTone();
      }
    }

    // gravity
    if(now - lastDrop >= dropInterval){
      lastDrop = now;
      if(!checkCollision(cur.x,cur.y+1,cur.type,cur.rot)){
        drawCurrentPiece(true);
        cur.y++;
        drawCurrentPiece(false);
      } else {
        drawCurrentPiece(true);
        placePiece();
      }
      drawHUD();
    }

  } else {
    // idle animation
    if(now % 1000 < 40) {
      tft.fillRect(0,0,240,10,pal[(now/1000)%7 + 1]);
    }
  }
}
