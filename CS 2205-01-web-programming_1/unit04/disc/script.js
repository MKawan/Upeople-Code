/* =========================================================
   PROTOTYPE SECTION
   ========================================================= */

function Transaction(amount, type, category) {
    this.id = Date.now() + Math.random();
    this.amount = amount;
    this.type = type;
    this.category = category.toLowerCase();
}

Transaction.prototype.getSummary = function () {
    return `Type: ${this.type}\nCategory: ${this.category}\nAmount: $${this.amount}`;
};


/* =========================================================
   HISTORY MODULE (Closure)
   ========================================================= */

function createHistoryModule() {

    let history = []; // Private history array

    return {
        add(transaction) {
            history.push(transaction);
        },

        getAll() {
            return history;
        }
    };
}


/* =========================================================
   INCOME MODULE (Closure)
   ========================================================= */

function createIncomeTracker() {

    let salaryTotal = 0;
    let incomes = [];

    return {

        add(transaction) {

            const exists = incomes.some(t =>
                t.amount === transaction.amount &&
                t.category === transaction.category &&
                t.type === transaction.type
            );

            if (exists) {
                alert("Duplicate transaction detected.");
                return false;
            }

            if (transaction.category === "salary") {
                salaryTotal += transaction.amount;
            } else {
                incomes.push(transaction);
            }

            return true;
        },

        getTotal() {
            const extraIncome = incomes.reduce((sum, t) => sum + t.amount, 0);
            return salaryTotal + extraIncome;
        },

        getSalaryTotal() {
            return salaryTotal;
        }
    };
}


/* =========================================================
   EXPENSE MODULE (Closure)
   ========================================================= */

function createExpenseTracker() {

    let expenses = [];

    return {

        add(transaction) {

            const exists = expenses.some(t =>
                t.amount === transaction.amount &&
                t.category === transaction.category &&
                t.type === transaction.type
            );

            if (exists) {
                alert("Duplicate transaction detected.");
                return false;
            }

            expenses.push(transaction);
            return true;
        },

        getTotal() {
            return expenses.reduce((sum, t) => sum + t.amount, 0);
        }
    };
}


/* =========================================================
   REPORT MODULE
   ========================================================= */

function createReportModule(incomeTracker, expenseTracker) {

    return function generateReport() {

        const totalIncome = incomeTracker.getTotal();
        const totalExpense = expenseTracker.getTotal();
        const balance = totalIncome - totalExpense;

        return {
            totalIncome,
            totalExpense,
            balance
        };
    };
}

/* =========================================================
   UPDATE INCOME
   ========================================================= */

function updateSalary() {
    const report = generateReport();
    document.getElementById("salary").textContent = `$${report.balance}`;
}


/* =========================================================
   INITIALIZATION
   ========================================================= */

const historyModule = createHistoryModule();
const incomeTracker = createIncomeTracker();
const expenseTracker = createExpenseTracker();
const generateReport = createReportModule(incomeTracker, expenseTracker);


/* =========================================================
   ADD TRANSACTION
   ========================================================= */

document.getElementById("addBtn").addEventListener("click", function () {

    const amount = parseFloat(document.getElementById("amount").value);
    const type = document.getElementById("type").value;
    const category = document.getElementById("category").value.trim().toLowerCase();

    if (isNaN(amount) || amount <= 0) {
        alert("Amount must be greater than 0.");
        return;
    }

    if (!category) {
        alert("Category is required.");
        return;
    }

    const transaction = new Transaction(amount, type, category);

    let added = false;

    if (type === "income") {
        added = incomeTracker.add(transaction);
    } else {
        added = expenseTracker.add(transaction);
    }

    // Only add to history if successfully added
    if (added) {
        historyModule.add(transaction);
    }

    updateSalary();

    document.getElementById("amount").value = "";
    document.getElementById("category").value = "";
});


/* =========================================================
   GENERATE REPORT WITH HISTORY
   ========================================================= */

document.getElementById("reportBtn").addEventListener("click", function () {

    const report = generateReport();
    const history = historyModule.getAll();
    const nowTime = new Date().toLocaleString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true,
        month: '2-digit',
        day: '2-digit',
        year: 'numeric'
    });

    // If there are no transactions
    if (report.totalIncome === 0 && report.totalExpense === 0) {
        alert("No transactions added yet.");
        return; // interrompe aqui
    }

    const container = document.getElementById("output");

    // Create coupon block
    const coupon = document.createElement("div");
    coupon.classList.add("coupon");

    // Create summary section
    const summary = document.createElement("div");
    summary.innerHTML = `
        <strong>Salary Total:</strong> $${incomeTracker.getSalaryTotal()}<br>
        <strong>Total Income:</strong> $${report.totalIncome}<br>
        <strong>Total Expenses:</strong> $${report.totalExpense}<br>
        <strong>Balance:</strong> $${report.balance}
        <hr>
    `;

    coupon.appendChild(summary);

    // Add transaction history inside coupon
    history.forEach(transaction => {

        const item = document.createElement("div");
        item.classList.add("transaction-item");

        item.innerHTML = `
            <strong>TRANSACTION ${nowTime}</strong><br>
            <div><strong>Type:</strong> ${transaction.type}</div>
            <div><strong>Category:</strong> ${transaction.category}</div>
            <div><strong>Amount:</strong> $${transaction.amount}</div>
            <br>
        `;

        coupon.appendChild(item);
    });

    // Add new coupon to container (without deleting previous ones)
    container.appendChild(coupon);
});