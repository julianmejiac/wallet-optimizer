
const addExpenseButton = document.getElementById("addExpenseButton");
const categoryInput = document.getElementById("category");
const monthlyAmountInput = document.getElementById("monthlyAmount");
const expenseTableBody = document.getElementById("expenseTableBody");
const recommendButton = document.getElementById("recommendButton")
const recommendationResults = document.getElementById("recommendationResults");
const recommendationTableBody = document.getElementById("recommendationTableBody");
const totalMonthlyExpenses = document.getElementById("totalMonthlyExpenses");
const totalMonthlyRewards = document.getElementById("totalMonthlyRewards");
const totalAnnualRewards = document.getElementById("totalAnnualRewards");

addExpenseButton.addEventListener("click", addExpense);
recommendButton.addEventListener("click", getRecommendation);

function addExpense() {
    const categoryName = categoryInput.value.trim();

    if (categoryName === ""){
        alert("Please enter a category");
        return;
                }
    const categoryAmount = monthlyAmountInput.value;
    if (categoryAmount === ""){
        alert("Please enter a monthly amount.");
        return;
    }
    if (Number(categoryAmount)<=0){
        alert("Monthly amount must be positive");
        return;
    }

    const row = createExpenseRow(categoryName, categoryAmount);

    expenseTableBody.appendChild(row);
    categoryInput.value = "";
    monthlyAmountInput.value = "";
    categoryInput.focus();
}

function createExpenseRow(categoryName, categoryAmount) {
    const row = document.createElement("tr");

    const categoryCell = document.createElement("td");
    const amountCell = document.createElement("td");
    const removeCell = document.createElement("td");

    categoryCell.textContent = categoryName;
    amountCell.textContent = Number(categoryAmount).toFixed(2);

    const removeButton = document.createElement("button");
    removeButton.textContent = "Remove";
    removeButton.type = "button";

    removeButton.addEventListener("click", () => {
        row.remove();
    });

    removeCell.appendChild(removeButton);

    row.appendChild(categoryCell);
    row.appendChild(amountCell);
    row.appendChild(removeCell);

    return row;
}
async function getRecommendation(){
    const expenses=[];
    const rows=expenseTableBody.querySelectorAll("tr");
    if (rows.length === 0){
        alert("please add at least one expense");
        return;
        }
    for (const row of rows){
        const category=row.children[0].textContent;
        const monthlyAmount=Number(row.children[1].textContent);
        const budgetItem={category: category,
                        monthlyAmount: monthlyAmount}
        expenses.push(budgetItem)
    }
    const budgetRequest={expenses:expenses};
    console.log("Budget request: ",budgetRequest);
    let response;
    try{
    response= await fetch(`${API_URL}/budget/recommendation`,
    {method: "POST",
    headers:{"Content-Type":"application/json"},
    body:JSON.stringify(budgetRequest)})
    }
    catch(error){
    alert("Could not connect to the server");
    return;
    }

    if (!response.ok){
        alert("Unable to calculate recommendations")
        return
    }
    const result=await response.json();
    console.log("Budget response: ",result);
    recommendationTableBody.innerHTML = "";
    for (const recommendation of result.recommendations){
    const row=document.createElement("tr");
    const categoryResultCell=document.createElement("td");
    const monthlyAmountResultCell=document.createElement("td");
    const cardNamesResultCell=document.createElement("td");
    const cashbackPercentResultCell=document.createElement("td");
    const monthlyRewardResultCell=document.createElement("td");
    categoryResultCell.textContent=recommendation.category;
    monthlyAmountResultCell.textContent=`$${recommendation.monthlyAmount.toFixed(2)}`;
    cardNamesResultCell.textContent=recommendation.cardNames.join(", ");
    cashbackPercentResultCell.textContent=`${recommendation.cashbackPercent}%`;
    monthlyRewardResultCell.textContent=`$${recommendation.monthlyReward.toFixed(2)}`;
    row.appendChild(categoryResultCell);
    row.appendChild(monthlyAmountResultCell);
    row.appendChild(cardNamesResultCell);
    row.appendChild(cashbackPercentResultCell);
    row.appendChild(monthlyRewardResultCell);
    recommendationTableBody.appendChild(row);
    }

    totalAnnualRewards.textContent=`Total annual rewards: $${result.totalAnnualRewards.toFixed(2)}`;
    totalMonthlyExpenses.textContent=`Total monthly expenses: $${result.totalMonthlyExpenses.toFixed(2)}`;
    totalMonthlyRewards.textContent=`Total monthly rewards: $${result.totalMonthlyRewards.toFixed(2)}`;

}