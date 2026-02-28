const chatWindow = document.getElementById('chat-window');
const messageInput = document.getElementById('message-input');
const sendBtn = document.getElementById('send-btn');

// Mini bot responses (JSON-like object)
const botResponses = {
  "hi": ["Hello!", "Hey there!", "Hi! How are you?"],
  "hello": ["Hi! Nice to see you.", "Hello hello!", "Heyyy!"],
  "how are you": ["I'm good, thanks! And you?", "Feeling great today 😄", "I'm just a bot but I'm happy you're here!"],
  "bye": ["See you later!", "Bye bye!", "Take care!"],
  "thanks": ["You're welcome!", "No problem 😊", "Anytime!"],
  "what is your name": ["I'm MiniBot!", "Call me Mini 😄", "I'm your friendly chat bot!"],
  "default": ["Hmm interesting...", "Cool!", "Tell me more!", "I don't know what to say... lol", "😅"]
};

function getBotReply(userText) {
  const lowerText = userText.toLowerCase().trim();

  // Find the best matching key
  for (const key in botResponses) {
    if (lowerText.includes(key)) {
      const replies = botResponses[key];
      return replies[Math.floor(Math.random() * replies.length)];
    }
  }

  // Fallback to default replies
  const defaults = botResponses.default;
  return defaults[Math.floor(Math.random() * defaults.length)];
}

function addMessage(text, isSent = true) {
  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message');
  messageDiv.classList.add(isSent ? 'sent' : 'received');
  messageDiv.textContent = text;

  chatWindow.appendChild(messageDiv);
  messageDiv.scrollIntoView({ behavior: 'smooth', block: 'end' });
}

// Handle send button click
sendBtn.addEventListener('click', () => {
  const text = messageInput.value.trim();
  if (text) {
    // Show user's message
    addMessage(text, true);
    messageInput.value = '';

    // Simulate bot thinking (small delay)
    setTimeout(() => {
      const botReply = getBotReply(text);
      addMessage(botReply, false);
    }, 800);
  }
});

// Handle Enter key press
messageInput.addEventListener('keydown', (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();
    sendBtn.click();  // Trigger the same send logic
  }
});

// Welcome message on load
addMessage("Hi! I'm MiniBot. Say something to me 😄", false);