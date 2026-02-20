const current = document.getElementById("current");
const previous = document.getElementById("previous");

let currentValue = "";
let previousValue = "";
let operator = "";

function updateDisplay() {
  current.textContent = currentValue || "0";
  previous.textContent = previousValue + (operator ? " " + operator : "");
}

document.querySelectorAll("button").forEach(button => {
  button.addEventListener("click", () => {

    // Numbers & Dot
    if (!button.dataset.operator && !button.dataset.action) {
      if (button.textContent === "." && currentValue.includes(".")) return;
      currentValue += button.textContent;
      updateDisplay();
    }

    // Operators
    if (button.dataset.operator) {
      if (!currentValue) return;
      operator = button.dataset.operator;
      previousValue = currentValue;
      currentValue = "";
      updateDisplay();
    }

    // Actions
    if (button.dataset.action === "clear") {
      currentValue = "";
      previousValue = "";
      operator = "";
      updateDisplay();
    }

    if (button.dataset.action === "delete") {
      currentValue = currentValue.slice(0, -1);
      updateDisplay();
    }

    if (button.dataset.action === "equals") {
      if (!currentValue || !operator) return;

      let result;
      const a = parseFloat(previousValue);
      const b = parseFloat(currentValue);

      switch (operator) {
        case "+": result = a + b; break;
        case "-": result = a - b; break;
        case "*": result = a * b; break;
        case "/": 
          result = b === 0 ? "Error" : a / b; 
          break;
        case "%": result = a % b; break;
      }

      currentValue = result.toString();
      previousValue = "";
      operator = "";
      updateDisplay();
    }
  });
});

updateDisplay();