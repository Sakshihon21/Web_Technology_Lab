const steps = document.querySelectorAll(".step");
const nextBtn = document.querySelector(".next");
const prevBtn = document.querySelector(".prev");
const submitBtn = document.querySelector(".submit");
const progressBar = document.querySelector(".progress-bar");

const errorToast = document.querySelector(".toast.error");
const successToast = document.querySelector(".toast.success");

let currentStep = 0;

/* STEP CONTROL */
function showStep(step) {
  steps.forEach((s, i) => s.classList.toggle("active", i === step));
  progressBar.style.width = step === 0 ? "50%" : "100%";
}

/* TOAST */
function showToast(toast) {
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2500);
}

/* TYPE-BASED VALIDATION (NO CLEARING) */
function attachValidation(id, regex) {
  const input = document.getElementById(id);

  input.addEventListener("input", () => {
    if (!regex.test(input.value)) {
      input.parentElement.classList.add("error");
    } else {
      input.parentElement.classList.remove("error");
    }
  });
}

/* APPLY RULES */
attachValidation("name", /^[a-zA-Z ]*$/);
attachValidation("roll", /^[0-9]*$/);
attachValidation("div", /^[A-Za-z]?$/);
attachValidation("prn", /^[0-9]*$/);
attachValidation("class", /^[a-zA-Z0-9-]*$/);

/* STEP VALIDATION */
function validateStep() {
  let valid = true;
  const fields = steps[currentStep].querySelectorAll(".field");

  fields.forEach(field => {
    const input = field.querySelector("input, select");
    if (!input.value.trim() || field.classList.contains("error")) {
      field.classList.add("error");
      valid = false;
    }
  });

  return valid;
}

/* BUTTON EVENTS */
nextBtn.addEventListener("click", () => {
  if (!validateStep()) {
    showToast(errorToast);
    return;
  }
  currentStep = 1;
  showStep(currentStep);
});

prevBtn.addEventListener("click", () => {
  currentStep = 0;
  showStep(currentStep);
});

submitBtn.addEventListener("click", () => {
  if (!validateStep()) {
    showToast(errorToast);
    return;
  }

  showToast(successToast);

  setTimeout(() => {
    document.querySelectorAll("input, select").forEach(el => el.value = "");
    document.querySelectorAll(".field").forEach(f => f.classList.remove("error"));
    currentStep = 0;
    showStep(currentStep);
  }, 1500);
});